package com.probitorg.eggfever;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static java.lang.Math.*;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends Activity {

    InterstitialAd mInterstitialAd;


    public static String PACKAGE_NAME;

    private int eggsCounter;
    private int levelDependentNumberOfEggs;
    private Button imageLevelPassed;
    private int levelPassedSizeX;
    private int levelPassedSizeY;

    private int egg1PosXReal;
    private int egg2PosXReal;
    private int egg3PosXReal;
    private int egg4PosXReal;

    private int egg1PosYReal;
    private int egg2PosYReal;
    private int egg3PosYReal;
    private int egg4PosYReal;

    private int egg1PosX;
    private int egg2PosX;
    private int egg3PosX;
    private int egg4PosX;

    private int egg1PosY;
    private int egg2PosY;
    private int egg3PosY;
    private int egg4PosY;

    private int chicken1PosXReal;
    private int chicken2PosXReal;
    private int chicken3PosXReal;
    private int chicken4PosXReal;

    private int chicken1PosYReal;
    private int chicken2PosYReal;
    private int chicken3PosYReal;
    private int chicken4PosYReal;

    private int chicken1PosX;
    private int chicken2PosX;
    private int chicken3PosX;
    private int chicken4PosX;

    private int chicken1PosY;
    private int chicken2PosY;
    private int chicken3PosY;
    private int chicken4PosY;

    private int dot1PosYReal;
    private int dot2PosYReal;
    private int dot3PosYReal;
    private int dot4PosYReal;

    private int dot1PosY;
    private int dot2PosY;
    private int dot3PosY;
    private int dot4PosY;

    MediaPlayer mp_chicken_sound_drop_egg;
    MediaPlayer mp_fox_sound_fox_bite_egg;
    MediaPlayer mp_egg_sound_egg_splat;


    Fox fox;
    Fox fox2;

    Egg egg1;
    Egg egg2;
    Egg egg3;
    Egg egg4;

    Chicken chicken1;
    Chicken chicken2;
    Chicken chicken3;
    Chicken chicken4;

    Dot dot1;
    Dot dot2;
    Dot dot3;
    Dot dot4;

    private int screenSizeX;
    private int screenSizeY;
    private float screenRatioX;
    private float screenRatioY;
    private float gamePanelSizeX_real;
    private float gamePanelSizeY_real;
    private int gamePanelSizeX;
    private int gamePanelSizeY;

    public int droppingDistance;
    public int eggDistanceFromTop;//80
    public int jumpingHeight;

    private ImageView imageViewBackground;//
    private ImageView imageViewOverFloor;//

    private int scoreChicken;
    private int scoreFox;
    private int scoreWinner;
    private final static int numberOfEggsPerChicken = 10;

    TextView textView_scoreChicken;
    TextView textView_scoreFox;
    TextView textView_scoreWinner;

    private int levelNumber;
    private int levelType;
    private int levelTypeNumber;

    private String gameStatus;

    private int eggType;
    private int foxType;




    class Dot {
        private int positionX;
        private int positionY;
        private int positionXDraw;
        private int positionYDraw;
        private String resource;//the image resource that represents the body
        private int dimensionX;//in pixels
        private int dimensionY;//in pixels
        private float dimensionXReal;//in pixels
        private float dimensionYReal;//in pixels
        private ImageView imageView;//placeholder for fox image
        private int positionToMove;

        private AnimationDrawable mAnimation;
        //private ImageView mAnimLogo;
        private boolean mStart;


        public Dot(int positionX, int positionY, int chickenId) {
            this.positionX  = positionX;
            this.positionY  = positionY;

            resource    = "transparent_dot";
            dimensionXReal = 144;            dimensionX = (int)(dimensionXReal * screenRatioX);
            dimensionYReal = 144;            dimensionY = (int)(dimensionYReal * screenRatioY);

            positionXDraw   = this.positionX - dimensionX / 2;
            positionYDraw   = this.positionY - dimensionY / 2;


            imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.transparent_dot);//R.drawable.chicken);
            switch(chickenId) {
                case 1:
                    imageView.setId(R.id.dot1);
                    break;
                case 2:
                    imageView.setId(R.id.dot2);
                    break;
                case 3:
                    imageView.setId(R.id.dot3);
                    break;
                case 4:
                    imageView.setId(R.id.dot4);
                    break;
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensionX, dimensionY);
            params.leftMargin = this.positionXDraw ; //Your X coordinate
            params.topMargin = this.positionYDraw; //Your Y coordinate
            //params.addRule(RelativeLayout.START_OF, RelativeLayout.TRUE);
            imageView.setLayoutParams(params);

        }

        public void setPosition(int positionX, int positionY){
            this.positionX   = positionX;
            this.positionY   = positionY;

            positionXDraw   = this.positionX - dimensionX / 2;
            positionYDraw   = this.positionY - dimensionY / 2;

            if (android.os.Build.VERSION.SDK_INT >= 11) {
                imageView.setX(this.positionXDraw);
                imageView.setY(this.positionYDraw);//200
            }
            else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensionX, dimensionY);
                params.leftMargin = this.positionXDraw; //Your X coordinate
                params.topMargin = this.positionYDraw; //Your Y coordinate
                imageView.setLayoutParams(params);
            }
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

    class Chicken {
        private int positionX;
        private int positionY;
        private int positionXDraw;
        private int positionYDraw;
        private String resource;//the image resource that represents the body
        private int dimensionX;//in pixels
        private int dimensionY;//in pixels
        private float dimensionXReal;//in pixels
        private float dimensionYReal;//in pixels
        private ImageView imageView;//placeholder for fox image
        private int positionToMove;

        private AnimationDrawable mAnimation;
        //private ImageView mAnimLogo;
        private boolean mStart;


        public Chicken(int positionX, int positionY, int chickenId) {
            this.positionX  = positionX;
            this.positionY  = positionY;

            resource    = "chicken";
            dimensionXReal = 128;            dimensionX = (int)(dimensionXReal * screenRatioX);
            dimensionYReal = 128;            dimensionY = (int)(dimensionYReal * screenRatioY);

            positionXDraw   = this.positionX - dimensionX / 2;
            positionYDraw   = this.positionY - dimensionY / 2;


            imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(+R.anim.chicken_animation_1);//R.drawable.chicken);
            switch(chickenId) {
                case 1:
                    imageView.setId(R.id.chicken1);
                    break;
                case 2:
                    imageView.setId(R.id.chicken2);
                    break;
                case 3:
                    imageView.setId(R.id.chicken3);
                    break;
                case 4:
                    imageView.setId(R.id.chicken4);
                    break;
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensionX, dimensionY);
            params.leftMargin = this.positionXDraw ; //Your X coordinate
            params.topMargin = this.positionYDraw; //Your Y coordinate
            //params.addRule(RelativeLayout.START_OF, RelativeLayout.TRUE);
            imageView.setLayoutParams(params);

            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //mAnimLogo = new ImageView(getActivity());
            //mAnimLogo.setImageResource(R.anim.chicken_animation_1);
            mAnimation = (AnimationDrawable) imageView.getDrawable();

            mStart = false;

            if (mStart == false) {
                // start animation
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setVisibility(View.VISIBLE);
                        mAnimation.start();
                        mStart = true;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //mbtnStart.setText("Stop");
                    }
                });
            } else {
                // stop animation
                mAnimation.stop();
                //mbtnStart.setText("Start");
                mStart = false;
            }
        }

        public void setPosition(int positionX, int positionY){
            this.positionX   = positionX;
            this.positionY   = positionY;

            positionXDraw   = this.positionX - dimensionX / 2;
            positionYDraw   = this.positionY - dimensionY / 2;

            if (android.os.Build.VERSION.SDK_INT >= 11) {
                imageView.setX(this.positionXDraw);
                imageView.setY(this.positionYDraw);//200
            }
            else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensionX, dimensionY);
                params.leftMargin = this.positionXDraw; //Your X coordinate
                params.topMargin = this.positionYDraw; //Your Y coordinate
                imageView.setLayoutParams(params);
            }
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

    class Egg {
        private int positionX;
        private int positionY;
        private int positionXDraw;
        private int positionYDraw;
        private String resource;//the image resource that represents the body
        private int dimensionX;//in pixels
        private int dimensionY;//in pixels
        private float dimensionXReal;//in pixels
        private float dimensionYReal;//in pixels
        private ImageView imageView;//placeholder for fox image
        private int positionToMove;
        private boolean hit;
        private boolean hitCheckForInvisible;

        private int type;
        private int extraType;


        public Egg(int positionX, int positionY) {
            hit = false;
            this.positionX  = positionX;
            this.positionY  = positionY;

            resource    = "egg";
            dimensionXReal = 40;            dimensionX = (int)(dimensionXReal * screenRatioX);
            dimensionYReal = 40;            dimensionY = (int)(dimensionYReal * screenRatioY);

            positionXDraw   = this.positionX - dimensionX / 2;
            positionYDraw   = this.positionY - dimensionY / 2;


            imageView = new ImageView(getApplicationContext());
            imageView.setImageResource(R.drawable.egg);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensionX, dimensionY);
            params.leftMargin = this.positionXDraw ; //Your X coordinate
            params.topMargin = this.positionYDraw; //Your Y coordinate
            imageView.setLayoutParams(params);

        }

        public void setImageView(int R_id_id) {
            this.imageView = (ImageView) findViewById(R_id_id);
        }

        public void setImageResource(int imageResource) {
            imageView.setImageResource(imageResource);
        }


        public void setPosition(int positionX, int positionY){
            this.positionX   = positionX;
            this.positionY   = positionY;

            positionXDraw   = this.positionX - dimensionX / 2;
            positionYDraw   = this.positionY - dimensionY / 2;

            if (android.os.Build.VERSION.SDK_INT >= 11) {
                imageView.setX(this.positionXDraw);
                imageView.setY(this.positionYDraw);//200
            }
            else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensionX, dimensionY);
                params.leftMargin = this.positionXDraw; //Your X coordinate
                params.topMargin = this.positionYDraw; //Your Y coordinate
                imageView.setLayoutParams(params);
            }
        }

        public void setY(int positionY){
            this.positionY   = positionY;
        }
        public void setXY(int positionX, int positionY){
            this.positionX   = positionX;
            this.positionY   = positionY;
        }

        public void displayY(int positionY){
            this.positionYDraw   = positionY - this.dimensionY / 2;
            if (android.os.Build.VERSION.SDK_INT >= 11) {

                this.imageView.setY(this.positionYDraw);
            }
            else {
                //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.dimensionX, this.dimensionY);
                params.leftMargin = this.positionXDraw; //Your X coordinate
                params.topMargin = this.positionYDraw; //Your Y coordinate
                this.imageView.setLayoutParams(params);
            }
        }

        public void displayXY(){
            this.positionXDraw   = this.positionX - this.dimensionX / 2;
            this.positionYDraw   = this.positionY - this.dimensionY / 2;
            if (android.os.Build.VERSION.SDK_INT >= 11) {

                this.imageView.setX(this.positionXDraw);
                this.imageView.setY(this.positionYDraw);
            }
            else {
                //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.dimensionX, this.dimensionY);
                params.leftMargin = this.positionXDraw; //Your X coordinate
                params.topMargin = this.positionYDraw; //Your Y coordinate
                this.imageView.setLayoutParams(params);
            }
        }

        public void setVisibility(int visibility) {
            this.imageView.setVisibility(visibility);
        }

        public ImageView getImageView() {
            return imageView;
        }


    }


    class Fox {
        private int positionX;
        private int positionY;
        private int positionXDraw;
        private int positionYDraw;
        private String resource;//the image resource that represents the body
        private int dimensionX;//in pixels
        private int dimensionY;//in pixels
        private float dimensionXReal;//in pixels
        private float dimensionYReal;//in pixels
        private ImageView imageView;//placeholder for fox image
        private int positionToMove;
        public int type;//1-orange//2-blue
        private float sry;

        private AnimationDrawable mAnimation;
        private boolean mStart;

        public Fox(int positionX, int positionY) {
            this.positionX  = positionX;
            this.positionY  = positionY;

            resource    = "fox";
            dimensionXReal = 100;            dimensionX = (int)(dimensionXReal * screenRatioX);
            dimensionYReal = 100;            dimensionY = (int)(dimensionYReal * screenRatioY);

            positionXDraw   = this.positionX - dimensionX / 2;
            positionYDraw   = this.positionY - dimensionY / 2;

            imageView = new ImageView(getApplicationContext());

            //imageView.setImageResource(R.drawable.fox);
            imageView.setImageResource(+R.anim.fox_animation_1);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensionX, dimensionY);
            params.leftMargin = this.positionXDraw ; //Your X coordinate
            params.topMargin = this.positionYDraw; //Your Y coordinate
            imageView.setLayoutParams(params);
            type    = 1;



            mAnimation = (AnimationDrawable) imageView.getDrawable();

            mStart = false;

            if (mStart == false) {
                // start animation
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setVisibility(View.VISIBLE);
                        mAnimation.start();
                        mStart = true;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //mbtnStart.setText("Stop");
                    }
                });
            } else {
                // stop animation
                mAnimation.stop();
                //mbtnStart.setText("Start");
                mStart = false;
            }


        }

        public void moveToPosX(int positionToMove) {
            this.positionToMove = positionToMove;
            Thread foxThread = new Thread(new UpdateThread(this, 2, this));
            foxThread.start();
        }

        public void createBody() {

        }

        public void setPosition(int positionX, int positionY){
            this.positionX   = positionX;
            this.positionY   = positionY;

            positionXDraw   = this.positionX - dimensionX / 2;
            positionYDraw   = this.positionY - dimensionY / 2;

            if (android.os.Build.VERSION.SDK_INT >= 11) {
                imageView.setX(this.positionXDraw);
                imageView.setY(this.positionYDraw);//200
            }
            else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensionX, dimensionY);
                params.leftMargin = this.positionXDraw; //Your X coordinate
                params.topMargin = this.positionYDraw; //Your Y coordinate
                imageView.setLayoutParams(params);
            }
        }

        public void setX(int positionX){
            this.positionX   = positionX;
            //Log.d("SSSSS         ------------", "" + positionX);
        }

        public void displayX(int positionX){
            //Log.d("DDDDD         ------------", "" + positionX);
            this.positionXDraw   = positionX - this.dimensionX / 2;

            if (android.os.Build.VERSION.SDK_INT >= 11) {

                this.imageView.setX(this.positionXDraw);
            }
            else {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.dimensionX, this.dimensionY);
                params.leftMargin = this.positionXDraw; //Your X coordinate
                params.topMargin = this.positionYDraw; //Your Y coordinate
                this.imageView.setLayoutParams(params);
            }
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageResource(int imageResource) {
            imageView.setImageResource(imageResource);
        }
    }

    class UpdateThread implements Runnable {
        // Variable to store the animation state.
        private boolean mMoveUp = false;
        private int type;
        private Egg egg;
        private Fox fox;
        private Fox xof;

        public UpdateThread(Object data, int type, Object xof) {
            this.type = type;
            if(type == 1) {
                this.egg = (Egg) data;
            } else if(type == 2) {
                this.fox = (Fox) data;
            }
            this.xof = (Fox) xof;

        }

        @Override
        public void run() {
            switch(type) {

                case 1: //egg
                    //Log.d("@@@------------eggsCounter = ", "" + eggsCounter);
                    // Move the image down and right.
                    if (egg.type == 1) {
                        eggsCounter++;
                        boolean eggHitTestForAfterCheck = false;
                        for (int i = 0; i < droppingDistance; i++) {
                            //Log.d("@@@------------", "2");
                            hitEgg(egg);
                            //Log.d("@@@------------", "3");
                            if (egg.hit == true) {
                                egg.hit = false;
                                eggHitTestForAfterCheck = true;
                                scoreFox++;
                                checkScore();


                                mp_fox_sound_fox_bite_egg.start();
                                //Log.d("@@@------------", "4");
                                break;
                            }
                            final int j = i + eggDistanceFromTop;
                            egg.setY(j);//draw
                            // Try posting a runnable to the UI thread to update the view.
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // IF the animation is in the moving down phase
                                        egg.displayY(j);//draw
                                        if (egg.hitCheckForInvisible == true) {
                                            egg.hitCheckForInvisible = false;
                                            egg.setVisibility(View.INVISIBLE);
                                        }
                                        //Log.d("@@@------------", "5");

                                    }
                                });
                                Thread.sleep(4);

                            } catch (InterruptedException e) {
                                //Log.d("@@@------------", "6");
                                e.printStackTrace();
                            }
                        }
                        if (eggHitTestForAfterCheck == false) {
                            scoreChicken++;
                            mp_egg_sound_egg_splat.start();
                            //egg.setVisibility(View.INVISIBLE);
                        }

                        //in level 4 change egg type 1 to mix (1 or 2)
                        if(levelNumber == 4) {
                            //let main thread update the egg view
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Random rand = new Random();
                                    if((egg.extraType = rand.nextInt(2) + 1) == 1) {
                                        egg.setImageResource(R.drawable.egg_small);
                                        egg.type = 1;
                                    } else if((egg.extraType = rand.nextInt(2) + 1) == 2) {
                                        egg.setImageResource(R.drawable.egg_small_blue);
                                        egg.type = 2;
                                    }
                                    //goto
                                }
                            });
                        }


                    } else if (egg.type == 2) {

                        eggsCounter++;
                        boolean eggHitTestForAfterCheck = false;
                        boolean chickinHitTestForAfterCheck = false;
                        int remainingPosition = 0;
                        int startPosition = -1;
                        //Log.d("---1---", "" + remainingPosition);
                        for (int i = 0; i < droppingDistance; i++) {
                            //Log.d("@@@------------", "2");
                            hitEgg(egg);
                            //Log.d("@@@------------", "3");
                            if (egg.hit == true) {
                                egg.hit = false;
                                eggHitTestForAfterCheck = true;
                                //Log.d("---2---", "" + remainingPosition);
                                break;
                            }
                            final int j = i + eggDistanceFromTop;
                            remainingPosition = j;
                            //Log.d("---#---", ""+remainingPosition );
                            egg.setY(j);//draw
                            // Try posting a runnable to the UI thread to update the view.
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // IF the animation is in the moving down phase

                                        egg.displayY(j);//draw
                                        //Log.d("@@@------------", "5");

                                    }
                                });
                                Thread.sleep(4);

                            } catch (InterruptedException e) {
                                //Log.d("@@@------------", "6");
                                e.printStackTrace();
                            }
                        }

                        if (eggHitTestForAfterCheck == false) {
                            //scoreChicken++;
                            //mp_egg_sound_egg_splat.start();
                            chickinHitTestForAfterCheck = true;
                            //egg.setVisibility(View.INVISIBLE);
                            //Log.d("---3---", "" + remainingPosition);

                        } else {
                            //Log.d("---4---", "" + remainingPosition);



                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // IF the animation is in the moving down phase
                                        //Log.d("---4---", "" + xof.positionX +" *** "+ (int) (450 * xof.sry));
                                        xof.positionToMove = xof.positionX - (int) (150 * xof.sry);
                                        Thread foxThread = new Thread(new UpdateThread(xof, 2, xof));
                                        foxThread.start();
                                    }
                                });
                                Thread.sleep(4);

                            } catch (InterruptedException e) {
                                //Log.d("@@@------------", "6");
                                e.printStackTrace();
                            }



                            eggHitTestForAfterCheck = false;
                            startPosition = remainingPosition;
                            //Log.d("---2---", ""+remainingPosition );
                            for (int i = 0; i < jumpingHeight; i++) {
                                final int j = startPosition - i;
                                remainingPosition = j;
                                //Log.d("---*---", ""+remainingPosition );
                                if (j % 3 == 0) egg.positionX = egg.positionX - 1;
                                egg.setXY(egg.positionX, j);//draw


                                // Try posting a runnable to the UI thread to update the view.
                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // IF the animation is in the moving down phase

                                            egg.displayXY();//draw
                                            //Log.d("@@@------------", "5");

                                        }
                                    });
                                    Thread.sleep(4);

                                } catch (InterruptedException e) {
                                    //Log.d("@@@------------", "6");
                                    e.printStackTrace();
                                }


                            }


                            if (eggHitTestForAfterCheck == true) {
                                ;//vulpea a prins oul in urcare
                                //nu mai continuam
                                //Log.d("---6---", "" + remainingPosition);

                            } else {
                                //Log.d("---7---", "" + remainingPosition);

                                //oul a ajuns la inaltimea maxima
                                //continuam
                                eggHitTestForAfterCheck = false;

                                startPosition = remainingPosition;
                                //Log.d("---3---", ""+remainingPosition );
                                for (int i = 0; i < jumpingHeight; i++) {
                                    final int j = startPosition + i;
                                    remainingPosition = j;
                                    //Log.d("---%---", ""+remainingPosition );
                                    if (j % 2 == 0) egg.positionX = egg.positionX - 1;
                                    egg.setXY(egg.positionX, j);//draw


                                    hitEgg(egg);
                                    //Log.d("@@@------------", "3");
                                    if (egg.hit == true) {
                                        egg.hit = false;
                                        eggHitTestForAfterCheck = true;
                                        scoreFox++;
                                        checkScore();
                                        mp_fox_sound_fox_bite_egg.start();
                                        //Log.d("@@@------------", "4");
                                        //Log.d("---8---", "" + remainingPosition);

                                        break;
                                    }


                                    // Try posting a runnable to the UI thread to update the view.
                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // IF the animation is in the moving down phase
                                                if (egg.hitCheckForInvisible == true) {
                                                    egg.hitCheckForInvisible = false;
                                                    egg.setVisibility(View.INVISIBLE);
                                                }
                                                egg.displayXY();//draw
                                                //Log.d("@@@------------", "5");

                                            }
                                        });
                                        Thread.sleep(4);

                                    } catch (InterruptedException e) {
                                        //Log.d("@@@------------", "6");
                                        e.printStackTrace();
                                    }
                                }

                                if (eggHitTestForAfterCheck == true) {
                                    ;//vulpea a prins oul in urcare
                                    //nu mai continuam
                                    //Log.d("---9---", "" + remainingPosition);

                                } else {
                                    //Log.d("---a---", "" + remainingPosition);

                                    eggHitTestForAfterCheck = false;

                                    startPosition = remainingPosition;
                                    //Log.d("---4---", ""+remainingPosition );
                                    for (int i = 0; i < droppingDistance - startPosition + eggDistanceFromTop; i++) {
                                        final int j = startPosition + i;
                                        remainingPosition = j;
                                        //Log.d("---%---", ""+remainingPosition );

                                        hitEgg(egg);
                                        //Log.d("@@@------------", "3");
                                        if (egg.hit == true) {
                                            egg.hit = false;
                                            eggHitTestForAfterCheck = true;
                                            scoreFox++;
                                            checkScore();
                                            mp_fox_sound_fox_bite_egg.start();
                                            //Log.d("@@@------------", "4");
                                            //Log.d("---b---", "" + remainingPosition);
                                            break;
                                        }

                                        egg.setXY(egg.positionX, j);//draw

                                        // Try posting a runnable to the UI thread to update the view.
                                        try {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // IF the animation is in the moving down phase
                                                    if (egg.hitCheckForInvisible == true) {
                                                        egg.hitCheckForInvisible = false;
                                                        egg.setVisibility(View.INVISIBLE);
                                                    }
                                                    egg.displayXY();//draw
                                                    //Log.d("@@@------------", "5");

                                                }
                                            });
                                            Thread.sleep(4);

                                        } catch (InterruptedException e) {
                                            //Log.d("@@@------------", "6");
                                            e.printStackTrace();
                                        }
                                    }

                                    if (eggHitTestForAfterCheck == false) {
                                        scoreChicken++;
                                        mp_egg_sound_egg_splat.start();
                                    //chickinHitTestForAfterCheck = true;
                                        //Log.d("---c---", "" + remainingPosition);

                                        //egg.setVisibility(View.INVISIBLE);
                                    }
                                    // //Log.d("---#---", ""+eggDistanceFromTop );
                                    //Log.d("---#---", ""+droppingDistance );
                                    //Log.d("---#---", ""+ (eggDistanceFromTop + droppingDistance) );
                                    //Log.d("---#---", ""+ (droppingDistance - remainingPosition + eggDistanceFromTop));
                                }
                            }
                        }


                        eggHitTestForAfterCheck = false;
                        startPosition = remainingPosition;
                        if (chickinHitTestForAfterCheck == true) {
                            for (int i = 0; i < jumpingHeight; i++) {
                                final int j = startPosition - i;
                                remainingPosition = j;
                                //Log.d("---%---", ""+remainingPosition );

                                hitEgg(egg);
                                //Log.d("@@@------------", "3");
                                if (egg.hit == true) {
                                    egg.hit = false;
                                    eggHitTestForAfterCheck = true;
                                    scoreFox++;
                                    checkScore();
                                    mp_fox_sound_fox_bite_egg.start();
                                    //Log.d("@@@------------", "4");
                                    //Log.d("---c---", "" + remainingPosition);
                                    break;
                                }

                                egg.setXY(egg.positionX, j);//draw

                                // Try posting a runnable to the UI thread to update the view.
                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // IF the animation is in the moving down phase
                                            if (egg.hitCheckForInvisible == true) {
                                                egg.hitCheckForInvisible = false;
                                                egg.setVisibility(View.INVISIBLE);
                                            }
                                            egg.displayXY();//draw
                                            //Log.d("@@@------------", "5");

                                        }
                                    });
                                    Thread.sleep(4);

                                } catch (InterruptedException e) {
                                    //Log.d("@@@------------", "6");
                                    e.printStackTrace();
                                }
                            }

                            if (eggHitTestForAfterCheck == false) {
                                //scoreChicken++;
                                //mp_egg_sound_egg_splat.start();
                                chickinHitTestForAfterCheck = true;
                                //egg.setVisibility(View.INVISIBLE);
                                //Log.d("---d---", "" + remainingPosition);
                                eggHitTestForAfterCheck = false;
                                startPosition = remainingPosition;
                                for (int i = 0; i < jumpingHeight; i++) {
                                    final int j = startPosition + i;
                                    remainingPosition = j;
                                    //Log.d("---%---", ""+remainingPosition );

                                    hitEgg(egg);
                                    //Log.d("@@@------------", "3");
                                    if (egg.hit == true) {
                                        egg.hit = false;
                                        eggHitTestForAfterCheck = true;
                                        scoreFox++;
                                        checkScore();
                                        mp_fox_sound_fox_bite_egg.start();
                                        //Log.d("@@@------------", "4");
                                        //Log.d("---e---", "" + remainingPosition);
                                        break;
                                    }

                                    egg.setXY(egg.positionX, j);//draw

                                    // Try posting a runnable to the UI thread to update the view.
                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // IF the animation is in the moving down phase
                                                if (egg.hitCheckForInvisible == true) {
                                                    egg.hitCheckForInvisible = false;
                                                    egg.setVisibility(View.INVISIBLE);
                                                }
                                                egg.displayXY();//draw
                                                //Log.d("@@@------------", "5");

                                            }
                                        });
                                        Thread.sleep(4);

                                    } catch (InterruptedException e) {
                                        //Log.d("@@@------------", "6");
                                        e.printStackTrace();
                                    }
                                }

                                if (eggHitTestForAfterCheck == false) {
                                    scoreChicken++;
                                    mp_egg_sound_egg_splat.start();
                                    //chickinHitTestForAfterCheck = true;
                                    //egg.setVisibility(View.INVISIBLE);
                                }

                            }

                        }

                        //in level 4 change egg type 1 to mix (1 or 2)
                        if(levelNumber == 4) {
                            //let main thread update the egg view
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Random rand = new Random();
                                    if((egg.extraType = rand.nextInt(2) + 1) == 1) {
                                        egg.setImageResource(R.drawable.egg_small);
                                        egg.type = 1;
                                    } else if((egg.extraType = rand.nextInt(2) + 1) == 2) {
                                        egg.setImageResource(R.drawable.egg_small_blue);
                                        egg.type = 2;
                                    }
                                    //goto
                                }
                            });
                        }

                    } else if (egg.type == 3) {
                        int speed = 0;
                        switch(egg.extraType) {
                            case 1: speed = 8;break;
                            case 2: speed = 6;break;
                            case 3: speed = 4;break;
                            case 4: speed = 2;break;
                            case 5: speed = 1;break;
                        }

                        eggsCounter++;
                        boolean eggHitTestForAfterCheck = false;
                        for (int i = 0; i < droppingDistance; i++) {
                            //Log.d("@@@------------", "2");
                            hitEgg(egg);
                            //Log.d("@@@------------", "3");
                            if (egg.hit == true) {
                                egg.hit = false;
                                eggHitTestForAfterCheck = true;
                                scoreFox++;
                                checkScore();


                                mp_fox_sound_fox_bite_egg.start();
                                //Log.d("@@@------------", "4");
                                break;
                            }
                            final int j = i + eggDistanceFromTop;
                            egg.setY(j);//draw
                            // Try posting a runnable to the UI thread to update the view.
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // IF the animation is in the moving down phase
                                        egg.displayY(j);//draw
                                        if (egg.hitCheckForInvisible == true) {
                                            egg.hitCheckForInvisible = false;
                                            egg.setVisibility(View.INVISIBLE);
                                        }
                                        //Log.d("@@@------------", "5");

                                    }
                                });
                                Thread.sleep(speed);

                            } catch (InterruptedException e) {
                                //Log.d("@@@------------", "6");
                                e.printStackTrace();
                            }
                        }
                        if (eggHitTestForAfterCheck == false) {
                            scoreChicken++;
                            mp_egg_sound_egg_splat.start();
                            //egg.setVisibility(View.INVISIBLE);
                        }

                        //let main thread update the egg view
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Random rand = new Random();
                                String image_id;
                                int idx = -1;
                                egg.extraType = rand.nextInt(5) + 1;
                                image_id = "egg_small_yellow_" + String.valueOf(egg.extraType);
                                if((idx = getResources().getIdentifier(image_id, "drawable", PACKAGE_NAME)) != 0) {
                                    egg.setImageResource(idx);
                                }
                            }
                        });

                    }

                    break;

                case 2://fox
                    int sense = 1;//left
                    int distanceToTravel = 0;

                    //Log.d("+++++fox.positionToMove", "" + fox.positionToMove);
                    //Log.d("+++++fox.positionX",      "" + fox.positionX);
                    if (fox.positionToMove > fox.positionX) {
                        sense = 2;//right
                        distanceToTravel = fox.positionToMove - fox.positionX;
                        //Log.d("+++++distanceToTravel", "" + distanceToTravel);

                    } else if (fox.positionToMove < fox.positionX) {
                        sense = 1;//stanga
                        distanceToTravel = fox.positionX - fox.positionToMove;
                        //Log.d("+++++distanceToTravel", "" + distanceToTravel);
                    }
                    // Move the image down and right.
                    for (int i = 0; i < distanceToTravel; i++) {
                        // Try posting a runnable to the UI thread to update the view.
                        try {

                            if (sense == 1) fox.positionX = fox.positionX - 1;
                            else if (sense == 2) fox.positionX = fox.positionX + 1;
                            final int j = fox.positionX;
                            fox.setX(j);//draw
                            //Log.d("!!!!!         ------------", "" + j + " ::: " + i);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // IF the animation is in the moving down phase
                                    fox.displayX(j);//draw
                                }
                            });

                            Thread.sleep(4);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    break;

            }

        }    // run()!
    } // UpdateThread!


    public void checkScore() {
        if(eggsCounter > levelDependentNumberOfEggs) {
            if(scoreChicken > scoreFox) {
                //display message
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        //Log.d("xxx","test showing page");
                        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                            //Log.d("xxx","test showing page - passed");
                            mInterstitialAd.show();
                        } else {
                            //Log.d("xxx","test showing page - failed");
                        }
                        //Log.d("xxx","interstitial status = " + mInterstitialAd.isLoading());
                        //Log.d("xxx","interstitial status = " + mInterstitialAd.isLoaded());

                        // IF the animation is in the moving down phase
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(levelPassedSizeX, levelPassedSizeY);
                        params.leftMargin = 0;
                        params.topMargin = 0;//levelPassedSizeY/4;
                        imageLevelPassed.setLayoutParams(params);
                        imageLevelPassed.setVisibility(View.VISIBLE);

                    }
                });

            }
        }
    }

    public void hitEgg(Egg egg) {

        //check if egg1 hit
       if(distance(fox.positionX, fox.positionY, egg.positionX, egg.positionY) < (int)(fox.dimensionX/2)) {
           egg.hit                 = true;
           egg.hitCheckForInvisible = true;
           //Log.d("$$$$$------ distance", "" + distance(fox.positionX, fox.positionY, egg.positionX, egg.positionY));
           //Log.d("$$$$$------ (int)(fox.dimensionX/2)", "" + (int)(fox.dimensionX/2));

       }
        else {
           egg.hit = false;
           egg.hitCheckForInvisible = false;
       }
    }

    private static int distance(int x1, int y1, int x2, int y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        int distanceInPx = (int) sqrt(dx * dx + dy * dy);
        return distanceInPx;
    }
    private static float distanceFloatWithDensity(float x1, float y1, float x2, float y2, float density) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) sqrt(dx * dx + dy * dy);
        return pxToDp(distanceInPx, density);
    }
    private static float pxToDp(float px, float density) {
        return px / density;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        //Log.i("fff-------", "on WindowFocusChanged .....");
        //Log.d("fff-------this.getWindow().getDecorView().getHeight()", "" + this.getWindow().getDecorView().getHeight());
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.topLayout);
        //Log.d("fff-------relativeLayout.getBottom()", "" + relativeLayout.getBottom());
        //Log.d("fff-------relativeLayout.getMeasuredHeight()", "" + relativeLayout.getMeasuredHeight());

        //screenSizeX = relativeLayout.getMeasuredWidth();
        //screenSizeY = relativeLayout.getMeasuredHeight();


    }


    @Override
    protected void onResume(){
        super.onResume();
        // put your code here...
        //Log.i("-------", "on Resume .....");
        //Log.d("this.getWindow().getDecorView().getHeight()", "" + this.getWindow().getDecorView().getHeight());
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.topLayout);
        //Log.d("relativeLayout.getBottom()", "" + relativeLayout.getBottom());
        //Log.d("relativeLayout.getMeasuredHeight()", "" + relativeLayout.getMeasuredHeight());


    }


    @Override
    protected void onStart() {
        super.onStart();
        //Log.i("-------", "on Start .....");
        //Log.d("this.getWindow().getDecorView().getHeight()", "" + this.getWindow().getDecorView().getHeight());
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.topLayout);
        //Log.d("relativeLayout.getBottom()", "" + relativeLayout.getBottom());
        //Log.d("relativeLayout.getMeasuredHeight()", "" + relativeLayout.getMeasuredHeight());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.topLayout);
        imageViewBackground = (ImageView) findViewById(R.id.imageViewBackground);



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (android.os.Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
        } else if (android.os.Build.VERSION.SDK_INT <= 12) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            size.set(displaymetrics.widthPixels, displaymetrics.heightPixels);
        }
        screenSizeX = size.x;
        screenSizeY = size.y;

        screenRatioX = ((float)screenSizeX / (float)(600));
        screenRatioY = screenRatioX;

        droppingDistance = (int)(495 * screenRatioY);
        jumpingHeight       = (int)(180 * screenRatioY);


        gamePanelSizeX_real = 600;
        gamePanelSizeY_real = 600;
        gamePanelSizeX      = (int)((float)gamePanelSizeX_real * screenRatioX);
        gamePanelSizeY      = (int)((float)gamePanelSizeY_real * screenRatioY);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gamePanelSizeX, gamePanelSizeY);
        imageViewBackground.setLayoutParams(params);



        //Log.d("screenSizeX", "" + (screenSizeX));
        //Log.d("screenRatioX", "" + (screenRatioX));
        //Log.d("gamePanelSizeX", "" + gamePanelSizeX);

        //Log.d("screenSizeY", "" + (screenSizeY));
        //Log.d("screenRatioY", "" + (screenRatioY));
        //Log.d("gamePanelSizeY", "" + gamePanelSizeY);


        egg1PosXReal = 75;      egg1PosX = (int)(((float)egg1PosXReal) * screenRatioX);
        egg2PosXReal = 225;     egg2PosX = (int)(((float)egg2PosXReal) * screenRatioX);
        egg3PosXReal = 375;     egg3PosX = (int)(((float)egg3PosXReal) * screenRatioX);
        egg4PosXReal = 525;     egg4PosX = (int)(((float)egg4PosXReal) * screenRatioX);

        eggDistanceFromTop = (int)(((float)70) * screenRatioY);
        egg1PosY = eggDistanceFromTop;
        egg2PosY = eggDistanceFromTop;
        egg3PosY = eggDistanceFromTop;
        egg4PosY = eggDistanceFromTop;

        chicken1PosXReal = 75;  chicken1PosX = (int)(((float)chicken1PosXReal) * screenRatioX);
        chicken2PosXReal = 225; chicken2PosX = (int)(((float)chicken2PosXReal) * screenRatioX);
        chicken3PosXReal = 375; chicken3PosX = (int)(((float)chicken3PosXReal) * screenRatioX);
        chicken4PosXReal = 525; chicken4PosX = (int)(((float)chicken4PosXReal) * screenRatioX);

        chicken1PosYReal = 65;  chicken1PosY = (int)(((float)chicken1PosYReal) * screenRatioY);
        chicken2PosYReal = 65;  chicken2PosY = (int)(((float)chicken2PosYReal) * screenRatioY);
        chicken3PosYReal = 65;  chicken3PosY = (int)(((float)chicken3PosYReal) * screenRatioY);
        chicken4PosYReal = 65;  chicken4PosY = (int)(((float)chicken4PosYReal) * screenRatioY);

        dot1PosYReal = 565;     dot1PosY = (int)(((float)dot1PosYReal) * screenRatioY);
        dot2PosYReal = 565;     dot2PosY = (int)(((float)dot2PosYReal) * screenRatioY);
        dot3PosYReal = 565;     dot3PosY = (int)(((float)dot3PosYReal) * screenRatioY);
        dot4PosYReal = 565;     dot4PosY = (int)(((float)dot4PosYReal) * screenRatioY);

        fox = new Fox((int)(300 * screenRatioX), (int) (450 * screenRatioY));//450
        //fox.setPosition((int)(300 * screenRatioX), (int) (450 * screenRatioY));//450
        fox.sry = screenRatioY;

        chicken1 = new Chicken(chicken1PosX, chicken1PosY, 1);
        chicken2 = new Chicken(chicken2PosX, chicken2PosY, 2);
        chicken3 = new Chicken(chicken3PosX, chicken3PosY, 3);
        chicken4 = new Chicken(chicken4PosX, chicken4PosY, 4);

        dot1 = new Dot(chicken1PosX, dot1PosY, 1);
        dot2 = new Dot(chicken2PosX, dot2PosY, 2);
        dot3 = new Dot(chicken3PosX, dot3PosY, 3);
        dot4 = new Dot(chicken4PosX, dot4PosY, 4);

        egg1 = new Egg(egg1PosX, egg1PosY);
        egg2 = new Egg(egg2PosX, egg2PosY);
        egg3 = new Egg(egg3PosX, egg3PosY);
        egg4 = new Egg(egg4PosX, egg4PosY);


        //Log.d("(int) (300 * screenRatioY)", "" + (int)(300 * screenRatioY));
        //Log.d("(int) (450 * screenRatioY)", "" + (int) (450 * screenRatioY));


        //fox.setPosition((screenSizeX / 2), (int) (450 * screenRatioY));
        //fox.setPosition(0, 0);



        relativeLayout.addView(egg1.getImageView());
        relativeLayout.addView(egg2.getImageView());
        relativeLayout.addView(egg3.getImageView());
        relativeLayout.addView(egg4.getImageView());
        relativeLayout.addView(chicken1.getImageView());
        relativeLayout.addView(chicken2.getImageView());
        relativeLayout.addView(chicken3.getImageView());
        relativeLayout.addView(chicken4.getImageView());

        imageViewOverFloor = new ImageView(this);
        imageViewOverFloor.setImageResource(R.drawable.over_floor);
        params = new RelativeLayout.LayoutParams(gamePanelSizeX, (int)(75 * screenRatioY));
        params.leftMargin = 0; //Your X coordinate
        params.topMargin = (int)(454 * screenRatioY);
        imageViewOverFloor.setLayoutParams(params);
        relativeLayout.addView(imageViewOverFloor);
        relativeLayout.addView(fox.getImageView());

        relativeLayout.addView(dot1.getImageView());
        relativeLayout.addView(dot2.getImageView());
        relativeLayout.addView(dot3.getImageView());
        relativeLayout.addView(dot4.getImageView());


        chicken1.imageView.setOnClickListener(viewOnClickListener);
        chicken2.imageView.setOnClickListener(viewOnClickListener);
        chicken3.imageView.setOnClickListener(viewOnClickListener);
        chicken4.imageView.setOnClickListener(viewOnClickListener);

        dot1.imageView.setOnClickListener(viewOnClickListener);
        dot2.imageView.setOnClickListener(viewOnClickListener);
        dot3.imageView.setOnClickListener(viewOnClickListener);
        dot4.imageView.setOnClickListener(viewOnClickListener);



        mp_chicken_sound_drop_egg = MediaPlayer.create(getApplicationContext(), R.raw.chicken_sound_drop_egg);
        mp_fox_sound_fox_bite_egg = MediaPlayer.create(getApplicationContext(), R.raw.fox_bite_egg);
        mp_egg_sound_egg_splat = MediaPlayer.create(getApplicationContext(), R.raw.egg_splat);


        //Log.i("-------", "On Create .....");
        //Log.d("this.getWindow().getDecorView().getHeight()", "" + this.getWindow().getDecorView().getHeight());
        //Log.d("relativeLayout.getBottom()", "" + relativeLayout.getBottom());
        //Log.d("relativeLayout.getMeasuredHeight()", "" + relativeLayout.getMeasuredHeight());

        scoreChicken    = 0;
        scoreFox        = 0;
        scoreWinner     = 0;
        levelNumber     = 0;
        levelType       = 0;
        levelTypeNumber = 0;
        levelDependentNumberOfEggs = 0;
        gameStatus      = "play";

        textView_scoreChicken = (TextView) findViewById(R.id.scoreChicken);
        textView_scoreFox = (TextView) findViewById(R.id.scoreFox);

        levelPassedSizeX      = (int)((float)600 * screenRatioX);
        levelPassedSizeY      = (int)((float)600 * screenRatioY);

        imageLevelPassed = new Button(this);
        imageLevelPassed.setBackgroundResource(R.drawable.mybutton);
        imageLevelPassed.setVisibility(View.VISIBLE);
        imageLevelPassed.setId(R.id.levelPassed);
        imageLevelPassed.setText("BEGIN GAME");
        imageLevelPassed.setTextSize(TypedValue.COMPLEX_UNIT_PX, 22);
        imageLevelPassed.setTextColor(Color.rgb(200, 0, 0));
        imageLevelPassed.setTypeface(null, Typeface.BOLD);

        params = new RelativeLayout.LayoutParams(levelPassedSizeX, levelPassedSizeY);
        params.leftMargin = 0;
        params.topMargin = 0;//levelPassedSizeY/4;
        imageLevelPassed.setLayoutParams(params);

        relativeLayout.addView(imageLevelPassed);
        imageLevelPassed.setOnClickListener(viewOnClickListener);

        gameStatus = "play";


        PACKAGE_NAME = getApplicationContext().getPackageName();
