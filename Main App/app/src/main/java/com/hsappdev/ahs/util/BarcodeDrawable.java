package com.hsappdev.ahs.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class BarcodeDrawable extends Drawable {
    private static final Paint blackPaint = new Paint();
    private static final int userIdLength = 5;

    // Represent each codeDigit as a 12-bit integer,
    // with the bits evenly distributed among members
    // of a size-6 set of narrow bands, wide bands,
    // and white spaces.
    //
    // 00 = 0: white space
    // 01 = 1: narrow band
    // 10 = 2: wide band

    private static final int codeDataLength = userIdLength + 2;
    private static final int codeDigitLength = 6;
    private static final short codeDelimiter = 0b01_00_01_10_10_01;
    private static final short[] codeDigits = new short[]{
        0b01_01_00_10_10_01,
        0b10_01_00_01_01_10,
        0b01_10_00_01_01_10,
        0b10_10_00_01_01_01,
        0b01_01_00_10_01_10,
        0b10_01_00_10_01_01,
        0b01_10_00_10_01_01,
        0b01_01_00_01_10_10,
        0b10_01_00_01_10_01,
        0b01_10_00_01_10_01,
    };
    private static final int viewportWidth = 208;
    private static final int[] stripeWidths = new int[]{
        2, // white space
        2, // narrow band
        5, // wide band
    };

    private final short[] codeData = new short[codeDataLength];
    private final Path codePath = new Path();

    public BarcodeDrawable() {

        codeData[0] = codeData[codeDataLength - 1] = codeDelimiter;

        // Set paint color
        blackPaint.setColor(Color.BLACK);

    }

    public void setUserId(int userId) {

        // Create path data from id
        // Go from least to most significant digit
        for(int i = userIdLength; i > 0; i--){

             // Extract unit digit as index
            codeData[i] = codeDigits[userId % 10];

            // Remove unit digit
            userId /= 10;
        }

    }

    @Override
    public void draw(Canvas canvas) {

        codePath.reset();

        // Get the drawable's bounds
        final float width = getBounds().width();
        final float height = getBounds().height();

        // Unit length
        final float unit = width / viewportWidth;

        // Draw from path data, with horizontal cursor offset set at 0
        float horizontalOffset = 0f;

        final float gapWidth = 2 * unit;

        for(int i = 0; i < codeDataLength; i++){

            final short codeDigit = codeData[i];

            for(int j = codeDigitLength-1; j >= 0; j--){

                // Extract stripe from codeDigit
                final int stripeType = codeDigit >> 2*j & 0b11;

                // Get width of stripe
                final float stripeWidth = unit * stripeWidths[stripeType];

                // draw black line if stripe type is not space
                if(stripeType != 0) codePath.addRect(
                        horizontalOffset,
                        0f,
                        horizontalOffset + stripeWidth,
                        height,
                        Path.Direction.CW
                );

                // move cursor rightwards
                horizontalOffset += stripeWidth + gapWidth;

            }

        }

        canvas.drawPath(codePath, blackPaint);

    }

    @Override
    public void setAlpha(int alpha) {
        // This method is required
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // This method is required
    }

    @Override
    public int getOpacity() {
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        return PixelFormat.OPAQUE;
    }
}
