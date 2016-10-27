package edu.usc.parknpay.owner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.ParkingSpot;


public class OwnerMainSpotAdapter extends ArrayAdapter<ParkingSpot> {

    public OwnerMainSpotAdapter(Context context, List<ParkingSpot> spots) {
        super(context, 0, spots);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.owner_main_spot, parent, false);
        }

        // Get the image view and clear it
        ImageView spotImageView = (ImageView) convertView.findViewById(R.id.spotImage);
        spotImageView.setImageResource(0);
        // Set the image view with url
        ParkingSpot spot = getItem(position);
        Picasso.with(getContext())
                .load(spot.getPhotoURL())
                .resize(450, 450)
                .centerCrop()
                .into(spotImageView);

        // Set the text
        TextView text = (TextView) convertView.findViewById(R.id.spotText);
        text.setText(spot.getAddress());

        return convertView;
    }
}
