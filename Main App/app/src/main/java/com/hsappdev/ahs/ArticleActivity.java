package com.hsappdev.ahs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.util.ScreenUtil;

import org.intellij.lang.annotations.JdkConstants;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements Adjusting_TextView.hello {

    private static final String TAG = "ArticleActivity";

    static final String data_KEY = "0";
    private boolean fontBarIsOpen = false;
    private final int FONT_BAR_MIN = 18;
    private final int FONT_BAR_MAX = 54;

    PopupWindow fontBarWindow;

    private ArrayList<Adjusting_TextView.TextSizeCallback> callbackList= new ArrayList<>();;
    @Override
    public void addTextSizeCallback(Adjusting_TextView.TextSizeCallback callback) {
        callbackList.add(callback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        final ArticleViewModel viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        viewModel.getTextScalar().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float newTextScalar) {
                for(Adjusting_TextView.TextSizeCallback callback: callbackList) {
                    callback.onTextSizeChanged(newTextScalar);
                }
            }
        });

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
                dismissFontWindow();
                finish();
                overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
            }
        });
        final Context context = this;

        final SeekBar fontSeekbar;
        articleToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.article_toolbar_font:
                        // TODO: handle action
                        /*if(fontBarIsOpen){
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
                        }*/
                        View view = getLayoutInflater().inflate(R.layout.article_textsizeadjustor, null);

                        fontBarWindow = new PopupWindow(context);
                        fontBarWindow.setContentView(view);

                        // Fontbar
                        SeekBar fontSeekbar = view.findViewById(R.id.article_font_adjuster_seekbar);
                        int progress = (int) (fontSeekbar.getMax()*(viewModel.getTextScalar().getValue()-0.5f));
                        fontSeekbar.setProgress(progress);
                        TextView fontSizeDisplay = view.findViewById(R.id.article_font_size_display);
                        fontSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                float convertedProgress = ((float) progress)/seekBar.getMax() + 0.5f;
                                Log.d(TAG, String.valueOf(convertedProgress));
                                viewModel.setTextScalar(convertedProgress);
                                fontSizeDisplay.setText(Integer.toString(progress));
                                /*int range = FONT_BAR_MAX - FONT_BAR_MIN;
                                int convertedProgress = (int)(progress/100f*range) + FONT_BAR_MIN;

                                body.setTextSize(TypedValue.COMPLEX_UNIT_SP, convertedProgress);*/
                                /*float scale = ((float) progress)/(seekBar.getMax());
                                adjustFontScale(getResources().getConfiguration(), scale);*/
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                /*fontBar.animate().translationY(-100).alpha(0).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        fontBar.setVisibility(LinearLayout.GONE);
                                    }
                                });
                                fontBarIsOpen = false;*/
                                fontBarWindow.dismiss();
                            }
                        });

                        // make shadow effect
                        /*window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            window.setElevation(10);
                        }*/

                        /*window.showAsDropDown(articleToolbar, 0,200);*/
                        fontBarWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.article_font_bar_background));
                        fontBarWindow.setElevation(getResources().getDimension(R.dimen.elevation));

                        fontBarWindow.setOutsideTouchable(true);
                        fontBarWindow.setTouchable(true);

                        int padding = (int) getResources().getDimension(R.dimen.padding);
                        fontBarWindow.setWidth( (articleToolbar.getWidth() - 2*padding));
                        fontBarWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                        fontBarWindow.showAsDropDown(articleToolbar, padding, padding/2);
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

    private void dismissFontWindow() {
        if(fontBarWindow != null) {
            fontBarWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissFontWindow();
        // Animation on back press
        overridePendingTransition(R.anim.empty_animation, R.anim.exit_to_right);
    }

    public void adjustFontScale(Configuration config, float scale) {
        config.fontScale = scale;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        metrics.scaledDensity = config.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(config, metrics);
    }
}