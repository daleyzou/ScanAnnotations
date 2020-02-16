package com.daleyzou.scanannotations.scantest;

import javassist.*;
import javassist.bytecode.*;
import jdk.nashorn.internal.objects.annotations.Constructor;
import org.assertj.core.util.Lists;


import javax.websocket.server.PathParam;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * DemoTest
 * @description TODO
 * @author daleyozu
 * @date 2020年02月14日 0:19
 * @version 1.1.11
 */
public class DemoTest {
    // 修改方法
    public void changeMethode() throws NotFoundException, IOException {

        ///////////////////////////////////
        //  使用javaassist修改 class/jar 代码
        ///////////////////////////////////
        //  设置jar包路径
        ClassPool classPool = ClassPool.getDefault();
        ClassPath classPath = classPool
                .insertClassPath("D:\\IDEAProjects\\work\\gaosiedu\\mw-grayscale-dubbo\\target\\mw-grayscale-dubbo-1.0.1-SNAPSHOT.jar");
//        Object o = ((ArrayList) (classPath.jarfileEntries).get(198);
//        System.out.println(classPath.toString());
        //        DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
        //        ClassFile cls = new ClassFile(dis);

        // 如果CtClass通过writeFile(),toClass(),toBytecode()转换了类文件，javassist冻结了CtClass对象。
        // 以后是不允许修改这个 CtClass对象。这是为了警告开发人员当他们试图修改一个类文件时，已经被JVM载入的类不允许被重新载入。
        CtClass clazz = classPool.get("BOOT-INF.classes.com.gaosiedu.middleware.mwgrayscaledubbo.service.IterationService");

        ClassFile cls = clazz.getClassFile();

        //获取方法
        List<MethodInfo> methods = cls.getMethods();

        //获取属性
        List<FieldInfo> fields = cls.getFields();

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

        //        Reflections reflections = new Reflections("com.gaosiedu.");
        //
        //        // 获取资源
        //        Set<String> properties = reflections.getResources(Pattern.compile(".*\\.properties"));
        //
        //
        //        Set<Method> someMethods =
        //                reflections.getMethodsMatchParams(long.class, int.class);
        //        Set<Method> voidMethods =
        //                reflections.getMethodsReturn(void.class);
        //        Set<Method> pathParamMethods =
        //                reflections.getMethodsWithAnyParamAnnotated(PathParam.class);

        System.out.println("end");
    }

    public static void main(String[] args) throws NotFoundException, IOException {
        DemoTest demoTest = new DemoTest();

        demoTest.changeMethode();
    }
}
