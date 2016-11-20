package edu.usc.parknpay.seeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.Review;

/**
 * Created by yunalee on 11/20/16.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Context context, ArrayList<Review> results) {
        super(context, 0, results);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.seeker_review_item, parent, false);
        }
        // Get data item
        Review r = getItem(position);

        // Set image
        ImageView spotImageView = (ImageView) convertView.findViewById(R.id.seeker_photo);
        spotImageView.setImageResource(0);
        Picasso.with(getContext())
                .load(r.getSeekerProfilePhotoURL())
                .placeholder(R.drawable.progress_animation)
                .resize(150, 150)
                .centerCrop()
                .into(spotImageView);

        // Set Address
        TextView Date = (TextView) convertView.findViewById(R.id.date);
        Date.setText(r.getDate());

        // Set comments
        TextView reviewText = (TextView) convertView.findViewById(R.id.review);
        reviewText.setText(r.getComments());

        // Set review
        TextView seekerText = (TextView) convertView.findViewById(R.id.seeker_name);
        seekerText.setText(r.getSeekerName());

        RatingBar rate = (RatingBar) convertView.findViewById(R.id.ratingBar2);
        rate.setRating((int) r.getRating());

        return convertView;
    }
}