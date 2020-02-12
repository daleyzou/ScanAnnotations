package com.daleyzou.scanannotations.scantest;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

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



    public static void main(String[] agrs){
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forResource("D:\\IDEAProjects\\mw-grayscale-dubbo\\target\\classes"))
                .setScanners(new SubTypesScanner(),
                        new TypeAnnotationsScanner()));

        // 获取资源
        Set<String> properties = reflections.getResources(Pattern.compile(".*\\.properties"));
        System.out.println(properties);
    }
}
