package com.hsappdev.themetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // See https://blog.prototypr.io/implementing-dark-theme-in-android-dfe63e62145d

    // In layout files, using the tools:theme attribute lets you update the preview, and notice the use of ?attrs/### in anything that uses colors
    // see attrs file and styles file in values

    private static final String themeFile = "themeFile";// better to do stuff in R.string, too lazy in this example lol
    private static final String themeKey = "themeKile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check light/ dark theme setting and update AppCompatDelegate
        SharedPreferences sharedPreferences = this.getSharedPreferences(themeFile, MODE_PRIVATE);

        // final ok since any changes results in activity restart
        // default should be light theme
        final boolean isNightThemeOn = sharedPreferences.getBoolean(themeKey, false);
        if(isNightThemeOn) {
            setTheme(R.style.DarkTheme);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            setTheme(R.style.LightTheme);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // do all this before drawing

        setContentView(R.layout.activity_main);


        TextView themeChanger = findViewById(R.id.theme_selector);
        if(isNightThemeOn)
            themeChanger.setText("Change to Light Theme");
        else
            themeChanger.setText("Change to Dark Theme");
        themeChanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNightThemeOn) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                updateThemeSetting(!isNightThemeOn);
                finish();

                // having to restart activity might be a little problematic later, but that's what the article recommends
                startActivity(new Intent(MainActivity.this, MainActivity.this.getClass()));
            }
        });
    }

    // what you'd expect
    private void updateThemeSetting(boolean isNightMode) {
        SharedPreferences.Editor editor = getSharedPreferences(themeFile, MODE_PRIVATE).edit();
        editor.putBoolean(themeKey, isNightMode).apply();
    }
}