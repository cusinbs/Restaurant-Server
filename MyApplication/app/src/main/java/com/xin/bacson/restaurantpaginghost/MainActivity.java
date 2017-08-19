package com.xin.bacson.restaurantpaginghost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String url = "http://192.168.1.75/restaurantpaging/getdata.php";
    String urlDelete = "http://192.168.1.75/restaurantpaging/delete.php";
    ListView listViewLocation;
    ArrayList<Paging> pagingArrayList;
    PagingAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewLocation = (ListView) findViewById(R.id.ListViewLocation);
        pagingArrayList = new ArrayList<>();
        adapter = new PagingAdapter(this,R.layout.listview_paging,pagingArrayList);
        listViewLocation.setAdapter(adapter);
        getData(url);
        //Log.d("Xin",pagingArrayList.get(0).getName()+"");
    }

    public void deleteCustomer(final int id){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                            getData(url);
                        }else{
                            Toast.makeText(MainActivity.this, "Error Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                        Log.d("Xin","Error:\n" + error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("idP",String.valueOf(id));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pagingArrayList.clear();
                        Log.d("Xin",response.toString());
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                pagingArrayList.add(new Paging(
                                        object.getInt("ID"),
                                        object.getString("NAME"),
                                        object.getInt("NUM"),
                                        object.getInt("READY")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_customer,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuAddStudent){
            startActivity(new Intent(MainActivity.this, AddCustomerActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
