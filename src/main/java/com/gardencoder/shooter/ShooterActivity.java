package com.gardencoder.shooter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.gardencoder.shooter.board.ShooterEventListener;


public class ShooterActivity extends Activity {
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
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        shooterEventListener.onRequestPermissionsResult(requestCode, grantResults);
    }
}