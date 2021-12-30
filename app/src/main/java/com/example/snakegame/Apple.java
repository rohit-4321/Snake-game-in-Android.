package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Apple {
    private Point positionOfApple = new Point();
    private Bitmap appleBitmap;
    private int BlOCK_SIZE;
    private int NUM_BLOCK_WIDE;
    private int NUM_BLOCK_HIGH;
    Apple(Context context, int NUM_BLOCK_WIDE , int NUM_BLOCK_HIGH , int BlOCK_SIZE)
    {
        this.BlOCK_SIZE = BlOCK_SIZE;
        this.NUM_BLOCK_HIGH= NUM_BLOCK_HIGH;
        this.NUM_BLOCK_WIDE = NUM_BLOCK_WIDE;
        appleBitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.apple);
        appleBitmap = Bitmap.createScaledBitmap(appleBitmap , BlOCK_SIZE , BlOCK_SIZE , true);
    }
    void setApplePosition()
    {
        Random random = new Random();
        positionOfApple.x = random.nextInt(NUM_BLOCK_WIDE) * BlOCK_SIZE;
        positionOfApple.y = random.nextInt(NUM_BLOCK_HIGH) * BlOCK_SIZE;
    }
    Point getPositionOfApple()
    {
        return positionOfApple;
    }

    void drawApple(Canvas canvas , Paint paint)
    {
        canvas.drawBitmap(appleBitmap,
                positionOfApple.x,
                positionOfApple.y,
                paint
        );
    }

}
