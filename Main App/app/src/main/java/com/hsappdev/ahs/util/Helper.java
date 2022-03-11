package com.hsappdev.ahs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.recyclerview.widget.SortedList;

import com.hsappdev.ahs.MainActivity;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.dataTypes.Article;

public class Helper {
    public static void setBoldRegularText(TextView textView, String boldText, String regularText) {
        SpannableStringBuilder builder = new SpannableStringBuilder(boldText);
        builder.setSpan(new StyleSpan(Typeface.BOLD),0,boldText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
        textView.append(regularText);
    }

    public static SpannableStringBuilder getSpanBoldRegularText(String boldText, String regularText) {
        if(boldText == null) boldText = "";
        SpannableStringBuilder builder = new SpannableStringBuilder(boldText);
        builder.setSpan(new StyleSpan(Typeface.BOLD),0,boldText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder.append(regularText);
    }


    /**
     * All credits to: http://rosettacode.org/wiki/Levenshtein_distance#Java
     * Calculates Levenshtein Distance
     * @param a
     * @param b
     * @return
     */
    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public static boolean isFirstTimeAppLoad(MainActivity mainActivity, Resources r) {
        SharedPreferences sharedPref = mainActivity.getPreferences(Context.MODE_PRIVATE);
        boolean firstTime = sharedPref.getBoolean(r.getString(R.string.isFirstTimeAppLoad), true);
        // Set value to false
        if(firstTime) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(r.getString(R.string.isFirstTimeAppLoad), false);
            editor.apply();
        }
        return firstTime;
    }
}
