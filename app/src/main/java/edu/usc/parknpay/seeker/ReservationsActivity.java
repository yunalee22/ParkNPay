package edu.usc.parknpay.seeker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpotPost;

public class ReservationsActivity extends TemplateActivity {

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private ListView reservationsList;
    private ArrayList<ParkingSpotPost> reservations;
    private ReservationsListAdapter reservationsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_reservations);
        super.onCreateDrawer();
        toolbarSetup();

        // Get references to UI views
        reservationsList = (ListView) findViewById(R.id.reservations_list);

        // Add adapter to ListView
        reservations = new ArrayList<ParkingSpotPost>();
        reservationsListAdapter = new ReservationsListAdapter(ReservationsActivity.this, reservations);
        reservationsList.setAdapter(reservationsListAdapter);
    }
    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
    // Call this function to update the reservations view
    private void loadReservations(ArrayList<ParkingSpotPost> reservations) {

        reservationsListAdapter.clear();
        reservationsListAdapter.addAll(reservations);
        reservationsListAdapter.notifyDataSetChanged();
    }

    protected class ReservationsListAdapter extends ArrayAdapter<ParkingSpotPost> {

        public ReservationsListAdapter(Context context, ArrayList<ParkingSpotPost> results) {
            super(context, 0, results);
        }

        // View lookup cache
        private class ViewHolder {
            ImageView parkingSpotImage;
            TextView address;
            TextView dateTimeRange;
            ImageView callButton;
            ImageView deleteButton;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Get data item
            ParkingSpotPost parkingSpotPost = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            ReservationsActivity.ReservationsListAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ReservationsActivity.ReservationsListAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.seeker_reservation_item, parent, false);
                viewHolder.parkingSpotImage = (ImageView) findViewById(R.id.parking_spot_image);
                viewHolder.address = (TextView) findViewById(R.id.address);
                viewHolder.dateTimeRange = (TextView) findViewById(R.id.date_time_range);
                viewHolder.callButton = (ImageView) findViewById(R.id.call_button);
                viewHolder.deleteButton = (ImageView) findViewById(R.id.delete_button);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = ( ReservationsActivity.ReservationsListAdapter.ViewHolder) convertView.getTag();
            }

            // Populate data into the views
            //viewHolder.parkingSpotImage.setImageBitmap(parkingSpotPost.get);
            //viewHolder.address.setText(parkingSpotPost.get);
            //viewHolder.dateTimeRange.setText(parkingSpotPost.get);

            // Get owner's phone number
            final String ownerPhoneNumber = "4087717264";

            // Called when call button is pressed
            viewHolder.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(ReservationsActivity.this,
                            Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED ) {

                        ActivityCompat.requestPermissions(ReservationsActivity.this,
                                new String[] {android.Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }

                    if (ContextCompat.checkSelfPermission(ReservationsActivity.this,
                            android.Manifest.permission.CALL_PHONE ) == PackageManager.PERMISSION_GRANTED) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + ownerPhoneNumber));
                        startActivity(callIntent);
                    }
                }
            });

            // Called when delete button is pressed
            final int positionToRemove = position;
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Delete reservation from database, add spot back to available/searchable spots



                    // Remove reservation from ListView
                    reservations.remove(positionToRemove);
                    reservationsListAdapter.notifyDataSetChanged();

                }
            });

            // Return completed view
            return convertView;
        }
    }
}
