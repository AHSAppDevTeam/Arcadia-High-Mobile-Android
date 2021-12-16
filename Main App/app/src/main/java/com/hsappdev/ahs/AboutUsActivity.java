package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hsappdev.ahs.util.ScreenUtil;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        TextView programmers = findViewById(R.id.programmers_list);
        ScreenUtil.setHTMLStringToTextView(getResources().getString(R.string.programmers_list), programmers);

        TextView graphicDesigners = findViewById(R.id.graphic_designers_list);
        ScreenUtil.setHTMLStringToTextView(getResources().getString(R.string.graphic_designers_list), graphicDesigners);

        TextView contentEditors = findViewById(R.id.content_editors_list);
        ScreenUtil.setHTMLStringToTextView(getResources().getString(R.string.content_editors_list), contentEditors);

        TextView previousMembers = findViewById(R.id.old_member_list);
        ScreenUtil.setHTMLStringToTextView(getResources().getString(R.string.previous_members_list), previousMembers);

        ImageButton homeButton = findViewById(R.id.about_us_activity_home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}