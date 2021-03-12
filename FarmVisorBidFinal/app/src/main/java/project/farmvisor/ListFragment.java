package project.farmvisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    String mode;
    public List<JobObject> items;
    private View root;
    private ArrayAdapter<JobObject> mArrayAdapter1;

    public static List<LandObject> items2;
    private ArrayAdapter<LandObject> mArrayAdapter2;

    public static List<EquipmentObject> items3;
    private ArrayAdapter<EquipmentObject> mArrayAdapter3;

    public static List<InvestObject> items4;
    private ArrayAdapter<InvestObject> mArrayAdapter4;

    public static List<CropObject> items5;
    private ArrayAdapter<CropObject> mArrayAdapter5;


    public static List<BidObject> items6;
    private ArrayAdapter<BidObject> mArrayAdapter6;

    static String farmId;
    ListView listView;
    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(String modeStr, String farm_id) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("mode", modeStr);
        farmId=farm_id;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getString("mode");

            if (mode.equals("job"))
            {
                items=new ArrayList<JobObject>();
                getJobs();

            }
            else if (mode.equals("invest"))
            {
                items4=new ArrayList<InvestObject>();
                getInvests();
            }
            else if (mode.equals("equipment"))
            {
                items3=new ArrayList<EquipmentObject>();
                getEquipments();
            }
            else if (mode.equals("land"))
            {
                items2=new ArrayList<LandObject>();
                getLands();
            }
            else if (mode.equals("crop"))
            {
//                items5=new ArrayList<CropObject>();
                items6=new ArrayList<BidObject>();

                getAuctions("mybids");
            }
            else if (mode.equals("crop_request"))
            {
                items5=new ArrayList<CropObject>();
                getCropRequest();
            }

        }


    }

//public void initOnClickListener()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_list, container, false);
        listView=(ListView)root.findViewById(R.id.listView);
        mode = getArguments().getString("mode");
        Log.i("MODE", mode);
//        if (mode.equals("crop")){
//            makeTabs();
//        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("CALLED", "INSIDE");
        if (getArguments() != null) {
            mode = getArguments().getString("mode");

            if (mode.equals("job"))
            {

                        JobObject jb=items.get(i);
                        Intent intent=new Intent(getActivity(),JobDetailsActivity.class);
                        intent.putExtra("id",jb.getId());
                        intent.putExtra("title",jb.getTittle());
                        intent.putExtra("contact",jb.getContact());
                        intent.putExtra("payment",jb.getPayment());
                        intent.putExtra("farmer_id",jb.getFarmer_id());
                        intent.putExtra("description",jb.getDescription());
                        intent.putExtra("user_id",farmId);
                        startActivity(intent);

            }
            else if (mode.equals("invest"))
            {

                InvestObject ln=items4.get(i);
                Intent intent=new Intent(getActivity(),InvestmentActivity.class);
                intent.putExtra("id",ln.getId());
                intent.putExtra("area_size",ln.getArea_size());
                intent.putExtra("contact",ln.getContact());
                intent.putExtra("soil_type",ln.getSoil_type());
                intent.putExtra("farmer_id",ln.getFarmer_id());
                intent.putExtra("description",ln.getDescription());
                intent.putExtra("landmark",ln.getLandmark());
                intent.putExtra("investment",ln.getinvestment());
                intent.putExtra("years",ln.getYears());
                intent.putExtra("user_id",farmId);
                startActivity(intent);
            }
            else if (mode.equals("equipment"))
            {

                EquipmentObject eq=items3.get(i);

                Intent intent=new Intent(getActivity(),EquipmentDetailsActivity.class);
                intent.putExtra("id",eq.getId());
                intent.putExtra("name",eq.getName());
                intent.putExtra("contact",eq.getContact());
                intent.putExtra("rent_price",eq.getRent());
                intent.putExtra("farmer_id",eq.getFarmer_id());
                intent.putExtra("description",eq.getDescription());
                intent.putExtra("model",eq.getModel());
                intent.putExtra("user_id",farmId);
                startActivity(intent);
            }
            else if (mode.equals("land"))
            {
                LandObject ln=items2.get(i);

                Intent intent=new Intent(getActivity(),LandDetailsActivity.class);
                intent.putExtra("id",ln.getId());
                intent.putExtra("area_size",ln.getArea_size());
                intent.putExtra("contact",ln.getContact());
                intent.putExtra("soil_type",ln.getSoil_type());
                intent.putExtra("farmer_id",ln.getFarmer_id());
                intent.putExtra("description",ln.getDescription());
                intent.putExtra("landmark",ln.getLandmark());
                intent.putExtra("cost_year",ln.getCost_year());
                intent.putExtra("years",ln.getYears());
                intent.putExtra("user_id",farmId);
                intent.putExtra("coords",ln.getCoords());

                startActivity(intent);

            }

            else if (mode.equals("crop"))
            {

                CropObject ln=items5.get(i)
                        ;
                Intent intent=new Intent(getActivity(),CropDetailsActivity.class);
                intent.putExtra("id",ln.getId());
                intent.putExtra("name",ln.getName());
                intent.putExtra("price",ln.getPrice());
                intent.putExtra("farmer_id",ln.getFarmer_id());
                intent.putExtra("description",ln.getDescription());
                intent.putExtra("image",ln.getImage());
                intent.putExtra("quantity",ln.getQuantity());
                intent.putExtra("mode",mode);
                intent.putExtra("user_id",farmId);
                startActivity(intent);
            }
            else if (mode.equals("crop_request"))
            {
                CropObject ln=items5.get(i)
                        ;
                Intent intent=new Intent(getActivity(),CropDetailsActivity.class);
                intent.putExtra("id",ln.getId());
                intent.putExtra("name",ln.getName());
                intent.putExtra("farmer_id",ln.getFarmer_id());
                intent.putExtra("description",ln.getDescription());
                intent.putExtra("quantity",ln.getQuantity());
                intent.putExtra("mode",mode);
                intent.putExtra("user_id",farmId);
                startActivity(intent);
            }


        }

            }
        });
        return root;
    }
