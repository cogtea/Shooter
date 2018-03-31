package com.gardencoder.shooter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.gardencoder.shooter.board.ShooterEventListener;


public class ShooterAppCompactActivity extends AppCompatActivity {
    public ShooterEventListener shooterEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shooterEventListener = new ShooterEventListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shooterEventListener != null)
            shooterEventListener.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shooterEventListener != null)
            shooterEventListener.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (shooterEventListener != null)
            shooterEventListener.onRequestPermissionsResult(requestCode, grantResults);
    }
}