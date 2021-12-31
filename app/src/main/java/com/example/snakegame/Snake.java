package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.ArrayList;

enum Heading
{
    UP,DOWN,RIGHT,LEFT
}
public class Snake {
    private  int BlOCK_SIZE;
    private final ArrayList<Point> snakeSegments = new ArrayList<>();
    private Bitmap headRightBitmap;
    private Heading heading;
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

        heading = Heading.LEFT;

        snakeSegments.add(new Point(200,200));
    }
    boolean haveSnakeEatenApple(Point applePosition)
    {
        if(applePosition.x  == snakeSegments.get(0).x * BlOCK_SIZE &&
                applePosition.y  == snakeSegments.get(0).y * BlOCK_SIZE)
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
        switch (heading)
        {
            case RIGHT:
                snakeSegments.get(0).x += 1;
                break;
            case UP:
                snakeSegments.get(0).y -= 1;
                break;
            case DOWN:
                snakeSegments.get(0).y += 1;
                break;
            case LEFT:
                snakeSegments.get(0).x -= 1;
                break;
        }
    }
    void drawSnake(Canvas canvas , Paint paint)
    {
        for(int i = snakeSegments.size()-1;i >= 1;i--)
        {
            canvas.drawBitmap(snakeBodyBitmap,snakeSegments.get(i).x*BlOCK_SIZE, snakeSegments.get(i).y*BlOCK_SIZE ,paint);
        }

        switch (heading)
        {
            case RIGHT:
                canvas.drawBitmap(headRightBitmap,snakeSegments.get(0).x*BlOCK_SIZE, snakeSegments.get(0).y*BlOCK_SIZE ,paint);
                break;
            case UP:
                canvas.drawBitmap(headUpBitmap,snakeSegments.get(0).x*BlOCK_SIZE, snakeSegments.get(0).y*BlOCK_SIZE ,paint);
                break;
            case DOWN:
                canvas.drawBitmap(headDownBitmap,snakeSegments.get(0).x*BlOCK_SIZE, snakeSegments.get(0).y*BlOCK_SIZE ,paint);
                break;
            case LEFT:
                canvas.drawBitmap(headLeftBitmap,snakeSegments.get(0).x*BlOCK_SIZE, snakeSegments.get(0).y*BlOCK_SIZE ,paint);
                break;
        }
    }
    Boolean isDead()
    {
        if(snakeSegments.get(0).x > movingRange.x  ||
                snakeSegments.get(0).y > movingRange.y ||
        snakeSegments.get(0).x == -1 || snakeSegments.get(0).y == -1)
        {
            return true;
        }
        for(int i = snakeSegments.size()-1;i>0;i--)
        {
            if(snakeSegments.get(i).x == snakeSegments.get(0).x &&
            snakeSegments.get(i).y == snakeSegments.get(0).y)
            {
                return true;
            }
        }
        return false;
    }
    void reset()
    {
        snakeSegments.clear();
        snakeSegments.add(new Point(20 ,movingRange.y/2));
        heading = Heading.RIGHT;
    }

    void setMovingDirection(Heading h)
    {
        heading = h;
    }
}
