package edu.usc.parknpay.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.database.Transaction;
import edu.usc.parknpay.database.User;

/**
 * Created by Bobo on 10/27/2016.
 */

public class ViewSpotActivity extends TemplateActivity{

    private ImageView parkingSpotImage;
    private TextView address;
    private RatingBar spotRatingBar;
    private TextView size, handicap;
    private TextView cancellationPolicy;
    private TextView additionalNotes;
    private ImageView ownerImage;
    private TextView ownerName;
    private RatingBar ownerRatingBar;
    private Button reserveButton;
    double tempLat, tempLong;
    String tempAddr;

    private ListView reviewsListView;
    private ArrayList<String> reviews;
    private ArrayAdapter<String> reviewsListAdapter;

    private ParkingSpotPost parkingSpotPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_view_spot);
        super.onCreateDrawer();
        toolbarSetup();

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

        // Add adapter to ListView
        reviews = new ArrayList<String>();
        reviewsListAdapter = new ArrayAdapter<String>(ViewSpotActivity.this,
                android.R.layout.simple_list_item_1, reviews);
        reviewsListView.setAdapter(reviewsListAdapter);

        // Get parking spot post
        Serializable object = getIntent().getSerializableExtra("Parking spot post");
        parkingSpotPost = (ParkingSpotPost) object;

        //for search pag
        tempLat = getIntent().getDoubleExtra("lat", 0);
        tempLong = getIntent().getDoubleExtra("long", 0);
        tempAddr = getIntent().getStringExtra("addr");


        // TO DO: Update all the view information using the parkingSpotPost object.
        Picasso.with(this)
                .load(parkingSpotPost.getPhotoUrl())
                .resize(450, 450)
                .centerCrop()
                .into(parkingSpotImage);
        address.setText(parkingSpotPost.getAddress());
        // TODO: parking spot rating bar
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
                        .resize(450, 450)
                        .centerCrop()
                        .into(ownerImage);
                // TODO: owner rating bar
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        addListeners();
    }

    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Spot");
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

    protected void addListeners() {

        // Called when reserve button is clicked
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Add spot reservation to database
                FirebaseDatabase.getInstance().getReference().child("Users").child(parkingSpotPost.getOwnerUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        // Add spot reservation to database
                        String TransactionId = UUID.randomUUID().toString();
                        User u = User.getInstance();
                        Transaction transaction = new Transaction(
                                parkingSpotPost.getOwnerUserId(),
                                u.getId(),
                                parkingSpotPost.getPhotoUrl(),
                                user.getFirstName(),
                                u.getFirstName(),
                                parkingSpotPost.getStartTime(),
                                parkingSpotPost.getEndTime(),
                                parkingSpotPost.getOwnerPhoneNumber(),
                                parkingSpotPost.getParkingSpotId(),
                                parkingSpotPost.getAddress(),
                                TransactionId,
                                parkingSpotPost.getPrice(),
                                false
                        );

                        FirebaseDatabase.getInstance().getReference().child("Transactions").child(TransactionId).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                FirebaseDatabase.getInstance().getReference().child("Browse").child(parkingSpotPost.getPostId()).child("reserved").setValue(true);
            }
        });

    }
}