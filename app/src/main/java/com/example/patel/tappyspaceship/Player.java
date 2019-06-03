package com.example.patel.tappyspaceship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

public class Player {
    private Bitmap playerImage;
    private Rect playerHitBox;
    private int xPosition;
    private int yPosition;

    public Player(Context context, int x, int y){
        //1. set initial position of the enem
        this.xPosition = x;
        this.yPosition = y;

        //2. Set the default imsge -- enemy has image
        this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien_ship2);

        // 3. se the default hitbox -- same hitbox
        this.playerHitBox = new Rect(
                this.xPosition,
                yPosition,
                this.xPosition + this.playerImage.getWidth(),
                this.yPosition + this.playerImage.getHeight());
    }

    public Bitmap getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(Bitmap playerImage) {
        this.playerImage = playerImage;
    }

    public Rect getPlayerHitBox() {
        return playerHitBox;
    }

    public void setPlayerHitBox(Rect playerHitBox) {
        this.playerHitBox = playerHitBox;
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
