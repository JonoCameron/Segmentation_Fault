package com.example.srujanvajram.afinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageWriter;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private TextView editText1;
    private TextView editText2;
    private TextView editText3;
    private TextView editText4;
    private TextView editText5;
    private Button goButton;
    private Button resetButton;
    private ImageButton imageButton;
    private RequestQueue requestQueue;
    public Vector<JSONObject> BigInfo = new Vector<JSONObject>();
    public Vector<String> instructions = new Vector<String>();
    public static JSONObject myObject = new JSONObject();
    public JSONObject myObject2 = new JSONObject();
    static int id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (TextView) findViewById(R.id.editText1);
        editText2 = (TextView) findViewById(R.id.editText2);
        editText3 = (TextView) findViewById(R.id.editText3);
        editText4 = (TextView) findViewById(R.id.editText4);
        editText5 = (TextView) findViewById(R.id.editText5);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        goButton = (Button) findViewById(R.id.goButton);
        resetButton = (Button) findViewById(R.id.resetButton);


        goButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                String ingredients = editText1.getText().toString();
                //editText1.setText("");
                ingredients = ingredients.replaceAll(",", "%2C");
                String uri = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&ingredients=" + ingredients + "&limitLicense=false&number=1&ranking=2";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                        new Response.Listener<String>() {


                            public String segment(String s) {

                                String rString = "";

                                for (int i = 0; i < s.length(); i++) {
                                    if (s.charAt(i) == '{') {
                                        while (s.charAt(i) != '}') {
                                            rString = rString + s.charAt(i);
                                            i++;
                                        }

                                        rString = rString + '}';
                                    }
                                }

                                return rString;

                            }


                            @Override
                            public void onResponse(String response) {

                                String sResponse = segment(response);

                                try {

                                    myObject = new JSONObject(sResponse);
                                    editText2.setText(myObject.get("title").toString());
                                    editText5.setText(myObject.get("image").toString());

                                } catch (JSONException e) {

                                    editText2.setText("error");
                                    e.printStackTrace();
                                }

                            }

                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        editText1.setText("Error");
                    }

                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("X-Mashape-Key", "fc35SKpfj6msh9NMeccNC3K9X6fOp1vQJXEjsnjcJF6csTbzWR");
                        params.put("Accept", "application/json");
                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);


                //SECOND CALL

                try {

                    id = (int) myObject.get("id");
                    String uri2 = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/" + id + "/information?includeNutrition=false";
                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, uri2,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response2) {

                                    try {
                                        myObject2 = new JSONObject(response2);

                                        if (myObject2.get("instructions").toString() == "null")
                                        {
                                            editText3.setText("Currently there are no directions on how to cook this meal.");

                                        } else {

                                            editText3.setText(myObject2.get("instructions").toString());

                                        }

                                        String s1 = "Vegetarian: " + myObject2.get("vegetarian");
                                        String s2 = "Vegan: " + myObject2.get("vegan");
                                        String s3 = "Gluten Free: " + myObject2.get("glutenFree");
                                        String s4 = "Dairy Free: " + myObject2.get("dairyFree");
                                        String s5 = "Servings: " + myObject2.get("servings");


                                        String total = s1 + "\n" + s2 + "\n" + s3 + "\n" + s4 + "\n" + s5;

                                        editText4.setText(total);

                                    } catch (JSONException e) {

                                        editText3.setText(id);
                                        e.printStackTrace();
                                    }


                                }

                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            editText1.setText("Error");
                        }

                    }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("X-Mashape-Key", "fc35SKpfj6msh9NMeccNC3K9X6fOp1vQJXEjsnjcJF6csTbzWR");
                            params.put("Accept", "application/json");
                            return params;
                        }
                    };

                    // Add the request to the RequestQueue.

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue2.add(stringRequest2);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v == resetButton) {

                    editText1.setText("");
                    editText2.setText("");
                    editText3.setText("");
                    editText4.setText("");
                    editText5.setText("");

                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));


                }

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (v == imageButton) {

                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(editText5.getText().toString()));
                    startActivity(browserIntent);

                }
            }
        });

        TextView message = (TextView) findViewById(R.id.editText3);
        message.setMovementMethod(new ScrollingMovementMethod());


    }
}



