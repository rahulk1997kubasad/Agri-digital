package project.farmvisor;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by guruvyasa on 12/3/18.
 */

public class CropListAdapter extends ArrayAdapter<BidObject> implements View.OnClickListener{

    private String farmerId,mode;
    private List<BidObject> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView cropName;
        TextView cropMinPrice;
        TextView cropBidDate;
        ImageView cropImage;
        Button viewBidButton;

    }

    public CropListAdapter(List<BidObject> data, Context context, String farmerId) {
        super(context,R.layout.crop_row,data);
        this.dataSet = data;
        this.mContext=context;
        this.farmerId = farmerId;
        this.mode = "crops";
        Log.d("CropListAdapter", "CropListAdapter: "+data.size());


    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public BidObject getItem(int position) {
        return dataSet.get(position);
    }


    @Override
    public void onClick(View v) {

        final int position=(Integer) v.getTag();
        Object object= getItem(position);

        final BidObject dataModel=(BidObject)object;
            Intent i = new Intent(mContext,ShowCropBiddings.class);
            i.putExtra("farmer_id",farmerId);
            i.putExtra("crop_id",dataModel.getId());
            mContext.startActivity(i);
            return;


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BidObject dataModel = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;
        Log.d("getView", "getView: "+dataModel.getCropName());
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.crop_row, parent, false);
            viewHolder.cropName = (TextView) convertView.findViewById(R.id.crop_name);
            viewHolder.cropMinPrice = (TextView) convertView.findViewById(R.id.crop_min_price);
            viewHolder.cropBidDate = (TextView) convertView.findViewById(R.id.crop_bid_date);
            viewHolder.cropImage = (ImageView) convertView.findViewById(R.id.crop_image);
            viewHolder.viewBidButton = (Button)convertView.findViewById(R.id.btn_view_bid);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);

        viewHolder.cropName.setText(dataModel.getName());
        viewHolder.cropMinPrice.setText(dataModel.getBidPrice());
        viewHolder.cropBidDate.setText(dataModel.getBidDate());
        viewHolder.viewBidButton.setOnClickListener(this);
        viewHolder.viewBidButton.setTag(position);
        Picasso.with(mContext).load(CONSTANTS.IPADDRESS+"/getImage/"+dataModel.getCrop()).into(viewHolder.cropImage);
        // Return the completed view to render on screen
        return convertView;
    }



    @Override
    public long getItemId(int position) {
        return Long.parseLong(dataSet.get(position).getId());
    }
}