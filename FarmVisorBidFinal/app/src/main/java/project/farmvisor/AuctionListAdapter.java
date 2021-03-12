package project.farmvisor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

/**
 * Created by guruvyasa on 12/3/18.
 */

public class AuctionListAdapter extends ArrayAdapter<BidObject> implements View.OnClickListener{

    private String farmerId,mode;
    private List<BidObject> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView cropName;
        TextView cropMinPrice;
        TextView cropBidDate;
        Button makeBidButton;
        ImageView cropImage;
    }

    public AuctionListAdapter(List<BidObject> data, Context context, String farmerId, String mode) {
        super(context, R.layout.auction_row_layout, data);
        this.dataSet = data;
        this.mContext=context;
        this.farmerId = farmerId;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }


    @Override
    public void onClick(View v) {

        final int position=(Integer) v.getTag();
        Object object= getItem(position);

        final BidObject dataModel=(BidObject)object;
        if (mode.equals("crops")){
            Intent i = new Intent(mContext,ShowCropBiddings.class);
            i.putExtra("farmer_id",farmerId);
            i.putExtra("crop_id",dataModel.getId());
            mContext.startActivity(i);
            return;
        }

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            final View dialogView = inflater.inflate(R.layout.bidding_dialog, null);
            dialogBuilder.setView(dialogView);

            final EditText edt = (EditText) dialogView.findViewById(R.id.bid);

            dialogBuilder.setTitle("Bidding dialog");
            dialogBuilder.setMessage("Enter your bid below");
            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    double currentBid = new Double(edt.getText().toString());
                    if(new Double(dataModel.getBidPrice()) > currentBid){
                        Toast.makeText(mContext,edt.getText().toString()+" is less than min price " ,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AndroidNetworking.post(CONSTANTS.IPADDRESS+ CONSTANTS.save_bid)
                                .addBodyParameter("farmer_id",farmerId)
                                .addBodyParameter("bid_price",Double.toString(currentBid))
                                .addBodyParameter("crop_id",dataModel.getId())
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            Toast.makeText(mContext,response.getString("status"),Toast.LENGTH_SHORT).show();
                                            if(response.getString("status").equals("success")) {
                                                dataSet.remove(position);
                                                AuctionListAdapter.this.notifyDataSetChanged();
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

                    }
                    //do something with edt.getText().toString();
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();

//        Toast.makeText(mContext,dataModel.getBidPrice(),Toast.LENGTH_SHORT).show();

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BidObject dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.auction_row_layout, parent, false);
            viewHolder.cropName = (TextView) convertView.findViewById(R.id.crop_name);
            viewHolder.cropMinPrice = (TextView) convertView.findViewById(R.id.crop_min_price);
            viewHolder.cropBidDate = (TextView) convertView.findViewById(R.id.crop_bid_date);
            viewHolder.cropImage = (ImageView) convertView.findViewById(R.id.crop_image);
            viewHolder.makeBidButton = (Button)convertView.findViewById(R.id.btn_make_bid);

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
        viewHolder.cropMinPrice.setText(dataModel.getBidPrice());
        viewHolder.cropBidDate.setText(dataModel.getBidDate());
        viewHolder.makeBidButton.setOnClickListener(this);
        viewHolder.makeBidButton.setTag(position);
        Picasso.with(mContext).load(CONSTANTS.IPADDRESS+"/getImage/"+dataModel.getCrop()).into(viewHolder.cropImage);
        // Return the completed view to render on screen
        return convertView;
    }
}