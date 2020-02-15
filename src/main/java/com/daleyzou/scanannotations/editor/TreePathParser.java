package com.daleyzou.scanannotations.editor;

import javax.swing.tree.TreePath;
import java.util.Arrays;

public class TreePathParser {
    public static String getRealPath(TreePath path) {
        Object[] objPathArr = Arrays.copyOfRange(path.getPath(), 1, path.getPath().length);
        StringBuilder builder = new StringBuilder();
        for (Object obj : objPathArr) {
            builder.append(obj);
            builder.append("/");
        }
        String realPath = builder.toString();
        if(realPath.length()  > 0)
            return realPath.substring(0, realPath.length() - 1);
        return realPath;
    }

    public static String getClassNameFromRealName(String realPath) {
        realPath = realPath.substring(0, realPath.lastIndexOf("."));
        return realPath.replace("/", ".");
    }
}
