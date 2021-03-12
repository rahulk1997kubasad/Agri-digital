package project.farmvisor;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestsListActivty extends AppCompatActivity {
    ListView listView;
    String user_id;
    private List<RequestObject> arrayListJob;
    private List<RequestObject> arrayListInvest;
    private List<RequestObject> arrayListLand;
    private List<RequestObject> arrayListEquipment;
    private List<RequestObject> arrayListCrop;
    private List<RequestObject> arrayListCropRequest;
    ArrayAdapter<RequestObject> mAdapter;
    String requestOf="job";
    String language;
    DBHelper dbHelper;
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
            setContentView(R.layout.activity_requests_list_activty);
        }
        else
        {
            setContentView(R.layout.kactivity_requests_list_activty);
        }

        arrayListJob=new ArrayList<RequestObject>();
        arrayListInvest=new ArrayList<RequestObject>();
        arrayListLand=new ArrayList<RequestObject>();
        arrayListEquipment=new ArrayList<RequestObject>();

        arrayListCrop=new ArrayList<RequestObject>();
        arrayListCropRequest=new ArrayList<RequestObject>();

        user_id=getIntent().getExtras().getString("user_id");
        listView=(ListView)findViewById(R.id.list_requestss);
        getRequests(user_id);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {




                if (requestOf.equals("job"))
                {
                    RequestObject requestObject=arrayListJob.get(i);
                    Intent intent=new Intent(RequestsListActivty.this,JobRequestDetailsActivity.class);
                    intent.putExtra("id",requestObject.getId());
                    intent.putExtra("farmer_contact",requestObject.getFarmer_contact());
                    intent.putExtra("farmer_name",requestObject.getFarmer_name());
                    intent.putExtra("object_id",requestObject.getObject_id());
                    intent.putExtra("request_for",requestObject.getRequest_for());
                    startActivity(intent);
                }
                else if (requestOf.equals("investment"))
                {

                    RequestObject requestObject=arrayListInvest.get(i);
                    Intent intent=new Intent(RequestsListActivty.this,InvestRequestDetailsActivity.class);
                    intent.putExtra("id",requestObject.getId());
                    intent.putExtra("farmer_contact",requestObject.getFarmer_contact());
                    intent.putExtra("farmer_name",requestObject.getFarmer_name());
                    intent.putExtra("object_id",requestObject.getObject_id());
                    intent.putExtra("request_for",requestObject.getRequest_for());
                    startActivity(intent);
                }
                else if (requestOf.equals("equipment"))
                {
                    RequestObject requestObject=arrayListEquipment.get(i);
                    Intent intent=new Intent(RequestsListActivty.this,EquipmentRequestDetailsActivity.class);
                    intent.putExtra("id",requestObject.getId());
                    intent.putExtra("farmer_contact",requestObject.getFarmer_contact());
                    intent.putExtra("farmer_name",requestObject.getFarmer_name());
                    intent.putExtra("object_id",requestObject.getObject_id());
                    intent.putExtra("request_for",requestObject.getRequest_for());
                    startActivity(intent);

                }else if (requestOf.equals("land"))
                {
                    RequestObject requestObject=arrayListLand.get(i);
                    Intent intent=new Intent(RequestsListActivty.this,LandRequestDetailsActivity.class);
                    intent.putExtra("id",requestObject.getId());
                    intent.putExtra("farmer_contact",requestObject.getFarmer_contact());
                    intent.putExtra("farmer_name",requestObject.getFarmer_name());
                    intent.putExtra("object_id",requestObject.getObject_id());
                    intent.putExtra("request_for",requestObject.getRequest_for());
                    startActivity(intent);

                }
                else if (requestOf.equals("crop"))
                {
                    RequestObject requestObject=arrayListCrop.get(i);
                    Intent intent=new Intent(RequestsListActivty.this,CropRequestDetailsActivity.class);
                    intent.putExtra("id",requestObject.getId());
                    intent.putExtra("farmer_contact",requestObject.getFarmer_contact());
                    intent.putExtra("farmer_name",requestObject.getFarmer_name());
                    intent.putExtra("object_id",requestObject.getObject_id());
                    intent.putExtra("request_for",requestObject.getRequest_for());
                    startActivity(intent);
                }
                else if (requestOf.equals("crop_request"))
                {
                    RequestObject requestObject=arrayListCropRequest.get(i);
                    Intent intent=new Intent(RequestsListActivty.this,CropRequestDetailsActivity.class);
                    intent.putExtra("id",requestObject.getId());
                    intent.putExtra("farmer_contact",requestObject.getFarmer_contact());
                    intent.putExtra("farmer_name",requestObject.getFarmer_name());
                    intent.putExtra("object_id",requestObject.getObject_id());
                    intent.putExtra("request_for",requestObject.getRequest_for());
                    startActivity(intent);
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final List<String> sizes = new ArrayList<String>();
//        sizes.add("job");
        sizes.add("equipment");
//        sizes.add("investment");
//        sizes.add("land");
        sizes.add("crop");
//        sizes.add("crop_request");
        final List<String> ksizes = new ArrayList<String>();
        ksizes.add("ಕೆಲಸ");
        ksizes.add("ಉಪಕರಣ");
        ksizes.add("ಬಂಡವಾಳ");
        ksizes.add("ಭೂಮಿ");
        ksizes.add("ಬೆಳೆ");
        ksizes.add("ಬೆಳೆಗಳಿಗೆ ವಿನಂತಿ");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter;
        if (language.equals("English")) {
            dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sizes);
        }
        else
        {
            dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ksizes);
        }

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);

                if (language.equals("English")) {
                    requestOf=parent.getItemAtPosition(i).toString();
                }
                else
                {
                    requestOf=sizes.get(i).toString();
                }




                if (requestOf.equals("job"))
                {
                    mAdapter = new ArrayAdapter<RequestObject >(RequestsListActivty.this,R.layout.list_content, R.id.label,arrayListJob);
                    listView.setAdapter(mAdapter);
                }
                if (requestOf.equals("investment"))
                {
                    mAdapter = new ArrayAdapter<RequestObject >(RequestsListActivty.this,R.layout.list_content, R.id.label,arrayListInvest);
                    listView.setAdapter(mAdapter);
                }
                if (requestOf.equals("equipment"))
                {
                    mAdapter = new ArrayAdapter<RequestObject >(RequestsListActivty.this,R.layout.list_element, R.id.label,arrayListEquipment);
                    listView.setAdapter(mAdapter);
                }if (requestOf.equals("land"))
                {
                    mAdapter = new ArrayAdapter<RequestObject >(RequestsListActivty.this,R.layout.list_content, R.id.label,arrayListLand);
                    listView.setAdapter(mAdapter);
                }
                if (requestOf.equals("crop"))
                {
                    mAdapter = new ArrayAdapter<RequestObject >(RequestsListActivty.this,R.layout.list_content, R.id.label,arrayListCrop);
                    listView.setAdapter(mAdapter);
                }
                if (requestOf.equals("crop_request"))
                {
                    mAdapter = new ArrayAdapter<RequestObject >(RequestsListActivty.this,R.layout.list_content, R.id.label,arrayListCropRequest);
                    listView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getRequests(String user_id) {

        final String Api=CONSTANTS.IPADDRESS+"/getMyRequests/"+user_id;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,Api , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray requests = response.getJSONArray("list");

                            for (int i =0; i<requests.length(); i++){
                                JSONObject object = requests.getJSONObject(i);

                                String  id=object.getString("id");
                                String contact=object.getString("farmer_contact");
                                String name=object.getString("farmer_name");
                                String object_id=object.getString("object_id");
                                String rfarmer_id=object.getString("rfarmer_id");
                                String request_for=object.getString("request_for");

                                if (request_for.equals("job"))
                                {
                                    arrayListJob.add(new RequestObject(id,request_for,contact,object_id,rfarmer_id,name));
                                }
                                if (request_for.equals("investment"))
                                {
                                    arrayListInvest.add(new RequestObject(id,request_for,contact,object_id,rfarmer_id,name));
                                }
                                if (request_for.equals("equipment"))
                                {
                                    arrayListEquipment.add(new RequestObject(id,request_for,contact,object_id,rfarmer_id,name));

                                }if (request_for.equals("land"))
                                {
                                    arrayListLand.add(new RequestObject(id,request_for,contact,object_id,rfarmer_id,name));
                                }
                                if (request_for.equals("crop"))
                                {
                                    arrayListCrop.add(new RequestObject(id,request_for,contact,object_id,rfarmer_id,name));
                                }
                                if (request_for.equals("crop_request"))
                                {
                                    arrayListCropRequest.add(new RequestObject(id,request_for,contact,object_id,rfarmer_id,name));
                                }


                            }

                            mAdapter = new ArrayAdapter<RequestObject >(RequestsListActivty.this,R.layout.list_content, R.id.label,arrayListJob);
                            listView.setAdapter(mAdapter);
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonRequest);
    }
}
