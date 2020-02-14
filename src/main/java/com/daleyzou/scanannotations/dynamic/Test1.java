package com.daleyzou.scanannotations.dynamic;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

/**
 * Test1
 * @description TODO
 * @author zoudaifa
 * @date 2020年02月14日 22:55
 * @version 1.1.11
 */
public class Test1 {
    public void test() throws NotFoundException, IOException {
        String jarFile = "D:\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar";

        JarFile jarFile1 = new JarFile(jarFile);
        String jarFileUrl = (new File(jarFile)).getCanonicalFile().toURI().toURL().toString();
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(jarFile);
//        pool.appendClassPath(new JarClassPath("/home/admin/code_cache/dump"));
        pool.insertClassPath(new ClassClassPath(this.getClass()));
//        String className = "BOOT-INF.classes.com.gaosiedu.middleware.mwgrayscaledubbo.service.IterationService";
        String className = "IterationService";
        CtClass ctClass1 = pool.get(className);
        CtClass ctClass = pool.getCtClass(className);

//        pool.importPackage("com.gaosiedu.middleware.mwgrayscaledubbo");
        CtClass class_ = pool.makeClass(className);

        ClassFile classFile = class_.getClassFile();
        ConstPool constPool = classFile.getConstPool();

        ClassFile cls = class_.getClassFile();

        //获取方法
        List<MethodInfo> methods = cls.getMethods();

        System.out.println("end");

    }

    public static void main(String[] args) throws NotFoundException, IOException {
        Test1 test = new Test1();
        test.test();
    }
}
