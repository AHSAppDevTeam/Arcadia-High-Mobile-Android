package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.ScreenUtil;

public class ArticleActivity extends AppCompatActivity {

    private static final String TAG = "ArticleActivity";

    static final String data_KEY = "0";
    private boolean fontBarIsOpen = false;
    private final int FONT_BAR_MIN = 18;
    private final int FONT_BAR_MAX = 54;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        overridePendingTransition(R.anim.enter_from_right, R.anim.empty_animation);
        Article article = getIntent().getParcelableExtra(data_KEY);
        View outer = findViewById(R.id.article_linearLayout);
        TextView title = findViewById(R.id.article_title);
        TextView author = findViewById(R.id.article_author);
        TextView body = findViewById(R.id.article_body);
        LinearLayout fontBar = findViewById(R.id.article_font_adjuster);
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
                        if(fontBarIsOpen){
                            fontBar.animate().translationY(-100).alpha(0).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    fontBar.setVisibility(LinearLayout.GONE);
                                }
                            });
                            fontBarIsOpen = false;

                        } else {
                            fontBar.animate().translationY(0).alpha(1);
                            fontBarIsOpen = true;
                            fontBar.setVisibility(LinearLayout.VISIBLE);
                        }
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

        // Fontbar
        SeekBar fontSeekbar = fontBar.findViewById(R.id.article_font_adjuster_seekbar);
        TextView fontSizeDisplay = fontBar.findViewById(R.id.article_font_size_display);
        fontSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int range = FONT_BAR_MAX - FONT_BAR_MIN;
                int convertedProgress = (int)(progress/100f*range) + FONT_BAR_MIN;
                fontSizeDisplay.setText(Integer.toString(convertedProgress));
                body.setTextSize(TypedValue.COMPLEX_UNIT_SP, convertedProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fontBar.animate().translationY(-100).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        fontBar.setVisibility(LinearLayout.GONE);
                    }
                });
                fontBarIsOpen = false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Animation on back press
        overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
    }
}