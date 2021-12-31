package com.example.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.GestureDetector;
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

    //For Swipe detection.
    private float x1 , x2 ,y1,y2;
    private float abs_x,abs_y;
    static final int MIN_DISTANCE = 100;

    private  GestureDetector gestureDetector;

    SnakeGameLoop(Context context , Point screenSize)
    {
        super(context);
        blockSize = screenSize.x / NUM_BLOCK_WIDE;
        NUM_BLOCK_HIGH = screenSize.y / blockSize;

        gestureDetector = new GestureDetector(getContext(),getGestureListener());
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

        if(snake.haveSnakeEatenApple(apple.getPositionOfApple()))
        {
            apple.setApplePosition();
        }
    }
    private boolean updateRequired() {
        if(System.currentTimeMillis() > nextFrameTime)
        {
            nextFrameTime += 120;
            return true;
        }
        return false;
    }

    void startNewGame()
    {
        score =0;
        apple.setApplePosition();
        snake.moveSnake();

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
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    GestureDetector.OnGestureListener getGestureListener()
    {
        return new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {}

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                if (paused) {
                    paused = false;
                    snake.reset();
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {}

            @Override
            public boolean onFling(MotionEvent downEvent, MotionEvent upEvent, float v, float v1) {
                x1 = downEvent.getX();
                y1 = downEvent.getY();
                x2 = upEvent.getX();
                y2 = upEvent.getY();
                abs_x = Math.abs(x1 - x2);
                abs_y = Math.abs(y1 - y2);
                if(abs_y > MIN_DISTANCE || abs_x > MIN_DISTANCE)
                {
                    if(abs_y > abs_x)
                    {
                        //Vertical Swipe.
                        if(y1 > y2)
                            snake.setMovingDirection(Heading.UP);
                        else
                            snake.setMovingDirection(Heading.DOWN);
                    }else if(abs_x > MIN_DISTANCE)
                    {
                        //Horizontal Swipe.
                        if(x1 > x2 )
                            snake.setMovingDirection(Heading.LEFT);
                        else
                            snake.setMovingDirection(Heading.RIGHT);
                    }
                }
                return true;
            }
        };
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