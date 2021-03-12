package project.farmvisor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;

import java.sql.SQLException;

public class FarmerHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener{

    private String farmer_id,contact,name;
    DBHelper dbHelper;
    String language;
    private String provider;
    public FusedLocationProviderClient mFused;
    private LocationManager locationManager;
    public Double latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper=new DBHelper(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            language=dbHelper.getLanguage(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (language.equals("English")) {

        }
        else
        {

        }
        setContentView(R.layout.activity_farmer_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Bundle data=getIntent().getExtras();
        farmer_id=data.getString("farmer_id");
        contact=data.getString("contact");
        name=data.getString("name");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (language.equals("English")) {

        }
        else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.kactivity_farmer_home_drawer);
        }
        navigationView.setNavigationItemSelectedListener(this);
        hideItems(navigationView.getMenu());
        View headerView = navigationView.getHeaderView(0);
        TextView userName=(TextView)headerView.findViewById(R.id.user_name);
        userName.setText(name);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_former_home,HomeFragment.newInstance( farmer_id,contact, language))
                .commit();


        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            System.out.println("location not available");
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = (Double) (location.getLatitude());
        longitude = (Double) (location.getLongitude());

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_former_home,HomeFragment.newInstance( farmer_id,contact, language))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.farmer_home, menu);

        return true;
    }

    public boolean hideItems(Menu menu){
        if(MyApplication.userType.equals("trader")){
            menu.findItem(R.id.nav_crop).setVisible(false);
            menu.findItem(R.id.nav_share_equipments).setVisible(false);
            menu.findItem(R.id.nav_response).setVisible(false);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_former_home,HomeFragment.newInstance( farmer_id,contact,language))
                    .commit();

        }
//        else if (id == R.id.nav_inverstment) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_former_home, AddInvestFragment.newInstance( farmer_id,contact,language))
//                    .commit();
//
//        } else if (id == R.id.nav_jobpost) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_former_home, AddJobFragment.newInstance( farmer_id,contact,language))
//                    .commit();
//
//        } else if (id == R.id.nav_lease) {
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_former_home, AddLandFragment.newInstance( farmer_id,contact,language))
//                    .commit();
//
//        }
        else if (id == R.id.nav_share_equipments) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_former_home, AddEquipmentFragment.newInstance( farmer_id,contact,language))
                    .commit();

        }

        else if (id == R.id.nav_logout) {

           finish();

        }

 else if (id == R.id.nav_help) {

           callWebLinkActivity();

        }

        else if (id == R.id.nav_response) {

            Intent intent=new Intent(getApplicationContext(),RequestsListActivty.class);
            intent.putExtra("user_id",farmer_id);
            startActivity(intent);

        }

        else if (id == R.id.nav_crop) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_former_home, AddCropFragment.newInstance( farmer_id,contact,language))
                    .commit();


        }

        else if (id == R.id.nav_crop_request) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_former_home, CropRequestFragment.newInstance( farmer_id,contact,language))
                    .commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callWebLinkActivity() {
        startActivity(new Intent(this,WebLinkActivity.class));
    }


}
