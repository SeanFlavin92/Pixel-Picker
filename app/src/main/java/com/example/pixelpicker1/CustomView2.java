package com.example.pixelpicker1;

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
	public static int imageWidth = 0, imageHeight = 0;
	private ScaleGestureDetector mScaleDector ;
	private float mScaleFactor = 1.0f ;

	/*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) StaticLayout.getLayoutParams();
	params.height = 130;
	someLayout.setLayoutParams(params);*/
	
	public CustomView2(Context context, AttributeSet attrs) {
		super(context, attrs);
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
		canvas.restore();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event)
	{
		Log.i("Scale Gesture", "Got here") ;
		if(onTouchEvent(event))
		{
			MainActivity.customView2.bringToFront() ;
			mScaleDector.onTouchEvent(event) ;
		}
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mScaleDector.onTouchEvent(event);
		Log.i("Scale Gesture", "Got here") ;
		return true;
	}
}

//https://developer.android.com/training/gestures/scale.html -> for zooming
//http://stackoverflow.com/questions/22291008/putting-custom-view-inside-a-custom-scrollview -> maybe this one for scrolling??