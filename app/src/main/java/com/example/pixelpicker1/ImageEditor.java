package com.example.pixelpicker1;

import android.graphics.Bitmap;

public class ImageEditor {

	public static Bitmap resizeBitmap(double width, double maxWidth,
			double height, double maxHeight, Bitmap bitmap) {

		Bitmap newBitmap;

		if (width > height) { // checks largest side (going to scale this
								// down/up to fit maxValue)
			width = (int) width / height;
			height = 1; // approx image ratios

			newBitmap = Bitmap.createScaledBitmap(bitmap, (int) maxWidth,
					(int) (maxHeight / width), true); // true??? :?

		} else {
			height = (int) height / width;
			width = 1;

			newBitmap = Bitmap.createScaledBitmap(bitmap,
					(int) (maxWidth / height), (int) maxHeight, true);
		}
		return newBitmap;
	}
}