package project.farmvisor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guruvyasa on 12/3/18.
 */

public class BidListAdapter extends ArrayAdapter<BidObject> implements View.OnClickListener{

    private String farmerId;
    private final String mode;
    private List<BidObject> dataSet;
    Context mContext;

    @Override
    public void onClick(View view) {

    }

    // View lookup cache
    private static class ViewHolder {
        TextView cropName;
        TextView bidPrice;
        TextView cropBidDate;
        TextView status;
        ImageView cropImage;
        Button payBtn;
    }

    public BidListAdapter(List<BidObject> data, Context context, String farmerId, String mode) {
        super(context, R.layout.bid_row, data);
        this.dataSet = data;
        this.mContext=context;
        this.farmerId = farmerId;

        this.mode = mode;

    }

    public void saveBid(){

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }


    public void pay(BidObject dModel) {
        final BidObject dataModel = dModel;
        Toast.makeText(mContext, dataModel.getBidPrice()+"",Toast.LENGTH_SHORT).show();
//
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View dialogView = inflater.inflate(R.layout.bidding_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView edt =  dialogView.findViewById(R.id.bidTextView);
        edt.setVisibility(View.GONE);

        dialogBuilder.setTitle("Initiating Payment");
        dialogBuilder.setMessage("Enter account number");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AndroidNetworking.post(CONSTANTS.IPADDRESS+ CONSTANTS.make_payment)
                        .addBodyParameter("farmer_id",farmerId)
                        .addBodyParameter("bid_price",dataModel.getBidPrice())
                        .addBodyParameter("bid_id",dataModel.getId())
//                .addBodyParameter("farmer_phone",dataModel.getFarmerPhone())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Toast.makeText(mContext,response.getString("status"),Toast.LENGTH_SHORT).show();
                                    if(response.getString("status").equals("success")) {
//                                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
//                                alertDialog.setTitle("Payment Initiated");
//                                alertDialog.setMessage("Thank You for your interest");
//                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//                                alertDialog.show();
                                    }
//                                startActivity(new Intent(getApplicationContext(), PatientLandingActivity.class));
//                                     startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(mContext,anError.toString(),Toast.LENGTH_LONG).show();
                            }
                        });


                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        dialogBuilder.show();


//        Toast.makeText(mContext,dataModel.getBidPrice(),Toast.LENGTH_SHORT).show();

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final BidObject dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.bid_row, parent, false);
            viewHolder.cropName = (TextView) convertView.findViewById(R.id.crop_name);
            viewHolder.bidPrice = (TextView) convertView.findViewById(R.id.bid_price);
            viewHolder.cropBidDate = (TextView) convertView.findViewById(R.id.bid_date);
            viewHolder.cropImage = (ImageView) convertView.findViewById(R.id.crop_image);
            viewHolder.status = (TextView) convertView.findViewById(R.id.bid_status);
            viewHolder.payBtn =  convertView.findViewById(R.id.payBtn);
            viewHolder.payBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext,"Clicked",Toast.LENGTH_SHORT).show();
                    pay(dataModel);
                }
            });

            if(dataModel.getStatus().equalsIgnoreCase("accepted")){
                viewHolder.payBtn.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.payBtn.setVisibility(View.GONE);

            }
            result=convertView;

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.cropName.setText(dataModel.getName());
        viewHolder.bidPrice.setText(dataModel.getBidPrice());
        viewHolder.cropBidDate.setText(dataModel.getBidDate());
        viewHolder.status.setText(dataModel.getStatus());

//        viewHolder.makeBidButton.setOnClickListener(this);
//        viewHolder.makeBidButton.setTag(position);
        Picasso.with(mContext).load(CONSTANTS.IPADDRESS+"/getImage/"+dataModel.getCrop()).into(viewHolder.cropImage);
        // Return the completed view to render on screen
        return convertView;
    }
}