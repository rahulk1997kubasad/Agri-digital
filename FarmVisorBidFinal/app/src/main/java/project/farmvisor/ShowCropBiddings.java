package project.farmvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowCropBiddings extends AppCompatActivity implements CropBidListAdapter.BidAcceptListener {
    Double s =0.0;

    String farmerId,cropId;
    TextView maxBid,minBid,meanBid;
    public List<BidObject> bids = new ArrayList<>();
    private CropBidListAdapter mBidAdapter;
    ListView listView;
    private boolean bidAccepted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_crop_biddings);
        listView=(ListView)findViewById(R.id.bids_list);
        maxBid = findViewById(R.id.maxBid);
        minBid = findViewById(R.id.minBid);
        meanBid = findViewById(R.id.meanBid);

        farmerId = getIntent().getExtras().getString("farmer_id");
        cropId = getIntent().getExtras().getString("crop_id");

        getBidsForCrop(cropId);
    }
    public void getBidsForCrop(String cropId){
        bids = new ArrayList<>();
        final String GET_JOB_URL=CONSTANTS.IPADDRESS+"/farmvisor/getBidsForCrop/"+cropId;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,GET_JOB_URL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                                JSONArray requests = response.getJSONArray("bids");
                                Toast.makeText(ShowCropBiddings.this, requests.toString(), Toast.LENGTH_LONG).show();

                                for (int i = 0; i < requests.length(); i++) {
                                    JSONObject object = requests.getJSONObject(i);
                                    String id = object.getString("bidId");

                                    String name = object.getString("farmerName");
                                    String phone = object.getString("farmerPhone");
                                    String status = object.getString("status");
//                                    String image=object.getString("image");
                                    String cropPrice = object.getString("cropPrice");
                                    String bidPrice = object.getString("bidPrice");
                                    s += Double.valueOf(bidPrice);

                                    String bidDate = object.getString("bidDate");

                                    bids.add(new BidObject(id, bidDate, bidPrice, cropPrice,name,phone,status));

                                    if ("accepted".equalsIgnoreCase(status) || "paid".equalsIgnoreCase(status))
                                        bidAccepted = true;

                                }
                                meanBid.setText("Mean: "+(s/bids.size()));
                            maxBid.setText("Max: "+(bids.get(0).getBidPrice()));
                            minBid.setText("Min: "+(bids.get(bids.size()-1).getBidPrice()));

                                mBidAdapter = new CropBidListAdapter(bids,ShowCropBiddings.this,bidAccepted,ShowCropBiddings.this);
                                listView.setAdapter(mBidAdapter);
                            }
                            catch (Exception e){
                                System.out.println(e.toString());

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonRequest);
    }

    @Override
    public void bidAccepted(BidObject bidObject) {
        acceptBid(bidObject.getId());
    }

    private void acceptBid(final String id) {
        String API= CONSTANTS.IPADDRESS+"/farmvisor/accept_bid";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.has("status"))
                            {
                             String status = jsonObject.getString("status");
                             if ("success".equalsIgnoreCase(status))
                             {
                                 getBidsForCrop(cropId);
                             }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("bid_id",id );
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ShowCropBiddings.this);
        requestQueue.add(stringRequest);
    }
}
