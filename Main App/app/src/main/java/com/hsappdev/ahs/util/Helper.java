package com.hsappdev.ahs.util;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.recyclerview.widget.SortedList;

import com.hsappdev.ahs.dataTypes.Article;

public class Helper {
    public static void setBoldRegularText(TextView textView, String boldText, String regularText) {
        SpannableStringBuilder builder = new SpannableStringBuilder(boldText);
        builder.setSpan(new StyleSpan(Typeface.BOLD),0,boldText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
        textView.append(regularText);
    }

    public static SpannableStringBuilder getSpanBoldRegularText(String boldText, String regularText) {
        SpannableStringBuilder builder = new SpannableStringBuilder(boldText);
        builder.setSpan(new StyleSpan(Typeface.BOLD),0,boldText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder.append(regularText);
    }

}
