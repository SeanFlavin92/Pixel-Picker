package com.example.pixelpicker1;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Paint;
import android.view.View.OnTouchListener;
import java.text.AttributedCharacterIterator;

/**
 * Created by Sean on 17/08/2015.
 */
public class CustomViewCrosshair extends View implements OnTouchListener {

    public static Crosshair crosshair;
    int customWidth, customHeight, imageWidth = 0, imageHeight = 0;
    int offsetX, offsetY;   //used to get location of pixels. 0,0 customView != 0,0 targetImage.

    public CustomViewCrosshair(Context context, AttributeSet as) {
        super(context, as);
        crosshair = new Crosshair(50, 50);

        this.setOnTouchListener(this);
    }

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Paint paint = new Paint();

        //Drawing the crosshair
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.pointer), crosshair.x, crosshair.y,
                paint);

        //Used to make sure the pointer is within the bounds of the picture
        this.imageHeight = CustomView2.imageHeight ;
        this.imageWidth = CustomView2.imageWidth ;

        this.customWidth = (int) findViewById(R.id.customViewCrosshair).getWidth();
        this.customHeight = (int) findViewById(R.id.customViewCrosshair).getHeight();
    }

    //Moving the pointer around
    public void onTouchEvent(MotionEvent event, Crosshair crosshair) {
        int eventaction = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventaction) {
            case MotionEvent.ACTION_MOVE:
                crosshair.x = (X - 45);
                crosshair.y = (Y - 90);

            case MotionEvent.ACTION_DOWN:
                crosshair.x = (X - 45);
                crosshair.y = (Y - 90);

            case MotionEvent.ACTION_UP:
                break;
        }

        //Changing the colour of the swatch to the colour the pointer has landed on
        if (MainActivity.targetImage != null) {
            if (inBounds(X, Y)) {
                int colour = MainActivity.targetImage.getPixel(
                        X - this.offsetX, Y - this.offsetY);
                MainActivity.hasColour = true;
                MainActivity.swatches[MainActivity.swatchSelected].colour = colour;
            }
        }
    }

    //Is the pointer within the picture
    public boolean inBounds(int x, int y) {
        double x1 = (this.customWidth - this.imageWidth) / 2; // thanks a bunch Jonathan!
        double x2 = this.imageWidth + x1;
        double y1 = (this.customHeight - this.imageHeight) / 2 + 40;
        double y2 = this.imageHeight + y1;

        this.offsetX = (int) x1;
        this.offsetY = (int) y1;

        if (x > x1 && x < x2 && y > y1 && y < y2) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
//        MainActivity.customViewCrosshair.bringToFront();
        onTouchEvent(motionEvent, crosshair);
        View parentView = (View) findViewById(R.id.customViewCrosshair).getParent();
        parentView.findViewById(R.id.customView3).invalidate();
        findViewById(R.id.customViewCrosshair).invalidate();
        return true;
    }
}