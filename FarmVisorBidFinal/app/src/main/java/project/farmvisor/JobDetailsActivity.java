package project.farmvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JobDetailsActivity extends AppCompatActivity {
    private EditText titleIn,paymentIn,descriptionIn;
    private String title,payment,description,farmer_id,contact,id,user_id;
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
            setContentView(R.layout.activity_job_details);
        }
        else
        {
            setContentView(R.layout.kactivity_job_details);
        }


        titleIn=(EditText)findViewById(R.id.job_title);
        paymentIn=(EditText)findViewById(R.id.job_payment);
        descriptionIn=(EditText)findViewById(R.id.job_description);

        final Button apply=(Button)findViewById(R.id.btn_apply_job);


        Bundle b=getIntent().getExtras();
        title=b.getString("title");
        id=b.getString("id");
        contact=b.getString("contact");
        payment=b.getString("payment");
        description=b.getString("description");
        farmer_id=b.getString("farmer_id");

        titleIn.setText(title);
        paymentIn.setText(payment);
        descriptionIn.setText(description);

        user_id=b.getString("user_id");
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyRequest();
            }
        });
    }

    private void applyRequest() {
        String API= CONSTANTS.IPADDRESS+"/farmvisor/apply_request";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                            if( jsonObject.get("status").equals("success"))
                            {
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Something is wnet Wrong Try again later",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("request_for","job");
                params.put("rfarmer_id",farmer_id);
                params.put("farmer_id",user_id);
                params.put("object_id",id);
                return params;
            }

        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}
