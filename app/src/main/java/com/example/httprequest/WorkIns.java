package com.example.httprequest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Trace;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.httprequest.util.UploadFile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import static com.example.httprequest.MainActivity.APP_PREFER;

public class WorkIns extends AppCompatActivity {
    String idr = "";
    String idp = "";
    String de = "";
    String lat = "";
    String lng = "";
    TextView tx;
    EditText detail;
    String ide = "";
    String idc = "";
    private ImageView imgPreview;
    private String imageFilePath;
    final private int CAPTURE_IMAGE = 1;
    final private int CAPTURE_SAVE_IMAGE = 2;
    final private int PICK_IMAGE = 3;
    final private int CAPTURE_VDO = 4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workins);
        Intent i = getIntent();
        idp = i.getExtras().getString("id_Problem");
        idr = i.getExtras().getString("idRental");

        de = i.getExtras().getString("Detail");
        lat = i.getExtras().getString("Lat");
        lng = i.getExtras().getString("Lng");
        idc = i.getExtras().getString("idCarregis");
        detail = findViewById(R.id.editText7);
        detail.setHint("รายละเอียดความเสียหาย");
        imgPreview = findViewById(R.id.imageView);
        TextView tx = (TextView) findViewById(R.id.textView18);
        TextView tx2 = (TextView) findViewById(R.id.textView19);
        TextView tx3 = (TextView) findViewById(R.id.textView17);
        tx.setText("รหัสการแจ้ง:" + idp);
        tx2.setText("รายละเอียดจากลูกค้า:" + de);
        tx3.setText("รายการเคลม");
        tx3.setTextSize(20);
        SharedPreferences sharedPrefer = getSharedPreferences(APP_PREFER, Context.MODE_PRIVATE);
//        String sharePrefUsername = sharedPrefer.getString("usernamePref",null);
        String sharePrefID = sharedPrefer.getString("idPref3", null);
        ide = sharePrefID;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
            return;
        }
        BottomNavigationView btm2 = findViewById(R.id.btm);
        btm2.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id == R.id.item_report){
//                            menuItem.setIcon()
                            Intent intent = new Intent(getApplicationContext(), Insurance.class);
                            startActivity(intent);
                        }else if(id == R.id.item_success){
                            Intent intent = new Intent(getApplicationContext(), InsSuccess.class);
                            startActivity(intent);
                        }else if(id == R.id.item_profile){
//                            SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER,
//                                    Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharePrefer.edit();
//                            editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
//                            editor.commit();  // ยืนยันการแก้ไข preferences
                            Intent intent = new Intent(getApplicationContext(), ProfileIns.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        return true;
                    }
                }
        );
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_map:
                                map();
                                return true;
                            case R.id.item_success:
                                success();
                                return true;
                            case R.id.item_fail:
                                fail();
                                return true;

                        }
                        return false;
                    }
                });
        BottomNavigationView btmcam = findViewById(R.id.bottomcamera);
        btmcam.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            //Capture and preview
