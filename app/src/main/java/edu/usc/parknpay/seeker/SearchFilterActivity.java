package edu.usc.parknpay.seeker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

public class SearchFilterActivity extends TemplateActivity {

    private EditText minPriceField, maxPriceField;
    private RatingBar ownerRatingBar, spotRatingBar;
    private CheckBox handicapOnlyCheckbox;
    private CheckBox normalCheckbox, compactCheckbox, suvCheckbox, truckCheckbox;
    private Button startDateButton, startTimeButton, endDateButton, endTimeButton;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_search_filter);

        // Get references to UI views
        minPriceField = (EditText) findViewById(R.id.min_price_field);
        maxPriceField = (EditText) findViewById(R.id.max_price_field);
        ownerRatingBar = (RatingBar) findViewById(R.id.owner_rating_bar);
        spotRatingBar = (RatingBar) findViewById(R.id.spot_rating_bar);
        handicapOnlyCheckbox = (CheckBox) findViewById(R.id.handicap_only_checkbox);
        normalCheckbox = (CheckBox) findViewById(R.id.normal_checkbox);
        compactCheckbox = (CheckBox) findViewById(R.id.compact_checkbox);
        suvCheckbox = (CheckBox) findViewById(R.id.suv_checkbox);
        truckCheckbox = (CheckBox) findViewById(R.id.truck_checkbox);
        startDateButton = (Button) findViewById(R.id.start_date_button);
        startTimeButton = (Button) findViewById(R.id.start_time_button);
        endDateButton = (Button) findViewById(R.id.end_date_button);
        endTimeButton = (Button) findViewById(R.id.end_time_button);
        searchButton = (Button) findViewById(R.id.search_button);
    }

    private void addListeners() {


    }
}
