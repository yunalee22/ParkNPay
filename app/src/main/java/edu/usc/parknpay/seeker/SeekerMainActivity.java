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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;

public class SeekerMainActivity extends TemplateActivity {

    ImageView filterButton;

    ListView searchResultView;
    ArrayList<ParkingSpot> searchResults;
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
        searchResults = new ArrayList<ParkingSpot>();
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

            ParkingSpot p = new ParkingSpot("yunalee22", "2651 Ellendale Pl",
                    "10/22/16", "10/22/16", "12:00", "14:00", "Compact",
                    20.0, 4.0, true, "", "My cancellation policy!");
            searchResults.add(p);
        }
        loadSearchResults(searchResults);
    }

    private void loadSearchResults(ArrayList<ParkingSpot> parkingSpots) {

        // Populate ListView with entries
        for (ParkingSpot p : parkingSpots) {

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
