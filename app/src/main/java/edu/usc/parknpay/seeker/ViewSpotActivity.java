package edu.usc.parknpay.seeker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
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

    private ParkingSpotPost parkingSpotPost;

    double tempLat, tempLong;
    String tempAddr;

    private Button reserveButton;
    String reservationStartDate;
    String reservationStartTime;
    String reservationEndDate;
    String reservationEndTime;

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

    private void processReservation(String startDate, String startTime, String endDate, String endTime) {

        // Check if valid time
        boolean validTime = false;




        if (!validTime) {
            Toast.makeText(ViewSpotActivity.this, "Please select a valid reservation time range",
                    Toast.LENGTH_SHORT).show();
            return;
        }

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

                //deduct your money
                u.changeBalance(-parkingSpotPost.getPrice());

                //get the other user and add to their moneys
                FirebaseDatabase.getInstance().getReference().child("Users").child(parkingSpotPost.getOwnerUserId()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        // Create user
                        User userToBePaid = snapshot.getValue(User.class);
                        userToBePaid.changeBalance( parkingSpotPost.getPrice() *.9);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
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
            public void onCancelled(DatabaseError databaseError) { }
        });

        // set as reserved spot
        FirebaseDatabase.getInstance().getReference().child("Browse").child(parkingSpotPost.getParkingSpotPostId()).child("reserved").setValue(true);
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
        Button startDateButton = (Button) dialogView.findViewById(R.id.startDateButton);
        Button endDateButton = (Button) dialogView.findViewById(R.id.endDateButton);



        // Set up time spinners
        Spinner startTimeSpinner = (Spinner) dialogView.findViewById(R.id.startTimeSpinner);
        Spinner endTimeSpinner = (Spinner) dialogView.findViewById(R.id.endTimeSpinner);



        dialogBuilder.setTitle("Enter reservation information");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Set start and end date/time information

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

                // Process the reservation
                processReservation(reservationStartDate, reservationStartTime,
                        reservationEndDate, reservationEndTime);
            }
        });
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