package com.example.pixelpicker1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView2 extends View implements OnTouchListener
{
	public static Crosshair crosshair;
	public static int customWidth, customHeight, imageWidth = 0, imageHeight = 0;
	private ScaleGestureDetector mScaleDector ;
	private float mScaleFactor = 1.0f ;
	int offsetX, offsetY;   //used to get location of pixels. 0,0 customView != 0,0 targetImage.
	private Paint paint ;
	private Rect boundary ;

	/*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) StaticLayout.getLayoutParams();
	params.height = 130;
	someLayout.setLayoutParams(params);*/
	
	public CustomView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		crosshair = new Crosshair(50, 50);
		this.setOnTouchListener(this);

		//Implementing zoom
		mScaleDector = new ScaleGestureDetector(context, new ScaleListener()) ;
	}

	//Drawing the images to be used
	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		//For zooming
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);

		//Setting the boundary of the image on the canvas
		paint = new Paint();

		boundary = canvas.getClipBounds();
		paint.setColor(Color.rgb(40, 40, 45));
		canvas.drawRect(boundary, paint);

		//Drawing the image loaded
		if (MainActivity.targetImage != null)
		{
			drawTargetImage(canvas);
		}

		//Drawing the crosshair
		drawCrossHair(canvas);

		//Used for inBounds, customWidth/Height the width/height of the canvas the image is drawn on
		this.customWidth = (int) findViewById(R.id.customView2).getWidth();
		this.customHeight = (int) findViewById(R.id.customView2).getHeight();

		canvas.restore();
	};

	//Is the pointer within the picture?
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

	//Moving the pointer around
	public void onTouchEvent(MotionEvent event, Crosshair crosshair) {
		int eventaction = event.getAction();
		float X = (int) event.getX()/((int)mScaleFactor + (boundary.left*(int)mScaleFactor));
		float Y = (int) event.getY()/((int)mScaleFactor + (boundary.top*(int)mScaleFactor));
		float tempX, tempY ;

		switch (eventaction) {
			case MotionEvent.ACTION_MOVE:
				tempX = (X - 45) ;
				crosshair.x = tempX ;
				tempY = (Y - 90) ;
				crosshair.y = tempY ;

			case MotionEvent.ACTION_DOWN:
				tempX = (X - 45) ;
				crosshair.x = tempX ;
				tempY = (Y - 90) ;
				crosshair.y = tempY ;

			case MotionEvent.ACTION_UP:
				break;
		}
		Log.i("Pointer Pos: x", " " + crosshair.x) ;
		Log.i("Pointer Pos: y", " " + crosshair.y) ;

		//Changing the colour of the swatch to the colour the pointer has landed on
		if (MainActivity.targetImage != null) {
			if (inBounds((int)X, (int)Y)) {
				int colour = MainActivity.targetImage.getPixel(
						(int)X - this.offsetX, (int)Y - this.offsetY);
				MainActivity.hasColour = true;
				MainActivity.swatches[MainActivity.swatchSelected].colour = colour;
			}
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		mScaleDector.onTouchEvent(event) ;
		onTouchEvent(event, crosshair);
		View parentView = (View) findViewById(R.id.customView2).getParent();
		parentView.findViewById(R.id.customView3).invalidate();
		findViewById(R.id.customView2).invalidate();

		return true;
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		public boolean onScale(ScaleGestureDetector detector)
		{
			mScaleFactor *= detector.getScaleFactor() ;
			mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 5.0f)) ;

			invalidate();
			return true ;
		}
	}

	public void drawCrossHair(Canvas canvas)
	{
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pointer);
		bitmap = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()/mScaleFactor), (int)(bitmap.getHeight()/mScaleFactor), true);
		canvas.drawBitmap(bitmap, crosshair.x, crosshair.y, this.paint);
	}

	public void drawTargetImage(Canvas canvas)
	{
		//Drawing the image loaded from either the gallery or camera
		canvas.drawBitmap(MainActivity.targetImage,
				(float) MainActivity.imageX, (float) MainActivity.imageY,null);

		//Width/height of the image being used
		this.imageWidth = MainActivity.targetImage.getWidth();  //used later
		this.imageHeight = MainActivity.targetImage.getHeight();
	}
}

///((int)mScaleFactor)

//https://developer.android.com/training/gestures/scale.html -> for zooming
//http://stackoverflow.com/questions/22291008/putting-custom-view-inside-a-custom-scrollview -> maybe this one for scrolling??