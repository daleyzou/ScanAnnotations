package com.daleyzou.scanannotations.utils;

/**
 * 
 * @author daleyzou
 *
 */
public class StringUtils {

    public static boolean isEmpty(final CharSequence cs) {
	return cs == null || cs.length() == 0;
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
