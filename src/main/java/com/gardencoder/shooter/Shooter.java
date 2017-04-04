package com.gardencoder.shooter;

import android.app.Activity;
import android.app.Application;

import com.gardencoder.shooter.models.ShooterModel;
import com.gardencoder.shooter.utilites.ConnectionHub;
import com.gardencoder.shooter.utilites.Debug;
import com.gardencoder.shooter.utilites.Tools;

public class Shooter {
    private static ConnectionHub connectionHub;

    private Shooter(Application application) {
        connectionHub = (ConnectionHub) application;
        Debug.d(Shooter.class.getName(), connectionHub.getUserAccessToken());
    }

    public Shooter(Activity activity) {
        connectionHub = (ConnectionHub) activity;
        Debug.d(Shooter.class.getName(), connectionHub.getUserAccessToken());
    }

    public static Shooter install(Application application) {
        return new Shooter(application);
    }

    public static Shooter install(Activity activity) {
        return new Shooter(activity);
    }

    public static boolean sendScreenshot(ShooterModel params) {
        return connectionHub.sendScreenShot(params);
    }

    public Shooter enableDebugMessages(Boolean isDebugEnabled) {
        Tools.isDebugEnabled = isDebugEnabled;
        return this;
    }

    public Shooter enableScreenShot(Boolean isScreenshot) {
        Tools.isScreenshot = isScreenshot;
        return this;
    }
}
