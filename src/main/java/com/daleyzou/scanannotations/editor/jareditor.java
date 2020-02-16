package com.daleyzou.scanannotations.editor;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.assertj.core.util.Lists;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * jareditor
 * @description TODO
 * @author daleyzou
 * @date 2020年02月15日 11:11
 * @version 1.1.11
 */
public class jareditor {
    private boolean baseNode;

    private boolean isDirectory;

    private String realName;

    private void createCtClass(JarFile jarFile, JarEntry jarEntry) throws IOException {
        if (!jarEntry.getName().endsWith(".class"))
            return;

        InputStream in = jarFile.getInputStream(jarEntry);
        byte[] fileBytes = ByteStreams.toByteArray(in);
        //        fileBytes = new byte[in.available()];
        String className = TreePathParser.getClassNameFromRealName(jarEntry.getName());
        String outFile = "D://test//out_" + className + ".class";
        final File newFile = new File(outFile);
        Files.write(fileBytes, newFile);

        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ByteArrayClassPath(className, fileBytes));
        try {
            CtClass ctClass = classPool.get(className);
            System.out.println(ctClass);
            System.out.println(String.format("Created CtClass %s", className));
        } catch (NotFoundException e1) {
            System.out.println("Class not found!");
        }
    }

    public void createTest(JarFile jarFile, JarEntry jarEntry) throws IOException {
        if (!jarEntry.getName().endsWith(".class"))
            return;

        InputStream inputStream = jarFile.getInputStream(jarEntry);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
        if (jarEntry.getName().equals("BOOT-INF/classes/com/gaosiedu/middleware/mwgrayscaledubbo/controller/AccessController.class")){
            System.out.println("begin error !!!!");
//            ClassFile clsss = new ClassFile(dis);
        }
        ClassFile cls = new ClassFile(dis);
        System.out.println("fileName: " + jarEntry.getName());
//        if (jarEntry.getName().equals("BOOT-INF/classes/com/gaosiedu/middleware/mwgrayscaledubbo/base/util/RestTemplateUtil.class")){
//            System.out.println("begin");
//        }
        //获取方法
        List<MethodInfo> methods = cls.getMethods();

        //获取属性
        List<javassist.bytecode.FieldInfo> fields = cls.getFields();

        //获取类的Runtime注解
        AnnotationsAttribute attribute = (AnnotationsAttribute) cls.getAttribute(AnnotationsAttribute.visibleTag);
        for (FieldInfo field : fields) {
            //获取属性的Runtime注解
            AnnotationsAttribute attribute1 = (AnnotationsAttribute) field.getAttribute(AnnotationsAttribute.visibleTag);
            //获取属性的Class注解
            AnnotationsAttribute attribute2 = (AnnotationsAttribute) field.getAttribute(AnnotationsAttribute.invisibleTag);
        }
        for (MethodInfo method : methods) {
            //获取方法的Runtime注解
            AnnotationsAttribute attribute1 = (AnnotationsAttribute) method.getAttribute(AnnotationsAttribute.visibleTag);

            //获取方法参数的注解
            List<ParameterAnnotationsAttribute> parameterAnnotationsAttributes = Lists
                    .newArrayList((ParameterAnnotationsAttribute) method.getAttribute(ParameterAnnotationsAttribute.visibleTag),
                            (ParameterAnnotationsAttribute) method.getAttribute(ParameterAnnotationsAttribute.invisibleTag));
        }
    }

    public void testScan(JarFile jarFile, JarEntry jarEntry) throws IOException {
        if (!jarEntry.getName().endsWith(".class"))
            return;

        InputStream inputStream = jarFile.getInputStream(jarEntry);
        DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));

    }

    public void test() throws IOException {
        String pathToJar = "D:\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar";
        JarFile jarFile = new JarFile(pathToJar);

        List<String> resultList = new ArrayList<>();
        System.out.println(jarFile.getName());
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar + "!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }

            //            createCtClass(jarFile, je);
            createTest(jarFile, je);
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');
            resultList.add(className);
            System.out.println(je.getName());
        }
    }

    public static void main(String[] args) throws IOException {
        jareditor jareditor = new jareditor();
        jareditor.test();
    }
}
