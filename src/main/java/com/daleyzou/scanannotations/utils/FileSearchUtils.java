package com.daleyzou.scanannotations.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wuxiaozeng
 *
 */
public class FileSearchUtils {

    /**
     * 在指定目录中查找包含关键字的文件(或查找后缀名为XXX的文件)，返回包含指定关键字的文件路径. String keyword
     */
    public static List<File> searchByFileDuff(File folder, String duff) {
	List<File> result = new ArrayList<File>();
	if (folder.isFile()) {
	    result.add(folder);
	}
	File[] subFolders = folder.listFiles(new FileFilter() {
	    @Override
	    public boolean accept(File file) {
		if (file.isDirectory()) {
		    return true;
		}
		if (file.getName().toLowerCase().endsWith(duff)) {
		    return true;
		}
		return false;
	    }
	});
	if (subFolders != null) {
	    for (File file : subFolders) {
		if (file.isFile()) {
		    // 如果是文件则将文件添加到结果列表中
		    result.add(file);
		} else {
		    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
		    result.addAll(searchByFileDuff(file, duff));
		}
	    }
	}
	return result;
    }

    public static List<String> searchByFileDuff1(File folder, String duff) {
	List<String> files = new ArrayList<>();
	findFile(folder, files);
	return files;
    }

    private static void findFile(File file, List<String> result) {
	File[] listFiles = file.listFiles();
	for (int i = 0; i < listFiles.length; i++) {
	    if (listFiles[i].isDirectory()) {
		findFile(listFiles[i], result);
	    } else if (listFiles[i].getName().endsWith(".java")) {
		result.add(listFiles[i].getAbsolutePath());
	    }
	}
    }

    public static List<String> searchByFileContent(File folder, Object[] words, String duff) {
	List<String> result = new ArrayList<>();
	findFile(folder, words, result);
	return result;
    }

    private static void findFile(File file, Object[] words, List<String> result) {
	File[] listFiles = file.listFiles();
	for (int i = 0; i < listFiles.length; i++) {
	    if (listFiles[i].isDirectory()) {
		findFile(listFiles[i], words, result);
	    } else if ((listFiles[i].getName().endsWith(".java"))) {
		search(listFiles[i], words, result);
	    }
	}
    }

    private static void search(File file, Object[] words, List<String> result) {
	try {
	    int ch = 0;
	    String str = null;
	    FileReader in = new FileReader(file);
	    while ((ch = in.read()) != -1) {
		str += (char) ch;
	    }
	    for (Object word : words) {
		if (str.contains(word.toString())) {
		    result.add(file.getAbsolutePath());
		    break;
		}
	    }
	    in.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	String filePath = "E:\\workspace-gs\\WildCard";
	// 创建一个 File 实例，表示路径名是指定路径参数的文件
	File file = new File(filePath);
	String[] words = new String[] { "@Autowired", "@Resource" };

	List<String> result = searchByFileContent(file, words, "java");
	System.out.println(result);

    }

}
