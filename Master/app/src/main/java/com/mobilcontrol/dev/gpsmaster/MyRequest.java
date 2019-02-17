package com.mobilcontrol.dev.gpsmaster;

import android.content.Context;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MyRequest {
    Context context;
    int method;
    String url;
    RequestQueue requestQueue;
    Map<String,String> params;
    int responseCode;
    String result;

    public MyRequest(Context context, int method, String url, Map<String,String> params){
        this.url = url;
        this.method = method;
        this.context = context;
        this.params = params;

        requestQueue = Volley.newRequestQueue(this.context);
    }

    public MyRequest(Context context, int method, String url){
        this.url = url;
        this.method = method;
        this.context = context;

        requestQueue = Volley.newRequestQueue(this.context);
    }

    public void processRequest(){
        StringRequest stringRequest = new StringRequest(this.method, this.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyRequest.this.OnSuccess(MyRequest.this.responseCode, MyRequest.this.result);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                return MyRequest.this.params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }

                MyRequest.this.responseCode = response.statusCode;

                try {
                    MyRequest.this.result = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }

    public void cancelRequest(){
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public void OnSuccess(int responseCode, String data){
    }
}
