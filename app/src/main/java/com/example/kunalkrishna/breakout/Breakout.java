package com.example.kunalkrishna.breakout;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aastha dixit and kunal krishna on 11/20/2015.
 */

public class Breakout extends AppCompatActivity {

    // gameView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    BreakoutView breakoutView;
    private static SeekBar seek_bar;
    private ImageView img;

    Timer timer;
    TimerTask timerTask;

    /* Author:kunal krishna
       Description: The onCreate method is overriden to create and object of breakoutView and the set taskbar for slider.
         */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize gameView and set it as the view
        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);

        addContentView(getLayoutInflater().inflate(R.layout.activity_paddle, null), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout controlBar=(RelativeLayout)findViewById(R.id.root_layout);
        controlBar.bringToFront();
        seekBar_Action();
    }

    /* Author:kunal krishna
       Description: Value of the progressbar (slider speed control) changes when onprogresschanged is called using screen touch.
         */
    public void seekBar_Action()
    {
        seek_bar=(SeekBar)findViewById(R.id.seekBar);

        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress;
                        breakoutView.Ball_speed_via_seekBar(progress_value);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


    }

    // Here is our implementation of BreakoutView
    // It is an inner class.
    // Note how the final closing curly brace }
    // is inside the BreakoutGame class
    int sec,min,hours=0;

    public void startTimer() {
        timer = new Timer();

        intializeTimerTask();
        timer.schedule(timerTask, 1, 1000);
    }

    /* Author:aastha dixit
       Description: Timertask is used as timer control. This function calculates the time every second.
         */
    public void intializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {

                System.out.println("Time Lapsed :"+sec);
                sec++;
                if(sec==60) {
                    sec=0;
                    min++;
                    if(min==60) {
                        min = 0;
                        hours++;
                    }
                }
            }
        };
    }

    /* Author:aastha dixit
       Description: Stops the Timertask when game is over or ended.
         */
    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }
    /* Author:aastha dixit
       Description: This method executes when the player resumes/starts the game.
         */
    @Override
    protected void onResume() {
        super.onResume();


        System.out.println("OnResume method called ");
        // Tell the gameView resume method to execute
        breakoutView.resume();
    }

    /* Author:aastha dixit
       Description: This method is called when the player pauses/stops the game.
         */
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("OnPause method called ");
        // Tell the gameView pause method to execute
        breakoutView.pause();
        stoptimertask();

    }

    /* Author:aastha dixit and kunal krishna
       Description: Class BreakoutView implements two threads which are used to execute the entire game.
       The gamethread is for the execution of the game.
       The timer thread is for the timer and to calculate high score in return.
    */
    class BreakoutView extends SurfaceView implements Runnable, SurfaceHolder.Callback, SensorEventListener {

        private static final int SHAKE_THRESHOLD = 600;
        RepositoryHandler rep=null;
        // This is our thread
        Thread gameThread = null;
        // This is new. We need a SurfaceHolder
        // When we use Paint and Canvas in a thread
        // We will see it in action in the draw method soon.
        SurfaceHolder ourHolder;
        // A boolean which we will set and unset
        // when the game is running- or not.
        volatile boolean playing;
        // Game is paused at the start
        boolean paused = true;
        // A Canvas and a Paint object
        Canvas canvas;
        Paint paint;
        // This variable tracks the game frame rate
        long fps;
        // The size of the screen in pixels
        int screenX;
        int screenY;

        // The players paddle
        Paddle paddle;
        // A ball
        Ball ball;
        // Up to 24 bricks
        Brick[] bricks = new Brick[24];
        int numBricks = 0;
        Integer[][] final_clr = new Integer[24][2];
        // This is used to help calculate the fps
        private long timeThisFrame;
        private SensorManager sensorManager;
        private Sensor accelerometer;
        //Variable for sensor data
        private long lastUpdate = 0;
        private long last1=0;
        private float last_x, last_y, last_z;


        /* Author:kunal krishna
           Description: Constructor is called when the object of breakoutview is created.
           Objects for ball, paddle, bricks and sensor are created here.
         */
        public BreakoutView(Context context) {
            // The next Fline of code asks the
            // SurfaceView class to set up our object.
            // How kind.
            super(context);


            // Initialize ourHolder and paint objects
            ourHolder = getHolder();
            paint = new Paint();

            ourHolder.addCallback(this);
            setFocusable(true);

            // Get a Display object to access screen details
            Display display = getWindowManager().getDefaultDisplay();
            // Load the resolution into a Point object
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            paddle = new Paddle(screenX, screenY);

            // Create a ball
            ball = new Ball(screenX, screenY);
            /*code for sensor manager*/
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            createBricksAndRestart();
        }

        /* Author: kunal krishna
           Description: Gets the speed value of slider from the ball class.
         */
        public void Ball_speed_via_seekBar(int value)
        {
            ball.set_speed_seekbar(value);
        }

        /* Author:aastha dixit
           Description: Function that calls the brick class to create the all the 24 bricks in 3 rows. This method is called from within the constructor.
         */
        public void createBricksAndRestart() {

            // Put the ball back to the start
            ball.reset(screenX, screenY);

            int brickWidth = screenX / 8;
            int brickHeight = screenY / 10;

            // Build a wall of bricks
            numBricks = 0;

            int i = 7;
            for (int row = 0; row < 3; row++) {
                for (int column = 0; column < i; column++) {
                    bricks[numBricks] = new Brick(row, column, (screenX) / i, screenX / 12);
                    numBricks++;
                }
                i++;
            }
            final_clr = Brick.getColor(bricks);
            count = 0;
        }

        /* Author:aastha dixit
           Description: Custom run method called for as long as breakout activity runs
         */
        @Override
        public void run() {

            while (playing) {

                // Capture the current time in milliseconds in startFrameTime
                long startFrameTime = System.currentTimeMillis();

                // Update the frame
                if (!paused) {
                    System.out.println("inside run pause logic-->");
                    update();
                }

                // Draw the frame
                draw();

                // Calculate the fps this frame
                // We can then use the result to
                // time animations and more.
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }

        /* Author:aastha dixit
           Description: Checks condition for change of color when the ball hits the brick
         */
        public Brick validateBrick(Brick brick) {
            switch (brick.getHit()) {
                case 1:
                    brick.setColor(Color.WHITE);
                    break;
                case 2:
                    brick.setColor(Color.rgb(51,105,232));
                    break;
                case 3:
                    brick.setColor(Color.rgb(0,153,37));
                    break;
                case 4:
                    brick.setColor( Color.rgb(213,15,37));
                    break;
            }
            return brick;
        }
        /* Author:aastha dixit
           Description: The function is called to check whether the ball has intersected the brick or not
         */
        public boolean intersect_brick(PointF coordinate, RectF rect) {

            if (coordinate.y - screenX/24 <= rect.bottom && coordinate.y - screenX/24 >= rect.top && coordinate.x + screenX/24 >= rect.left && coordinate.x - screenX/24 <= rect.right) {
                return true;
            } else
                return false;
        }
        /* Author:kunal krishna
           Description: The function is called to check whether the ball has intersected the paddle or not.
         */
        public boolean intersect_paddle(PointF coordinate, RectF rect) {
            System.out.println(coordinate.x + " and " + rect.top + " and " + rect.bottom);
            //  if (coordinate.y + screenX/24 >= rect.top && coordinate.y + screenX/24 <= rect.bottom && coordinate.x + screenX/24 >= rect.left && coordinate.x - screenX/24<= rect.right) {
            if (coordinate.y + screenX/24 >= rect.top && coordinate.x + screenX/24 >= rect.left && coordinate.x - screenX/24<= rect.right) {
                return true;
            } else
                return false;
        }
        int count = 0;
        boolean gameend = false;

        /* Author:aastha dixit
           Description: Updates the position, movement, collision detection etc. for the ball, paddle and bricks and top/bottom of screen
        */
        public void update() {
            System.out.println("screeny "+screenX);
            System.out.println("screenx "+screenY);
            // Move the paddle if required
            paddle.update(fps);

            //ball.update(fps);
            int hits = 0;
            // Check for ball colliding with a brick

            if (ball.getRect().y <= screenY / 2) {
                //System.out.println("Ball is in top area");
                for (int i = 0; i < numBricks; i++) {

                    if (bricks[i].getVisibility()) {
                        if (intersect_brick(ball.getRect(), bricks[i].getRect())) {
                            ball.reverseYVelocity();
                            ball.update(fps);
                            System.out.println("Intersected brick");
                            if (bricks[i].getHit() == 1) {
                                bricks[i].setInvisible();
                                count ++;
                            } else {
                                bricks[i].setHit(bricks[i].getHit() - 1);
                                bricks[i] = validateBrick(bricks[i]);
                                final_clr[i][0] = bricks[i].getColor();
                                final_clr[i][1] = bricks[i].getHit();
                            }

                        }
                    }
                }
            }

            // Check for ball colliding with paddle
            if (intersect_paddle(ball.getRect(), paddle.getRect()))
            {
                System.out.println("inside paddle");
                ball.setRandomXVelocity();
                long currpaddel = System.currentTimeMillis();

                if ((currpaddel - last1) > 2000)
                {
                    ball.reverseYVelocity();
                    ball.clearObstacleY1(screenY/5 + 30);
                }
                else
                {
                    last1 = currpaddel;
                }
                // ball.reverseYVelocity();
                //  ball.clearObstacleY1(1*screenY/5-2);
                System.out.println("ycoordinate: " + ball.yVelocity);
                //ball.update(fps);
            }
            System.out.println("After paddle intersect code");

            // Bounce the ball back when it hits the bottom of screen
            if (ball.getRect().y - screenX/24 > screenY) {
                float angle = FindAngleOfIncidence();
                paused = true;
                stoptimertask(); initial=0;
                gameend = false;
                time=""+String.format("%02d",hours)+":"+String.format("%02d",min)+":"+String.format("%02d",sec);
                select_activity(gameend);
            }

            // Bounce the ball back when it hits the top of screen
         /*   if (ball.getRect().y + screenX/24 < 0) { //screenX/24 instead of 0
                if(count == 24) {
                    gameend = true;
                    time=""+String.format("%02d",hours)+":"+String.format("%02d",min)+":"+String.format("%02d",sec);
                    select_activity(gameend);
                }
                ball.reverseYVelocity();
                ball.clearObstacleY(screenX/24); //22
            }    */

            if (ball.getRect().y + screenX/24 < 0) { //screenX/24 instead of 0

                gameend = true;
                time=""+String.format("%02d",hours)+":"+String.format("%02d",min)+":"+String.format("%02d",sec);
                select_activity(gameend);

            }

            // If the ball hits left wall bounce
            if (ball.getRect().x - screenX/24 < 0) {
                ball.reverseXVelocity();
                ball.clearObstacleX(10); //2
            }

            // If the ball hits right wall bounce
            if (ball.getRect().x + screenX/24 > screenX) {
                ball.reverseXVelocity();
                ball.clearObstacleX1(10);

            }

        }
        String time;

        /* Author:kunal krishna
           Description: Calling Add high score and View High Score activities depending upon whether the player completed the game or not
         */
        public void select_activity(boolean check) {
            System.out.println("inside select");

            rep = RepositoryHandler.GetOrCreateRepository(Breakout.this);
            ArrayList<Record> records = rep.GetAllRecords();

            int itemCount = records.size();
            boolean valid=false;
            if (itemCount !=0) {
                int minIndex = records.indexOf(Collections.min(records));
                int maxIndex = records.indexOf(Collections.max(records));

                if (itemCount == 10 && check) {
                    for (Record curr : records) {
                        if (toSec(time) < toSec(curr.GetScore())) {
                            valid = true;
                        }
                    }
                    if (valid) {
                        rep.DeleteRecord(maxIndex);
                        Intent add_or_view_intent = new Intent(Breakout.this, ADD_Score_Activity.class);
                        add_or_view_intent.putExtra("Score", time);
                        finish();
                        startActivity(add_or_view_intent);
                    } else {
                        Intent view_high_score = new Intent(Breakout.this, Score_List_Activity.class);
                        finish();
                        startActivity(view_high_score);
                    }

                } else {
                    if (check) {
                        System.out.println("inside check");
                        Intent add_or_view_intent = new Intent(Breakout.this, ADD_Score_Activity.class);
                        add_or_view_intent.putExtra("Score", time);
                        finish();
                        startActivity(add_or_view_intent);
                    } else {
                        System.out.println("outside check");
                        Intent view_high_score = new Intent(Breakout.this, Score_List_Activity.class);
                        finish();
                        startActivity(view_high_score);

                    }
                }
            }

            else {
                if (check) {
                    System.out.println("inside check");
                    Intent add_or_view_intent = new Intent(Breakout.this, ADD_Score_Activity.class);
                    add_or_view_intent.putExtra("Score", time);
                    finish();
                    startActivity(add_or_view_intent);
                } else {
                    System.out.println("outside check");
                    Intent view_high_score = new Intent(Breakout.this, Score_List_Activity.class);
                    finish();
                    startActivity(view_high_score);

                }
            }
        }

        /* Author:kunal krishna
           Description: Calculating the total number of seconds taken to complete the game to check the high score
         */
        public int toSec (String s) {
            String[] hourMin = s.split(":");
            int hour = Integer.parseInt(hourMin[0]);
            int mins = Integer.parseInt(hourMin[1]);
            int sec = Integer.parseInt(hourMin[2]);
            int hoursInSec = hour * 60*60;
            int MinInSec = mins * 60;
            return (hoursInSec + MinInSec+sec);
        }

        /* Author:aastha dixit
           Description: Draws the complete layout of the game while the game is running
         */
        public void draw() {

            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                // Draw the background color
                canvas.drawColor(Color.rgb(188, 210, 238));
                paint.setColor(Color.rgb(255, 140, 0));
                // Draw the paddle
                canvas.drawRect(paddle.getRect(), paint);
                // Draw the ball

                canvas.drawCircle(ball.getRect().x, ball.getRect().y, screenX/24, paint);

                //below is the color for bricks
                for (int i = 0; i < numBricks; i++) {
                    if (bricks[i].getVisibility()) {
                        bricks[i].setColor(final_clr[i][0]);
                        paint.setColor(bricks[i].getColor());
                        bricks[i].setHit(final_clr[i][1]);
                        canvas.drawRect(bricks[i].getRect(), paint);
                        paint.setColor(Color.BLACK);
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(bricks[i].getRect(), paint);
                        paint.setStyle(Paint.Style.FILL);
                    }
                }
                //below is the font color for displaying scores
                paint.setColor(Color.BLACK);
                paint.setTextSize(40);
                time="Time Elapsed -- "+String.format("%02d",hours)+":"+String.format("%02d",min)+":"+String.format("%02d",sec);
                //setContentView(R.layout.);
                canvas.drawText(time, 10,screenY/2, paint);
                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }
        /* Author:aastha dixit
           Description: This function is used to randomly change the value of angle and add a random value from +7 to -7, when the ball hits an obstacle.
         */
        public float FindAngleOfIncidence() {
            Random rn = new Random();
            int range = 7 - (-7);
            int randomNum = rn.nextInt(range) + (-7);
            int angle = 7 + randomNum ;

            float relativeIntersectY = (paddle.LEFT + (paddle.getRect().height() / 2)) - (ball.getRect().x - 10);
            float normalizedRelativeIntersectionY = (relativeIntersectY / (paddle.getRect().height() / 2));
            float bounceAngle = normalizedRelativeIntersectionY * angle;
            System.out.println("bounce angle "+bounceAngle);
            return bounceAngle;
        }

        /* Author:kunal krishna
           Description: If the activity is paused/stop the thread is shut donw using the below pause method.
         */
        public void pause() {
            System.out.println("inside custom pause method");
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }
        }

        /* Author:kunal krishna
           Description: The method starts the thread everytime the game is resumed.
         */
        public void resume() {
            System.out.println("inside custom resume method");
            playing = true;
            //paused=true;
            gameThread = new Thread(this);

            //runOnUiThread(gameThread);
            gameThread.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int initial = 0;

        /* Author:kunal krishna
           Description: The method is used to calculate the motion/position of finger to finally calculate the position of paddle.
         */
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                // Player has touched the screen
                case MotionEvent.ACTION_DOWN:
                    if(initial==0){
                        startTimer();
                        System.out.println("-----------TIMER STARTED--------------");
                        initial=1;
                    }

                    start = 1;
                    System.out.println("down is pressed");
                    paused = false;

                    if ((motionEvent.getX() > screenX / 2)) {
                        paddle.setMovementState(paddle.RIGHT);
                        if (motionEvent.getX() > screenX - 5)
                            paddle.setPaddlePosition(screenX);
                    } else if ((motionEvent.getX() < screenX / 2)) {
                        paddle.setMovementState(paddle.LEFT);
                        if (motionEvent.getX() < 5)
                            paddle.setPaddlePosition(0);
                    }
                    break;

                // Player has removed finger from screen
                case MotionEvent.ACTION_UP:


                    paddle.setMovementState(paddle.STOPPED);
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (motionEvent.getY() > (4*screenY/5)) {
                        paddle.setPaddlePosition(motionEvent.getX());
                    }
            }
            return true;
        }

        /* Author:kunal krishna
           Description: This is the starting point for sensor/accelerometer
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        /* Author:kunal krishna
           Description: Stops the accelerometer as soon as the game is stopped/paused/destroyed.
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            sensorManager.unregisterListener(this,accelerometer);
        }

        int start = 0;

        /* Author:kunal krishna
           Description: Updates the x-coordinate of the ball when we use the sensor and calculates the position of the ball.
         */
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 20)
                {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    if (start == 0) {

                    } else {
                        ball.update1(x, fps);

                    }

                    float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}