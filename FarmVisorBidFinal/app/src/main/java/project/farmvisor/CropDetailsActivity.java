package project.farmvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CropDetailsActivity extends AppCompatActivity {

    private EditText name,price,description,quantity;
    ImageView imageView;
    LinearLayout layout;
    private String namet,pricet,descriptiont,quantityt,mode,user_id,farmer_id,id,image;
    Button btn_add;
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
            setContentView(R.layout.activity_crop_details);
        }
        else
        {
            setContentView(R.layout.kactivity_crop_details);
        }
        name=(EditText)findViewById(R.id.crop_name);
        price=(EditText)findViewById(R.id.crop_price);
        description=(EditText)findViewById(R.id.crop_description);
        imageView=(ImageView)findViewById(R.id.crop_image);
        btn_add=(Button)findViewById(R.id.btn_add_crop);
        layout=(LinearLayout)findViewById(R.id.hide_layout);
        quantity=(EditText)findViewById(R.id.crop_quantity);
        Bundle data=getIntent().getExtras();

        if (data.getString("mode").equals("crop"))
        {
            mode=data.getString("mode");
            namet=data.getString("name");
            pricet=data.getString("price");
            descriptiont=data.getString("description");
            quantityt=data.getString("quantity");
            user_id=data.getString("user_id");
            farmer_id=data.getString("farmer_id");
            image=data.getString("image");
            id=data.getString("id");
            name.setText(namet);
            price.setText(pricet);
            description.setText(descriptiont);
            quantity.setText(quantityt);
            Picasso.with(getApplicationContext())
                    .load(CONSTANTS.IPADDRESS+"/getImage/"+image)
                    .into(imageView);

        }
        if (data.getString("mode").equals("crop_request"))
        {
            mode=data.getString("mode");
            namet=data.getString("name");
            descriptiont=data.getString("description");
            quantityt=data.getString("quantity");
            user_id=data.getString("user_id");
            farmer_id=data.getString("farmer_id");
            id=data.getString("id");
            name.setText(namet);
            description.setText(descriptiont);
            quantity.setText(quantityt);
            imageView.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        }



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               requestCrop();
            }
        });

    }

    private void requestCrop() {
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
                params.put("request_for",mode);
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
