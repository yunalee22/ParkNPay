package edu.usc.parknpay.seeker;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.utility.TemplateActivity;

public class PriorBookingsActivity extends TemplateActivity {

    private ListView priorBookingsList;
    private ArrayList<ParkingSpot> priorBookings;
    private PriorBookingsAdapter priorBookingsAdapter;

    private PlaceAutocompleteFragment autocompleteFragment;
    private Geocoder coder;
    private static final String LOG_TAG = "PlaceSelectionListener";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_prior_bookings);
        setUpToolbar("Prior Bookings");

        // Get references to UI views
        priorBookingsList = (ListView) findViewById(R.id.prior_bookings_list);

        // Add search text field and geocoder
        coder = new Geocoder(this);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Enter an address");
        autocompleteFragment.setBoundsBias(new LatLngBounds(
            new LatLng(34.0224, -118.2851),
            new LatLng(34.0224, -118.2851)
        ));

        // Add adapter to ListView
        priorBookings = new ArrayList<ParkingSpot>();
        priorBookingsAdapter = new PriorBookingsAdapter(PriorBookingsActivity.this, priorBookings);
        priorBookingsList.setAdapter(priorBookingsAdapter);
    }

    private void addListeners() {

        // Called when user types into search field
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.i(LOG_TAG, "Place Selected: " + place.getName());
                List<Address> addressInfo;

                try {
//                    address = place.getName().toString();
//                    addressInfo = coder.getFromLocationName(address, 5);
//                    Address location = addressInfo.get(0);
//
//                    // Store into variables that are used later
//                    adapterLatitude = location.getLatitude();
//                    adapterLongitude = location.getLongitude();
//                    executeSearch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
                Log.e(LOG_TAG, "onError: Status = " + status.toString());
            }
        });
    }

    protected class PriorBookingsAdapter extends ArrayAdapter<ParkingSpot> {

        public PriorBookingsAdapter(Context context, ArrayList<ParkingSpot> priorBookings) {
            super(context, 0, priorBookings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.seeker_prior_bookings_list_item, parent, false);
            }

            // Get data item
            ParkingSpot parkingSpot = getItem(position);

            // Fill in list item view with data
            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            TextView address = (TextView) convertView.findViewById(R.id.address);
            TextView date_time_range = (TextView) convertView.findViewById(R.id.date_time_range);
            TextView size = (TextView) convertView.findViewById(R.id.size);
            TextView handicap = (TextView) convertView.findViewById(R.id.handicap);
            TextView distance = (TextView) convertView.findViewById(R.id.distance);

            return convertView;
        }
    }
}