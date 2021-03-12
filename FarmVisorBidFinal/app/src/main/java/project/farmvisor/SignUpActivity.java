package project.farmvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText reg_fname,reg_lname,reg_number,reg_phone,reg_password;
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
            setContentView(R.layout.activity_sign_up);
        }
        else
        {
            setContentView(R.layout.kactivity_sign_up);
        }


        reg_fname=(EditText)findViewById(R.id.farmer_reg_fname);
        reg_lname=(EditText)findViewById(R.id.farmer_reg_lname);
        reg_number=(EditText)findViewById(R.id.farmer_reg_number);
        if (MyApplication.userType.equals("trader"))
            ((TextView)findViewById(R.id.regnum)).setText("License No");

        reg_phone=(EditText)findViewById(R.id.farmer_reg_phone);
        reg_password=(EditText)findViewById(R.id.farmer_reg_password);

        Button btn_sign=(Button)findViewById(R.id.btn_farmer_signup);


        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname=reg_fname.getText().toString().trim();
                String lname=reg_lname.getText().toString().trim();
                String kisan_reg_number=reg_number.getText().toString().trim();
                String phone=reg_phone.getText().toString().trim();
                String password=reg_password.getText().toString().trim();

                if (fname.equals("")||kisan_reg_number.equals("")||phone.equals("")||password.equals(""))
                {
                    Toast.makeText(SignUpActivity.this,"All feilds are Mandatory!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    signUp(fname,lname,kisan_reg_number,phone,password);
                }
            }
        });
    }

    private void signUp(final String fname, final String lname, final String kisan_reg_number, final String phone, final String password) {

        String API= CONSTANTS.IPADDRESS+"/farmvisor/farmer_registration";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response);

                            if( jsonObject.get("register").equals("OK"))
                            {
                                Toast.makeText(getApplicationContext(), "Registered!",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fname",fname );
                params.put("lname",lname);
                params.put("kisan_number",kisan_reg_number);
                params.put("contact",phone);
                params.put("password",password);
                params.put("userType",MyApplication.userType);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
