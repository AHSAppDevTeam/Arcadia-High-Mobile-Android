package com.example.youtubetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private YoutubeVideoCallback<YouTubeFragment> youtubeVideoCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewpager = findViewById(R.id.viewPager);

        this.youtubeVideoCallback = new YoutubeVideoCallback<>();

        Media[] mediaList = new Media[4];
        mediaList[0] = new Media("W4hTJybfU7s", true);
        mediaList[1] = new Media("https://media.discordapp.net/attachments/787088938933157952/848096637078994974/unknown.png", false);
        mediaList[2] = new Media("VriiDn676PQ", true);
        mediaList[3] = new Media("AeJ9q45PfD0", true);


        ImageVideoAdapter imageVideoAdapter = new ImageVideoAdapter(getSupportFragmentManager(), getLifecycle(), youtubeVideoCallback, viewpager, mediaList);

        viewpager.setAdapter(imageVideoAdapter);



    }


}