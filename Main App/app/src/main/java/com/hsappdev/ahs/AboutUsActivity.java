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


        // example

        DatabaseReference ref = FirebaseDatabase.getInstance(FirebaseApp.getInstance(DatabaseConstants.FIREBASE_REALTIME_DB))
                .getReference()
                .child(getString(R.string.db_credits));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String programmers_list = "";
                String graphic_designers_list = "";
                String content_editors_list = "";
                String previous_members_list = "";//use a hashmap?
                for (DataSnapshot person : snapshot.getChildren()) {
                    String name = person.child("name").getValue(String.class);
                    boolean isRetired = person.child("retired").getValue(Boolean.class);
                    String role = person.child("role").getValue(String.class);
                    boolean hasUrl = person.child("url").exists();
                    String url;
                    if (hasUrl) {
                        url = person.child("url").getValue(String.class);
                    }
                    if (isRetired) {
                        previous_members_list += "<br/>" + name;
                    }
                    else {
                        if (role.contains("programmer")) { //change to actual role
                            programmers_list += "<br/>" + name; //add the a tag for url
                        }
                        if (role.equals("designer")) { //change to actual role
                            graphic_designers_list += "<br/>" + name;
                        }
                        if (role.equals("editor")) {
                            content_editors_list += "<br/>" + name;
                        }
                    }

                }
                programmers_list = "<![CDATA["+ programmers_list.substring(5) + "]]>";
                graphic_designers_list = "<![CDATA["+ graphic_designers_list.substring(5) + "]]>";
                content_editors_list = "<![CDATA["+ content_editors_list.substring(5) + "]]>";
                previous_members_list = "<![CDATA["+ previous_members_list.substring(5) + "]]>";

                ScreenUtil.setHTMLStringToTextView(programmers_list, programmers);
                ScreenUtil.setHTMLStringToTextView(graphic_designers_list, graphicDesigners);
                ScreenUtil.setHTMLStringToTextView(content_editors_list, contentEditors);
                ScreenUtil.setHTMLStringToTextView(previous_members_list, previousMembers);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}