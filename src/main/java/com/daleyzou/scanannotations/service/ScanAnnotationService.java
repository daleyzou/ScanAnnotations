package com.daleyzou.scanannotations.service;

import com.daleyzou.scanannotations.utils.FileSearchUtils;
import com.daleyzou.scanannotations.utils.StringUtils;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ScanAnnotationService
 * @description
 *
 * 参考链接：https://memorynotfound.com/java-tar-example-compress-decompress-tar-tar-gz-files/
 *          https://blog.csdn.net/lan861698789/article/details/20492661
 * @author daleyozu
 * @date 2020年02月16日 0:10
 * @version 1.1.11
 */
@Service
public class ScanAnnotationService {

    public class IgnoreDTDEntityResolver implements EntityResolver {

        @Override
        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {
            return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
        }

    }

    SAXReader reader;
    public SAXReader getReader(){
        if (reader == null){
            reader = new SAXReader();
            reader.setEntityResolver(new IgnoreDTDEntityResolver());
        }
        return reader;
    }

    public void scanJarEntry(JarFile jarFile, JarEntry jarEntry) throws IOException {
        if (!jarEntry.getName().endsWith(".class"))
            return;
        if (!StringUtils.containsAny(jarEntry.getName(),"gaosi", "gaosiedu", "aixuexi", "axx")){
            return;
        }

        scanAnnocation(jarFile.getInputStream(jarEntry));
    }

    private void scanAnnocation(InputStream inputStream) throws IOException {
        DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));

        ClassFile cls = new ClassFile(dis);

        //获取属性
        List<FieldInfo> fields = cls.getFields();

        System.out.println("-------------------------------------------------------------");
        System.out.println("类的名称：" + cls.getName());
        //获取类的Runtime注解
        AnnotationsAttribute attribute = (AnnotationsAttribute) cls.getAttribute(AnnotationsAttribute.visibleTag);
        if (attribute != null){
            Annotation[] annotations = attribute.getAnnotations();
            for (Annotation annotation : annotations){
                System.out.println("类的注解名称： " + annotation.getTypeName());
            }
        }

        for (FieldInfo field : fields) {
            //获取属性的Runtime注解
            AnnotationsAttribute attribute1 = (AnnotationsAttribute) field.getAttribute(AnnotationsAttribute.visibleTag);
            if (attribute1 != null) {
                Annotation[] annotations = attribute1.getAnnotations();
                for (Annotation annotation : annotations){
                    System.out.println("属性的名称：" + field.getDescriptor());
                    System.out.println("属性的类型： " + annotation.getTypeName());
                }
            }
        }
    }

    public void scanJar(File file) throws IOException, DocumentException {
//        String pathToJar = "D:\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar";
//        String pathToJar = "D:\\initialD.war";
        JarFile jarFile = new JarFile(file);

        List<String> resultList = new ArrayList<>();
//        System.out.println(jarFile.getName());
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory()){
                continue;
            }
            if (je.getName().endsWith(".xml")){
                System.out.println(jarFile.getName());
                System.out.println(je.getName());
                try {
                    scanXmlFile(getReader(), jarFile.getInputStream(je));
                }catch (Exception excetion){
                    excetion.printStackTrace();
                }
            }
            // !je.getName().contains("classes")
            if (!je.getName().endsWith(".class")) {
                continue;
            }

            //            createCtClass(jarFile, je);
            scanJarEntry(jarFile, je);
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            resultList.add(className);
        }
//        System.out.println("扫出来的类结果如下：");
//        resultList.stream().forEach(System.out::println);
    }

    private void scanXmlFile(SAXReader reader, InputStream inputStream) throws IOException, DocumentException {

        Document document = reader.read(inputStream);
        // 获取根元素
        Element root = document.getRootElement();

        List<Element> reference = root.elements("reference");
        if (!CollectionUtils.isEmpty(reference)){
            System.out.println("-------------------------reference--------------------------------");
        }
        reference.forEach(ref ->{
            System.out.println(ref.attribute("interface").getValue());
        });
        List<Element> service = root.elements("service");
        if (!CollectionUtils.isEmpty(service)){
            System.out.println("-----------------------service----------------------------------");
        }
        service.forEach(se->{
            System.out.println(se.attribute("interface").getValue());
        });
    }

    public void scanDir() throws IOException, DocumentException {
        String path = "D:\\IDEAProjects\\work\\gaosiedu\\Azeroth\\target\\classes";
        List<File> xmlFiles = FileSearchUtils.searchByFileDuff(new File(path), "xml");
        List<File> classFiles = FileSearchUtils.searchByFileDuff(new File(path), "class");
        long classBegin = System.currentTimeMillis();
        for (File file : classFiles){
            scanAnnocation(new FileInputStream(file));
        }
        long classEnd = System.currentTimeMillis();
        System.out.println("扫描注解耗时：" + String.valueOf(classEnd - classBegin));
        SAXReader saxReader = new SAXReader();
        for (File file : xmlFiles){
            scanXmlFile(getReader(), new FileInputStream(file));
        }
        System.out.println(xmlFiles.size());
        System.out.println("扫描xml耗时：" + String.valueOf(System.currentTimeMillis() - classBegin));
        System.out.println("pause");

    }

    public void scanTarGz() throws IOException, DocumentException {
        // Azeroth-assembly.tar.gz
        // D:\Azeroth-assembly
        // tar.gz 文件路径
        String sourcePath = "D:\\Azeroth-assembly.tar.gz";
        // 要解压到的目录
        String extractPath = "D:\\test\\Azeroth";
        File sourceFile = new File(sourcePath);
        // decompressing *.tar.gz files to tar
        TarArchiveInputStream fin = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(sourceFile)));
        File extraceFolder = new File(extractPath);
        TarArchiveEntry entry;
        // 将 tar 文件解压到 extractPath 目录下
        while ((entry = fin.getNextTarEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            File curfile = new File(extraceFolder, entry.getName());
            File parent = curfile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            // 将文件写出到解压的目录
            IOUtils.copy(fin, new FileOutputStream(curfile));
        }

        List<File> jarFiles = FileSearchUtils.searchByFileDuff(extraceFolder, "jar");
        for (File fileItem : jarFiles) {
            scanJar(fileItem);
        }
        SAXReader saxReader = new SAXReader();
        List<File> xmlFiles = FileSearchUtils.searchByFileDuff(extraceFolder, "xml");
        for (File xmlFile : xmlFiles) {
            scanXmlFile(getReader(), new FileInputStream(xmlFile));
        }
    }

    public static void main(String[] args) throws IOException, DocumentException {
        long begin = System.currentTimeMillis();
        ScanAnnotationService scanAnnotationService = new ScanAnnotationService();
//        scanAnnotationService.scanJar();
//        scanAnnotationService.scanDir();
        scanAnnotationService.scanTarGz();
        long timeUsed = System.currentTimeMillis() - begin;
        System.out.println("扫描耗时： " + String.valueOf(timeUsed));
    }
}
