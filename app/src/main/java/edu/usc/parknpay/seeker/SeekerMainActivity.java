package edu.usc.parknpay.seeker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.authentication.RegistrationActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.utility.Utility;

public class SeekerMainActivity extends TemplateActivity {

    private static final int START_DATE_PICKER = 0;
    private static final int END_DATE_PICKER = 2;
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
    private int size;
    private double minPrice, maxPrice;
    private float minOwnerRating, minSpotRating;
    private boolean handicapOnly, showNormal, showCompact, showSuv, showTruck;
    private String address, startDate, startTime, endDate, endTime;

    //date stuff
    private Spinner startSpinner, endSpinner;
    private Button startDateButton, endDateButton;

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Toast.makeText(SeekerMainActivity.this, "This function is run", Toast.LENGTH_SHORT).show();
//        adapterLatitude = getIntent().getDoubleExtra("lat", 0);
//        adapterLongitude = getIntent().getDoubleExtra("long", 0);
//        address = getIntent().getStringExtra("addr");
//        executeSearch();

        if(getIntent().getStringExtra("page") == "filter") {
            minPrice = getIntent().getDoubleExtra("minPrice", 0);
            maxPrice = getIntent().getDoubleExtra("maxPrice", 10000);
            minOwnerRating = getIntent().getFloatExtra("minOwnerRating", 0);
            minSpotRating = getIntent().getFloatExtra("minSpotRating", 0);
            handicapOnly = getIntent().getBooleanExtra("handicapOnly", false);
            size = getIntent().getIntExtra("size", 1);
            executeSearch();
        }
        //searchResults.clear();
    }

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

        // Add time and date selectors
        startSpinner = (Spinner) findViewById(R.id.spinnerStart);
        endSpinner = (Spinner) findViewById(R.id.spinnerEnd);
        startDateButton = (Button) findViewById(R.id.start_date_button);
        endDateButton = (Button) findViewById(R.id.end_date_button);

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
        endSpinner.setSelection(1);

        // Set default search parameters
        minPrice = 0;
        maxPrice = 100000000;                   // What is the max price?????
        minOwnerRating = 0.0f;
        minSpotRating = 0.0f;
        handicapOnly = false;
        size = ParkingSpot.Size.Normal.getValue();
        startDate = date;
        startTime = time;
        endDate = date;
        endTime = "23:59";

        startDateButton.setText(startDate);
        endDateButton.setText(endDate);

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

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object parkingSpotPost = (ParkingSpotPost) searchList.getItemAtPosition(position);
                Intent intent = new Intent(SeekerMainActivity.this, ViewSpotActivity.class);
                intent.putExtra("Parking spot post", (Serializable)parkingSpotPost);
                intent.putExtra("lat", adapterLatitude);
                intent.putExtra("long", adapterLongitude);
                intent.putExtra("addr", address);
                startActivity(intent);
            }
        });

        // Add view listeners
        addListeners();
    }

    private void executeSearch() {

        searchResults.clear();

        startTime = startSpinner.getSelectedItem().toString() + ":00";
        endTime = endSpinner.getSelectedItem().toString() + ":00";
        startDate = startDateButton.getText().toString();
        endDate = endDateButton.getText().toString();



        System.out.println("Executing search: " + address + " at (" + adapterLatitude + ", " + adapterLongitude + ")");

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);

        try {
            Date date = df.parse(startDate + " " + startTime);
            final String sStartTime = df.format(date);
            date = df.parse(endDate + " " + endTime);
            final String sEndTime = df.format(date);


            System.out.println("startTime: " + sStartTime);
            System.out.println("endTime: " + sEndTime);

            // TODO: Show error popup if bad input?
            if(sStartTime.compareTo(sEndTime) > 0)
                System.out.println("ERROR");

            DatabaseReference browseRef = FirebaseDatabase.getInstance().getReference().child("Browse/");
            // order by endtimes at or later
            browseRef.orderByChild("endTime").startAt(sEndTime).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                    ParkingSpotPost post = dataSnapshot.getValue(ParkingSpotPost.class);
                    // check start time is earlier
                    if(!post.isReserved()) {
                        if (sStartTime.compareTo(post.getStartTime()) >= 0) {
                            // check distance
                            double distance = Utility.distance(adapterLatitude, adapterLongitude, post.getLatitude(), post.getLongitude(), "M");
                            if (distance < RADIUS_LIMIT) {
                                if (post.getPrice() >= minPrice || post.getPrice() <= maxPrice) {
                                    int postSize = Utility.convertSize(post.getSize());
                                    if (postSize >= size && post.isHandicap() == handicapOnly) {
                                        if (post.getOwnerRating() >= minOwnerRating) {
                                            System.out.println("Got spot: " + post.getAddress());
                                            searchResults.add(post);
                                            searchResultsAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
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

                try {
                    address = place.getName().toString();
                    addressInfo = coder.getFromLocationName(address, 5);
                    Address location = addressInfo.get(0);

                    // Store into variables that are used later
                    adapterLatitude = location.getLatitude();
                    adapterLongitude = location.getLongitude();
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
            startDateButton.setText(year + "-" + (month + 1) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));
        }
    };

    private DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            endDateButton.setText(year + "-" + (month + 1) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));
        }
    };

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

            Picasso.with(getContext())
                    .load(parkingSpotPost.getPhotoUrl())
                    .placeholder(R.drawable.progress_animation)
                    .resize(150, 150)
                    .centerCrop()
                    .into(spotImageView);

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
            if (!parkingSpotPost.isHandicap())
            {
                TextView handiText = (TextView) convertView.findViewById(R.id.handicap);
                handiText.setVisibility(View.GONE);
            }

            // Set Price
            TextView priceText = (TextView) convertView.findViewById(R.id.price);
            DecimalFormat df = new DecimalFormat("0.00");
            priceText.setText("$" + df.format(parkingSpotPost.getPrice()));

            TextView distText = (TextView) convertView.findViewById(R.id.distance);
            distText.setText(
                    df.format(Utility.distance(parkingSpotPost.getLatitude(),
                            parkingSpotPost.getLongitude(),
                            adapterLatitude,
                            adapterLongitude,
                            "M"))
                    + " mi"
            );



            return convertView;
        }

    }
}
