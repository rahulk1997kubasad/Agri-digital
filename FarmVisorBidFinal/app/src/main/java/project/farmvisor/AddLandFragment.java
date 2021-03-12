package project.farmvisor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.farmvisor.CONSTANTS;
import project.farmvisor.R;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class AddLandFragment extends Fragment {
    private static final int REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE = 35;
    static String farmer_id, contact, lang;
    LatLng currentLocation = null;
    LocationRequest mLocationRequest;
//    public FusedLocationProviderClient mFused;
    private long UPDATE_INTERVAL = 1 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 1000; /* 2 sec */
    String area, soil, landmark, coords, description,cost,year;
    EditText areaIN, costIn,yearIn,soilIn, landmarkIn, descriptionIn;

    public AddLandFragment() {
        // Required empty public constructor
    }


    public static AddLandFragment newInstance(String farmer_idStr, String contactStr, String language) {
        AddLandFragment fragment = new AddLandFragment();

        farmer_id = farmer_idStr;
        contact = contactStr;
        lang = language;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLocationUpdates();
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }


    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }


    //    --------------------------------
//--------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        coords = "";
        if (lang.equals("English")) {
            root = inflater.inflate(R.layout.fragment_add_land, container, false);
        } else {
            root = inflater.inflate(R.layout.kfragment_add_land, container, false);
        }

        areaIN = (EditText) root.findViewById(R.id.land_area);
        soilIn = (EditText) root.findViewById(R.id.land_soil);
        landmarkIn = (EditText) root.findViewById(R.id.land_mark);
        descriptionIn = (EditText) root.findViewById(R.id.land_description);
        costIn=(EditText)root.findViewById(R.id.land_cost);
        yearIn=(EditText)root.findViewById(R.id.land_year);
        Button btn_add_land = (Button) root.findViewById(R.id.btn_add_land);
        Button btn_land_coords = (Button) root.findViewById(R.id.btn_land_coords);
        btn_land_coords.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(coords.length() == 0)
                    coords = currentLocation.latitude+","+currentLocation.longitude;
                else
                    coords += "#"+currentLocation.latitude+","+currentLocation.longitude;
                Toast.makeText(getActivity(),coords,Toast.LENGTH_LONG).show();

            }
        });
        btn_add_land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soil=soilIn.getText().toString().trim();
                area=areaIN.getText().toString().trim();
                landmark=landmarkIn.getText().toString().trim();
                description=descriptionIn.getText().toString().trim();
                cost=costIn.getText().toString().trim();
                year=yearIn.getText().toString().trim();
                if (area.equals("" ) || soil.equals("") || landmark.equals("") || coords.equals("") || description.equals(""))
                {
                    Toast.makeText(getActivity(),"All feilds are Mandatory!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (coords.trim().split("#").length < 4)
                    Toast.makeText(getActivity(),"Minimum 4 lat/long required",Toast.LENGTH_LONG).show();
                else
                {
                    addLand();
                }
            }
        });

        return root;
    }


    private void addLand() {

        String API= CONSTANTS.IPADDRESS+"/farmvisor/add_land";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                            JSONObject jsonObject=new JSONObject(response);
                            if( jsonObject.get("land").equals("added"))
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
                params.put("description",description);
                params.put("soil_type",soil);
                params.put("land_mark",landmark);
                params.put("coords",coords);
                params.put("cost_year",cost);
                params.put("contact",contact);

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
    }


}