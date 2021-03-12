package project.farmvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JobRequestDetailsActivity extends AppCompatActivity {

    private EditText titleIn,paymentIn,descriptionIn;
    private String title,payment,description,farmer_id,contact,id,user_id;
    EditText request_by,request_contact;
    DBHelper dbHelper;
    String language;
    Button accept,reject;
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
            setContentView(R.layout.activity_job_request_details);
        }
        else
        {
            setContentView(R.layout.kactivity_job_request_details);
        }


        titleIn=(EditText)findViewById(R.id.job_title);
        paymentIn=(EditText)findViewById(R.id.job_payment);
        descriptionIn=(EditText)findViewById(R.id.job_description);

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

                                    title=b.getString("jobTitle");
                                    payment=b.getString("jobPayment");
                                    description=b.getString("jobDescription");

                                    titleIn.setText(title);
                                    paymentIn.setText(payment);
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
