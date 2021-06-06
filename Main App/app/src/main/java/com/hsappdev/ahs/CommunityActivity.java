package com.hsappdev.ahs;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CommunityActivity extends AppCompatActivity {
    public static final String DATA_KEY = "DATA_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity);
        findViewById(R.id.community_activity_go_back_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
            }
        });
    }
}
