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


public class AddEquipmentFragment extends Fragment {

    private EditText nameIn,modelIn,rentIn,descriptionIn;
    static String contact,farmer_id;
    private String name,model,rent,description;
    static String lang;
    public AddEquipmentFragment() {
        // Required empty public constructor
    }


    public static AddEquipmentFragment newInstance(String farmer_idStr, String contactStr, String language) {
        AddEquipmentFragment fragment = new AddEquipmentFragment();
        contact=contactStr;
        farmer_id=farmer_idStr;
        lang=language;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        if (lang.equals("English")) {
            root=inflater.inflate(R.layout.fragment_add_equipment, container, false);
        }
        else
        {
            root=inflater.inflate(R.layout.kfragment_add_equipment, container, false);
        }

        nameIn=(EditText)root.findViewById(R.id.eq_name);
        modelIn=(EditText)root.findViewById(R.id.eq_model);
        rentIn=(EditText)root.findViewById(R.id.eq_rent);
        descriptionIn=(EditText)root.findViewById(R.id.eq_description);

        Button add_eq=(Button)root.findViewById(R.id.btn_add_eq);

        add_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=nameIn.getText().toString().trim();
                model=modelIn.getText().toString().trim();
                rent=rentIn.getText().toString().trim();
                description=descriptionIn.getText().toString().trim();

                if (name.equals("") || model.equals("") || rent.equals("") || description.equals(""))
                {
                    Toast.makeText(getActivity(),"All feilds are Mandatory!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    addEquipment();
                }
            }
        });

        return root;
    }

    private void addEquipment() {

        String API= CONSTANTS.IPADDRESS+"/farmvisor/addEquipment";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if( jsonObject.get("equipment").equals("added"))
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
                params.put("name",name);
                params.put("contact",contact);
                params.put("description",description);
                params.put("rent_price",rent);
                params.put("model",model);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);



    }

    private void clear() {
            nameIn.setText("");
        modelIn.setText("");
        rentIn.setText("");
        descriptionIn.setText("");
    }


}
