package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hsappdev.ahs.dataTypes.Article;

public class ArticleActivity extends AppCompatActivity {

    static final String data_KEY = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        Article article = getIntent().getParcelableExtra(data_KEY);
        View outer = findViewById(R.id.article_outerLayout);
        outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView textView = findViewById(R.id.article_textView);
        textView.setText(article.toString());
    }
}