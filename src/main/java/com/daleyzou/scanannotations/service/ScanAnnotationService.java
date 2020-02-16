package com.daleyzou.scanannotations.service;

import com.daleyzou.scanannotations.utils.StringUtils;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ScanAnnotationService
 * @description TODO
 * @author daleyozu
 * @date 2020年02月16日 0:10
 * @version 1.1.11
 */
@Service
public class ScanAnnotationService {
    public void scanJarEntry(JarFile jarFile, JarEntry jarEntry) throws IOException {
        if (!jarEntry.getName().endsWith(".class"))
            return;
        if (!StringUtils.containsAny(jarEntry.getName(),"gaosi", "gaosiedu", "aixuexi", "axx")){
            return;
        }

        InputStream inputStream = jarFile.getInputStream(jarEntry);
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
                    System.out.println("属性的注解： " + annotation.getTypeName());
                }
            }
        }
    }

    public void scanJar() throws IOException, DocumentException {
//        String pathToJar = "D:\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar";
        String pathToJar = "D:\\initialD.war";
        JarFile jarFile = new JarFile(pathToJar);

        List<String> resultList = new ArrayList<>();
        System.out.println(jarFile.getName());
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);
        SAXReader reader = new SAXReader();
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.getName().endsWith(".xml") && je.getName().contains("classes")){
                scanXmlFile(reader, jarFile, je);
            }
            if (1 == 1){
                continue;
            }
            if (je.isDirectory() || !je.getName().endsWith(".class") || !je.getName().contains("classes")) {
                continue;
            }

            //            createCtClass(jarFile, je);
            scanJarEntry(jarFile, je);
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            resultList.add(className);
        }
        System.out.println("扫出来的类结果如下：");
        resultList.stream().forEach(System.out::println);
    }

    private void scanXmlFile(SAXReader reader, JarFile jarFile, JarEntry jarEntry) throws IOException, DocumentException {

        Document document = reader.read(jarFile.getInputStream(jarEntry));
        // 获取根元素
        Element root = document.getRootElement();
        for (Iterator i = root.elementIterator(); i.hasNext();) {

            Element el = (Element) i.next();
            if (!"reference".equals(el.getName())){
                continue;
            }
            System.out.println("---------------------------------------------------------");
            System.out.println(el.getNamespacePrefix());
            Attribute anInterface = el.attribute("interface");
            if (anInterface != null){
                System.out.println(anInterface.getValue());
            }
            System.out.println(el);
        }

        List<Element> reference = root.elements("reference");
        reference.forEach(ref ->{
            System.out.println(ref.attribute("interface").getValue());
        });
        System.out.println("pause");
    }

    public static void main(String[] args) throws IOException, DocumentException {
        ScanAnnotationService scanAnnotationService = new ScanAnnotationService();
        scanAnnotationService.scanJar();
    }
}
