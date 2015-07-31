package com.example.pixelpicker1;

import android.graphics.Color;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView3 extends View implements OnTouchListener
{
	public Rect[] rectangles = new Rect[4];
	public Rect boundary;

	public CustomView3(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setOnTouchListener(this);
		highLight(0) ;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		
		this.boundary = canvas.getClipBounds();	//have to do this here because it needs canvas to work
		int right = (this.boundary.width() - 3) / 4;
		int bottom = this.boundary.height();
		float rightF = (this.boundary.width() - 3) / 4;
		float bottomF = this.boundary.height();

		rectangles[0] = new Rect(1, 0, right, bottom); // left, top, right, bottom
		rectangles[1] = new Rect(right + 2, 0, 2 * right, bottom);
		rectangles[2] = new Rect((2 * right) + 2, 0, 3 * right, bottom);
		rectangles[3] = new Rect((3 * right) + 2, 0, 4 * right + 4, bottom);

		//drawing the pallet boxes
		paint.setColor(MainActivity.swatches[0].colour);
		canvas.drawRect(rectangles[0], paint);

		paint.setColor(MainActivity.swatches[1].colour);
		canvas.drawRect(rectangles[1], paint);

		paint.setColor(MainActivity.swatches[2].colour);
		canvas.drawRect(rectangles[2], paint);

		paint.setColor(MainActivity.swatches[3].colour);
		canvas.drawRect(rectangles[3], paint);

		//drawing the highlighting of the boxes
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);

		paint.setColor(MainActivity.highLightSwatch[0].colour);
		canvas.drawRect(rectangles[0], paint);

		paint.setColor(MainActivity.highLightSwatch[1].colour);
		canvas.drawRect(rectangles[1], paint);

		paint.setColor(MainActivity.highLightSwatch[2].colour);
		canvas.drawRect(rectangles[2], paint);

		paint.setColor(MainActivity.highLightSwatch[3].colour);
		canvas.drawRect(rectangles[3], paint);
	}

	//selecting which swatch is being used and making sure that it is highlighted
	public boolean onTouchEvent(MotionEvent event)
	{
		int X = (int) event.getX();
		int Y = (int) event.getY();

		if (rectangles[0].contains(X, Y)) {
			MainActivity.swatchSelected = 0;
		} else if (rectangles[1].contains(X, Y)) {
			MainActivity.swatchSelected = 1;
		} else if (rectangles[2].contains(X, Y)) {
			MainActivity.swatchSelected = 2;
		} else if (rectangles[3].contains(X, Y)) {
			MainActivity.swatchSelected = 3;
		} else {

		}
		highLight(MainActivity.swatchSelected) ;
		return true;
	}

	public void highLight(int x)
	{
		for(int i = 0; i <= 3; i++)
		{
			if(i == x)
			{
				MainActivity.highLightSwatch[i].colour = Color.RED ;
			} else {
				MainActivity.highLightSwatch[i].colour = Color.TRANSPARENT;
			}
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event)
	{
		findViewById(R.id.customView3).invalidate();
		return onTouchEvent(event);
	}
}