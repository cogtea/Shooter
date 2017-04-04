package com.gardencoder.shooter.board;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.gardencoder.shooter.utilites.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;


public class ShooterEventListener implements SensorEventListener {
    public static final int UPDATE_TIME_FREQUENCY = 100;
    private static final int SHAKE_THRESHOLD = 2000;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 4010;
    private final Sensor sensor;
    private SensorManager sensorManager;
    private long lastUpdate;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private Activity activity;
    private boolean isPermissionGranted = false;

    public ShooterEventListener(Activity activity) {
        this.activity = activity;
        sensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                isPermissionGranted = grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestPermission() {
        // Assume thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            sensorManager.registerListener(this, sensor, UPDATE_TIME_FREQUENCY);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > UPDATE_TIME_FREQUENCY) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = sensorEvent.values;
                x = values[0];
                y = values[1];
                z = values[2];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    if (Tools.isScreenshot) {
                        takeScreenshot();
                        if (sensorManager != null)
                            sensorManager.unregisterListener(this);
                    }
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            File screensDir = new File(activity.getFilesDir().toString() + "/screenshots/");
            if (!screensDir.exists()) {
                screensDir.mkdir();
            }
            //
            String mPath = activity.getFilesDir().toString() + "/screenshots/" + now + ".jpg";
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            //
            bitmap = Bitmap.createBitmap(bitmap, 0, 40, bitmap.getWidth(), bitmap.getHeight() - 40);
            //
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, Tools.QUALITY, outputStream);
            outputStream.flush();
            outputStream.close();
            openScreenshot(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private void openScreenshot(File imageFile) {
        Intent intent = new Intent(activity, ShooterDrawingActivity.class);
        Uri uri = Uri.fromFile(imageFile);
        intent.putExtra(ShooterDrawingActivity.SCREEN_SHOT, uri.toString());
        intent.putExtra(ShooterDrawingActivity.SCREEN_SHOT_PATH, imageFile.getPath());

        intent.putExtra(ShooterDrawingActivity.ACTIVITY_NAME, this.getClass().getName());
        activity.startActivity(intent);
    }

    public void onPause() {
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    public void onResume() {
        if (Tools.isScreenshot) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isPermissionGranted) {
                //
                requestPermission();
                //
            } else {
                isPermissionGranted = true;
                sensorManager.registerListener(this, sensor, UPDATE_TIME_FREQUENCY);
            }
            //
        }
    }
}
