package com.example.draggablesheets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    // Constants
    public static final boolean ANIMATE_BACKGROUND_COLOR = true;

    // Views
    private CardView draggableSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The Draggable Sheet
        draggableSheet = findViewById(R.id.draggableSheet);

        // Initialize Bottom Sheet Behavior
        BottomSheetBehavior<CardView> bottomSheetBehavior = BottomSheetBehavior.from(draggableSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Perform the animation
                if(ANIMATE_BACKGROUND_COLOR) {
                    interpolateBackgroundColor(slideOffset);
                }
            }
        });
    }

    private void interpolateBackgroundColor(float fraction) {
        // interpolate the color values
        int colorStart = Color.parseColor("#4287f5"); // light blue
        int colorEnd = Color.WHITE;

        // interpolate fraction (so that it never fully reaches colorEnd)
        float FRACTION_MAX = .75f;
        fraction *= FRACTION_MAX;

        // color interpolation in bytes (credit: Orkhan Gasimli medium.com)
        int startA = (colorStart >> 24) & 0xff;
        int startR = (colorStart >> 16) & 0xff;
        int startG = (colorStart >> 8) & 0xff;
        int startB = colorStart & 0xff;
        int endA = (colorEnd >> 24) & 0xff;
        int endR = (colorEnd >> 16) & 0xff;
        int endG = (colorEnd >> 8) & 0xff;
        int endB = colorEnd & 0xff;
        int finalColor = ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));

        draggableSheet.setCardBackgroundColor(finalColor);
    }
}