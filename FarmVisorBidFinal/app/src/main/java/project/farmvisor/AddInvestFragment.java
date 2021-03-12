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


public class AddInvestFragment extends Fragment {

    static String farmer_id,contact;
    String area,soil,landmark,cost,year,description;
    EditText areaIN,soilIn,landmarkIn,costIn,yearIn,descriptionIn;
    static String lang;
    public AddInvestFragment() {
        // Required empty public constructor
    }


    public static AddInvestFragment newInstance(String farmer_idStr, String contactStr, String language) {
        AddInvestFragment fragment = new AddInvestFragment();
        farmer_id=farmer_idStr;
        contact=contactStr;
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
            root=inflater.inflate(R.layout.fragment_add_invest, container, false);
        }
        else
        {
            root=inflater.inflate(R.layout.kfragment_add_invest, container, false);
        }


        areaIN=(EditText)root.findViewById(R.id.invest_area);
        soilIn=(EditText)root.findViewById(R.id.invest_soil);
        landmarkIn=(EditText)root.findViewById(R.id.invest_landmark);
        costIn=(EditText)root.findViewById(R.id.invest_cost);
        yearIn=(EditText)root.findViewById(R.id.invest_year);
        descriptionIn=(EditText)root.findViewById(R.id.invest_description);

        Button btn_add_land=(Button)root.findViewById(R.id.btn_add_invest);

        btn_add_land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soil=soilIn.getText().toString().trim();
                area=areaIN.getText().toString().trim();
                landmark=landmarkIn.getText().toString().trim();
                cost=costIn.getText().toString().trim();
                year=yearIn.getText().toString().trim();
                description=descriptionIn.getText().toString().trim();

                if (area.equals("" ) || soil.equals("") || landmark.equals("") || cost.equals("") || year.equals("") || description.equals(""))
                {
                    Toast.makeText(getActivity(),"All feilds are Mandatory!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    addInvestment();
                }
            }
        });

        return root;

    }

    private void addInvestment() {

        String API= CONSTANTS.IPADDRESS+"/farmvisor/addInvest";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if( jsonObject.get("investment").equals("added"))
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
                params.put("area_size",area);
                params.put("contact",contact);
                params.put("description",description);
                params.put("soil_type",soil);
                params.put("land_mark",landmark);
                params.put("cost_year",cost);
                params.put("years",year);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    private void clear() {
        areaIN.setText("");
        soilIn.setText("");
        descriptionIn.setText("");
        landmarkIn.setText("");
        costIn.setText("");
        yearIn.setText("");

    }


}
