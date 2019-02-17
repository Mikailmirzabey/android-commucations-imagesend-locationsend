package com.mobilcontrol.dev.gpsmaster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import com.android.volley.Request;
import org.json.JSONException;
import org.json.JSONObject;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSnapshot();
    }

    @Override
    public void onBackPressed() {
        Intent itc = new Intent(CameraActivity.this.getApplicationContext(), MainActivity.class);
        startActivity(itc);
    }

    public void getSnapshot(){
        MyRequest snapshotRequest = new MyRequest(CameraActivity.this, Request.Method.GET,
                Config.baseAddress+"/mobil-control/api/camera-snapshot/"){
            @Override
            public void OnSuccess(int responseCode, String result) {
                JSONObject coordinate;

                try {
                    coordinate = new JSONObject(result);
                    String encodedImage = coordinate.getString("image");

                    byte[] decodedImage = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

                    ImageView im = (ImageView)CameraActivity.this.findViewById(R.id.imageView);
                    im.setImageBitmap(bmp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        snapshotRequest.processRequest();
    }
}
