package com.example.kunalkrishna.breakout;

import android.graphics.RectF;

/**
 * Created by aastha dixit and kunal krishna on 11/19/2015.
 */
public class Paddle {

    private RectF rectangle;

    private float length;
    private float height;

    private float x, y;
    private float paddlespeed;
    private int limit;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int paddleMoving = 0;

    /* Author:aastha dixit
       Description: Sets the size, position and speed for the paddle.
    */
    public Paddle(int positionX, int positiony) {
        length = positionX / 6;
        height = 20;
        x = positionX / 2 - 50;
        y = 4 * positiony / 5;
        limit = (int) (positionX - length);

        rectangle = new RectF(x, y, x + length, y + height);
        paddlespeed = 500;
    }

    /* Author:aastha dixit
       Description: Return the top, left, bottom and right coordinates for the paddle.
    */
    public RectF getRect() {
        return rectangle;
    }

    /* Author:aastha dixit
       Description: Gets the state of the paddle which is one among left, right or stopped.
    */
    public void setMovementState(int State) {
        paddleMoving = State;
    }

    /* Author:aastha dixit
       Description: changes the paddle position between limits of the screen, which is the x coordinates of the screen.
    */
    public void setPaddlePosition(float Xposition) {
        x = Xposition;
        if (x >= 1) {
            if (x < limit) {
                rectangle.left = x;
                rectangle.right = x + length;
            } else {
                rectangle.left = limit;
                rectangle.right = limit + length;
            }
        }
    }

    /* Author:aastha dixit
       Description: Updates the position of the paddle depending on where the player touches on the screen.
       The paddle position is changed according to the set speed limit for the paddle.
    */
    public void update(long fps) {
        if (paddleMoving == LEFT) {
            x = x - paddlespeed / fps;
        }
        if (paddleMoving == RIGHT) {
            x = x + paddlespeed / fps;
        }
        if (x >= 1) {
            if (x < limit) {
                rectangle.left = x;
                rectangle.right = x + length;
            }
        }
    }
}