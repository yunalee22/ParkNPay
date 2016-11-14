package edu.usc.parknpay.owner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.ParkingSpotPost;

/**
 * Created by Bobo on 10/31/2016.
 */

public class AddAvailabilityAdapter extends ArrayAdapter<ParkingSpotPost> {

    public AddAvailabilityAdapter(Context context, ArrayList<ParkingSpotPost> avails) {
        super(context, 0, avails);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.owner_add_avail_item, parent, false);
        }

        ParkingSpotPost parkingSpotPost = getItem(position);

        // Date TextViews
        TextView startDate = (TextView) convertView.findViewById(R.id.start);
        TextView endDate = (TextView) convertView.findViewById(R.id.end);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView cancel = (TextView) convertView.findViewById(R.id.cancellationPolicy);

        // Set the text
        //"yyyy-MM-dd'T'HH:mmZ"
        String startTime, temp1 = parkingSpotPost.getStartTime();
        String endTime, temp2 = parkingSpotPost.getEndTime();
        startTime = temp1.substring(5,7) + "/" + temp1.substring(8, 10) + "/" + temp1.substring(0,4)+ "    " + temp1.substring(11, 16);
        endTime = temp2.substring(5,7) + "/" + temp2.substring(8, 10) + "/" + temp2.substring(0,4)+ "    " + temp2.substring(11, 16);
        startDate.setText(startTime);
        cancel.setText(parkingSpotPost.getCancellationPolicy());
        endDate.setText(endTime);
        DecimalFormat df = new DecimalFormat("#.00");
        price.setText("$" + df.format(parkingSpotPost.getPrice()));

        // If not reserved, hide the reserved tag
        TextView reserved = (TextView) convertView.findViewById(R.id.reserved);
        if (!parkingSpotPost.isReserved()) {
            reserved.setVisibility(View.GONE);
        }

        return convertView;
    }
}
