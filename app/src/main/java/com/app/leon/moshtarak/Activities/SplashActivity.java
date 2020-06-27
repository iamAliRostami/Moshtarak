package com.app.leon.moshtarak.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.leon.moshtarak.R;
import com.app.leon.moshtarak.databinding.SplashActivityBinding;

public class SplashActivity extends Activity {
    SplashActivityBinding binding;
    private boolean splashLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = SplashActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (!splashLoaded) {
            setContentView(view);
            initialize();
            startSplash();
        } else {
            Intent goToLoginActivity = new Intent(SplashActivity.this, HomeActivity.class);
            goToLoginActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToLoginActivity);
            finish();
        }
    }

    private void initialize() {
        int splashResourceId = R.drawable.img_splash;
        binding.imageViewSplashScreen.setImageResource(splashResourceId);
        binding.shimmerViewContainer.startShimmer();
    }

    private void startSplash() {
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    splashLoaded = true;
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
