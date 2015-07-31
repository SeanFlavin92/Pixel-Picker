package com.example.pixelpicker1;

import java.io.FileNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {

    static final int RESULT_LOAD_IMAGE = 0; // new
    static final int CAMERA_PIC_REQUEST = 2;
    static final int TAKE_PICTURE = 0;

    public static Bitmap targetImage;
    private Bitmap bitmap;

    static double imageX = 0;   //used in drawing customV2
    static double imageY = 0;

    static int swatchSelected = 0;
    static Swatch[] swatches = new Swatch[4];
    static highLightSwatch[] highLightSwatch = new highLightSwatch[4] ;

    public static boolean hasColour = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swatches[0] = new Swatch();
        swatches[1] = new Swatch();
        swatches[2] = new Swatch();
        swatches[3] = new Swatch();

        highLightSwatch[0] = new highLightSwatch() ;
        highLightSwatch[1] = new highLightSwatch() ;
        highLightSwatch[2] = new highLightSwatch() ;
        highLightSwatch[3] = new highLightSwatch() ;

        setContentView(R.layout.activity_main);
    }

    //loading an image from the gallery
    public void aMethod(View v) {
        Intent imageLoader = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(imageLoader, RESULT_LOAD_IMAGE); // new
    }

    //loading an image from the camera after taking a picture
    public void bMethod(View v) {
        PackageManager pm = this.getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent cameraIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
    }

    public void saveMethod(View v) {
        if (hasColour == true) {
            showDialog("Save Palette?");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
        } else if (requestCode == CAMERA_PIC_REQUEST && null != data) {
            try {
                this.bitmap = (Bitmap) data.getExtras().get("data");
            } catch (Exception e) {
            }

        } else if (requestCode == RESULT_LOAD_IMAGE && null != data) {

            Uri targetUri = data.getData();
            try {
                this.bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (resultCode != RESULT_CANCELED) {
            double width = bitmap.getWidth();
            double height = bitmap.getHeight();
            double maxWidth = findViewById(R.id.customView2).getWidth();
            double maxHeight = findViewById(R.id.customView2).getHeight();

            MainActivity.targetImage = ImageEditor.resizeBitmap(width, maxWidth, height, maxHeight, bitmap);
            MainActivity.imageX = ((double) (maxWidth - MainActivity.targetImage.getWidth())) / 2;
            MainActivity.imageY = ((double) (maxHeight - MainActivity.targetImage.getHeight())) / 2;

            findViewById(R.id.customView2).invalidate();
        }
    }

    //showDialog, doPositiveClick & doNegativeClick are for saving the pallet
    void showDialog(String title)
    {
        DialogFragment popUp = MySaveFragment.newInstance(title);
        popUp.setCancelable(false);
        popUp.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClick(String title)
    {
        Save2 save = new Save2(title) ;
//        Intent colourLoader = new Intent(this, Save.class);
//        startActivity(colourLoader);
    }

    public void doNegativeClick()
    { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
    }
}