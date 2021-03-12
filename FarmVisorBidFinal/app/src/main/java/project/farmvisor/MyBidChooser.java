package project.farmvisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MyBidChooser extends AppCompatActivity {
    String language;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bid_chooser);
        if(MyApplication.userType.equals("trader")){
            findViewById(R.id.btn_my_crops).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.btn_my_bids).setVisibility(View.GONE);
        }

    }

    public void showMyCrops(View view) {
        Intent i = new Intent(this,ShowCropAuctionsActivity.class);
        i.putExtra("farmer_id",getIntent().getExtras().getString("farmer_id"));
        i.putExtra("mode","crops");

        startActivity(i);
    }

    public void showMyBids(View view) {
        Intent i = new Intent(this,ShowCropAuctionsActivity.class);
        i.putExtra("farmer_id",getIntent().getExtras().getString("farmer_id"));
        i.putExtra("mode","bids");

        startActivity(i);

    }
}
