package com.daleyzou.scanannotations.scantest;

import javassist.*;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * ScanFile
 * @description TODO
 * @author daleyzou
 * @date 2020年02月12日 22:33
 * @version 3.2.0
 */
public class ScanFile {
    // 修改方法
    public static void changeMethode() {
        try {
            ///////////////////////////////////
            //  使用javaassist修改 class/jar 代码
            ///////////////////////////////////
            //  设置jar包路径
            ClassPool.getDefault().insertClassPath("D:\\IDEAProjects\\work\\gaosiedu\\mw-grayscale-dubbo\\target\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar");

            // 获取需要修改的类
            CtClass testJarClass = ClassPool.getDefault().getCtClass("TestJar");
            // 获取类中的printTest方法
            CtMethod printTestMethod = testJarClass.getDeclaredMethod("printTest");
            // 修改该方法的内容
            printTestMethod.setBody("System.out.println(\"hello obo\");");




            ///////////////////////////////////
            //  使用反射测试输出,查看修改结果
            ///////////////////////////////////
            Class newTestJarClass = testJarClass.toClass();
            // 使用修改过的类创建对象
            Object newTestJar = newTestJarClass.newInstance();
            Method newPrintTestMethod = newTestJarClass.getDeclaredMethod("printTest");
            newPrintTestMethod.invoke(newTestJar);

            // 解除代码锁定,恢复可编辑状态
            testJarClass.defrost();
            // 写出到外存中
            testJarClass.writeFile();
            // testJarClass.writeFile(other path);

        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] agrs){
//        Reflections reflections = new Reflections(new ConfigurationBuilder()
//                .setUrls(ClasspathHelper.forResource("D:\\IDEAProjects\\mw-grayscale-dubbo\\target\\classes"))
//                .setScanners(new SubTypesScanner(),
//                        new TypeAnnotationsScanner()));
//
//        // 获取资源
//        Set<String> properties = reflections.getResources(Pattern.compile(".*\\.properties"));
//        System.out.println(properties);
    }
}
