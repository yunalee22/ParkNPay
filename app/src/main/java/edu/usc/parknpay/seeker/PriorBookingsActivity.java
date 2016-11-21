package edu.usc.parknpay.seeker;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import edu.usc.parknpay.R;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.utility.TemplateActivity;
import edu.usc.parknpay.utility.Utility;
public class PriorBookingsActivity extends TemplateActivity {
    private ListView priorBookingsList;
    private ArrayList<ParkingSpot> priorBookings;
    private PriorBookingsAdapter priorBookingsAdapter;
    // Long and Lat used for Adapter
    private double adapterLongitude;
    private double adapterLatitude;
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
        addListeners();
        System.out.println("~~~~~~~~~~~TESTING CREATE~~~~~~~~~~~~~");
    }
    private void addListeners() {
        // Called when user types into search field
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~PLACE SELECTEDDDDDDDDD");
                Log.i(LOG_TAG, "Place Selected: " + place.getName());
                List<Address> addressInfo;
                // First parse the lat and lng from the place they selected
                try {
                    String address = place.getName().toString();
                    addressInfo = coder.getFromLocationName(address, 5);
                    Address location = addressInfo.get(0);
                    // Store into variables that are used later
                    adapterLatitude = location.getLatitude();
                    adapterLongitude = location.getLongitude();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Unconditional search to get all parking spots
                DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
                Ref.child("Parking-Spots").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> spots = (Map<String,Object>)dataSnapshot.getValue();
                        if (spots == null) {return;}
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            // Compare to see if the distance is within one mile
                            ParkingSpot spot = snapshot.getValue(ParkingSpot.class);
                            double distance = Utility.distance(adapterLatitude, adapterLongitude, spot.getLatitude(), spot.getLongitude(), "M");
                            // If within one mile
                            if (distance <= 1) {
                                System.out.println(spot);
                                priorBookings.add(spot);
                                priorBookingsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
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

            int numRes = parkingSpot.getNumReserved();

            // Fill in list item view with data
            RelativeLayout back = (RelativeLayout)convertView.findViewById(R.id.layout);

            //the background color
            if(numRes >= 10)
                back.setBackgroundColor(Color.parseColor("#9999ff"));
            else if (numRes >= 5)
                back.setBackgroundColor(Color.parseColor("#ccccff"));
            else if (numRes >=1)
                back.setBackgroundColor(Color.parseColor("#e5e5ff"));


            ImageView image = (ImageView) convertView.findViewById(R.id.image);
            TextView address = (TextView) convertView.findViewById(R.id.address);
            TextView size = (TextView) convertView.findViewById(R.id.size);
            TextView numResLabel = (TextView) convertView.findViewById(R.id.numRes);
            TextView handicap = (TextView) convertView.findViewById(R.id.handicap);
            TextView distance = (TextView) convertView.findViewById(R.id.distance);

            Picasso.with(getContext())
                    .load(parkingSpot.getPhotoURL())
                    .placeholder(R.drawable.progress_animation)
                    .resize(150, 150)
                    .centerCrop()
                    .into(image);

            address.setText(parkingSpot.getAddress());
            size.setText(parkingSpot.getSize());
            numResLabel.setText(String.valueOf(parkingSpot.getNumReserved()) + " res.");
            // Set handicap (hide if not handicap)
            if (!parkingSpot.isHandicap())
            {
                handicap.setVisibility(View.GONE);
            }
            else {
                handicap.setText("Handicap");
            }
            DecimalFormat df = new DecimalFormat("0.00");

            // Set distance
            distance.setText(
                    df.format(Utility.distance(parkingSpot.getLatitude(),
                            parkingSpot.getLongitude(),
                            adapterLatitude,
                            adapterLongitude,
                            "M"))
                            + " mi"
            );
            return convertView;
        }
    }
}