//
//    private void makeTabs() {
//        LinearLayout tabs = (LinearLayout)root.findViewById(R.id.tabs);
//
//        Button button1 = new Button(getActivity());
//        button1.setText("Current Auctions");
////        button1.setTextColor(0x01060014);
//        button1.setTextSize(20);
//        button1.setPadding(5,5,5,5);
//
//
//        Button button2 = new Button(getActivity());
//        button2.setText("My Bids");
////        button2.setTextColor(0x01060014);
//        button2.setTextSize(20);
//        button2.setPadding(5,5,5,5);
//
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getAuctions("current");
////                mArrayAdapter5 = new ArrayAdapter<CropObject>(getActivity(),R.layout.list_content, R.id.label, items5);
////                listView.setAdapter(mArrayAdapter5);
////
//            }
//        });
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getAuctions("mybids");
//
////                mArrayAdapter5 = new ArrayAdapter<CropObject>(getActivity(),R.layout.list_content, R.id.label, items5);
////                listView.setAdapter(mArrayAdapter5);
//            }
//        });
//        tabs.addView(button1);
//        tabs.addView(button2);
//
//    }




    private void getJobs() {
        final String GET_JOB_URL=CONSTANTS.IPADDRESS+"/farmvisor/getjobs/"+farmId;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,GET_JOB_URL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray requests = response.getJSONArray("jobs");

                            for (int i =0; i<requests.length(); i++){
                                JSONObject object = requests.getJSONObject(i);
                                String  id=object.getString("jobId");
                                String jobTitle=object.getString("jobTitle");
                                String jobContact=object.getString("jobContact");
                                String payment=object.getString("jobPayment");
                                String farmer_id=object.getString("farmer_id");
                                String description=object.getString("jobDescription");

                                items.add(new JobObject(id,jobTitle,farmer_id,jobContact,description,payment));

                            }

                            mArrayAdapter1 = new ArrayAdapter<JobObject>(getActivity(),R.layout.list_content, R.id.label, items);
                            listView.setAdapter(mArrayAdapter1);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonRequest);
    }
    private void getLands() {
        final String GET_JOB_URL=CONSTANTS.IPADDRESS+"/farmvisor/getLands/"+farmId;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,GET_JOB_URL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray requests = response.getJSONArray("lands");

                            for (int i =0; i<requests.length(); i++){
                                JSONObject object = requests.getJSONObject(i);
                                String  id=object.getString("id");
                                String area_size=object.getString("area_size");
                                String contact=object.getString("contact");
                                String soil_type=object.getString("soil_type");
                                String farmer_id=object.getString("farmer_id");
                                String description=object.getString("description");
                                String landmark=object.getString("land_mark");
                                String cost_year=object.getString("cost_year");
                                String years=object.getString("years");
                                String coords=object.getString("coords");


                                items2.add(new LandObject(id,farmer_id,area_size,soil_type,landmark,cost_year,years,contact,description,coords));

                            }

                            mArrayAdapter2 = new ArrayAdapter<LandObject>(getActivity(),R.layout.list_content, R.id.label, items2);
                            listView.setAdapter(mArrayAdapter2);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonRequest);
    }
    private void getInvests() {
        final String GET_JOB_URL=CONSTANTS.IPADDRESS+"/farmvisor/getInvests/"+farmId;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,GET_JOB_URL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray requests = response.getJSONArray("invests");

                            for (int i =0; i<requests.length(); i++){
                                JSONObject object = requests.getJSONObject(i);
                                String  id=object.getString("id");
                                String area_size=object.getString("area_size");
                                String contact=object.getString("contact");
                                String soil_type=object.getString("soil_type");
                                String farmer_id=object.getString("farmer_id");
                                String description=object.getString("description");
                                String landmark=object.getString("land_mark");
                                String cost_year=object.getString("investments");
                                String years=object.getString("years");

                                items4.add(new InvestObject(id,farmer_id,area_size,soil_type,landmark,cost_year,years,contact,description));

                            }

                            mArrayAdapter4 = new ArrayAdapter<InvestObject>(getActivity(),R.layout.list_content, R.id.label, items4);
                            listView.setAdapter(mArrayAdapter4);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonRequest);
    }
    private void getEquipments() {
        final String GET_JOB_URL=CONSTANTS.IPADDRESS+"/farmvisor/getEquipments/"+farmId;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,GET_JOB_URL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray requests = response.getJSONArray("equipment");

                            for (int i =0; i<requests.length(); i++){
                                JSONObject object = requests.getJSONObject(i);
                                String  id=object.getString("id");
                                String name=object.getString("name");
                                String contact=object.getString("contact");
                                String rent=object.getString("rent_price");
                                String farmer_id=object.getString("farmer_id");
                                String description=object.getString("description");
                                String model=object.getString("model");

                                items3.add(new EquipmentObject(id,name,farmer_id,model,rent,contact,description));

                            }

                            mArrayAdapter3 = new ArrayAdapter<EquipmentObject>(getActivity(),R.layout.list_element, R.id.label, items3);
                            listView.setAdapter(mArrayAdapter3);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonRequest);
    }


    private void getCropRequest() {

        final String GET_JOB_URL=CONSTANTS.IPADDRESS+"/farmvisor/getCropRequest/"+farmId;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,GET_JOB_URL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray requests = response.getJSONArray("crops");

                            for (int i =0; i<requests.length(); i++){
                                JSONObject object = requests.getJSONObject(i);
                                String  id=object.getString("id");
                                String name=object.getString("name");
                                String farmer_id=object.getString("farmer_id");
                                String description=object.getString("description");
                                String quantity=object.getString("quantity");

                                items5.add(new CropObject(id,farmer_id,name,description,quantity));

                            }

                            mArrayAdapter5 = new ArrayAdapter<CropObject>(getActivity(),R.layout.list_content, R.id.label, items5);
                            listView.setAdapter(mArrayAdapter5);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonRequest);
    }

    private void getAuctions(final String value_types) {
        String url;

        if (value_types.equals("current"))
            url = CONSTANTS.IPADDRESS+"/farmvisor/getAuctions/"+farmId;
        else if (value_types.equals("mybids"))
            url = CONSTANTS.IPADDRESS+"/farmvisor/getBids/"+farmId;
        else
            url = CONSTANTS.IPADDRESS+"/farmvisor/getMyCrops/"+farmId;
        final String GET_JOB_URL=url;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,GET_JOB_URL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(!value_types.equals("mybids")){


                                JSONArray requests = response.getJSONArray("crops");
                                Toast.makeText(getActivity(),requests.toString(),Toast.LENGTH_LONG).show();

                                for (int i =0; i<requests.length(); i++){
                                    JSONObject object = requests.getJSONObject(i);
                                    String  id=object.getString("cropId");
                                    String name=object.getString("cropName");
                                    String farmer_id=object.getString("farmer_id");
                                    String description=object.getString("cropDescription");
                                    String quantity=object.getString("cropQuantity");
//                                    String image=object.getString("image");
                                    String price=object.getString("cropPrice");
                                    String bid_date=object.getString("bid_date");

                                    items5.add(new CropObject(id,farmer_id,name,price,description,quantity,bid_date));

                                }
                                Toast.makeText(getActivity(),items5.size()+"",Toast.LENGTH_SHORT).show();

                                mArrayAdapter5 = new ArrayAdapter<CropObject>(getActivity(),R.layout.list_content, R.id.label, items5);
                                listView.setAdapter(mArrayAdapter5);
                            }
                            else{
                                JSONArray requests = response.getJSONArray("bids");

                                for (int i =0; i<requests.length(); i++){
                                    JSONObject object = requests.getJSONObject(i);
                                    String  id=object.getString("bidId");
                                    String bidDate=object.getString("bidDate");
                                    String bidPrice=object.getString("bidPrice");
                                    String crop=object.getString("crop");


                                    String  cropId=object.getString("id");
                                    String cropName=object.getString("cropName");
                                    String cropMinPrice=object.getString("cropPrice");
                                    String cropBidDate=object.getString("bid_date");
                                    String cropUrl=object.getString("cropUrl");
                                    String cropQuantity=object.getString("cropQuantity");

                                    items6.add(new BidObject(cropId,cropName,cropBidDate,cropMinPrice,cropUrl));



//                                    items6.add(new BidObject(id,bidDate,bidPrice,crop));

                                }

                                mArrayAdapter6 = new ArrayAdapter<BidObject>(getActivity(),R.layout.list_content, R.id.label, items6);
                                listView.setAdapter(mArrayAdapter6);

                            }
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonRequest);

    }


}
