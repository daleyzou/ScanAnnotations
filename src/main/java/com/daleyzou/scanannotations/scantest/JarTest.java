package com.daleyzou.scanannotations.scantest;

import javassist.*;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * JarTest
 * @description
 *  https://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
 * @author daleyzou
 * @date 2020年02月14日 8:47
 * @version 1.1.11
 */
public class JarTest {
    public void test() throws IOException, NotFoundException {
        String pathToJar = "D:\\IDEAProjects\\work\\gaosiedu\\mw-grayscale-dubbo\\target\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar";
        ClassPool classPool = ClassPool.getDefault();
//        classPool.insertClassPath(new ClassClassPath(this.getClass()));
        classPool.insertClassPath(pathToJar);
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));

        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            System.out.println(className);
//            Class c = cl.loadClass(className);
            // 加载方法
            CtClass clazz = classPool.get("com.gaosiedu.middleware.mwgrayscaledubbo.service.IterationService");

            ClassFile cls = clazz.getClassFile();

            //获取方法
            List<MethodInfo> methods = cls.getMethods();
        }
    }

    public static void main(String[] args) throws IOException, NotFoundException {
        JarTest jarTest = new JarTest();
        jarTest.test();
    }
}
