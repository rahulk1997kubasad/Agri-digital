package project.farmvisor;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String landId, coords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        landId = getIntent().getStringExtra("id");
        coords = getIntent().getStringExtra("coords");

        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String[] latlngStrings = coords.trim()
                .split("#");
        Toast.makeText(this, Arrays.toString(latlngStrings),Toast.LENGTH_LONG).show();
        List<LatLng> latLngs = new ArrayList<>();
        for(String s:latlngStrings){
            String[] ltlg = s.split(",");

            latLngs.add(new LatLng(Double.valueOf(ltlg[0]),Double.valueOf(ltlg[1])));
        }
        mMap.addPolygon(new PolygonOptions()
                            .addAll(latLngs)
                            );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(0)));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 20.0f ) );
    }
}