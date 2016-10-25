package edu.usc.parknpay.seeker;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.utility.Utility;

public class SeekerMainActivity extends TemplateActivity {

    public static final double RADIUS_LIMIT = 3;

    // Filter button
    private ImageView filterButton;

    // Search autocomplete text field
    private PlaceAutocompleteFragment autocompleteFragment;
    private Geocoder coder;
    private static final String LOG_TAG = "PlaceSelectionListener";

    // Search results list view
    private ListView searchList;
    private List<ParkingSpotPost> searchResults;
    private ArrayAdapter<ParkingSpot> searchResultsAdapter;

    // Search parameters
    private double minPrice, maxPrice;
    private int minOwnerRating, minSpotRating;
    private boolean handicapOnly;
    private boolean showNormal, showCompact, showSuv, showTruck;
    private String startDate, startTime, endDate, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_main);

        // Get references to UI views
        filterButton = (ImageView) findViewById(R.id.filter_button);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        searchList = (ListView) findViewById(R.id.search_list);

        // Set default search parameters
        minPrice = 0;
        maxPrice = 100000000;                   // What is the max price?????
        minOwnerRating = 0;
        minSpotRating = 0;
        handicapOnly = false;
        showNormal = true;
        showCompact = true;
        showSuv = true;
        showTruck = true;
        startDate = null;                       // This should be today's date
        startTime = "00:00";
        endDate = null;                         // This should be today's date also
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
//        searchResultsAdapter = new ArrayAdapter<ParkingSpot>(SeekerMainActivity.this,
//                R.layout.seeker_search_list_item, searchResults);

        // Add view listeners
        addListeners();
    }

    private void executeSearch(String address, final double latitude, final double longitude) {

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

    private void loadSearchResults(ArrayList<ParkingSpotPost> parkingSpots) {

        // Populate ListView with entries
        for (ParkingSpotPost p : parkingSpots) {

            // Add a parking spot

        }
    }

    private void addListeners() {

        // Called when search filter button is pressed
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
            Intent intent = new Intent(SeekerMainActivity.this, SearchFilterActivity.class);
            startActivity(intent);
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
                    executeSearch(address, location.getLatitude(), location.getLongitude());
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

//    private class SearchListAdapter extends
}
