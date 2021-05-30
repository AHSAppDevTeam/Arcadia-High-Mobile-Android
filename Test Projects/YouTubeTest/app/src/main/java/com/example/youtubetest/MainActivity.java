package com.example.youtubetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private YoutubeVideoCallback<MediaFragment> youtubeVideoCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewpager = findViewById(R.id.viewPager);

        this.youtubeVideoCallback = new YoutubeVideoCallback<>();

        Media[] mediaList = new Media[4];
        mediaList[0] = new Media("W4hTJybfU7s", true);
        mediaList[1] = new Media("ovJcsL7vyrk", true);
        mediaList[2] = new Media("VriiDn676PQ", true);
        mediaList[3] = new Media("AeJ9q45PfD0", true);


        ImageVideoAdapter imageVideoAdapter = new ImageVideoAdapter(getSupportFragmentManager(), getLifecycle(), youtubeVideoCallback, viewpager, mediaList);

        viewpager.setAdapter(imageVideoAdapter);



    }


}