package project.farmvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CropRequestDetailsActivity extends AppCompatActivity {
    private EditText name,price,description,quantity;
    ImageView imageView;
    LinearLayout layout;
    private String namet,pricet,descriptiont,quantityt,mode,user_id,farmer_id,id,image="",request_for;
    EditText request_by,request_contact;
    Button accept,reject;
    DBHelper dbHelper;
    String language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper=new DBHelper(this);
        try {
            language=dbHelper.getLanguage(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (language.equals("English")) {
            setContentView(R.layout.activity_crop_request_details);
        }
        else
        {
            setContentView(R.layout.kactivity_crop_request_details);
        }

        name=(EditText)findViewById(R.id.crop_name);
        price=(EditText)findViewById(R.id.crop_price);
        description=(EditText)findViewById(R.id.crop_description);
        imageView=(ImageView)findViewById(R.id.crop_image);

        layout=(LinearLayout)findViewById(R.id.hide_layout);
        quantity=(EditText)findViewById(R.id.crop_quantity);
        request_by=(EditText)findViewById(R.id.request_by);
        request_contact=(EditText)findViewById(R.id.requester_contact);

        accept=(Button)findViewById(R.id.btn_accept);
        reject=(Button)findViewById(R.id.btn_delete);


        Bundle data=getIntent().getExtras();

//        if (data.getString("mode").equals("crop"))
//        {
//            mode=data.getString("mode");
//            namet=data.getString("name");
//            pricet=data.getString("price");
//            descriptiont=data.getString("description");
//            quantityt=data.getString("quantity");
//            user_id=data.getString("user_id");
//            farmer_id=data.getString("farmer_id");
//            image=data.getString("image");
//            id=data.getString("id");
//            name.setText(namet);
//            price.setText(pricet);
//            description.setText(descriptiont);
//            quantity.setText(quantityt);
//            Picasso.with(getApplicationContext())
//                    .load(CONSTANTS.IPADDRESS+"/getImage/"+image)
//                    .into(imageView);
//
//        }
//        if (mode.equals("crop_request"))
//        {
//            mode=data.getString("mode");
//            namet=data.getString("name");
//            descriptiont=data.getString("description");
//            quantityt=data.getString("quantity");
//            user_id=data.getString("user_id");
//            farmer_id=data.getString("farmer_id");
//            id=data.getString("id");
//            name.setText(namet);
//            description.setText(descriptiont);
//            quantity.setText(quantityt);
//            imageView.setVisibility(View.GONE);
//            layout.setVisibility(View.GONE);
//        }

        Bundle object=getIntent().getExtras();
        final String  id=object.getString("id");
        String contact=object.getString("farmer_contact");
        String names=object.getString("farmer_name");
        String object_id=object.getString("object_id");
//        String rfarmer_id=object.getString("rfarmer_id");
        request_for=object.getString("request_for");

        request_by.setText(names);
        request_contact.setText(contact);
        getDetails(object_id,request_for);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest(id);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRequest(id);
            }
        });

    }

    private void deleteRequest(final String id) {

        String API= CONSTANTS.IPADDRESS+"/delete_request";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if( jsonObject.get("status").equals("success"))
                            {

                                Toast.makeText(getApplicationContext(),"Okay",Toast.LENGTH_LONG).show();
                                finish();

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("request_id",id );
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void acceptRequest(final String id) {
        String API= CONSTANTS.IPADDRESS+"/accept_request";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if( jsonObject.get("status").equals("success"))
                            {

                                Toast.makeText(getApplicationContext(),"Okay",Toast.LENGTH_LONG).show();
                                finish();

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("request_id",id );
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void getDetails(final String object_id, final String request_for) {

        String API= CONSTANTS.IPADDRESS+"/get_details";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if( jsonObject.get("status").equals("success"))
                            {
                                JSONArray requests =jsonObject.getJSONArray("list");

                                for (int i =0; i<requests.length(); i++) {
                                    JSONObject data = requests.getJSONObject(i);
                                    if (request_for.equals("crop"))
                                    {
                                        namet=data.getString("name");
                                        pricet=data.getString("price");
                                        descriptiont=data.getString("description");
                                        quantityt=data.getString("quantity");
                                        farmer_id=data.getString("farmer_id");
                                        image=data.getString("image");
                                        Log.w("Farvisor  ", "onResponse: "+image);
                                        id=data.getString("id");
                                        name.setText(namet);
                                        price.setText(pricet);
                                        description.setText(descriptiont);
                                        quantity.setText(quantityt);

//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
                                                Picasso.with(getApplicationContext())
                                                        .load(CONSTANTS.IPADDRESS+"/getImage/"+image)
                                                        .into(imageView);
//                                            }
//                                        });


                                    }
                                    else
                                    {
                                        namet=data.getString("name");
                                        descriptiont=data.getString("description");
                                        quantityt=data.getString("quantity");
                                        farmer_id=data.getString("farmer_id");
                                        id=data.getString("id");
                                        name.setText(namet);
                                        description.setText(descriptiont);
                                        quantity.setText(quantityt);
                                        imageView.setVisibility(View.GONE);
                                        layout.setVisibility(View.GONE);
                                    }

                                }


                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Something went wrong please try again later!",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("object_id",object_id );
                params.put("object_for",request_for);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }




}
