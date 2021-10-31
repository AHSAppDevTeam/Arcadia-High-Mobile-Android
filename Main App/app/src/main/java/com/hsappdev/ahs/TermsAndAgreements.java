package com.hsappdev.ahs;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

public class TermsAndAgreements extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_agreements);

        TextView terms = findViewById(R.id.terms_textView);
        terms.setText(HtmlCompat.fromHtml(getResources().getString(R.string.terms), HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE));

        ImageButton homeButton = findViewById(R.id.terms_activity_home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}