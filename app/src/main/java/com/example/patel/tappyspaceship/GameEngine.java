package com.example.patel.tappyspaceship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

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

    // Player variables
//    Bitmap playerImage;
//    Rect playerHitbox;
//    Point playerPos;    // (left,top) of the player


    Player player;

    // Enemy variables
    Enemy enemy1;
    Enemy enemy2;


    // ----------------------------
    // ## GAME STATS
    // ----------------------------
    int score = 0;
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

        // ----------------
        // PLAYER SETUP
        // ----------------
        int initialPlayerX = 100;
        int initialPlayerY = this.screenHeight / 2 - 100;
        this.player = new Player(context,initialPlayerX, initialPlayerY);


        // ----------------
        // ENEMY SETUP
        // ----------------
        this.enemy1 = new Enemy(context, this.screenWidth - 500, 120);
        this.enemy2 = new Enemy(context, this.screenWidth - 500, this.screenHeight - 400);
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
    final int ENEMY_SPEED = 45;
    boolean gameOver = false;



    public void updatePositions() {
        // @TODO: Update position of player


        // PLAYER ALWAYS MOVE DOWN
        this.player.setyPosition(this.player.getyPosition() + PLAYER_SPEED);

        // UPDATE HIS HITBOX
        this.player.getHitbox().left = this.player.getxPosition();
        this.player.getHitbox().top = this.player.getyPosition();
        this.player.getHitbox().right = this.player.getxPosition() + this.player.getImage().getWidth();
        this.player.getHitbox().bottom = this.player.getyPosition() + this.player.getImage().getHeight();




        // @TODO: Update position of enemy ships

        // 1. update position of enemy

        // enemy 1
        this.enemy1.setxPosition(this.enemy1.getxPosition() - ENEMY_SPEED);
        // enemy 2
        this.enemy2.setxPosition(this.enemy2.getxPosition() - ENEMY_SPEED);

        // 2. update position of hitbox

        // enemy1 hitbox
        this.enemy1.getHitbox().left = this.enemy1.getxPosition();
        this.enemy1.getHitbox().top = this.enemy1.getyPosition();
        this.enemy1.getHitbox().right = this.enemy1.getxPosition() + this.enemy1.getImage().getWidth();
        this.enemy1.getHitbox().bottom = this.enemy1.getyPosition() + this.enemy1.getImage().getHeight();

        // enemy2 hitbox
        this.enemy2.getHitbox().left = this.enemy2.getxPosition();
        this.enemy2.getHitbox().top = this.enemy2.getyPosition();
        this.enemy2.getHitbox().right = this.enemy2.getxPosition() + this.enemy2.getImage().getWidth();
        this.enemy2.getHitbox().bottom = this.enemy2.getyPosition() + this.enemy2.getImage().getHeight();



        // @TODO: Collision detection between player and enemy

        // RULE 1: If player hits the enemy, then reduce a life
        if (this.player.getHitbox().intersect(this.enemy1.getHitbox())) {
            Log.d(TAG, "COLLISION!!!!!");
            this.lives = this.lives - 1;
            Log.d(TAG, "Lives remaining: " + this.lives);


            // decide if you should be game over:
            if (this.lives == 0) {
                this.gameOver = true;
                return;
            }

            // restart player from starting position
            this.player.setxPosition(100);
            this.player.setyPosition(120);

            Rect hitbox = new Rect(this.player.getxPosition(),
                    this.player.getyPosition(),
                    this.player.getxPosition() + this.player.getImage().getWidth(),
                    this.player.getyPosition() + this.player.getImage().getHeight()
            );

            this.player.setHitbox(hitbox);
        }

        // RULE 2:  If enemy hits LEFT wall, then restart at original position
        // -------------------------------------------------------------------

        // backOfShip = (x + imageWidth)

        // enemy1
        int backOfShip = this.enemy1.getxPosition() + this.enemy1.getImage().getWidth();
        if (backOfShip <= 0) {
            // restart him at original position
            this.enemy1.setxPosition(this.screenWidth - 500);
            this.enemy1.setyPosition(120);
        }


        // enemy2
        int backOfShip2 = this.enemy2.getxPosition() + this.enemy2.getImage().getWidth();
        if (backOfShip2 <= 0) {
            // restart him at original position
            this.enemy2.setxPosition(this.screenWidth - 500);
            this.enemy2.setyPosition(this.screenHeight - 400);
        }

    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);


            //@TODO: Draw the player
            canvas.drawBitmap(this.player.getImage(), this.player.getxPosition(), this.player.getyPosition(), paintbrush);


            //@TODO: Draw the enemy

            // refactored to use Enemy object
            canvas.drawBitmap(this.enemy1.getImage(), this.enemy1.getxPosition(), this.enemy1.getyPosition(), paintbrush);

            canvas.drawBitmap(this.enemy2.getImage(), this.enemy2.getxPosition(), this.enemy2.getyPosition(), paintbrush);


            // DRAW THE PLAYER HITBOX
            // ------------------------
            // 1. change the paintbrush settings so we can see the hitbox
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            // 2. draw the hitbox
            canvas.drawRect(this.player.getHitbox().left,
                    this.player.getHitbox().top,
                    this.player.getHitbox().right,
                    this.player.getHitbox().bottom,
                    paintbrush
            );


            // Draw enemy hitbox - refactored to use Enemy object
            paintbrush.setColor(Color.RED);
            canvas.drawRect(this.enemy1.getHitbox().left,
                    this.enemy1.getHitbox().top,
                    this.enemy1.getHitbox().right,
                    this.enemy1.getHitbox().bottom,
                    paintbrush
            );
            canvas.drawRect(this.enemy2.getHitbox().left,
                    this.enemy2.getHitbox().top,
                    this.enemy2.getHitbox().right,
                    this.enemy2.getHitbox().bottom,
                    paintbrush
            );


            // DRAW GAME STATS

            paintbrush.setTextSize(100);     // set font size
            paintbrush.setStrokeWidth(5);  // make text narrow
            canvas.drawText("Lives: " + this.lives, 50, 100, paintbrush);

            if (gameOver == true) {
                canvas.drawText("GAME OVER!", 50, 200, paintbrush);
            }








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

            // @TODO: Write code so when person taps down, player MOVES UP!
            this.player.setyPosition(this.player.getyPosition() - 60);

            // update hitbox position
            this.player.getHitbox().left = this.player.getxPosition();
            this.player.getHitbox().top = this.player.getyPosition();
            this.player.getHitbox().right = this.player.getxPosition() + this.player.getImage().getWidth();
            this.player.getHitbox().bottom = this.player.getyPosition() + this.player.getImage().getHeight();
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            Log.d(TAG, "Person lifted finger");
        }

        return true;
    }
}
