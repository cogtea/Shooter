package com.gardencoder.shooter;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gardencoder.shooter.board.ShooterEventListener;


public class ShooterAppCompactActivity extends AppCompatActivity {
    private ShooterEventListener shooterEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shooterEventListener = new ShooterEventListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        shooterEventListener.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shooterEventListener.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        shooterEventListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}