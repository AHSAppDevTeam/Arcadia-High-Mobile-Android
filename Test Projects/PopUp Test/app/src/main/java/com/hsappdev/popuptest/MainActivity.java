package com.hsappdev.popuptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView title, bodyText;
    private int seekBarProgress = 0; // this default should be initialized in a better way
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.sampleTitle);
        bodyText = findViewById(R.id.sampleBody);
        final ImageView popupBtn = findViewById(R.id.make_popup_btn);
        final ViewGroup popupRoot = findViewById(R.id.btn_root);
        final Context context = this;
        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow window = new PopupWindow(context);
                View view = getLayoutInflater().inflate(R.layout.popup_layout, null);

                // measure the view for positioning of window (see below)
                // since this view is not drawn yet until show is called on window
                // but you still need the dimensions of the view anyway
                view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                window.setContentView(view);

                // close window on click checkmark image
                final ImageView popupImg = view.findViewById(R.id.popUp_img);
                popupImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                    }
                });

                // initialize seekbar and set its original position (eventually from stored values, not here tho)
                final SeekBar seekBar = view.findViewById(R.id.seekBar);
                final TextView seekBarIndicator = view.findViewById(R.id.seekBarProgressIndicator);
                seekBarIndicator.setText(Integer.toString(seekBarProgress));
                seekBar.setProgress(seekBarProgress);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekBarProgress = progress;
                        seekBarIndicator.setText(Integer.toString(progress));
                        updateText(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                //dismiss window on touch outside
                window.setOutsideTouchable(true);
                window.setFocusable(true);

                // make shadow effect
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setElevation(10);
                }
                // show window below popuproot but centered on the screen
                window.showAsDropDown(popupRoot,popupRoot.getWidth()/2-view.getMeasuredWidth()/2,0);


            }
        });
    }

    // these are just random values for demonstration
    private static final int largeRangeMin = 18;
    private static final int largeRangeMax = 54;
    private static final int smallRangeMin = 14;
    private static final int smallRangeMax = 42;
 
    private void updateText(int progress) {
        title.setTextSize((largeRangeMax-largeRangeMin)*progress/10.0f+largeRangeMin);
        bodyText.setTextSize((smallRangeMax-smallRangeMin)*progress/10.0f+smallRangeMin);
    }
}