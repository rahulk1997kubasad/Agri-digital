package project.farmvisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class FarmerLoginActivity extends AppCompatActivity {
    EditText usernameText,passwordText,forget_phone;
    LinearLayout forget;
    String language="";

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent().getStringExtra("userType");
        dbHelper=new DBHelper(this);
        try {
            language=dbHelper.getLanguage(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (language.equals("English")) {
            setContentView(R.layout.activity_farmer_login);
        }
        else
        {
            setContentView(R.layout.kactivity_farmer_login);
        }

        usernameText=(EditText)findViewById(R.id.farmer_username);
        passwordText=(EditText)findViewById(R.id.farmer_password);
        Button btn_login=(Button)findViewById(R.id.btn_farmer_login);

        forget_phone=(EditText)findViewById(R.id.forget_phone);
        forget=(LinearLayout)findViewById(R.id.forget_layout);
        Button btn_send_pass=(Button)findViewById(R.id.btn_Send_password);


        TextView forgetLink=(TextView)findViewById(R.id.forget_password_link);
        TextView newUser=(TextView)findViewById(R.id.farmer_signup_link);


        forgetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forget.setVisibility(View.VISIBLE);
            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FarmerLoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=usernameText.getText().toString().trim();
                String password=passwordText.getText().toString().trim();
                loginFormer(username,password);
            }
        });

        btn_send_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number=forget_phone.getText().toString().trim();

                if(!phone_number.equals(""))
                {
                    forgetPassword(phone_number);
                }
                else{
                    Toast.makeText(FarmerLoginActivity.this,"Enter the phone number!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void forgetPassword(final String phone_number) {
        String API= CONSTANTS.IPADDRESS+"/farmvisor/forget_password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(getApplicationContext(), response.toString(),Toast.LENGTH_SHORT).show();
                            if( jsonObject.get("status").equals("OK"))
                            {
                                Toast.makeText(FarmerLoginActivity.this,"Password is sent to registered Phone Number!",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FarmerLoginActivity.this,"Something is wnet Wrong Try again later",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",phone_number);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loginFormer(final String username, final String password) {
        String API= CONSTANTS.IPADDRESS+"/farmvisor/farmer_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if( jsonObject.get("login").equals("OK"))
                            {
                                usernameText.setText("");
                                passwordText.setText("");
                                String farmer_id= jsonObject.getString("farmer_id");
                                String contact=jsonObject.getString("contact");
                                String name=jsonObject.getString("name");
                                Intent intent=new Intent(getApplicationContext(),FarmerHomeActivity.class);
                                intent.putExtra("farmer_id",farmer_id);
                                intent.putExtra("contact",contact);
                                intent.putExtra("name",name);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Invalid Username or Password / your registration has not been approved", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Invalid Username or Password", Toast.LENGTH_LONG).show();
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
                params.put("username", username);
                params.put("userType",MyApplication.userType);
                params.put("password",password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.language_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_lang)
        {
            Intent intent=new Intent(getApplicationContext(),LangueageSettingActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
