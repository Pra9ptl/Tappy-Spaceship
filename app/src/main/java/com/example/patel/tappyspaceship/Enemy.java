package com.example.patel.tappyspaceship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

public class Enemy {

    //Properties
    //image
    //hitbox
    private Bitmap enemyImage;
    private Rect enemyHitBox;
    private int xPosition;
    private int yPosition;

    public Enemy(Context context, int x, int y){
        //1. set initial position of the enem
        this.xPosition = x;
        this.yPosition = y;

        //2. Set the default imsge -- enemy has image
        this.enemyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien_ship1);

        // 3. se the default hitbox -- same hitbox
        this.enemyHitBox = new Rect(
                this.xPosition,
                yPosition,
                this.xPosition + this.enemyImage.getWidth(),
                this.yPosition + this.enemyImage.getHeight());


    }

    public Bitmap getEnemyImage() {
        return enemyImage;
    }

    public void setEnemyImage(Bitmap enemyImage) {
        this.enemyImage = enemyImage;
    }

    public Rect getEnemyHitBox() {
        return enemyHitBox;
    }

    public void setEnemyHitBox(Rect enemyHitBox) {
        this.enemyHitBox = enemyHitBox;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }
}
