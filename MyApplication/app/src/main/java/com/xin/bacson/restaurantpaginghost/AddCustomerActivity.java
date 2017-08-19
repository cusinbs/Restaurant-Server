package com.xin.bacson.restaurantpaginghost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends AppCompatActivity {
    String urlInsert = "http://192.168.1.75/restaurantpaging/insert.php";
    EditText edtName,edtNum,edtReady;
    Button btnRun, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        Initialize();
        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String num = edtNum.getText().toString().trim();
                String ready = edtReady.getText().toString().trim();
                if(name.isEmpty() || num.isEmpty() || ready.isEmpty()){
                    Toast.makeText(AddCustomerActivity.this, "Please fill all the info", Toast.LENGTH_SHORT).show();
                }else{
                    addStudent(urlInsert);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Initialize() {
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnRun = (Button)findViewById(R.id.buttonRun);
        edtName = (EditText)findViewById(R.id.editName);
        edtNum = (EditText)findViewById(R.id.editNum);
        edtReady = (EditText)findViewById(R.id.editReady);
    }

    private void addStudent(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            Toast.makeText(AddCustomerActivity.this,"Added",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddCustomerActivity.this,MainActivity.class));
                        }else{
                            Toast.makeText(AddCustomerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddCustomerActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                        Log.d("Xin","Error:\n" + error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nameP",edtName.getText().toString().trim());
                params.put("numP",edtNum.getText().toString().trim());
                if(edtReady.getText().toString().trim().equals("YES")||edtReady.getText().toString().trim().equals("Yes")){
                    params.put("readyP","1");
                }else{
                    params.put("readyP","0");
                }

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
