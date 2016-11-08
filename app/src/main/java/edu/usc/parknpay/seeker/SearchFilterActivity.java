package edu.usc.parknpay.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.utility.TemplateActivity;
import edu.usc.parknpay.utility.Utility;

public class SearchFilterActivity extends TemplateActivity {

    private EditText minPriceField, maxPriceField;
    private RatingBar ownerRatingBar, spotRatingBar;
    private CheckBox handicapOnlyCheckbox;
    private Spinner sizeSpinner;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_search_filter);

        setUpToolbar("Search Filter");

        // Get references to UI views
        minPriceField = (EditText) findViewById(R.id.min_price_field);
        maxPriceField = (EditText) findViewById(R.id.max_price_field);
        ownerRatingBar = (RatingBar) findViewById(R.id.owner_rating_bar);
        spotRatingBar = (RatingBar) findViewById(R.id.spot_rating_bar);
        handicapOnlyCheckbox = (CheckBox) findViewById(R.id.handicap_only_checkbox);
        sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        searchButton = (Button) findViewById(R.id.search_button);
        setSpinners();
        // Get initial values
        Bundle extras = getIntent().getExtras();
        double minPrice = extras.getDouble("minPrice");
        double maxPrice = extras.getDouble("maxPrice");
        float minOwnerRating = extras.getFloat("minOwnerRating");
        float minSpotRating = extras.getFloat("minSpotRating");
        boolean handicapOnly = extras.getBoolean("handicapOnly");
        String startDate = extras.getString("startDate");
        String startTime = extras.getString("startTime");
        String endDate = extras.getString("endDate");
        String endTime = extras.getString("endTime");

        // Set initial values of views
        minPriceField.setText(String.valueOf(minPrice));
        maxPriceField.setText(String.valueOf(maxPrice));
        ownerRatingBar.setRating(minOwnerRating);
        spotRatingBar.setRating(minSpotRating);
        handicapOnlyCheckbox.setChecked(handicapOnly);

        // Add view listeners
        addListeners();
    }

    protected void setSpinners(){
        List<String> sizeArray =  new ArrayList<>();
        sizeArray.add("Normal");
        sizeArray.add("Compact");
        sizeArray.add("SUV");
        sizeArray.add("Truck");
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, sizeArray);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get initial values
        Bundle extras = getIntent().getExtras();
        double minPrice = extras.getDouble("minPrice");
        System.out.println("Min price is " + minPrice);
        double maxPrice = extras.getDouble("maxPrice");
        float minOwnerRating = extras.getFloat("minOwnerRating");
        float minSpotRating = extras.getFloat("minSpotRating");
        boolean handicapOnly = extras.getBoolean("handicapOnly");
        String startDate = extras.getString("startDate");
        String endDate = extras.getString("endDate");

        // Set initial values of views
        minPriceField.setText(String.valueOf(minPrice));
        maxPriceField.setText(String.valueOf(maxPrice));
        ownerRatingBar.setRating(minOwnerRating);
        spotRatingBar.setRating(minSpotRating);
        handicapOnlyCheckbox.setChecked(handicapOnly);
//        startDateButton.setText(startDate);
//        endDateButton.setText(endDate);
    }

    private void addListeners() {

        // Called when user clicks search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent(SearchFilterActivity.this, SeekerMainActivity.class);
                output.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                // Add return data to intent
                output.putExtra("page", "filter");
                output.putExtra("minPrice", Double.parseDouble(minPriceField.getText().toString()));
                output.putExtra("maxPrice", Double.parseDouble(maxPriceField.getText().toString()));
                output.putExtra("minOwnerRating", ownerRatingBar.getRating());
                output.putExtra("minSpotRating", spotRatingBar.getRating());
                output.putExtra("handicapOnly", handicapOnlyCheckbox.isChecked());
                output.putExtra("size", Utility.convertSize(sizeSpinner.getSelectedItem().toString()));
                startActivity(output);
               // setResult(RESULT_OK, output);
                finish();
            }
        });
    }
}