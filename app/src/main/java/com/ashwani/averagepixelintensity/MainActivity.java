package com.ashwani.averagepixelintensity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvCount;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCount = (TextView) findViewById(R.id.tvCount);
        imageView = (ImageView) findViewById(R.id.imageView);

        final Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.sample2);
        imageView.setImageBitmap(image);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..calculating pixels..");
        progressDialog.setCancelable(false);
        progressDialog.show();



        new Thread(){
            @Override
            public void run() {
                GreyColor greyColor = rgbValuesFromBitmap(getResizedBitmap(image,422));
                progressDialog.cancel();
                float avg = (float)greyColor.getIntensityCount() / (float)greyColor.getPixelCount();
                setText(avg);
            }
        }.start();

    }

    public void setText(final float avg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCount.setText(""+avg);
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private GreyColor rgbValuesFromBitmap(Bitmap bitmap) {

        GreyColor greyColor = new GreyColor();

        ColorMatrix colorMatrix = new ColorMatrix();
        ColorFilter colorFilter = new ColorMatrixColorFilter(
                colorMatrix);
        Bitmap argbBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(argbBitmap);

        Paint paint = new Paint();

        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int totalPixels = width * height;

        @ColorInt int[] argbPixels = new int[totalPixels];
        argbBitmap.getPixels(argbPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < width; i++) {
            for(int y= 0; y < height; y++) {
                int rgb = argbBitmap.getPixel(i,y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb) & 0xFF;
//            calculating grey
                int grey = (red + blue + green) / 3;
                greyColor.setIntensityCount(greyColor.getIntensityCount() + grey);
                greyColor.setPixelCount(greyColor.getPixelCount() + 1);
                Log.d("Debug", "GreyValue: " + grey);
            }

        }

        return greyColor;
    }

}
