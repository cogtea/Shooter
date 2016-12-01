package com.gardencoder.example;

import android.os.Bundle;

import com.gardencoder.shooter.ShooterAppCompactActivity;

public class MainActivity extends ShooterAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
