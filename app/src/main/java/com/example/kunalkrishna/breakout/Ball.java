package com.example.kunalkrishna.breakout;

import android.graphics.PointF;

import java.util.Random;

/**
 * Created by aastha dixit and kunal krishna on 11/20/2015.
 */

public class Ball {
    float xVelocity;
    float yVelocity;
    public static float slider_velocity=300;
    public static PointF coordinate = new PointF();
    int screenX, screenY;

    /* Author:kunal krishna
       Description: Constructor used to set the speed of the ball. It all gets the screen size of the device to accordingly set the initial ball position.
    */
    public Ball(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        yVelocity = -(slider_velocity+100);
        xVelocity = 50 ;
    }

    /* Author:kunal krishna
       Description: Gets the coordinates of the point which is the center of the ball. The ball is created using the coordinates of this point.
    */
    public PointF getRect() {
        return coordinate;
    }
    float xVel;

    /* Author:kunal krishna
       Description: Updates the x and y coordinates of the point which is the center point of the ball everytime the ball moves.
    */
    public void update(long fps) {
        coordinate.y = coordinate.y + (yVelocity / fps);
    }

    /* Author:kunal krishna
       Description: Updates the x and y coordinates of the center of the ball when the ball moves using accelerometer.
    */
    public void update1(float x, long fps) {
        coordinate.x = coordinate.x - (x);
        coordinate.y = coordinate.y + (yVelocity / fps);
    }

    /* Author:kunal krishna
       Description: Reverses the speed of ball in Y-axis when the ball hits the bricks or paddle or top of screen.
    */
    public void reverseYVelocity() {
        System.out.println("REVERSEYVE" +yVelocity);
        yVelocity = -yVelocity;

    }

    /* Author:kunal krishna
       Description: Reverses the speed of ball in X-axis when the ball hits the left or right of the screen.
    */
    public void reverseXVelocity() {
        xVelocity = -xVelocity;
    }

    /* Author:kunal krishna
       Description: changes the Colliding velocity if the balls hits an obstacle.
    */
    public void setCollidingXvelocity(float x) {
        xVel = (float) (-(xVelocity+xVelocity*Math.sin(Math.toRadians(x))));
        xVel=-(xVel + 10);
    }

    /* Author:kunal krishna
       Description: generates a random value to the velocity/direction when ball hits an obstacle.
    */
    public void setRandomXVelocity() {
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if (answer == 0) {
            reverseXVelocity();
        }
    }

    /* Author:kunal krishna
       Description: Clears the obstacle, that is changes the coordinates of the point such that the ball reverse its direction when it hits a brick or top of screen.
    */
    public void clearObstacleY(float y) {
        coordinate.y = y + coordinate.y;
    }

    /* Author:kunal krishna
       Description: Clears the obstacle, that is changes the coordinates of the point such that the ball reverse its direction when it hits the paddle.
    */
    public void clearObstacleY1(float y) {

        coordinate.y = coordinate.y - y;
    }

    /* Author:kunal krishna
       Description: Clears the obstacle, that is changes the coordinates of the point such that the ball reverse its direction when it hits the left wall.
    */
    public void clearObstacleX(float x) {
        coordinate.x = coordinate.x + x;
    }

    /* Author:kunal krishna
       Description: Clears the obstacle, that is changes the coordinates of the point such that the ball reverse its direction when it hits the right wall.
    */
    public void clearObstacleX1(float x) {

        coordinate.x = coordinate.x - x;
    }
    /* Author:kunal krishna
       Description: Sets the initial position of the ball.
    */
    public void reset(int x, int y) {

        coordinate.x = x / 2;
        coordinate.y = 4*y/5 - 50;

    }

    /* Author:kunal krishna
       Description: Changes the speed of ball in y-direction when the slider value is changed.
    */
    public void set_speed_seekbar(int v)
    {
        slider_velocity=v;
        if(yVelocity<0) {
            yVelocity = -(slider_velocity+100);
        }
        else{
            yVelocity=(slider_velocity+100);
        }
    }
}
