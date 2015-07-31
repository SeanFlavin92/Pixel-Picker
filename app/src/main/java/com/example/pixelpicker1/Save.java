package com.example.pixelpicker1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Save extends Activity {
	Bitmap sBit;
	Canvas saveCanvas;
	EditText editTextbox;
	String path;
	int count;
	Button save ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save);

		this.path = Environment.getExternalStorageDirectory() + File.separator
				+ "Pictures";
		count = 0;
		drawColours();
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

	public void saveMethod(View v) {

		sBit = Bitmap.createBitmap(sBit, 0, 0, (50 * count), 50);

		String text = "DefaultPalette.png";
		EditText editTextbox = (EditText) findViewById(R.id.editText1);
		if (editTextbox.getText() != null) {
			text = editTextbox.getText().toString();
		}

		File dest = new File(path, text + ".png");

		writeBitmapToMemory(dest, this.sBit);
		finish();
	}

}