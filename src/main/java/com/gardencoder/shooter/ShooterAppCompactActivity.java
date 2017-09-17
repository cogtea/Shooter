package com.gardencoder.shooter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gardencoder.shooter.board.ShooterEventListener;


public abstract class ShooterAppCompactActivity extends AppCompatActivity {
    public ShooterEventListener shooterEventListener;

    public abstract boolean isEnabled();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isEnabled())
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
                                           String permissions[], int[] grantResults) {
        if (shooterEventListener != null)
            shooterEventListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}