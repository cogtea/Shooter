package com.gardencoder.shooter.utilites;

import com.gardencoder.shooter.models.ShooterModel;

public interface ConnectionHub {

    String getUserAccessToken();

    boolean sendScreenShot(ShooterModel screenshot);
}
