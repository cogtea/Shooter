package com.gardencoder.shooter.board;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.gardencoder.shooter.utilites.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;


public class ShooterEventListener implements SensorEventListener {
    public static final int UPDATE_TIME_FREQUENCY = 100;
    private static final int SHAKE_THRESHOLD = 800;
    private final Sensor sensor;
    private SensorManager sensorManager;
    private long lastUpdate;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private Activity activity;

    public ShooterEventListener(Activity activity) {
        this.activity = activity;
        sensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
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
        intent.putExtra(ShooterDrawingActivity.ACTIVITY_NAME, this.getClass().getName());
        activity.startActivity(intent);
    }

    public void onPause() {
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    public void onResume() {
        if (Tools.isScreenshot) {
            sensorManager.registerListener(this, sensor, UPDATE_TIME_FREQUENCY);
        }
    }
}
