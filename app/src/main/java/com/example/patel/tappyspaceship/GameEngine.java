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
    Player player;

    //enemy imagee

    //Enemy Variables
    Enemy enemy1;
    Enemy enemy2;


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

        int initialPlayerX = 100;
        int initialPlayerY = 120;
        this.player = new Player(context,initialPlayerX, initialPlayerY);

        //setup initial position of the player

        //set up hit box


        this.enemy1 = new Enemy(context, this.screenWidth - 500, 120);
        this.enemy2 = new Enemy(context, this.screenWidth - 500, 500);
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
        this.player.setxPosition(this.player.getxPosition() + PLAYER_SPEED);
        //move the hitbox position
        this.player.getPlayerHitBox().left = this.player.getPlayerHitBox().left + PLAYER_SPEED;
        this.player.getPlayerHitBox().right = this.player.getPlayerHitBox().right + PLAYER_SPEED;

        // @TODO: Update position of enemy ships
        // @TODO: Collision detection between player and enemy
        if(player.getPlayerHitBox().intersect(this.enemy1.getEnemyHitBox())){
            Log.d(TAG, "BOOOM");
            this.lives--;
            Log.d(TAG, "Lives Left" + this.lives);

            if(lives <= 0){
                gameOver = true;
            }


            //restart when collided
            this.player.setxPosition(100);
            this.player.setyPosition(120);
            this.player.getPlayerHitBox().left = this.player.getxPosition();
            this.player.getPlayerHitBox().top = this.player.getyPosition();
            this.player.getPlayerHitBox().right = this.player.getxPosition() + this.player.getPlayerImage().getWidth();
            this.player.getPlayerHitBox().bottom = this.player.getyPosition() + this.player.getPlayerImage().getHeight();
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

            canvas.drawBitmap(player.getPlayerImage(), player.getxPosition(), player.getyPosition(), paintbrush);
            //draw player hitbox


            //change paint brush setting
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            // 2. draw the hitbox
            canvas.drawRect(this.player.getPlayerHitBox().left, this.player.getPlayerHitBox().top,this.player.getPlayerHitBox().right,this.player.getPlayerHitBox().bottom,paintbrush);


            //@TODO: Draw the enemy
            // refactored to use Enemy object
            canvas.drawBitmap(this.enemy1.getEnemyImage(), this.enemy1.getxPosition(), this.enemy1.getyPosition(), paintbrush);
            canvas.drawBitmap(this.enemy2.getEnemyImage(), this.enemy2.getxPosition(), this.enemy2.getyPosition(), paintbrush);
            //change paint brush setting
            paintbrush.setColor(Color.RED);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            // Draw enemy hitbox - refactored to use Enemy object
            canvas.drawRect(this.enemy1.getEnemyHitBox().left,
                    this.enemy1.getEnemyHitBox().top,
                    this.enemy1.getEnemyHitBox().right,
                    this.enemy1.getEnemyHitBox().bottom,
                    paintbrush
            );

            canvas.drawRect(this.enemy2.getEnemyHitBox().left,
                    this.enemy2.getEnemyHitBox().top,
                    this.enemy2.getEnemyHitBox().right,
                    this.enemy2.getEnemyHitBox().bottom,
                    paintbrush
            );

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
