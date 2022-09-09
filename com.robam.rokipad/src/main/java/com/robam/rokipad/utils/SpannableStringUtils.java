package com.robam.rokipad.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

public class SpannableStringUtils {

    public static SpannableString createSpannableString(int level) {
        SpannableString spannableString = new SpannableString(level + "档");
        AbsoluteSizeSpan absoluteSizeSpan0 = new AbsoluteSizeSpan(44, false);
        AbsoluteSizeSpan absoluteSizeSpan1 = new AbsoluteSizeSpan(18, false);
        if (level < 10) {
            spannableString.setSpan(absoluteSizeSpan0, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(absoluteSizeSpan1, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (level == 10) {
            spannableString.setSpan(absoluteSizeSpan0, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(absoluteSizeSpan1, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    ;

}
