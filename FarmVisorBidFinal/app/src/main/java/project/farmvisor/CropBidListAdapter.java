package project.farmvisor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by guruvyasa on 12/3/18.
 */

public class CropBidListAdapter extends ArrayAdapter<BidObject> implements View.OnClickListener{

    private List<BidObject> dataSet;
    private Context mContext;
    private boolean bidAccepted;
    private BidAcceptListener listener;

    // View lookup cache
    private static class ViewHolder {
        TextView label;
        Button btnBidAccept;
    }

    CropBidListAdapter(List<BidObject> data, Context context, boolean bidAccepted,BidAcceptListener listener) {
        super(context, R.layout.list_content, data);
        this.dataSet = data;
        this.mContext=context;
        this.bidAccepted = bidAccepted;
        this.listener = listener;
    }

    private void acceptBid(BidObject dataModel){
     listener.bidAccepted(dataModel);
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        final BidObject dataModel = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_content, parent, false);
            viewHolder.label = convertView.findViewById(R.id.label);
            viewHolder.btnBidAccept = convertView.findViewById(R.id.btnBidAccept);
//            viewHolder.status = (TextView) convertView.findViewById(R.id.bid_status);

            viewHolder.label.setText(dataModel.toString());

            if (bidAccepted)
            {
                viewHolder.btnBidAccept.setVisibility(View.GONE);
            }
            viewHolder.btnBidAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptBid(dataModel);
                }
            });



        }
        return convertView;
    }

    public interface BidAcceptListener{

        void bidAccepted(BidObject bidObject);
    }
}