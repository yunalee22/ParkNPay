package edu.usc.parknpay.seeker;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.utility.Utility;

public class SeekerMainActivity extends TemplateActivity {

    public static final double RADIUS_LIMIT = 3;
    private static final int SEARCH_FILTER = 2;

    // Filter button
    private ImageView filterButton;

    // Search autocomplete text field
    private PlaceAutocompleteFragment autocompleteFragment;
    private Geocoder coder;
    private static final String LOG_TAG = "PlaceSelectionListener";

    // Search results list view
    private ListView searchList;
    private ArrayList<ParkingSpot> searchResults;
    private SearchListAdapter searchResultsAdapter;

    // Search parameters
    private double latitude, longitude, minPrice, maxPrice;
    private float minOwnerRating, minSpotRating, size;
    private boolean handicapOnly, showNormal, showCompact, showSuv, showTruck;
    private String address, startDate, startTime, endDate, endTime;

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
        searchResults = new ArrayList<ParkingSpot>();
        searchResultsAdapter = new SearchListAdapter(SeekerMainActivity.this, searchResults);
        searchList.setAdapter(searchResultsAdapter);

        // Add view listeners
        addListeners();
    }

    private void executeSearch() {

        searchResults.clear();

        System.out.println("Executing search: " + address + " at (" + latitude + ", " + longitude + ")");

        // TODO: YUNA: Set these variables as well as filters?
        final double sLatitude = 34.0224;
        final double sLongitude = 118.2851;

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date date = sdf.parse(startDate + " " + startTime);
            final String sStartTime = df.format(date);
            date = sdf.parse(endDate + " " + endTime);
            final String sEndTime = df.format(date);
            System.out.println("DATE: " + sStartTime);
            System.out.println("END TIME: " + sEndTime);

            // TODO: Show error popup if bad input?
            if(startTime.compareTo(endTime) > 0 || startDate.compareTo(endDate) > 0) {
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

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    // Call this function to update the search results view
    private void loadSearchResults(ArrayList<ParkingSpot> parkingSpots) {

        searchResultsAdapter.clear();
        searchResultsAdapter.addAll(parkingSpots);
        searchResultsAdapter.notifyDataSetChanged();
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
                    address = place.getName().toString();
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    executeSearch();
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
            executeSearch();
        }
    }

    protected class SearchListAdapter extends ArrayAdapter<ParkingSpot> {

        public SearchListAdapter(Context context, ArrayList<ParkingSpot> results) {
            super(context, 0, results);
        }

        // View lookup cache
        private class ViewHolder {
            ImageView image;
            TextView address;
            TextView dateTimeRange;
            TextView size;
            TextView handicap;
            TextView price;
            TextView distance;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Get data item
            ParkingSpot parkingSpot = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.seeker_search_list_item, parent, false);
                viewHolder.image = (ImageView) findViewById(R.id.image);
                viewHolder.address = (TextView) findViewById(R.id.address);
                viewHolder.dateTimeRange = (TextView) findViewById(R.id.date_time_range);
                viewHolder.size = (TextView) findViewById(R.id.size);
                viewHolder.handicap = (TextView) findViewById(R.id.handicap);
                viewHolder.price = (TextView) findViewById(R.id.price);
                viewHolder.distance = (TextView) findViewById(R.id.distance);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Populate data into the views
            //viewHolder.image.setImageBitmap(parkinigSpot.get);       // Need the image bitmap in ParkingSpot class
            viewHolder.address.setText(parkingSpot.getAddress());
            //viewHolder.dateTimeRange.setText(parkingSpot.get);       // Need date time range
            viewHolder.size.setText(parkingSpot.getSize());
            if (!parkingSpot.isHandicapped()) {
                viewHolder.handicap.setVisibility(View.INVISIBLE);
            }
            //viewHolder.price.setText(parkingSpot.get);               // Need spot price
            //viewHolder.distance.setText();                           // Need distance from entered location

            // Return completed view
            return convertView;
        }

    }
}
