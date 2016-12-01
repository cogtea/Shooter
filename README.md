Shooter
====

Simple ScreenShot library , allow you to take screenshoot , draw on it ,send it to your server , mail , etc . 


Download
--------
```xml
<dependency>
  <groupId>com.gardencoder.shooter</groupId>
  <artifactId>shooter</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
```groovy
buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
     maven {
        url 'https://dl.bintray.com/gardencode/Shooter'
     }
  }
}

compile 'com.gardencoder.shooter:shooter:1.0.0@aar'
```

Install
--------

- in your Application class , implement `ConnectionHub` 
```java
public class MyApplication extends Application implements ConnectionHub {
    ... 
     @Override
    public void onCreate() {
        super.onCreate();
        
        ...
        Shooter.install(this).enableScreenShot(true);
    }

    @Override
    public String getUserAccessToken() {
        return "your name ";
    }

    @Override
    public boolean sendScreenShot(ShooterModel shooterModel) {
        Debug.d(getClass().getName(), shooterModel.getPhoto());
        return true;
    }
}

```

- Now in your activity extends instead of `AppCompatActivity` , use `ShooterAppCompactActivity` .
- Add Storage Permission  `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />` to your manifest .
- Shake your mobile / \ / \ , you will notify with screen you and draw over it , you will recieve screenshot with `sendScreenShot` .

#ShooterModel
- getPhoto() returns base64 of png image .
- getPath() returns path of image in device.  
- you can get Uri of image with such away `Uri uri = Uri.parse(shooterModel.getPath())`;
