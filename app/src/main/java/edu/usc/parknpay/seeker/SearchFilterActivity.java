package edu.usc.parknpay.seeker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

public class SearchFilterActivity extends TemplateActivity {

    private static final int START_DATE_PICKER = 0;
    private static final int START_TIME_PICKER = 1;
    private static final int END_DATE_PICKER = 2;
    private static final int END_TIME_PICKER = 3;

    private EditText minPriceField, maxPriceField;
    private RatingBar ownerRatingBar, spotRatingBar;
    private CheckBox handicapOnlyCheckbox;
    private Spinner sizeSpinner, startSpinner, endSpinner;
    private Button startDateButton, endDateButton;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_search_filter);
        toolbarSetup();

        // Get references to UI views
        minPriceField = (EditText) findViewById(R.id.min_price_field);
        maxPriceField = (EditText) findViewById(R.id.max_price_field);
        ownerRatingBar = (RatingBar) findViewById(R.id.owner_rating_bar);
        spotRatingBar = (RatingBar) findViewById(R.id.spot_rating_bar);
        handicapOnlyCheckbox = (CheckBox) findViewById(R.id.handicap_only_checkbox);
        sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        startSpinner = (Spinner) findViewById(R.id.spinnerStart);
        endSpinner = (Spinner) findViewById(R.id.spinnerEnd);
        startDateButton = (Button) findViewById(R.id.start_date_button);
        endDateButton = (Button) findViewById(R.id.end_date_button);
        searchButton = (Button) findViewById(R.id.search_button);
        setSpinners();
        // Get initial values
        Bundle extras = getIntent().getExtras();
        double minPrice = extras.getDouble("minPrice");
        double maxPrice = extras.getDouble("maxPrice");
        float minOwnerRating = extras.getFloat("minOwnerRating");
        float minSpotRating = extras.getFloat("minSpotRating");
        boolean handicapOnly = extras.getBoolean("handicapOnly");
        boolean showNormal = extras.getBoolean("showNormal");
        boolean showCompact = extras.getBoolean("showCompact");
        boolean showSuv = extras.getBoolean("showSuv");
        boolean showTruck = extras.getBoolean("showTruck");
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
        startDateButton.setText(startDate);
        endDateButton.setText(endDate);

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
        List<String> timeSpinner =  new ArrayList<>();
        for(int i=0; i<24; i++) {
            if(i <10)
                timeSpinner.add("0"+Integer.toString(i));
            else
                timeSpinner.add(Integer.toString(i));
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, timeSpinner);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endSpinner.setAdapter(timeAdapter);
        startSpinner.setAdapter(timeAdapter);
    }

    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
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
        startDateButton.setText(startDate);
        endDateButton.setText(endDate);
    }

    private void addListeners() {

        // Called when user clicks start date button
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prompt user to pick a date
                showDialog(START_DATE_PICKER);
            }
        });


        // Called when user clicks end date button
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prompt user to pick a date
                showDialog(END_DATE_PICKER);
            }
        });


        // Called when user clicks search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();

                // Add return data to intent
                output.putExtra("minPrice", Double.parseDouble(minPriceField.getText().toString()));
                output.putExtra("maxPrice", Double.parseDouble(maxPriceField.getText().toString()));
                output.putExtra("minOwnerRating", ownerRatingBar.getRating());
                output.putExtra("minSpotRating", spotRatingBar.getRating());
                output.putExtra("handicapOnly", handicapOnlyCheckbox.isChecked());
                output.putExtra("startDate", startDateButton.getText());
                output.putExtra("endDate", endDateButton.getText());
                output.putExtra("startTime", startSpinner.getSelectedItem().toString() + ":00");
                output.putExtra("endTime", endSpinner.getSelectedItem().toString() + ":00");

                setResult(RESULT_OK, output);
                finish();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {

            case START_DATE_PICKER:
            {
                // Get current date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(this, startDatePickerListener, year, month, day);
            }


            case END_DATE_PICKER:
            {
                // Get current date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(this, endDatePickerListener, year, month, day);
            }


            default:
            {
                return null;
            }
        }
    }

    private DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            startDateButton.setText(year + "-" + month + "-" + dayOfMonth);
        }
    };

    private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            endDateButton.setText(year + "-" + month + "-" + dayOfMonth);
        }
    };

}