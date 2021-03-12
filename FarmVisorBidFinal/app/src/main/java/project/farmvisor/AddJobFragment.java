package project.farmvisor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AddJobFragment extends Fragment {

    static String farmer_id,contact,lang;
    private EditText titleIn,paymentIn,descriptionIn;
    private String title,payment,description;
    public AddJobFragment() {
        // Required empty public constructor
    }

    public static AddJobFragment newInstance(String farmer_id_str, String contact_str, String language) {
        AddJobFragment fragment = new AddJobFragment();
        Bundle args = new Bundle();
        farmer_id=farmer_id_str;
        contact=contact_str;
        lang=language;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        if (lang.equals("English")) {
            root =inflater.inflate(R.layout.fragment_add_job, container, false);
        }
        else
        {
            root =inflater.inflate(R.layout.kfragment_add_job, container, false);
        }

        titleIn=(EditText)root.findViewById(R.id.job_title);
        paymentIn=(EditText)root.findViewById(R.id.job_payment);
        descriptionIn=(EditText)root.findViewById(R.id.job_description);

        Button add=(Button)root.findViewById(R.id.btn_add_job);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=titleIn.getText().toString().trim();
                payment=paymentIn.getText().toString().trim();
                description=descriptionIn.getText().toString().trim();

                if (title.equals("") || payment.equals("") || description.equals(""))
                {
                    Toast.makeText(getActivity(),"All feilds are Mandatory!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    addJob();


                }
            }
        });


        return root;
    }

    private void addJob() {
        String API= CONSTANTS.IPADDRESS+"/farmvisor/add_job";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if( jsonObject.get("job").equals("added"))
                            {

                                Toast.makeText(getActivity(),"Added",Toast.LENGTH_LONG).show();
                                clear();

                            }
                            else{
                                Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("farmer_id",farmer_id );
                params.put("title",title);
                params.put("contact",contact);
                params.put("description",description);
                params.put("payment",payment);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    private void clear() {
        titleIn.setText("");
        descriptionIn.setText("");
        paymentIn.setText("");
    }


}
