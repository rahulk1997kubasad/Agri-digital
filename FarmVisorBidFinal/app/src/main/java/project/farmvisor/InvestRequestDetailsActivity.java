package project.farmvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InvestRequestDetailsActivity extends AppCompatActivity {
    private String user_id,area,soil,landmark,cost,year,description,id,contact,farmer_id;
    EditText areaIN,soilIn,landmarkIn,costIn,yearIn,descriptionIn;
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
            setContentView(R.layout.activity_invest_request_details);
        }
        else
        {
            setContentView(R.layout.kactivity_invest_request_details);
        }


        areaIN=(EditText)findViewById(R.id.invest_area);
        soilIn=(EditText)findViewById(R.id.invest_soil);
        landmarkIn=(EditText)findViewById(R.id.invest_landmark);
        costIn=(EditText)findViewById(R.id.invest_cost);
        yearIn=(EditText)findViewById(R.id.invest_year);
        descriptionIn=(EditText)findViewById(R.id.invest_description);


        request_by=(EditText)findViewById(R.id.request_by);
        request_contact=(EditText)findViewById(R.id.requester_contact);

        accept=(Button)findViewById(R.id.btn_accept);
        reject=(Button)findViewById(R.id.btn_delete);

        Bundle object=getIntent().getExtras();
        final String  id=object.getString("id");
        String contact=object.getString("farmer_contact");
        String name=object.getString("farmer_name");
        String object_id=object.getString("object_id");
//        String rfarmer_id=object.getString("rfarmer_id");
        String request_for=object.getString("request_for");

        request_by.setText(name);
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
                                    JSONObject b = requests.getJSONObject(i);

                                    area=b.getString("area_size");
                                    soil=b.getString("soil_type");
                                    landmark=b.getString("land_mark");
                                    cost=b.getString("investments");
                                    year=b.getString("years");
                                    description=b.getString("description");



                                    areaIN.setText(area);
                                    soilIn.setText(soil);
                                    landmarkIn.setText(landmark);
                                    costIn.setText(cost);
                                    yearIn.setText(year);
                                    descriptionIn.setText(description);
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
