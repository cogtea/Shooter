package com.gardencoder.shooter.utilites;

import com.gardencoder.shooter.models.ShooterModel;

public interface ConnectionHub {

    public String getUserAccessToken();

    public boolean sendScreenShot(ShooterModel screenshot);
}
