package project.farmvisor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class ShowCropAuctionsActivity extends AppCompatActivity {
    private List<BidObject> currentAuctions;
    private AuctionListAdapter mAdapter;
    private CropListAdapter mCAdapter;
    private BidListAdapter mBidAdapter;

    private ListView listView;
    private String farmerId,mode;
    private String url = "/farmvisor/getAuctions/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        farmerId = this.getIntent().getStringExtra("farmer_id");
        mode = getIntent().getExtras().getString("mode","auctions");
        setContentView(R.layout.activity_show_crop_auctions);
        listView = findViewById(R.id.auctions_list);
        currentAuctions = new ArrayList<>();

        if (mode.equals("auctions")) {
//            setContentView(R.layout.activity_show_crop_auctions);
            getRequests();
        }
        else if(mode.equals("crops")){
            url = "/farmvisor/getMyCrops/";
            getRequests();
        }
        else{
            url="/farmvisor/getBids/";
            getRequests();

        }
    }

    private void getRequests() {

        final String Api=CONSTANTS.IPADDRESS+url+farmerId;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,Api , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            JSONArray requests = response.getJSONArray(mode);
                            currentAuctions = new ArrayList<>();
                            for (int i =0; i<requests.length(); i++){
                                JSONObject object = requests.getJSONObject(i);
                                if(mode.equals("bids")){

                                    String  id=object.getString("bidId");
                                    String cropName=object.getString("cropName");
                                    String cropMinPrice=object.getString("bidPrice");
                                    String cropBidDate=object.getString("bidDate");
                                    String cropUrl=object.getString("cropUrl");
                                    String status=object.getString("status");
                                    if (status.equalsIgnoreCase("accepted"))
                                        ;
                                    currentAuctions.add(new BidObject(id,cropName,cropBidDate,cropMinPrice,cropUrl,status));


                                }
                                else {

                                    String id = object.getString("cropId");
                                    String cropName = object.getString("cropName");
                                    String cropMinPrice = object.getString("cropPrice");
                                    String cropBidDate = object.getString("bid_date");
                                    String cropUrl = object.getString("cropUrl");
                                    String cropQuantity = object.getString("cropQuantity");
                                    currentAuctions.add(new BidObject(id,cropName,cropBidDate,cropMinPrice,cropUrl));
                                }

                            }

                            if (mode.equals("bids"))
                            {
//                                Toast.makeText(ShowCropAuctionsActivity.this, currentAuctions.toString(), Toast.LENGTH_SHORT).show();
                                mBidAdapter = new BidListAdapter(currentAuctions,ShowCropAuctionsActivity.this,farmerId,mode);
                                listView.setAdapter(mBidAdapter);
                            }else if(mode.equals("crops"))
                            {
                                mCAdapter = new CropListAdapter(currentAuctions, ShowCropAuctionsActivity.this, farmerId);
                                listView.setAdapter(mCAdapter);
                            }
                            else
                            {
                                mAdapter = new AuctionListAdapter(currentAuctions, ShowCropAuctionsActivity.this, farmerId, mode);
                                listView.setAdapter(mAdapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonRequest);
    }
}

