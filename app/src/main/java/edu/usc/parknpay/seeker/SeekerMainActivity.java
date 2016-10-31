package edu.usc.parknpay.seeker;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.utility.Utility;

public class SeekerMainActivity extends TemplateActivity {

    public static final double RADIUS_LIMIT = 3;
    private static final int SEARCH_FILTER = 2;

    // Long and Lat used for Adapter
    private double adapterLongitude;
    private double adapterLatitude;

    // Filter button
    private ImageView filterButton;

    // Search autocomplete text field
    private PlaceAutocompleteFragment autocompleteFragment;
    private Geocoder coder;
    private static final String LOG_TAG = "PlaceSelectionListener";

    // Search results list view
    private ListView searchList;
    private ArrayList<ParkingSpotPost> searchResults;
    private SearchListAdapter searchResultsAdapter;

    // Search parameters
    private double minPrice, maxPrice;
    private float minOwnerRating, minSpotRating;
    private boolean handicapOnly;
    private boolean showNormal, showCompact, showSuv, showTruck;
    private String startDate, startTime, endDate, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_main);
        super.onCreateDrawer();

        // Get references to UI views
        filterButton = (ImageView) findViewById(R.id.filter_button);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        searchList = (ListView) findViewById(R.id.search_list);

        // Get current date and time
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(today);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(today);

        // Set default search parameters
        minPrice = 0;
        maxPrice = 100000000;                   // What is the max price?????
        minOwnerRating = 0.0f;
        minSpotRating = 0.0f;
        handicapOnly = false;
        showNormal = true;
        showCompact = true;
        showSuv = true;
        showTruck = true;
        startDate = date;
        startTime = time;
        endDate = date;
        endTime = "23:59";

        // Add search text field and geocoder
        coder = new Geocoder(this);
        autocompleteFragment.setHint("Enter an address");
        autocompleteFragment.setBoundsBias(new LatLngBounds(
            new LatLng(34.0224, -118.2851),
            new LatLng(34.0224, -118.2851)
        ));

        // Add adapter to ListView
        searchResults = new ArrayList<ParkingSpotPost>();
        searchResultsAdapter = new SearchListAdapter(SeekerMainActivity.this, searchResults);
        searchList.setAdapter(searchResultsAdapter);

        // Add view listeners
        addListeners();
    }


    private void executeSearch(String address, final double latitude, final double longitude) {

        searchResults.clear();

        System.out.println("Executing search: " + address + " at (" + latitude + ", " + longitude + ")");

        // TODO: YUNA: Set these variables as well as filters?
        final double sLatitude = 34.0224;
        final double sLongitude = 118.2851;
        final String sStartTime = "1997-07-16T19:20+01:00";
        final String sEndTime = "1997-07-16T19:25+01:00";
        // filters?

        // TODO: Show error popup if bad input?
        if(sStartTime.compareTo(sEndTime) > 0) {
            System.out.println("Start time is later than end time");
        }

        DatabaseReference browseRef = FirebaseDatabase.getInstance().getReference().child("Browse/");
        browseRef.orderByChild("startTime").equalTo(sStartTime).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                ParkingSpotPost post = dataSnapshot.getValue(ParkingSpotPost.class);
                // check end time
                System.out.println("Found with start time");

                if(sEndTime.compareTo(post.getEndTime()) <= 0) {
                    // check distance
                    System.out.println("Found with end time");
                    // TODO: change the first two parameters when the maps is fixed (longitude is negative?)
                    double distance = Utility.distance(sLatitude, sLongitude, post.getLatitude(), post.getLongitude(), "M");
                    if(distance < RADIUS_LIMIT) {
                        // TODO: check filters
                        // if(insert filters)
                            // display dynamically
                        System.out.println("SPOT WITHIN 3 MILES: " + post.toString());
                    }
                }
            }
            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
            });
    }

    private void addListeners() {

        // Called when search filter button is pressed
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SeekerMainActivity.this, SearchFilterActivity.class);

                // Add initial data to intent
                intent.putExtra("minPrice", minPrice);
                System.out.println("Added a parameter min price of " + minPrice);
                intent.putExtra("maxPrice", maxPrice);
                intent.putExtra("minOwnerRating", minOwnerRating);
                intent.putExtra("minSpotRating", minSpotRating);
                intent.putExtra("handicapOnly", handicapOnly);
                intent.putExtra("showNormal", showNormal);
                intent.putExtra("showCompact", showCompact);
                intent.putExtra("showSuv", showSuv);
                intent.putExtra("showTruck", showTruck);
                intent.putExtra("startDate", startDate);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endDate", endDate);
                intent.putExtra("endTime", endTime);

                startActivityForResult(intent, SEARCH_FILTER);
            }
        });

        // Called when user types into search field
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.i(LOG_TAG, "Place Selected: " + place.getName());
                List<Address> addressInfo;
                String address = place.getName().toString();

                try {
                    addressInfo = coder.getFromLocationName(address, 5);
                    Address location = addressInfo.get(0);
                    // Store into variables that are used later
                    adapterLatitude = location.getLatitude();
                    adapterLongitude = location.getLongitude();
                    executeSearch(address, adapterLatitude, adapterLongitude);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SEARCH_FILTER && resultCode == RESULT_OK && data != null) {
            // Update search parameters
            minPrice = data.getDoubleExtra("minPrice", 0);
            System.out.println("Min price is updated to " + minPrice);
            maxPrice = data.getDoubleExtra("maxPrice", 100000000);                   // What is the max price?????
            minOwnerRating = data.getFloatExtra("minOwnerRating", 0.0f);
            minSpotRating = data.getFloatExtra("minSpotRating", 0.0f);
            handicapOnly = data.getBooleanExtra("handicapOnly", false);
            showNormal = data.getBooleanExtra("showNormal", true);
            showCompact = data.getBooleanExtra("showCompact", true);
            showSuv = data.getBooleanExtra("showSuv", true);
            showTruck = data.getBooleanExtra("showTruck", true);
            startDate = data.getStringExtra("startDate");
            startTime = data.getStringExtra("startTime");
            endDate = data.getStringExtra("endDate");
            endTime = data.getStringExtra("endTime");
        }
    }

    protected class SearchListAdapter extends ArrayAdapter<ParkingSpotPost> {

        public SearchListAdapter(Context context, ArrayList<ParkingSpotPost> results) {
            super(context, 0, results);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.seeker_search_list_item, parent, false);
            }
            // Get data item
            ParkingSpotPost parkingSpotPost = getItem(position);

            // Set image
            ImageView spotImageView = (ImageView) convertView.findViewById(R.id.image);
            spotImageView.setImageResource(0);
