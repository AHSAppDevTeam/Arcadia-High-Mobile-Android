package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ScreenUtil;

public class ArticleActivity extends AppCompatActivity {

    static final String data_KEY = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        overridePendingTransition(R.anim.enter_from_right, R.anim.empty_animation);
        Article article = getIntent().getParcelableExtra(data_KEY);
        View outer = findViewById(R.id.article_linearLayout);
        outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
            }
        });
        TextView title = findViewById(R.id.article_title);
        TextView author = findViewById(R.id.article_author);
        TextView body = findViewById(R.id.article_body);
        title.setText(article.getTitle());
        author.setText("By " + article.getAuthor());
        ScreenUtil.setHTMLStringToTextView(article.getBody(), body);

        // Toolbar
        MaterialToolbar articleToolbar = (MaterialToolbar) findViewById(R.id.article_topAppBar);
        articleToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
            }
        });
        articleToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.article_toolbar_font:
                        // TODO: handle action
                        return true;
                    case R.id.article_toolbar_theme:
                        // TODO: handle action
                        return true;
                    case R.id.article_toolbar_saved:
                        // TODO: handle action
                        return true;
                }
                return false;
            }
        });

        // Set details for toolbar
        articleToolbar.setTitle(Helper.getSpanBoldRegularText(article.getCategoryDisplayName(), " News"));
        articleToolbar.setTitleTextAppearance(this, R.style.ArticleAppBarTitleFont);
        boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        if(isNightModeOn){
            articleToolbar.setTitleTextColor(article.getCategoryDisplayColor()[0]);
        }else{
            articleToolbar.setTitleTextColor(article.getCategoryDisplayColor()[1]);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Animation on back press
        overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
    }
}