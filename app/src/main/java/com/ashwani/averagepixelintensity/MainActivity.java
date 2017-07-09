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

    RecyclerView colorList;
    TextView tvCount;
    ImageView imageView;

    List<MyColor> swatchList = new ArrayList<>();
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorList = (RecyclerView) findViewById(R.id.colorList);
        tvCount = (TextView) findViewById(R.id.tvCount);
        imageView = (ImageView) findViewById(R.id.imageView);

        final Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.gplus);
        imageView.setImageBitmap(image);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        colorList.setLayoutManager(mLinearLayoutManager);
        colorList.setAdapter(myAdapter);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..calculating pixels..");
        progressDialog.setCancelable(false);
        progressDialog.show();



        new Thread(){
            @Override
            public void run() {
                swatchList = rgbValuesFromBitmap(getResizedBitmap(image,50));

                progressDialog.cancel();
                int population = 0;
                int size = swatchList.size();
                for (MyColor color: swatchList) {
                    population+=color.getPopulation();
                    Log.d("Color rgb: ",""+color.getRED()+", "+color.getGREEN()+", "+color.getBLUE()+" Population: "+ color.getPopulation());
                }

                float avg = (float)population / (float)size;
                setText(avg);
            }
        }.start();

    }

    public void setText(final float avg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCount.setText(""+avg);
                myAdapter = new MyAdapter(MainActivity.this, swatchList);
                colorList.setAdapter(myAdapter);
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

    private List<MyColor> rgbValuesFromBitmap(Bitmap bitmap) {
       List<MyColor> myColors = new ArrayList<>();
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
        for (int i = 0; i < totalPixels; i++) {
            @ColorInt int argbPixel = argbPixels[i];
            int red = Color.red(argbPixel);
            int green = Color.green(argbPixel);
            int blue = Color.blue(argbPixel);
            MyColor myColor = new MyColor();
            myColor.setRED(red);
            myColor.setBLUE(blue);
            myColor.setGREEN(green);
            Log.d("Debug","Calculating: "+red+", "+green+", "+blue);
            if(myColors.size() > 0) {
                boolean found = false;
                for (MyColor color : myColors) {
                    if(color.isEqual(myColor)){
                        found = true;
                        color.setPopulation(color.getPopulation() + 1);
                    }
                }
                if(!found){
                    myColor.setPopulation(1);
                    myColors.add(myColor);

                }
            }else{
                myColor.setPopulation(1);
                myColors.add(myColor);
            }
        }

        return myColors;
    }

}
