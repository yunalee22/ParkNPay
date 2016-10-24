package edu.usc.parknpay.seeker;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
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
import java.util.Map;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.utility.Utility;

public class SeekerMainActivity extends TemplateActivity {

    public static final double RADIUS_LIMIT = 3;
    ImageView filterButton;

    ListView searchResultView;
    ArrayList<ParkingSpotPost> searchResults;
    ArrayAdapter<ParkingSpot> searchResultsAdapter;

    PlaceAutocompleteFragment autocompleteFragment;
    Geocoder coder;
    private static final String LOG_TAG = "PlaceSelectionListener";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_main);

        // Get references to UI views
        filterButton = (ImageView) findViewById(R.id.filter_button);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        searchResultView = (ListView) findViewById(R.id.search_results);

        // Add search text field and geocoder
        coder = new Geocoder(this);
        autocompleteFragment.setHint("Enter an address");
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(34.0224, -118.2851),
                new LatLng(34.0224, -118.2851)
        ));

        // Add adapter to ListView
        searchResults = new ArrayList<ParkingSpotPost>();
        //searchResultsAdapter = new ArrayAdapter<ParkingSpot>(this, , searchResults);

        // Add view listeners
        addListeners();
    }

    private void executeSearch(String address, double latitude, double longitude) {

        System.out.println("Executing search: " + address + " at (" + latitude + ", " + longitude + ")");

        // Perform search and load results
        for (int i = 0; i < 4; i++) {

            /* TIFF: LOAD UP SOME NUMBER OF SEARCH RESULTS.
            Load the closest 100ish (or whatever) parking spots.
            Create a ParkingSpot for each spot and add to array called "searchResults". */

            // So something like this:
//            ParkingSpot p = new ParkingSpot("yunalee22", "2651 Ellendale Pl",
//                    "10/22/16", "10/22/16", "12:00", "14:00", "Compact",
//                    20.0, 4.0, true, "", "My cancellation policy!");
//            searchResults.add(p);
        }

        final String sStartTime = "1997-07-16T19:20+01:00";
        final String sEndTime = "1997-07-16T19:20+01:05";
        final double sLatitude = 34.0168;
        final double sLongitude = 118.2820;

        DatabaseReference browseRef = FirebaseDatabase.getInstance().getReference().child("Browse/");
        browseRef.
                orderByChild("startTime").startAt(sStartTime)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                        ParkingSpotPost post = dataSnapshot.getValue(ParkingSpotPost.class);

                        System.out.println("WHAT THE HECK");
                        System.out.println("Search end: " + sEndTime + " Post end: " + post.getEndTime());

                        if(sEndTime.compareTo(post.getEndTime()) <= 0) {
                            System.out.println("WITHIN END TIME");
                            double distance = Utility.distance(sLatitude, sLongitude, post.getLatitude(), post.getLongitude(), "M");
                            System.out.println("DISTANCE: " + distance);
                            if(distance < RADIUS_LIMIT) {
                                //if(FILTERS)
                                    //DISPLAY
                                System.out.println("SPOT WITHIN 3 MILES: " + post.toString());
                            }

                        }

                        System.out.println("POST: " + post.toString());
                    }

                    @Override
                    public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        loadSearchResults(searchResults);
    }

    private void loadSearchResults(ArrayList<ParkingSpotPost> parkingSpots) {

        // Populate ListView with entries
        for (ParkingSpotPost p : parkingSpots) {

            // Add a parking spot

        }
    }

    protected void addListeners() {

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
}
