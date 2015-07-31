package com.example.pixelpicker1;

import android.view.ScaleGestureDetector;
import android.view.View.OnTouchListener;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class CustomView2 extends View implements OnTouchListener {

	public static Crosshair crosshair;
	int customWidth, customHeight, imageWidth = 0, imageHeight = 0;
	private ScaleGestureDetector mScaleDector ;
	private float mScaleFactor = 1.0f ;
	int offsetX, offsetY;   //used to get location of pixels. 0,0 customView != 0,0 targetImage.

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
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);

		//for zooming
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);

		//painting the image onto the canvas
	    Paint paint = new Paint();
		Rect boundary = canvas.getClipBounds();
		paint.setColor(Color.rgb(40, 40, 45));
		canvas.drawRect(boundary, paint);

		if (MainActivity.targetImage != null) {
			canvas.drawBitmap(MainActivity.targetImage,
					(float) MainActivity.imageX, (float) MainActivity.imageY,
					null);
			
			this.imageWidth = MainActivity.targetImage.getWidth();  //used later
			this.imageHeight = MainActivity.targetImage.getHeight();
		}

		//drawing the crosshair
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.pointer), crosshair.x, crosshair.y,
				paint);

		this.customWidth = (int) findViewById(R.id.customView2).getWidth();
		this.customHeight = (int) findViewById(R.id.customView2).getHeight();

		canvas.restore();
	}

	public boolean inBounds(int x, int y) {
		double x1 = (this.customWidth - this.imageWidth) / 2; // thanks a bunch
																// Jonathan!
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

		if (MainActivity.targetImage != null) {
			if (inBounds(X, Y)) {
				int colour = MainActivity.targetImage.getPixel(
						X - this.offsetX, Y - this.offsetY);
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
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f)) ;

			invalidate(); ;
			return true ;
		}
	}
}

//https://developer.android.com/training/gestures/scale.html -> for zooming
//http://stackoverflow.com/questions/22291008/putting-custom-view-inside-a-custom-scrollview -> maybe this one for scrolling??