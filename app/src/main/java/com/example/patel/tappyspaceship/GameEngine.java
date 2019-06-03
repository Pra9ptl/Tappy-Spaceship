package com.example.patel.tappyspaceship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable{

    // Android debug variables
    final static String TAG="TAPPY-SPACESHIP";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;



    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------

    //Player image
    Bitmap playerImage;
    Rect playerHitBox;
    Point playerPos;

    //enemy imagee
    Bitmap enemyImage;
    Rect enemyHitBox;
    Point enemyPos;
    boolean gameOver = false;

    // ----------------------------
    // ## GAME STATS
    // ----------------------------
    int lives = 3;

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

        // @TODO: Add your sprites
        // @TODO: Any other game setup

        this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien_ship2);
        this.enemyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien_ship1);

        //setup initial position of the player
        this.playerPos = new Point();
        this.playerPos.x = 100;
        this.playerPos.y = 120;

        //set up hit box
        this.playerHitBox = new Rect(playerPos.x, playerPos.y, playerPos.x + this.playerImage.getWidth(), playerPos.y + this.playerImage.getHeight());
        this.enemyHitBox = new Rect(this.screenWidth - 500, 120, this.screenWidth - 500 + this.enemyImage.getWidth(), 120 + this.enemyImage.getHeight());
    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        //@TODO: Start the player at the left side of screen
    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location

    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------
    final int PLAYER_SPEED = 30;

    public void updatePositions() {
        // @TODO: Update position of player

        //move the player position
        this.playerPos.x = this.playerPos.x + PLAYER_SPEED;
        //move the hitbox position
        this.playerHitBox.left = this.playerHitBox.left + PLAYER_SPEED;
        this.playerHitBox.right = this.playerHitBox.right + PLAYER_SPEED;

        // @TODO: Update position of enemy ships
        // @TODO: Collision detection between player and enemy
        if(playerHitBox.intersect(enemyHitBox)){
            Log.d(TAG, "BOOOM");
            this.lives--;
            Log.d(TAG, "Lives Left" + this.lives);

            if(lives <= 0){
                gameOver = true;
            }


            //restart when collided
            this.playerPos.x = 100;
            this.playerPos.y = 120;
            this.playerHitBox.left = this.playerPos.x;
            this.playerHitBox.top = this.playerPos.y;
            this.playerHitBox.right = this.playerPos.x + this.playerImage.getWidth();
            this.playerHitBox.bottom = this.playerPos.y + this.playerImage.getHeight();
        }
    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,0));
            paintbrush.setColor(Color.WHITE);


            //@TODO: Draw the player

            canvas.drawBitmap(playerImage, playerPos.x, playerPos.y, paintbrush);
            //draw player hitbox
            canvas.drawRect(this.playerHitBox.left, this.playerHitBox.top,this.playerHitBox.right,this.playerHitBox.bottom, paintbrush);

            //change paint brush setting
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            // 2. draw the hitbox
            canvas.drawRect(this.playerHitBox.left, this.playerHitBox.top,this.playerHitBox.right,this.playerHitBox.bottom,paintbrush);


            //@TODO: Draw the enemy
            canvas.drawBitmap(enemyImage, this.screenWidth - 500, 120, paintbrush);
            //change paint brush setting
            paintbrush.setColor(Color.RED);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            // 2. draw the hitbox
            canvas.drawRect(this.enemyHitBox.left, this.enemyHitBox.top,this.enemyHitBox.right,this.enemyHitBox.bottom,paintbrush);

            //draw Game Stats
            paintbrush.setTextSize(50);
            canvas.drawText("Lives: " + this.lives, 50, 50, paintbrush);

            if(gameOver==true){
                paintbrush.setTextSize(50);
                canvas.drawText("GAME OVER", 50, 120, paintbrush);
            }
            //
            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(120);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "Person tapped the screen");
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            Log.d(TAG, "Person lifted finger");
        }

        return true;
    }
}