//goto
        //levelNumber = 2;












        //Log.d("xxx", "1 new InterstitialAd ... ");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5508564355533349/7600094917");

        // Request a new ad if one isn't already loaded
        //if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
        //    AdRequest adRequest = new AdRequest.Builder().addTestDevice("B1DA4847753CA25E69418A1C33CB9FBE").build();
        //    mInterstitialAd.loadAd(adRequest);
        //}

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener viewOnClickListener
            = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            final int id = v.getId();
            //ImageButton imgButton;
            final int chicken1ID = chicken1.imageView.getId();
            switch (id) {
                case R.id.levelPassed:

                    //Log.d("xxx", "test new ad loading");
                    // Request a new ad if one isn't already loaded
                    if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                        //Log.d("xxx", "test new ad loading - passed");
                        AdRequest adRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adRequest);
                    }
                    //else {
                        //Log.d("xxx", "test new ad loading - failed");
                    //}


                    if(levelNumber >= 3) {
                        imageLevelPassed.setText("GAME OVER");
                    } else if(levelNumber == 2){
                        imageLevelPassed.setText("START LEVEL " + (levelNumber+2) + "\n" + "200 EGGS" + "\n" + "MIXED LEVEL");
                    } else {
                        imageLevelPassed.setText("START LEVEL " + (levelNumber+2) + "\n" + "40 EGGS");
                    }
                    switch(levelNumber) {
                        case 0:
                            //level 1
                            imageLevelPassed.setVisibility(View.GONE);
                            scoreChicken    = 0;
                            scoreFox        = 0;
                            eggsCounter     = 0;
                            scoreWinner     = 0;
                            levelNumber     = 1;
                            levelType       = 1;
                            levelTypeNumber = 1;
                            levelDependentNumberOfEggs = 40;
                            gameStatus      = "play";                            //change fox
                            fox.type        = 1;//orange fox
                            eggType         = 1;//yellow eggs
                            egg1.type       = 1;
                            egg2.type       = 1;
                            egg3.type       = 1;
                            egg4.type       = 1;
                            //activate new type of egg
                            break;
                        case 1:
                            //level 2
                            imageLevelPassed.setVisibility(View.GONE);
                            scoreChicken    = 0;
                            scoreFox        = 0;
                            eggsCounter     = 0;
                            scoreWinner     = 0;
                            levelNumber     = 2;
                            levelType       = 1;
                            levelTypeNumber = 2;
                            levelDependentNumberOfEggs = 40;
                            gameStatus      = "play";
                            fox.type        = 1;//orange blue
                            eggType         = 2;//blue eggs - jumping eggs
                            //fox.setImageResource(R.drawable.blue_fox);
                            egg1.type       = 2;
                            egg2.type       = 2;
                            egg3.type       = 2;
                            egg4.type       = 2;
                            egg1.setImageResource(R.drawable.egg_small_blue);
                            egg2.setImageResource(R.drawable.egg_small_blue);
                            egg3.setImageResource(R.drawable.egg_small_blue);
                            egg4.setImageResource(R.drawable.egg_small_blue);
                            //activate new type of egg
                            break;
                        case 2:
                            //level 3-work
                            imageLevelPassed.setVisibility(View.GONE);
                            scoreChicken    = 0;
                            scoreFox        = 0;
                            eggsCounter     = 0;
                            scoreWinner     = 0;
                            levelNumber     = 3;
                            levelType       = 1;
                            levelTypeNumber = 3;
                            levelDependentNumberOfEggs = 40;
                            gameStatus      = "play";
                            fox.type        = 1;//orange blue
                            egg1.type       = 3;
                            egg2.type       = 3;
                            egg3.type       = 3;
                            egg4.type       = 3;
                            egg1.extraType  = 1;
                            egg2.extraType  = 2;
                            egg3.extraType  = 4;
                            egg4.extraType  = 5;
                            egg1.setImageResource(R.drawable.egg_small_yellow_1);
                            egg2.setImageResource(R.drawable.egg_small_yellow_2);
                            egg3.setImageResource(R.drawable.egg_small_yellow_4);
                            egg4.setImageResource(R.drawable.egg_small_yellow_5);

                            //activate new type of egg
                            break;
                        case 3:
                            //mix level 4
                            imageLevelPassed.setVisibility(View.GONE);
                            scoreChicken    = 0;
                            scoreFox        = 0;
                            eggsCounter     = 0;
                            scoreWinner     = 0;
                            levelNumber     = 4;
                            levelType       = 2;
                            levelTypeNumber = 1;
                            levelDependentNumberOfEggs = 200;
                            gameStatus      = "play";
                            fox.type        = 1;//orange blue
                            eggType         = 4;//blue eggs - jumping eggs
                            egg1.type       = 1;
                            egg2.type       = 2;
                            egg3.type       = 2;
                            egg4.type       = 1;
                            egg1.setImageResource(R.drawable.egg_small);
                            egg2.setImageResource(R.drawable.egg_small_blue);
                            egg3.setImageResource(R.drawable.egg_small_blue);
                            egg4.setImageResource(R.drawable.egg_small);

                            break;

                        case 4:
                            //imageLevelPassed.setVisibility(View.GONE);
                            break;
                    }

                    break;
                case R.id.chicken1:
                    mp_chicken_sound_drop_egg.start();
                    egg1.setPosition(chicken1PosX, eggDistanceFromTop);
                    egg1.setVisibility(View.VISIBLE);
                    Thread myThread1 = new Thread(new UpdateThread(egg1, 1, fox));
                    myThread1.start();
                    fox.moveToPosX(chicken1PosX);
                    break;
                case R.id.chicken2:
                    mp_chicken_sound_drop_egg.start();
                    egg2.setPosition(chicken2PosX, eggDistanceFromTop);
                    egg2.setVisibility(View.VISIBLE);
                    Thread myThread2 = new Thread(new UpdateThread(egg2, 1, fox));
                    myThread2.start();
                    fox.moveToPosX(chicken2PosX);
                    break;
                case R.id.chicken3:
                    mp_chicken_sound_drop_egg.start();
                    egg3.setPosition(chicken3PosX, eggDistanceFromTop);
                    egg3.setVisibility(View.VISIBLE);
                    Thread myThread3 = new Thread(new UpdateThread(egg3, 1, fox));
                    myThread3.start();
                    fox.moveToPosX(chicken3PosX);
                    break;
                case R.id.chicken4:
                    mp_chicken_sound_drop_egg.start();
                    egg4.setPosition(chicken4PosX, eggDistanceFromTop);
                    egg4.setVisibility(View.VISIBLE);
                    Thread myThread4 = new Thread(new UpdateThread(egg4, 1, fox));
                    myThread4.start();
                    fox.moveToPosX(chicken4PosX);
                    break;

                case R.id.dot1:
                    mp_chicken_sound_drop_egg.start();
                    egg1.setPosition(chicken1PosX, eggDistanceFromTop);
                    egg1.setVisibility(View.VISIBLE);
                    //egg1.setVisibility(View.INVISIBLE);
                    Thread myThread5 = new Thread(new UpdateThread(egg1, 1, fox));
                    myThread5.start();
                    fox.moveToPosX(chicken1PosX);
                    break;
                case R.id.dot2:
                    mp_chicken_sound_drop_egg.start();
                    egg2.setPosition(chicken2PosX, eggDistanceFromTop);
                    egg2.setVisibility(View.VISIBLE);
                    Thread myThread6 = new Thread(new UpdateThread(egg2, 1, fox));
                    myThread6.start();
                    fox.moveToPosX(chicken2PosX);
                    break;
                case R.id.dot3:
                    mp_chicken_sound_drop_egg.start();
                    egg3.setPosition(chicken3PosX, eggDistanceFromTop);
                    egg3.setVisibility(View.VISIBLE);
                    Thread myThread7 = new Thread(new UpdateThread(egg3, 1, fox));
                    myThread7.start();
                    fox.moveToPosX(chicken3PosX);
                    break;
                case R.id.dot4:
                    mp_chicken_sound_drop_egg.start();
                    egg4.setPosition(chicken4PosX, eggDistanceFromTop);
                    egg4.setVisibility(View.VISIBLE);
                    Thread myThread8 = new Thread(new UpdateThread(egg4, 1, fox));
                    myThread8.start();
                    fox.moveToPosX(chicken4PosX);
                    break;
            }

            textView_scoreFox.setText("Fox\n  " + String.valueOf(scoreFox));
            textView_scoreChicken.setText("Chicken\n  " + String.valueOf(scoreChicken));
        }

    };

}

