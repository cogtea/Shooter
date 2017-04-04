package com.gardencoder.example;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import com.gardencoder.shooter.Shooter;
import com.gardencoder.shooter.models.ShooterModel;
import com.gardencoder.shooter.utilites.ConnectionHub;

/**
 * Created by Ramy on 12/1/16.
 */

public class MyApplication extends Application implements ConnectionHub {
    @Override
    public void onCreate() {
        super.onCreate();
        Shooter.install(this).enableScreenShot(true);
    }

    @Override
    public String getUserAccessToken() {
        return "your name";
    }

    @Override
    public boolean sendScreenShot(ShooterModel screenshot) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("png/image");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{
                "mail@mail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Issue");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please fix this issue");
        Uri uri = Uri.parse(screenshot.getPath());
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.setType("text/plain");
        startActivity(emailIntent);
        return true;
    }
}
