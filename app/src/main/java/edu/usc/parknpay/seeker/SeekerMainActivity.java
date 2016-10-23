package edu.usc.parknpay.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;

public class SeekerMainActivity extends TemplateActivity {

    ImageView filterButton;
    EditText searchText;
    ListView searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_main);

        // Get references to UI views
        filterButton = (ImageView) findViewById(R.id.filter_button);
        searchText = (EditText) findViewById(R.id.search_text);
        searchResults = (ListView) findViewById(R.id.search_results);

        // Add view listeners
        addListeners();
    }

    private void executeSearch(String searchText) {

        // Get search parameters
        String address = searchText;

        // Perform search and load results
        ArrayList<ParkingSpot> results = new ArrayList<ParkingSpot>();
        for (int i = 0; i < 4; i++) {
//            ParkingSpot p = new ParkingSpot("yunalee22", "2651 Ellendale Pl",
//                    "10/22/16", "10/22/16", "12:00", "14:00", "Compact",
//                    20.0, 4.0, true, "", "My cancellation policy!");
//            results.add(p);
        }
        loadSearchResults(results);
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

        // Called when user presses enter in the
        searchText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    executeSearch(searchText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }
}
