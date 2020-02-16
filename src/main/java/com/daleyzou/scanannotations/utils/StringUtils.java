package com.daleyzou.scanannotations.utils;

/**
 * 
 * @author wuxiaozeng
 *
 */
public class StringUtils {

    public static final int INDEX_NOT_FOUND = -1;
    public static final String EMPTY = "";

    public static void main(String[] args) throws Exception {
	System.out.println(StringUtils.substringBefore("dubbo-admin-diy-private-test-766b854f9f-n2rgq", "-diy-private-test"));
	System.out.println(StringUtils.substringAfterLast("com.gaosi.api.azeroth.service.old.StorageOldService", "."));
    }

    public static String substringBetween(final String str, final String open, final String close) {
	if (str == null || open == null || close == null) {
	    return null;
	}
	final int start = str.indexOf(open);
	if (start != INDEX_NOT_FOUND) {
	    final int end = str.indexOf(close, start + open.length());
	    if (end != INDEX_NOT_FOUND) {
		return str.substring(start + open.length(), end);
	    }
	}
	return null;
    }

    public static String substringAfterLast(final String str, final String separator) {
	final int pos = str.lastIndexOf(separator);
	if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
	    return EMPTY;
	}
	return str.substring(pos + separator.length());
    }

    public static String substringBefore(final String str, final String separator) {
	if (isEmpty(str) || separator == null) {
	    return str;
	}
	if (separator.isEmpty()) {
	    return EMPTY;
	}
	final int pos = str.indexOf(separator);
	if (pos == INDEX_NOT_FOUND) {
	    return str;
	}
	return str.substring(0, pos);
    }

    public static String replace(final String text, final String searchString, final String replacement) {
	return replace(text, searchString, replacement, -1);
    }

    public static String replace(final String text, final String searchString, final String replacement, int max) {
	if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
	    return text;
	}
	int start = 0;
	int end = text.indexOf(searchString, start);
	if (end == INDEX_NOT_FOUND) {
	    return text;
	}
	final int replLength = searchString.length();
	int increase = replacement.length() - replLength;
	increase = increase < 0 ? 0 : increase;
	increase *= max < 0 ? 16 : max > 64 ? 64 : max;
	final StringBuilder buf = new StringBuilder(text.length() + increase);
	while (end != INDEX_NOT_FOUND) {
	    buf.append(text.substring(start, end)).append(replacement);
	    start = end + replLength;
	    if (--max == 0) {
		break;
	    }
	    end = text.indexOf(searchString, start);
	}
	buf.append(text.substring(start));
	return buf.toString();
    }

    public static boolean isEmpty(final CharSequence cs) {
	return cs == null || cs.length() == 0;
    }

    public static boolean isBlank(final CharSequence cs) {
	int strLen;
	if (cs == null || (strLen = cs.length()) == 0) {
	    return true;
	}
	for (int i = 0; i < strLen; i++) {
	    if (!Character.isWhitespace(cs.charAt(i))) {
		return false;
	    }
	}
	return true;
    }

    public static boolean containsAny(final CharSequence cs, final CharSequence... searchCharSequences) {
	if (isEmpty(cs)) {
	    return false;
	}
	for (final CharSequence searchCharSequence : searchCharSequences) {
	    if (contains(cs, searchCharSequence)) {
		return true;
	    }
	}
	return false;
    }

    public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
	if (seq == null || searchSeq == null) {
	    return false;
	}
	return indexOf(seq, searchSeq, 0) >= 0;
    }

    static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
	return cs.toString().indexOf(searchChar.toString(), start);
    }

}
