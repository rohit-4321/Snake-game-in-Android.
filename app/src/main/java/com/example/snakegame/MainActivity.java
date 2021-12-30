package com.example.snakegame;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;

public class MainActivity extends Activity {
    SnakeGameLoop snakeGameLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Display display = getWindowManager()
                .getDefaultDisplay();
        Point size  = new Point();
        display.getSize(size);

        snakeGameLoop = new SnakeGameLoop(this , size);

        super.onCreate(savedInstanceState);
        setContentView(snakeGameLoop);
    }
    @Override
    protected void onPause()
    {
        snakeGameLoop.onPaused();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        snakeGameLoop.onResume();
        super.onResume();
    }

}