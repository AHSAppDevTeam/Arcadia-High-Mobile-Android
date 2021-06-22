package com.hsappdev.ahs.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class BarcodeDrawable extends Drawable {
    private final Paint blackPaint = new Paint();
    private final Path codePath = new Path();
    private final String codeData;
    private final int userIdLength = 5;
    private final int codeDigitLength = 6;
    private final int codeDataLength = ( userIdLength + 2 ) * codeDigitLength;

    public BarcodeDrawable(int userId) {
        // Create path data from id
        final String codeDelimiter = "1 1001";
        final StringBuilder codeBuilder = new StringBuilder(codeDelimiter);
        for(int i = 0; i < userIdLength; i++){
            final String[] codeDigits = new String[]{
                    "11 001",
                    "01 110",
                    "10 110",
                    "00 111",
                    "11 010",
                    "01 011",
                    "10 011",
                    "11 100",
                    "01 101",
                    "10 101",
            };
            codeBuilder.append(codeDigits[userId % 10]); // Extract unit digit as index
            userId /= 10; // Remove unit digit
        }
        codeBuilder.append(codeDelimiter);
        codeData = codeBuilder.toString();

        blackPaint.setColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas) {

        // Get the drawable's bounds
        final float width = getBounds().width();
        final float height = getBounds().height();

        // Unit length
        final int viewportWidth = 208;
        final float unit = width / viewportWidth;

        // Draw from path data, with horizontal cursor offset set at 0
        float horizontalOffset = 0f;
        for(int i = 0; i < codeDataLength; i++){
            final char type = codeData.charAt(i);

            final int narrowWidth = 2;
            final int wideWidth = 5;
            final int spaceWidth = 2;
            final float stripeWidth = unit * (
                type == ' ' ? spaceWidth :
                type == '0' ? wideWidth :
                type == '1' ? narrowWidth : 0
            );

            // draw black line if stripe type is not space
            if(type != ' ') {
                codePath.addRect(
                        horizontalOffset,
                        0f,
                        horizontalOffset + stripeWidth,
                        height,
                        Path.Direction.CW
                );
            }

            // move cursor rightwards
            final float gapWidth = 2 * unit;
            horizontalOffset += stripeWidth + gapWidth;
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
