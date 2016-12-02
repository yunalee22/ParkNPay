package edu.usc.parknpay.seeker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.database.Review;
import edu.usc.parknpay.database.Transaction;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.utility.TemplateActivity;

/**
 * Created by Bobo on 10/27/2016.
 */

public class ViewSpotActivity extends TemplateActivity{

    private ImageView parkingSpotImage;
    private TextView address;
    private RatingBar spotRatingBar;
    private TextView price;
    private TextView size, handicap;
    private TextView cancellationPolicy;
    private TextView additionalNotes;
    private ImageView ownerImage;
    private TextView ownerName;
    private RatingBar ownerRatingBar;

    private ListView reviewsListView;
    private ArrayList<Review> reviews;
    private ReviewAdapter reviewsListAdapter;

    //date
    Calendar startCalendar;
    Calendar endCalendar;
    DatePickerDialog.OnDateSetListener dateStart, dateEnd;
    String startDate, startTime, endDate, endTime;
    Button startDateButton, endDateButton;

    private ParkingSpotPost parkingSpotPost;

    double tempLat, tempLong;
    String tempAddr;

    private Button reserveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_view_spot);

        // Set up UI
        setUpToolbar("View Parking Spot");
        initializeComponents();
        enableListViewScroll();

        // Add adapter to ListView
        reviews = new ArrayList<Review>();
        reviewsListAdapter = new ReviewAdapter(ViewSpotActivity.this, reviews);
        reviewsListView.setAdapter(reviewsListAdapter);

        loadSpotInformation();
        addListeners();
    }

    private void processReview(Review t) {
        /*for (int i = 0; i < reviews.size(); ++i) {
            // If item exists, replace it
            if (reviews.get(i).getDate().equals(t.getDate()))
            {
                reviews.set(i, t);
                return;
            }
        }*/
        // review was not part of array
        reviews.add(t);
    }

    private void initializeComponents() {

        // Get references to UI views
        parkingSpotImage = (ImageView) findViewById(R.id.parkingSpotImage);
        address = (TextView) findViewById(R.id.address);
        spotRatingBar = (RatingBar) findViewById(R.id.spotRatingBar);
        size = (TextView) findViewById(R.id.size);
        handicap = (TextView) findViewById(R.id.handicap);
        cancellationPolicy = (TextView) findViewById(R.id.cancellationPolicy);
        additionalNotes = (TextView) findViewById(R.id.additionalNotes);
        ownerImage = (ImageView) findViewById(R.id.ownerImage);
        ownerName = (TextView) findViewById(R.id.ownerName);
        ownerRatingBar = (RatingBar) findViewById(R.id.ownerRatingBar);
        reserveButton = (Button) findViewById(R.id.reserveButton);
        reviewsListView = (ListView) findViewById(R.id.reviewsListView);
        price = (TextView) findViewById(R.id.price);
    }

    private void loadSpotInformation() {

        // Get parking spot post
        Serializable object = getIntent().getSerializableExtra("Parking spot post");
        parkingSpotPost = (ParkingSpotPost) object;
        ownerRatingBar.setRating((float) parkingSpotPost.getOwnerRating());
        spotRatingBar.setRating((float) parkingSpotPost.getRating());

        //for search pag
        tempLat = getIntent().getDoubleExtra("lat", 0);
        tempLong = getIntent().getDoubleExtra("long", 0);
        tempAddr = getIntent().getStringExtra("addr");
        startDate = getIntent().getStringExtra("startDate");
        startTime = getIntent().getStringExtra("startTime");
        endDate = getIntent().getStringExtra("endDate");
        endTime = getIntent().getStringExtra("endTime");

        // TO DO: Update all the view information using the parkingSpotPost object.
        Picasso.with(this)
                .load(parkingSpotPost.getPhotoUrl())
                .placeholder(R.drawable.progress_animation)
                .resize(450, 450)
                .centerCrop()
                .into(parkingSpotImage);
        address.setText(parkingSpotPost.getAddress());
        spotRatingBar.setRating((float)parkingSpotPost.getRating());
        DecimalFormat df = new DecimalFormat("0.00");
        price.setText("$" + df.format(parkingSpotPost.getPrice()));
        size.setText(parkingSpotPost.getSize());
        handicap.setText(parkingSpotPost.isHandicap() ? "Handicap" : "Not Handicap");
        cancellationPolicy.setText(parkingSpotPost.getCancellationPolicy());
        additionalNotes.setText(parkingSpotPost.getDescription());

        DatabaseReference browseRef = FirebaseDatabase.getInstance().getReference().child("Users/" + parkingSpotPost.getOwnerUserId());
        browseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User owner = dataSnapshot.getValue(User.class);
                ownerName.setText(owner.getFirstName() + " " + owner.getLastName());
                Picasso.with(ViewSpotActivity.this)
                        .load(owner.getProfilePhotoURL())
                        .placeholder(R.drawable.progress_animation)
                        .resize(450, 450)
                        .centerCrop()
                        .into(ownerImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.child("Parking-Spot-Reviews").child(parkingSpotPost.getParkingSpotId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> spots = (Map<String,Object>)dataSnapshot.getValue();
                if (spots == null) {return;}
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Review t = snapshot.getValue(Review.class);
                    processReview(t);
                    reviewsListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // used for split booking - create new posts with times based on the current spot
    private void createNewPost(String startTime, String endTime) {
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();

        ParkingSpotPost post = new ParkingSpotPost(parkingSpotPost.getParkingSpotPostId(), parkingSpotPost.getOwnerUserId(), parkingSpotPost.getOwnerFullName(), parkingSpotPost.getOwnerPhoneNumber(), parkingSpotPost.getParkingSpotId(), parkingSpotPost.getAddress(), startTime, endTime, parkingSpotPost.getLatitude(), parkingSpotPost.getLongitude(), parkingSpotPost.getPrice(),
                parkingSpotPost.getRating(), parkingSpotPost.getSize(), parkingSpotPost.getCancellationPolicy(), parkingSpotPost.isHandicap(), parkingSpotPost.getOwnerRating(), parkingSpotPost.getPhotoUrl(), parkingSpotPost.getDescription(), false);

        // generate a different parking spot post id
        String postId = UUID.randomUUID().toString();

        Ref.child("Browse").child(postId).setValue(post);

        System.out.println("CREATED NEW POST " + parkingSpotPost.getAddress() + " " + startTime + " "  + endTime);
    }


    private void processReservation(String startDate, String startTime, String endDate, String endTime) {
        DatabaseReference SpotDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Browse").child(parkingSpotPost.getParkingSpotPostId());

        String origStartTime = parkingSpotPost.getStartTime();
        String origEndTime = parkingSpotPost.getEndTime();
        String reqStartTime = startDate + " " + startTime + ":00";
        String reqEndTime = endDate + " " + endTime + ":00";
        System.out.println(origStartTime);
        System.out.println(origEndTime);
        System.out.println(reqStartTime);
        System.out.println(reqEndTime);

        int compareStart = reqStartTime.compareTo(origStartTime);
        int compareEnd = reqEndTime.compareTo(origEndTime);

        // reserve entire spot
        if(compareStart == 0 && compareEnd == 0) {
            processTransaction();
        }
        // reserve beginning slot
        else if(compareStart == 0 && compareEnd < 0) {
            // post from requested end time to original end time
            createNewPost(reqEndTime, origEndTime);

            // change the transaction endtime to the requested end
            SpotDatabaseRef.child("endTime").setValue(reqEndTime);

            parkingSpotPost.setEndTime(reqEndTime);

            processTransaction();
        }
        // reserve ending slot time
        else if(compareStart > 0 && compareEnd == 0) {
            // change current post's start time to reqStartTime
            createNewPost(origStartTime, reqStartTime);

            // change the transaction's start time to requested start
            SpotDatabaseRef.child("startTime").setValue(reqStartTime);

            parkingSpotPost.setStartTime(reqStartTime);

            processTransaction();
        }
        // reserve middle slot
        else if(compareStart > 0 && compareEnd < 0){
            // create two new posts
            createNewPost(origStartTime, reqStartTime);
            createNewPost(reqEndTime, origEndTime);

            // change transaction's start/end times to requested ones
            SpotDatabaseRef.child("startTime").setValue(reqStartTime);
            SpotDatabaseRef.child("endTime").setValue(reqEndTime);

            parkingSpotPost.setStartTime(reqStartTime);
            parkingSpotPost.setEndTime(reqEndTime);

            processTransaction();
        }
        // error -- not within bounds
        else {
            Toast.makeText(ViewSpotActivity.this, "Requested time not within spot's bounds",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void processTransaction() {
        // Add spot reservation to database
        FirebaseDatabase.getInstance().getReference().child("Users").child(parkingSpotPost.getOwnerUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                // Add spot reservation to database
                String transactionId = UUID.randomUUID().toString();
                User u = User.getInstance();
                Transaction transaction = new Transaction(
                        transactionId,
                        parkingSpotPost.getOwnerUserId(),
                        u.getId(),
                        parkingSpotPost.getParkingSpotPostId(),
                        parkingSpotPost.getPhotoUrl(),
                        user.getFirstName(),
                        u.getFirstName(),
                        parkingSpotPost.getStartTime(),
                        parkingSpotPost.getEndTime(),
                        parkingSpotPost.getOwnerPhoneNumber(),
                        user.getPhoneNumber(),
                        parkingSpotPost.getParkingSpotId(),
                        parkingSpotPost.getAddress(),
                        parkingSpotPost.getPrice(),
                        false, // not rated
                        false // not cancelled
                );

                // deduct your money
                u.changeBalance(-parkingSpotPost.getPrice());

                // get the other user and add to their moneys
                FirebaseDatabase.getInstance().getReference().child("Users").child(parkingSpotPost.getOwnerUserId()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Create user
                        User userToBePaid = snapshot.getValue(User.class);
                        userToBePaid.changeBalance(parkingSpotPost.getPrice() * .9);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                FirebaseDatabase.getInstance().getReference().child("Transactions").child(transactionId).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), SeekerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.putExtra("page", "viewspot");
                        intent.putExtra("lat", tempLat);
                        intent.putExtra("long", tempLong);
                        intent.putExtra("addr", tempAddr);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // set as reserved spot
        parkingSpotPost.setReserved(true);
        FirebaseDatabase.getInstance().getReference().child("Browse").child(parkingSpotPost.getParkingSpotPostId()).setValue(parkingSpotPost);
        FirebaseDatabase.getInstance().getReference().child("Parking-Spots").child(parkingSpotPost.getParkingSpotId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create parking spot
                ParkingSpot parkingSpot = dataSnapshot.getValue(ParkingSpot.class);
                parkingSpot.setNumReserved(parkingSpot.getNumRatings() + 1);
                FirebaseDatabase.getInstance().getReference().child("Parking-Spots").child(parkingSpotPost.getParkingSpotId()).setValue(parkingSpot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showSeekerReservationDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.seeker_reservation_dialog, null);
        dialogBuilder.setView(dialogView);

        // Set up date selectors
        startDateButton = (Button) dialogView.findViewById(R.id.startDateButton);
        endDateButton = (Button) dialogView.findViewById(R.id.endDateButton);
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        dateStart = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel("start");
            }
        };
        dateEnd = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel("end");
            }

        };

        startDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ViewSpotActivity.this, dateStart, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(ViewSpotActivity.this, dateEnd, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // Set up time spinners
        final Spinner startSpinner = (Spinner) dialogView.findViewById(R.id.startTimeSpinner);
        final Spinner endSpinner = (Spinner) dialogView.findViewById(R.id.endTimeSpinner);
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
        //index 11-12 are the hours in start string

        startSpinner.setSelection(Integer.parseInt(startTime));
        endSpinner.setSelection(Integer.parseInt(endTime));

        startDateButton.setText(startDate);
        endDateButton.setText(endDate);

        dialogBuilder.setTitle("Enter reservation information");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Set start and end date/time information
                processReservation(startDateButton.getText().toString(), startSpinner.getSelectedItem().toString(),
                        endDateButton.getText().toString(), endSpinner.getSelectedItem().toString());

            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    protected void addListeners() {

        // Called when reserve button is clicked
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Show time selection dialog
                showSeekerReservationDialog();

            }
        });
    }

    private void updateLabel(String end) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(end == "end") {
            if (endCalendar.get(Calendar.MONTH) < 10) {
                endDateButton.setText(sdf.format("0" + endCalendar.getTime()));
            }
            endDateButton.setText(sdf.format(endCalendar.getTime()));
        }
        else {
            if (endCalendar.get(Calendar.MONTH) < 10) {
                startDateButton.setText(sdf.format("0" + startCalendar.getTime()));
            }
            startDateButton.setText(sdf.format(startCalendar.getTime()));
        }

    }

    // This function allows for listviews within scrollviews to be scrolled
    private void enableListViewScroll() {

        reviewsListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
    }
}