package com.hsappdev.ahs.util;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.hsappdev.ahs.R;

import java.time.Instant;
import java.time.ZoneId;

public class ScreenUtil {
    public static float px_to_sp(float px, Context context) {
        return px/context.getResources().getDisplayMetrics().scaledDensity;
    }
    public static float dp_to_px(float dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp, context.getResources().getDisplayMetrics());
    }

    public static void setHTMLStringToTextView(String html, TextView textView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
        } else {
            textView.setText(Html.fromHtml(html));
        }
    }

    public static void setPlainHTMLStringToTextView(String html, TextView textView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT).toString());
        } else {
            textView.setText(Html.fromHtml(html));
        }

    }

    public static void setTimeToTextView(long timestamp, TextView view){
        long time = (long) (System.currentTimeMillis()/1000f);
        Log.d("time", time+"");
        String timeText = "";

        final int second = 1, minute = second*60, hour = minute*60, day = hour*24, week = day*7, month = day*30;

        // Configurable vars
        final int justNowInterval = 10; // Threshold for when a time should be considered as "just now"
        final String justNowMessage = "Just Now"; // What to display for "just now"
        final String pastMessage = " ago";
        final String futureMessage = " ahead";

        long diff = time - timestamp;

        if (time < timestamp) {
            // Future
            diff *= -1;
        }
        boolean justNow = false;
        int tempTime = 0;
        if(diff<justNowInterval){
            justNow = true;
            timeText = justNowMessage;
        } else if(diff<hour) {
            tempTime = (int) Math.floor(diff/minute);
            timeText = tempTime + " min";
        } else if(diff<day) {
            tempTime = (int) Math.floor(diff/hour);
            timeText = tempTime + " hour";
        } else if(diff<week) {
            tempTime = (int) Math.floor(diff/day);
            timeText = tempTime + " day";
        } else if(diff<month) {
            tempTime = (int) Math.floor(diff/week);
            timeText = tempTime + " week";
        } else {
            tempTime = (int) Math.floor(diff/month);
            timeText = tempTime + " month";
        }

        // Plural
        timeText += (tempTime == 1? "":"s");

        if(!justNow) {
            if (time > timestamp) {
                // Past
                timeText += pastMessage;
            } else {
                // Future
                timeText += futureMessage;
            }
        }

        view.setText(timeText);

    }
}
