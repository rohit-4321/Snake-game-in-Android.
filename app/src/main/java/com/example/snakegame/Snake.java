package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

public class Snake {
    private  int BlOCK_SIZE;
    private final ArrayList<Point> snakeSegments = new ArrayList<>();
    private Bitmap headRightBitmap;
    private Bitmap headLeftBitmap;
    private Bitmap headUpBitmap;
    private Bitmap headDownBitmap;
    private Point movingRange;
    private Bitmap snakeBodyBitmap;


    Snake(Context context, Point movingRange , int BlOCK_SIZE)
    {
        this.BlOCK_SIZE = BlOCK_SIZE;

        this.movingRange = movingRange;
        Matrix matrix = new Matrix();
        headRightBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.head);

        headRightBitmap= Bitmap.createScaledBitmap(headRightBitmap ,BlOCK_SIZE ,BlOCK_SIZE, true);
        matrix.preScale(-1,1);
        headLeftBitmap = Bitmap.createBitmap(headRightBitmap ,0,0,BlOCK_SIZE ,BlOCK_SIZE ,matrix, true);
        matrix.preRotate(90);
        headDownBitmap = Bitmap.createBitmap(headRightBitmap ,0,0,BlOCK_SIZE ,BlOCK_SIZE ,matrix, true);
        matrix.preRotate(-180);
        headUpBitmap = Bitmap.createBitmap(headRightBitmap ,0,0,BlOCK_SIZE ,BlOCK_SIZE ,matrix, true);


        snakeBodyBitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.body);
        snakeBodyBitmap = Bitmap.createScaledBitmap(snakeBodyBitmap ,BlOCK_SIZE,BlOCK_SIZE,true);


        snakeSegments.add(new Point(200,200));
    }
    boolean haveSnakeEatenApple(Apple apple)
    {
        //Need to modify take apple block.
        if(apple.getPositionOfApple().x  == snakeSegments.get(0).x * BlOCK_SIZE &&
        apple.getPositionOfApple().y  == snakeSegments.get(0).y * BlOCK_SIZE)
        {
            snakeSegments.add(new Point(-100,-100));
            return true;

        }
        return false;
    }
    void moveSnake()
    {
        for(int i = snakeSegments.size()-1;i >= 1;i--)
        {
            snakeSegments.get(i).x = snakeSegments.get(i-1).x;
            snakeSegments.get(i).y = snakeSegments.get(i-1).y;
        }
        snakeSegments.get(0).x += 1;
    }
    void drawSnake(Canvas canvas , Paint paint)
    {
        for(int i = snakeSegments.size()-1;i >= 1;i--)
        {
            canvas.drawBitmap(snakeBodyBitmap,snakeSegments.get(i).x*BlOCK_SIZE, snakeSegments.get(i).y*BlOCK_SIZE ,paint);
        }
        canvas.drawBitmap(headRightBitmap,snakeSegments.get(0).x*BlOCK_SIZE, snakeSegments.get(0).y*BlOCK_SIZE ,paint);
    }
    Boolean isDead()
    {
        if(snakeSegments.get(0).x > movingRange.x  ||
                snakeSegments.get(0).y > movingRange.y ||
        snakeSegments.get(0).x == -1 || snakeSegments.get(0).y == -1)
        {
            return true;
        }
        return false;
    }
    void reset()
    {
        snakeSegments.clear();
        snakeSegments.add(new Point(20 ,movingRange.y/2));
    }

}
