package com.hsappdev.ahs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsappdev.ahs.db.DatabaseConstants;
import com.hsappdev.ahs.util.ScreenUtil;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.about_us_gradient).getBackground();
        animationDrawable.setEnterFadeDuration(10);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        TextView programmers = findViewById(R.id.programmers_list);
        TextView graphicDesigners = findViewById(R.id.graphic_designers_list);
        TextView contentEditors = findViewById(R.id.content_editors_list);
        TextView previousMembers = findViewById(R.id.old_member_list);
        TextView contacts = findViewById(R.id.contacts_list);
        ScreenUtil.setHTMLStringToTextView(getResources().getString(R.string.contacts_list), contacts);

        ImageButton homeButton = findViewById(R.id.about_us_activity_home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB))
                .getReference()
                .child(getString(R.string.db_credits));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String programmers_list = "", graphic_designers_list = "", content_editors_list = "", previous_members_list = "";
                for (DataSnapshot person : snapshot.getChildren()) {
                    String name = person.child("name").getValue(String.class);
                    boolean isRetired = person.child("retired").getValue(Boolean.class);
                    String role = person.child("role").getValue(String.class);
                    boolean hasUrl = person.child("url").exists();
                    String url = "";
                    String list = "";

                    if (hasUrl) {
                        url = person.child("url").getValue(String.class);
                    }

                    list += ("<br/>");
                    if (hasUrl) {
                        list+=("<a href=\"");
                        list+=(url);
                        list+=("\">");
                    }
                    list += (name);
                    if (hasUrl) {
                        list += ("</a>");
                    }

                    if (isRetired) {
                        previous_members_list += list;
                    }
                    else if (role.contains("programmer")) {
                        programmers_list += list;
                    }
                    else if (role.equals("designer")) {
                        graphic_designers_list += list;
                    }
                    else if (role.equals("editor")) {
                        content_editors_list += list;
                    }
                }
                ScreenUtil.setHTMLStringToTextView(programmers_list.toString().substring(5), programmers);
                ScreenUtil.setHTMLStringToTextView(graphic_designers_list.toString().substring(5), graphicDesigners);
                ScreenUtil.setHTMLStringToTextView(content_editors_list.toString().substring(5), contentEditors);
                ScreenUtil.setHTMLStringToTextView(previous_members_list.toString().substring(5), previousMembers);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}