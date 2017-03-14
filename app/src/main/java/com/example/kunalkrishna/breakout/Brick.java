package com.example.kunalkrishna.breakout;

/**
 * Created by aastha dixit and kunal krishna on 11/22/2015.
 */
import android.graphics.Color;
import android.graphics.RectF;

import java.util.Random;


public class Brick {
    private RectF rect;
    private int hit;
    private int color;
    private boolean isVisible;

    /* Author:aastha dixit
       Description: Returns the numbers of times the brick is hit. Used to find out the brick color.
    */
    public int getHit() {
        return hit;
    }

    /* Author:aastha dixit
       Description: sets the number of hits required to break a brick when the brick is hit by the ball.
    */
    public void setHit(int hit) {
        this.hit = hit;
    }

    /* Author:aastha dixit
       Description: Gets the current color of a specific brick.
    */
    public int getColor() {
        return color;
    }

    /* Author:aastha dixit
       Description: Sets the color of the brick to the new color when the brick is hit by the ball.
    */
    public void setColor(int color) {
        this.color = color;
    }

    /* Author:aastha dixit
       Description: Calculates the position of the brick using two point coordinates of a rectangle.
       These coordinates are the top, left, bottom and right position of the brick.
    */
    public Brick(int row, int column, int width, int height) {
        isVisible = true;
        rect = new RectF(column * width,
                row * height,
                column * width + width,
                row * height + height);
    }

    /* Author:aastha dixit
       Description: Function to calculate the color of every brick randomly at the start of the game.
       No two adjacent bricks have the same color.
       This color is stored in an array final_clr everytime the game runs.
    */
    public static Integer[][] getColor(Brick brick1[]) {
        int[] colors = {Color.WHITE, Color.rgb(51,105,232), Color.rgb(0,153,37), Color.rgb(213,15,37), Color.BLACK};
        int[] hits = {1, 2, 3, 4, 5};
        Integer[][] brick_color = new Integer[24][2];
        int prev_color = 6;

        for (int i = 0; i < 24; i++) {
            int current = new Random().nextInt(5);

            while (prev_color == current) {
                current = new Random().nextInt(5);
            }
            brick_color[i][0] = colors[current];
            brick_color[i][1] = hits[current];
            prev_color = current;
        }
        return brick_color;
    }

    /* Author:aastha dixit
       Description: Returns the complete value of rectangle which contains all the four coordinates, ie top, left, right and bottom.
    */
    public RectF getRect() {
        return this.rect;
    }

    /* Author:aastha dixit
       Description: If a white color ball is hit the brick becomes invisible in that case.
    */
    public void setInvisible() {
        isVisible = false;
    }

    /* Author:aastha dixit
       Description: Checks if the brick is visible or not, if the brick is visible only then other operations are performed on it, else not.
    */
    public boolean getVisibility() {
        return isVisible;
    }
}

