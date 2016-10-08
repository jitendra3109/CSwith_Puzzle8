package com.google.engedu.puzzle8;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;


public class PuzzleActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap = null;
    private PuzzleBoardView boardView;
    //private ImageView pictureView;///
    private String mTempPhotoPath;
    private Uri mTempImageUri;
    private String LOG_TAG = "DLG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        // This code programmatically adds the PuzzleBoardView to the UI.
        RelativeLayout container = (RelativeLayout) findViewById(R.id.puzzle_container);
        boardView = new PuzzleBoardView(this);
        // Some setup of the view.
        boardView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        container.addView(boardView);
        //pictureView = (ImageView) findViewById(R.id.pictureView);///

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_puzzle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void dispatchTakePictureIntent(View view) {
        // https://developer.android.com/training/camera/photobasics.html

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);///
        if (intent.resolveActivity(getPackageManager()) != null) {///
           // startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);///
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                      File photoFile = null;
                        try {
                                photoFile = createImageFile();
                        } catch (IOException ex){
                                // nothing
                                    }
                        if (photoFile != null){
                                mTempImageUri = Uri.fromFile(photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            }



        }
    }

    private File createImageFile() throws IOException {
                // Create an image file name
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
               String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
               File image = File.createTempFile(
                               imageFileName,  /* prefix */
                                ".jpg",         /* suffix */
                                storageDir      /* directory */
                               );


                               mTempPhotoPath = "file:" + image.getAbsolutePath();
               return image;
           }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){///
                       // Bundle extras = data.getExtras();    ///
                      //Bitmap imageBitmap = (Bitmap) extras.get("data"); ///
                      //  pictureView.setImageBitmap(imageBitmap);  ///
                  try {
                               imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                                boardView.initialize(imageBitmap);
                                boardView.invalidate(); // re draw
                            } catch (Exception e){
                                Log.d(LOG_TAG, "imageDraw " + e.toString());
                            }


                    }
    }

    public void shuffleImage(View view) {
        boardView.shuffle();
    }

    public void solve(View view) {
      boardView.solve();

    }
}
