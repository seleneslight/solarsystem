package com.example.solarsystem;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private void animateBackgroundColor(final RelativeLayout layout, int startColor, int endColor, long duration) {
        ObjectAnimator colorAnimation = ObjectAnimator.ofObject(layout, "backgroundColor", new ArgbEvaluator(), startColor, endColor);
        colorAnimation.setDuration(duration);
        colorAnimation.start();
    }
    
    private void generateStars(int count) {
        for (int i = 0; i < count; i++) {
            TextView tv = new TextView(getApplicationContext());
            Animation blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
            blink.setDuration((int) (Math.random() * (1000-200) + 200));
            tv.startAnimation(blink);
            tv.setRotation(45);
            int size = (int) (Math.random() * (25-15) + 15);
            tv.setWidth(size);
            tv.setHeight(size);
            tv.setX((float) (Math.random() * (1000-15) + 15));
            tv.setY((float) (Math.random() * (2000-15) + 15));
            tv.setBackgroundResource(R.drawable.white_square);
            tv.setZ(-1);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            rl.addView(tv, params);

        }
    }
    private TextView mercury, venus, earth, sun;
    RelativeLayout rl;
    boolean darkmode = false;
    private float centerX, centerY;
    private float radius1, radius2, radius3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = findViewById(R.id.matrix);
        sun = findViewById(R.id.sun);
        mercury = findViewById(R.id.mercury);
        venus = findViewById(R.id.venus);
        earth = findViewById(R.id.earth);

        // Load the animation
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);

        // Start the animation
        mercury.startAnimation(rotateAnimation);
        venus.startAnimation(rotateAnimation);
        earth.startAnimation(rotateAnimation);

        // Get the center of the screen
        final ViewTreeObserver viewTreeObserver = mercury.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    centerX = mercury.getX() + mercury.getWidth() / 2;
                    centerY = mercury.getY() + mercury.getHeight() / 2;

                    radius1 = 300; // Radius for the first circle
                    radius2 = 400; // Radius for the second circle
                    radius3 = 550; // Radius for the third circle

                    positionCircles();
                    mercury.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!darkmode) {
                    animateBackgroundColor(rl,Color.WHITE,Color.BLACK,500);
                    darkmode=true;
                } else {
                    animateBackgroundColor(rl,Color.BLACK,Color.WHITE,500);
                    darkmode=false;
                }
            }
        });
        generateStars(45);
    }

    private void positionCircles() {
        setPosition(mercury, radius1, 0);
        setPosition(venus, radius2, 120); // 120 degrees phase difference
        setPosition(earth, radius3, 240); // 240 degrees phase difference
    }

    private void setPosition(TextView circle, float radius, float angle) {
        float angleRad = (float) Math.toRadians(angle);
        float x = centerX + radius * (float) Math.cos(angleRad) - circle.getWidth() / 2;
        float y = centerY + radius * (float) Math.sin(angleRad) - circle.getHeight() / 2;
        circle.setX(x);
        circle.setY(y);
    }
}
