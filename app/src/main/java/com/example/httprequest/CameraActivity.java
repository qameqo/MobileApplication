package com.example.httprequest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

//import com.example.httprequest.UploadFile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private ImageView imgPreview;
    private String imageFilePath;
    final private int CAPTURE_IMAGE = 1;
    final private int CAPTURE_SAVE_IMAGE = 2;
    final private int PICK_IMAGE = 3;
    final private int CAPTURE_VDO = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imgPreview = findViewById(R.id.imageView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            //Capture and preview
                            case R.id.item_camera_capture:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAPTURE_IMAGE);
                                return true;

                            //Capture, save and preview
                            case R.id.item_camera_upload:
                                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri photoURI = null;
                                try {
                                    photoURI = FileProvider.getUriForFile(CameraActivity.this,
                                            BuildConfig.APPLICATION_ID + ".provider",
                                            createImageFile());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(imageIntent, CAPTURE_SAVE_IMAGE);
                                return true;
                            case R.id.item_camera_pick:
                                Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                pickIntent.setType("image/*");
                                startActivityForResult(pickIntent, PICK_IMAGE);
                                return true;
//                            case R.id.item_camera_video:
//                                Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                                startActivityForResult(videoIntent, CAPTURE_VDO);
//                                return true;
                        }
                        return false;
                    }
                });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
                if (data != null && data.getExtras() != null) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    imageBitmap = this.resizeImage(imageBitmap, 1024, 1024);//Resize image
                    imgPreview.setImageBitmap(imageBitmap);//Preview image
                }
            } else if (requestCode == CAPTURE_SAVE_IMAGE && resultCode == RESULT_OK) {
                Uri imageUri = Uri.parse("file:" + imageFilePath);
                File file = new File(imageUri.getPath());
                try {
                    //show image
                    InputStream ims = new FileInputStream(file);
                    Bitmap imageBitmap = BitmapFactory.decodeStream(ims);
                    imageBitmap = this.resizeImage(imageBitmap, 1024, 1024);//resize image
                    imageBitmap = resolveRotateImage(imageBitmap, imageFilePath);//Resolve auto rotate image
                    imgPreview.setImageBitmap(imageBitmap);//Preview image

                    //upload image
//                    String sourceUri = imageUri.getPath();
//                    String uploadUri = getString(R.string.root_url) + getString(R.string.upload_url);
//                    new UploadFile(sourceUri, uploadUri).execute("");

                } catch (FileNotFoundException e) {
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
                Uri contentURI = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imgPreview.setImageBitmap(bitmap);//Preview image
                    imgPreview.setImageURI(contentURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAPTURE_VDO && resultCode == RESULT_OK) {

            }
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
        File image = File.createTempFile(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()), ".png",storageDir);
        imageFilePath = image.getAbsolutePath();//imageFilePath = "file:" + image.getAbsolutePath();
        return image;
    }
    private Bitmap resizeImage(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
private Bitmap resolveRotateImage(Bitmap bitmap, String photoPath) throws IOException {
    ExifInterface ei = new ExifInterface(photoPath);
    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED);

    Bitmap rotatedBitmap = null;
    switch(orientation) {

        case ExifInterface.ORIENTATION_ROTATE_90:
            rotatedBitmap = rotateImage(bitmap, 90);
            break;

        case ExifInterface.ORIENTATION_ROTATE_180:
            rotatedBitmap = rotateImage(bitmap, 180);
            break;

        case ExifInterface.ORIENTATION_ROTATE_270:
            rotatedBitmap = rotateImage(bitmap, 270);
            break;

        case ExifInterface.ORIENTATION_NORMAL:
        default:
            rotatedBitmap = bitmap;
    }
    return rotatedBitmap;
}
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


}
