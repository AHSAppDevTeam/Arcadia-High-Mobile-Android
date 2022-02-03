package com.hsappdev.ahs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        TextView programmers = findViewById(R.id.programmers_list);
        TextView graphicDesigners = findViewById(R.id.graphic_designers_list);
        TextView contentEditors = findViewById(R.id.content_editors_list);
        TextView previousMembers = findViewById(R.id.old_member_list);

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
                StringBuilder programmers_list = new StringBuilder(), graphic_designers_list = new StringBuilder(), content_editors_list = new StringBuilder(), previous_members_list = new StringBuilder();
                for (DataSnapshot person : snapshot.getChildren()) {
                    String name = person.child("name").getValue(String.class);
                    boolean isRetired = person.child("retired").getValue(Boolean.class);
                    String role = person.child("role").getValue(String.class);
                    boolean hasUrl = person.child("url").exists();
                    String url = "";
                    StringBuilder list;

                    if (isRetired) {
                        list = previous_members_list;
                    }
                    else if (role.contains("programmer")) {
                        list = programmers_list;
                    }
                    else if (role.equals("designer")) {
                        list = graphic_designers_list;
                    }
                    else if (role.equals("editor")) {
                        list = content_editors_list;
                    }
                    else {
                        list = new StringBuilder();
                    }

                    if (hasUrl) {
                        url = person.child("url").getValue(String.class);
                    }

                    list.append("<br/>");
                    if (hasUrl) {
                        list.append("<a href=\"");
                        list.append(url);
                        list.append("\" style=\"text-decoration:none\">");
                    }
                    list.append(name);
                    if (hasUrl) {
                        list.append("</a>");
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