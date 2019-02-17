package com.mobilcontrol.dev.gpsmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import java.util.HashMap;
import java.util.Map;

public class MapCommandWaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_command_waiting);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onResume(){
        MapCommandWait();
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();

    }
    public void MapCommandWait(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("type",Integer.toString(CommandType.MAP.ordinal()));

                MyRequest r = new MyRequest(MapCommandWaitingActivity.this, Request.Method.POST,
                        Config.baseAddress+"/mobil-control/api/command/create", params){

                    @Override
                    public void OnSuccess(int responseCode, String result){
                                MyRequest r2 = new MyRequest(MapCommandWaitingActivity.this, Request.Method.GET,
                                        Config.baseAddress+"/mobil-control/api/command/"){

                            @Override
                            public void OnSuccess(int responseCode, String result){
                                if(result.isEmpty()){
                                    Intent it = new Intent(MapCommandWaitingActivity.this.getApplicationContext(), MapsActivity.class);
                                    startActivity(it);
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

                        r2.processRequest();
                    }
                };
                r.processRequest();
            }
        }).start();
    }


}
