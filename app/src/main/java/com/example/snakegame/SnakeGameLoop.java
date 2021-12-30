package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SnakeGameLoop extends SurfaceView implements Runnable{


    private int score;


    private final int NUM_BLOCK_WIDE = 40;
    private int NUM_BLOCK_HIGH;
    private int blockSize;

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint = new Paint();
    private Bitmap backgroundImage;

    private Thread thread;
    private Boolean isThreadRunning = false;

    private Apple apple;
    private Snake snake;


    private Boolean paused  = true;
    private long nextFrameTime;
    SnakeGameLoop(Context context , Point screenSize)
    {
        super(context);
        blockSize = screenSize.x / NUM_BLOCK_WIDE;
        NUM_BLOCK_HIGH = screenSize.y / blockSize;


        backgroundImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.background);
        backgroundImage = Bitmap.createScaledBitmap(backgroundImage , screenSize.x ,screenSize.x , false);
        surfaceHolder = getHolder();

        apple = new Apple(context ,NUM_BLOCK_WIDE , NUM_BLOCK_HIGH ,blockSize);
        snake = new Snake(context ,new Point(NUM_BLOCK_WIDE, NUM_BLOCK_HIGH),blockSize);

        startNewGame();

    }
    @Override
    public void run() {
        while(isThreadRunning)
        {
            Log.i("TAG","thread is runnig.");
            if(updateRequired())
            {
                if(!paused)
                {
                    update();

                }
                draw();

            }
        }


    }

    private void update()
    {
        snake.moveSnake();
        if(snake.isDead()) paused = true;

        // Snake eat apple?
        // draw snake and apple.
    }
    private boolean updateRequired() {
        if(System.currentTimeMillis() > nextFrameTime)
        {
            nextFrameTime += 1000;
            return true;
        }
        return false;
    }

    void startNewGame()
    {
        score =0;
        apple.setApplePosition();
        snake.moveSnake();
        //initialized snake and apple position.
        nextFrameTime = System.currentTimeMillis();

    }
    void draw()
    {
        if(surfaceHolder.getSurface().isValid())
        {

            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(backgroundImage ,0,0,paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(120);
            canvas.drawText("" + score , 1,120,paint);
            if(!paused)
            {
                //Draw Snake.
                apple.drawApple(canvas , paint);
                snake.drawSnake(canvas,paint);

            }else
            {
                paint.setTextSize(250);
                paint.setColor(Color.BLACK);
                canvas.drawText("Tap to play" , 250 , 700, paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);

        }


    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                if(paused)
                {
                    paused = false;
                    snake.reset();
                    return true;
                }
        }
        return true;
    }

    void onResume()
    {
        isThreadRunning = true;
        thread = new Thread(this);
        thread.start();

    }
    void onPaused()
    {
        isThreadRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
