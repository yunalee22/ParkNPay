package edu.usc.parknpay.seeker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

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
        normalCheckbox.setChecked(showNormal);
        compactCheckbox.setChecked(showCompact);
        suvCheckbox.setChecked(showSuv);
        truckCheckbox.setChecked(showTruck);
        startDateButton.setText(startDate);
        startTimeButton.setText(startTime);
        endDateButton.setText(endDate);
        endTimeButton.setText(endTime);

        // Add view listeners
        addListeners();
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
        normalCheckbox.setChecked(showNormal);
        compactCheckbox.setChecked(showCompact);
        suvCheckbox.setChecked(showSuv);
        truckCheckbox.setChecked(showTruck);
        startDateButton.setText(startDate);
        startTimeButton.setText(startTime);
        endDateButton.setText(endDate);
        endTimeButton.setText(endTime);
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

        // Called when user clicks start time button
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prompt user to pick a time
                showDialog(START_TIME_PICKER);
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

        // Called when user clicks end time button
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prompt user to pick a time
                showDialog(END_TIME_PICKER);
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
                output.putExtra("showNormal", normalCheckbox.isChecked());
                output.putExtra("showCompact", compactCheckbox.isChecked());
                output.putExtra("showSuv", suvCheckbox.isChecked());
                output.putExtra("showTruck", truckCheckbox.isChecked());
                output.putExtra("startDate", startDateButton.getText());
                output.putExtra("startTime", startTimeButton.getText());
                output.putExtra("endDate", endDateButton.getText());
                output.putExtra("endTime", endTimeButton.getText());

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

            case START_TIME_PICKER:
            {
                // Get current time
                Calendar cal = Calendar.getInstance();
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);

                return new TimePickerDialog(this, startTimePickerListener, hours, minutes, false);
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

            case END_TIME_PICKER:
            {
                // Get current time
                Calendar cal = Calendar.getInstance();
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);

                return new TimePickerDialog(this, endTimePickerListener, hours, minutes, false);
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

    private TimePickerDialog.OnTimeSetListener startTimePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startTimeButton.setText(hourOfDay + ":" + minute);
        }
    };

    private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            endDateButton.setText(year + "-" + month + "-" + dayOfMonth);
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endTimeButton.setText(hourOfDay + ":" + minute);
        }
    };
}