//TODO: @tiff
//            Picasso.with(getContext())
//                    .load(/*TIFF ADD parkingSpotPost.getPhotoUrl HERE*/)
//                    .placeholder(R.drawable.progress_animation)
//                    .resize(150, 150)
//                    .centerCrop()
//                    .into(spotImageView);
            // Set Address
            TextView addrText = (TextView) convertView.findViewById(R.id.address);
            addrText.setText(parkingSpotPost.getAddress());

            // Set date/time
            TextView dateText = (TextView) convertView.findViewById(R.id.date_time_range);
            dateText.setText(parkingSpotPost.getStartTime() + " - " + parkingSpotPost.getEndTime());

            // Set Size
            TextView sizeText = (TextView) convertView.findViewById(R.id.size);
            sizeText.setText(parkingSpotPost.getSize());

            // Set Handicap (hide if not handicap)
            if (!parkingSpotPost.getIsHandicap())
            {
                TextView handiText = (TextView) convertView.findViewById(R.id.handicap);
                handiText.setVisibility(View.GONE);
            }

            // Set Price
            TextView priceText = (TextView) convertView.findViewById(R.id.price);
            priceText.setText("$" + parkingSpotPost.getPrice());

            TextView distText = (TextView) convertView.findViewById(R.id.distance);
            distText.setText(
                    Utility.distance(parkingSpotPost.getLatitude(),
                            parkingSpotPost.getLongitude(),
                            adapterLatitude,
                            adapterLongitude,
                            "M")
                    + "mi"
            );



            return convertView;
        }

    }
}
