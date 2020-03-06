package com.example.httprequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.String;
import cz.msebera.android.httpclient.Header;

public class MapActivity extends AppCompatActivity implements LocationListener {
    private TextView textView5;
    private EditText lat;
    private EditText lon;
    private LocationManager locationManager;
    String locationlat = "";
    String locationlon = "";
    String oo ;
    String bb ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        textView5 = (TextView) findViewById(R.id.textView5);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        onLocationChanged(location);


    }

    @Override
    public void onLocationChanged(Location location) {

        final double longtitude = location.getLongitude();
        final double lattitude = location.getLatitude();
        textView5.setText("Latitude: " + lattitude + "\n" + "Longtitude: " + longtitude);
        locationlat = location.getLatitude() + "";
        locationlon = location.getLongitude() + "";
        final EditText lat = (EditText) findViewById(R.id.lat);
        final EditText lon = (EditText) findViewById(R.id.lon);
        lat.setEnabled(false);
        lon.setEnabled(false);

        //params.add("lon",lon.getText(locationlat).toString());
        lat.setText(locationlat);
        lon.setText(locationlon);
        Button btn = (Button) findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.add("lon", oo = lon.getText().toString());
                params.add("lat", bb = lat.getText().toString());
                AsyncHttpClient http = new AsyncHttpClient();
                http.post("http://gg.harmonicmix.xyz/Map_api/", params, new JsonHttpResponseHandler() {
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
                        if (status.equals("insert")) {
                            Toast.makeText(getApplicationContext(), "ลงเบส", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
//                    startActivity(intent);
//                    isSuccess = true;

                        } else {
                            Toast.makeText(getApplicationContext(), "ไม่ลง", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.d("onFailure", Integer.toString(statusCode));
                    }
                });
//        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
            }
        });

// Uri uri = Uri.parse(String.valueOf(lattitude+longtitude)+"?z=18");
// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
// startActivity(Intent.createChooser(intent
// , "View map with"));

        Button buttonIntent = (Button)findViewById(R.id.button2);
        buttonIntent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
// onLocationChanged(location);
                Uri uri = Uri.parse("geo:"+String.valueOf(longtitude+lattitude)+"?q="+String.valueOf(lattitude)+","+String.valueOf(longtitude));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(Intent.createChooser(intent
                        , "View map with"));
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
//    public void onInsert(View v)
//    {
//
//        RequestParams params = new RequestParams();
//        params.add("lat", lat.getText().toString());
//        params.add("lon",lon.getText().toString());
//        //params.add("password", ok2.getText().toString());
//
//        AsyncHttpClient http = new AsyncHttpClient();
//        http.post("http://gg.harmonicmix.xyz/Map_api/",params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                JSONObject obj = null;
//                try {
//                    obj = new JSONObject(response.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                String status = null;
//                try {
//                    status = (String) obj.get("status");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (status.equals("insert")) {
//                    Toast.makeText(getApplicationContext(), "ลงเบส", Toast.LENGTH_LONG).show();
////                    Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
////                    startActivity(intent);
////                    isSuccess = true;
//
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "ไม่ลง", Toast.LENGTH_LONG).show();
//                }
//
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                Log.d("onFailure", Integer.toString(statusCode));
//            }
//        });
////        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
//    }
}
