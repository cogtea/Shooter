package com.gardencoder.shooter.board;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gardencoder.shooter.R;
import com.gardencoder.shooter.Shooter;
import com.gardencoder.shooter.models.ShooterModel;
import com.gardencoder.shooter.utilites.Debug;
import com.gardencoder.shooter.utilites.Tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ShooterDrawingActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SCREEN_SHOT = "screen_shot";
    public static final String ACTIVITY_NAME = "activity";
    public static final String SCREEN_SHOT_PATH = "path";
    private FloatingActionButton orangeBtn;// redBtn, greenBtn;
    private LinearLayout drawLayout;
    private Paint mPaint;
    private DrawingView dv;
    private ImageButton clearBtn;
    private View mProgressView;
    private View mLayout;
    private FloatingActionButton send;
    private String activtyName;
    private String path = "";
    private String pathStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooter);
        //
        mProgressView = findViewById(R.id.progress);
        mLayout = findViewById(R.id.layout);
        drawLayout = (LinearLayout) findViewById(R.id.draw_layout);
        //redBtn = (FloatingActionButton) findViewById(R.id.red);
        orangeBtn = (FloatingActionButton) findViewById(R.id.orange);
        //greenBtn = (FloatingActionButton) findViewById(R.id.green);
        //greenBtn = (FloatingActionButton) findViewById(R.id.green);
        clearBtn = (FloatingActionButton) findViewById(R.id.clear);
        send = (FloatingActionButton) findViewById(R.id.send);

        //
        //redBtn.setOnClickListener(this);
        orangeBtn.setOnClickListener(this);
        //greenBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        send.setOnClickListener(this);
        //
        dv = new DrawingView(this);
        drawLayout.addView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(getResources().getColor(R.color.colorOrange));
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        mPaint.setStrokeWidth(3);
        //
        if (getIntent().getExtras() != null) {
            path = getIntent().getStringExtra(SCREEN_SHOT);
            pathStr = getIntent().getStringExtra(SCREEN_SHOT_PATH);

            Uri uri = Uri.parse(getIntent().getStringExtra(SCREEN_SHOT));
            activtyName = getIntent().getStringExtra(ACTIVITY_NAME);
            File f = new File(getRealPathFromURI(uri));
            Drawable d = Drawable.createFromPath(f.getAbsolutePath());
            ((ImageView) findViewById(R.id.image)).setImageDrawable(d);
        }
        //

        DescribeServiceFragment describeServiceFragment = new DescribeServiceFragment();
        describeServiceFragment.show(getSupportFragmentManager(), DescribeServiceFragment.class.getName());
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
//        if (i == R.id.red) {
//            mPaint.setColor(getResources().getColor(R.color.colorRed));
//            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
//            mPaint.setStrokeWidth(3);
//
//        }
        if (i == R.id.orange) {
            mPaint.setColor(getResources().getColor(R.color.colorOrange));
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            mPaint.setStrokeWidth(3);

        }
//        else if (i == R.id.green) {
//            mPaint.setColor(getResources().getColor(R.color.colorGreen));
//            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
//            mPaint.setStrokeWidth(3);
//
//        }
        else if (i == R.id.clear) {
            mPaint.setColor(Color.WHITE);
            mPaint.setAlpha(Color.WHITE);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mPaint.setStrokeWidth(30);

        } else if (i == R.id.send) {
            mLayout.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(mLayout.getDrawingCache());
            try {
                // create bitmap screen capture
                View v1 = getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                //
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                //
                v1.setDrawingCacheEnabled(false);
                Uri uri = Uri.parse(path);
                File imageFile = new File(getRealPathFromURI(uri));
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, Tools.QUALITY, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Throwable e) {
                //Log.d("error", e.getMessage());
            }
            new ImageBase64(Tools.QUALITY, pathStr).execute(bitmap);
        }
    }

    private void sendScreenshot(final String ImageFileName, String path) {
        showProgress(true);
        ShooterModel screenShotModel = new ShooterModel();
        screenShotModel.setActivity_name(activtyName);
        screenShotModel.setDevice_model(getDeviceModel());
        screenShotModel.setDevice_name(getDeviceName());
        screenShotModel.setPath(path);
        screenShotModel.setPhoto(ImageFileName);
        screenShotModel.setPhoto_extension("png");

        if (Shooter.sendScreenshot(screenShotModel)) {
            Debug.d(ShooterDrawingActivity.class.getName(), "Sending screenshot is successful");
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            mLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        }
        //
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public String getDeviceModel() {
        return capitalize(Build.MODEL);
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public class DrawingView extends View {

        private static final float TOUCH_TOLERANCE = 4;
        public int width;
        public int height;
        Context context;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        private Paint circlePaint;
        private Path circlePath;
        private float mX, mY;

        public DrawingView(Context c) {
            super(c);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(getResources().getColor(R.color.colorPrimary));
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(3f);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            canvas.drawPath(circlePath, circlePaint);
        }

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    private class ImageBase64 extends AsyncTask<Bitmap, Integer, String> {
        private final String path;
        private int quality;

        ImageBase64(int quality, String path) {
            this.quality = quality;
            this.path = path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Get the dimensions of the View
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            if (params[0] != null) {

                Matrix rotMatrix = new Matrix();

                params[0] = Bitmap.createBitmap(params[0], 0, 0, params[0].getWidth(), params[0].getHeight(), rotMatrix, true);

                //}
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                params[0].compress(Bitmap.CompressFormat.JPEG, quality, baos);
                byte[] b = baos.toByteArray();
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return Base64.encodeToString(b, Base64.DEFAULT);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String ImageFileName) {
            super.onPostExecute(ImageFileName);
            sendScreenshot(ImageFileName, path);
        }
    }
}