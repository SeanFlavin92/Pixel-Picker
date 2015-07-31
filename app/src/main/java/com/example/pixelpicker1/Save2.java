package com.example.pixelpicker1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sean on 23/07/2015.
 */
public class Save2
{
    Bitmap sBit;
    Canvas saveCanvas;
    String path, title;
    int count;

    public Save2(String title)
    {
        createDir() ;
        count = 0;
        this.title = title ;
        drawColours() ;
        saveMethod() ;
    }

    public void drawColours() {
        Paint myPaint = new Paint();
        sBit = Bitmap.createBitmap(200, 50, Bitmap.Config.ARGB_8888);
        saveCanvas = new Canvas(sBit);

        Rect[] rect = new Rect[4];
        int x1 = 0;
        int x2 = 50;
        for (int i = 0; i < 4; i++) {
            rect[i] = new Rect(x1, 0, x2, 50);
            myPaint.setColor(MainActivity.swatches[i].colour);
            if (myPaint.getColor() != Color.rgb(50, 50, 60)) {
                saveCanvas.drawRect(rect[i], myPaint);
                x1 = x1 + 50;
                x2 = x2 + 50;
                count++;
            }
        }
    }

    public void writeBitmapToMemory(File dest, Bitmap bitmap) {

        try {
            FileOutputStream stream = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMethod() {

        sBit = Bitmap.createBitmap(sBit, 0, 0, (50 * count), 50);
        File dest = new File(path, this.title + ".png");
        writeBitmapToMemory(dest, this.sBit);
    }

    public void createDir()
    {
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Pallet" ;
        File folder = new File(folderPath) ;
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            this.path = folderPath ;
        }
    }
}

