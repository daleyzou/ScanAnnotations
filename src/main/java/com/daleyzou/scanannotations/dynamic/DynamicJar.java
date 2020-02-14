package com.daleyzou.scanannotations.dynamic;

import javassist.*;
import javassist.bytecode.MethodInfo;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * DynamicJar
 * @description TODO
 * @author daleyzou
 * @date 2020年02月14日 19:20
 * @version 1.1.11
 */
public class DynamicJar {
    public void test() throws NotFoundException, CannotCompileException, IOException {
        String jarFile = "D:\\IDEAProjects\\work\\gaosiedu\\mw-grayscale-dubbo\\target\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar";
        String classPath = "D:\\IDEAProjects\\work\\gaosiedu\\mw-grayscale-dubbo\\target\\classes";
        //第二种
        String fileUrl = "file:D:\\IDEAProjects\\work\\gaosiedu\\mw-grayscale-dubbo\\target\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar";
        URL url1 = new URL(fileUrl);
        URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()
                .getContextClassLoader());
        ClassPool pool = ClassPool.getDefault();

//        pool.insertClassPath(new ClassClassPath(this.getClass()));
//        pool.insertClassPath(jarFile);
        pool.insertClassPath(classPath);
//        pool.appendClassPath(jarFile);
        CtClass ctClass1 = pool.get("BOOT-INF.classes.com.gaosiedu.middleware.mwgrayscaledubbo.service.IterationService");
//        ctClass1.toClass(getClass().getClassLoader(), getClass().getProtectionDomain());
//        System.out.println(ctClass1);
        //获取方法
//        CtMethod[] methods = ctClass1.getMethods();

        List<String> classes = listJarClasses(jarFile);
        for (final String className : classes) {
//            CtClass ctClass = pool.get(className);
            CtClass ctClass = pool.get("org.slf4j.Logger");
            System.out.println(ctClass);
//            ctClass.toClass(getClass().getClassLoader(), getClass().getProtectionDomain());
//            System.out.println(className);
        }
    }

    private List<String> listJarClasses(String pathToJar) throws IOException {
        List<String> resultList = new ArrayList<>();
        JarFile jarFile = new JarFile(pathToJar);
        System.out.println(jarFile.getName());
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
            resultList.add(className);
            System.out.println(je.getName());
        }
        System.out.println("class 类结果如下：");
        System.out.println(resultList.toString());
        return resultList;
    }

    public static void main(String[] args) throws IOException, CannotCompileException, NotFoundException {
        DynamicJar dynamicJar = new DynamicJar();
        dynamicJar.test();
    }
}