//                            case R.id.item_camera_capture:
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                startActivityForResult(intent, CAPTURE_IMAGE);
//                                return true;

                            //Capture, save and preview
                            case R.id.item_camera_upload:
                                Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri photoURI = null;
                                try {
                                    photoURI = FileProvider.getUriForFile(WorkIns.this,
                                            BuildConfig.APPLICATION_ID + ".provider",
                                            createImageFile());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(imageIntent, CAPTURE_SAVE_IMAGE);
                                return true;
                            case R.id.item_up:
                                upload();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
//            if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
//                if (data != null && data.getExtras() != null) {
//                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//                    imageBitmap = this.resizeImage(imageBitmap, 1024, 1024);//Resize image
//                    imgPreview.setImageBitmap(imageBitmap);//Preview image
//                }
//            } else
                if (requestCode == CAPTURE_SAVE_IMAGE && resultCode == RESULT_OK) {
                Uri imageUri = Uri.parse("file:" + imageFilePath);
                File file = new File(imageUri.getPath());
                try {
                    //show image
                    InputStream ims = new FileInputStream(file);
                    Bitmap imageBitmap = BitmapFactory.decodeStream(ims);
                    imageBitmap = this.resizeImage(imageBitmap, 1024, 1024);//resize image
                    imageBitmap = resolveRotateImage(imageBitmap, imageFilePath);//Resolve auto rotate image
                    imgPreview.setImageBitmap(imageBitmap);//Preview image
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(imageFilePath);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    getApplicationContext().sendBroadcast(mediaScanIntent);
//                    Log.d("ko",imageFilePath);
//                    Log.d("ko", String.valueOf(file));
//                    RequestParams params = new RequestParams();
//                    params.put("file", imageFilePath);
//                    AsyncHttpClient http = new AsyncHttpClient();
//                    String url = getString(R.string.upload_url) + idp;
//                    http.post(url, params, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            JSONObject obj = null;
//                            try {
//                                obj = new JSONObject(response.toString());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            String status = null;
//                            try {
//                                status = (String) obj.get("status");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//
//
//                            }
//                            if (status.equals("true")) {
//                                Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), NextActivity.class);
//                                startActivity(intent);
//                                Log.d("ko",status);
//
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
//                                Log.d("ko",status);
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            super.onFailure(statusCode, headers, responseString, throwable);
//                            Log.d("onFailure", Integer.toString(statusCode));
//                        }
//                    });


                } catch (FileNotFoundException e) {
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
//                Uri contentURI = data.getData();
//                Bitmap bitmap = null;
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    imgPreview.setImageBitmap(bitmap);//Preview image
//                    imgPreview.setImageURI(contentURI);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } else if (requestCode == CAPTURE_VDO && resultCode == RESULT_OK) {
//
           }
        }

    }

    //Create image file
    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
        File image = File.createTempFile(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()), ".png",storageDir);
        imageFilePath = image.getAbsolutePath();//imageFilePath = "file:" + image.getAbsolutePath();
        return image;
    }



    //Resize image
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

    //Resolve auto rotate image problem
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

    //Rotate image
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    public void upload(){
                    RequestParams params = new RequestParams();
                    File photo = new File(imageFilePath);

                    try {
                    params.put("file", photo);
                    } catch(FileNotFoundException e) {}
                    AsyncHttpClient http = new AsyncHttpClient();
                    String url = getString(R.string.upload_url);
                    http.post(url, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(response.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String status = null;
                            String error = null;
                            try {
                                status = (String) obj.get("status");
                                error = (String) obj.get("error");
                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                            if (status.equals("true")) {
//                                Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), WorkIns.class);
//                                startActivity(intent);
//                                imageFilePath = null;
                                imgPreview.setImageDrawable(getResources().getDrawable(R.drawable.photo));
//                                imgPreview.setImageDrawable(R.drawable.ic_menu_gallery);
                                Log.d("ko",status);
                                Log.d("ko",error);
                                Log.d("ko", imageFilePath);
                                RequestParams params = new RequestParams();

                                    params.add("img4", error);

                                AsyncHttpClient http = new AsyncHttpClient();
                                String url = getString(R.string.save_url) + idp;
                                http.post(url, params, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        JSONObject obj = null;
                                        try {
                                            obj = new JSONObject(response.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        String status = null;
//                                        String error = null;
                                        try {
                                            status = (String) obj.get("status");
//                                            error = (String) obj.get("error");
                                        } catch (JSONException e) {
                                            e.printStackTrace();


                                        }
                                        if (status.equals("true")) {
                                            Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), WorkIns.class);
//                                startActivity(intent);
//                                imageFilePath = null;
                                            imgPreview.setImageDrawable(getResources().getDrawable(R.drawable.photo));
//                                imgPreview.setImageDrawable(R.drawable.ic_menu_gallery);
                                            Log.d("ko",status);
//                                            Log.d("ko",error);
//                                            Log.d("ko", imageFilePath);

                                        } else {
                                            Toast.makeText(getApplicationContext(), "กรุณาถ่ายรูปก่อนอัปโหลด", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), WorkIns.class);
//                                startActivity(intent);
                                            Log.d("ko",status);
//                                            Log.d("ko",error);
//                                            Log.d("ko", imageFilePath);
                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                        Log.d("onFailure", Integer.toString(statusCode));
                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(), "กรุณาถ่ายรูปก่อนอัปโหลด", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getApplicationContext(), WorkIns.class);
//                                startActivity(intent);
                                Log.d("ko",status);
                                Log.d("ko",error);
                                Log.d("ko", imageFilePath);
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            Log.d("onFailure", Integer.toString(statusCode));
                        }
                    });
    }

    public void success() {
        Boolean cancel = false;
        String prefix = "กรุณาระบุ";
        detail.setError(null);
        if (TextUtils.isEmpty(detail.getText().toString())){
            cancel = true;
            detail.setError(prefix + detail.getHint().toString());
            detail.requestFocus();
        }
        if(cancel==false) {
//        EditText detail = (EditText) findViewById(R.id.editText7);
            RequestParams params = new RequestParams();
            params.add("detail2", detail.getText().toString());
            AsyncHttpClient http = new AsyncHttpClient();
            String url = getString(R.string.root_url8) + idp + "/" + idr + "/" + ide;
            http.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String status = null;
                    try {
                        status = (String) obj.get("status");
                    } catch (JSONException e) {
                        e.printStackTrace();


                    }
                    if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), InsSuccess.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("onFailure", Integer.toString(statusCode));
                }
            });
        }
//        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
    }
    public void fail(){
        Boolean cancel = false;
        String prefix = "กรุณาระบุ";
        detail.setError(null);
        if (TextUtils.isEmpty(detail.getText().toString())){
            cancel = true;
            detail.setError(prefix + detail.getHint().toString());
            detail.requestFocus();
        }
        if(cancel==false) {
//        EditText detail = (EditText) findViewById(R.id.editText7);
            RequestParams params = new RequestParams();
            params.add("detail2", detail.getText().toString());
            AsyncHttpClient http = new AsyncHttpClient();
            String url = getString(R.string.root_url9) + idp + "/" + idr + "/" + ide + "/" + idc;
            http.post(url, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String status = null;
                    try {
                        status = (String) obj.get("status");
                    } catch (JSONException e) {
                        e.printStackTrace();


                    }
                    if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), "บันทึกข้อมูลเรียบร้อย", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), InsSuccess.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d("onFailure", Integer.toString(statusCode));
                }
            });
        }
//        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
    }
    public void map(){
        Uri uri = Uri.parse("geo:"+String.valueOf(lng+lat)+"?q="+String.valueOf(lat)+","+String.valueOf(lng));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(intent
                , "View map with"));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            SharedPreferences sharePrefer = getSharedPreferences(MainActivity.APP_PREFER,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharePrefer.edit();
            editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
            editor.commit();  // ยืนยันการแก้ไข preferences
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
//        else if(id == R.id.action_rent){
//            Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
//            startActivity(intent);
//        }
//        else if(id == R.id.action_problem){
//            Intent intent = new Intent(getApplicationContext(), NextActivity.class);
//            startActivity(intent);
//        }

        return super.onOptionsItemSelected(item);
    }


}

