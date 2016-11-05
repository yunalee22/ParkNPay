package edu.usc.parknpay.seeker;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.Review;
import edu.usc.parknpay.database.Transaction;
import edu.usc.parknpay.database.User;

public class RateActivity extends TemplateActivity {
    ImageView spotPhoto;
    TextView address, date, time;
    RatingBar spotRatingBar, ownerRatingBar;
    Button doneButton;
    EditText additionalNotesField;
    Transaction t;
    DatabaseReference Ref;
    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_rate);

        setUpToolbar("Rate Your Experience");
        initializeEdits();
        addListeners();
        u = User.getInstance();

        //update the components: address, date, time, photo
        t = (Transaction) getIntent().getSerializableExtra("transaction");
        address.setText(t.getAddress());
        date.setText(t.getStartTime() + " - " + t.getEndTime());
        Picasso.with(this)
                .load(t.getPhotoUrl())
                .resize(350, 350)
                .centerCrop()
                .into(spotPhoto);
        Ref = FirebaseDatabase.getInstance().getReference();
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

    protected void initializeEdits() {
        spotPhoto = (ImageView) findViewById(R.id.imageView);
        address = (TextView) findViewById(R.id.address);
        spotRatingBar = (RatingBar) findViewById(R.id.ratingBarOwner);
        ownerRatingBar = (RatingBar) findViewById(R.id.ratingBarSpot);
        time = (TextView) findViewById(R.id.handicap);
        date = (TextView) findViewById(R.id.date);
        doneButton = (Button) findViewById(R.id.button);
        additionalNotesField = (EditText) findViewById(R.id.additionalNotesField);
    }

    protected void addListeners() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //upload the ratings to the database
                // VALUES ARE INTENTIONALLY FLIPPED cause our ui screwed up
                final double ownerRating = spotRatingBar.getRating();
                final double spotRating = ownerRatingBar.getRating();
                final String review = additionalNotesField.getText().toString().trim();

                FirebaseDatabase.getInstance().getReference().child("Users").child(t.getOwnerId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User userToBeRated = dataSnapshot.getValue(User.class);
                                userToBeRated.updateRating((int) ownerRating);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                FirebaseDatabase.getInstance().getReference().child("Parking-Spots").child(t.getParkingId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ParkingSpot spotToBeRated = dataSnapshot.getValue(ParkingSpot.class);
                                spotToBeRated.updateRating((int) spotRating);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });


                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String currTime = dateFormat.format(today);

                Review reviewPost = new Review(
                        u.getFirstName(),
                        review,
                        spotRating,
                        currTime,
                        u.getProfilePhotoURL()
                );

                FirebaseDatabase.getInstance().getReference().child("Parking-Spot-Reviews").child(t.getParkingId())
                        .child(t.getTransactionId()).setValue(reviewPost);

                t.setRated(true);
                FirebaseDatabase.getInstance().getReference().child("Transactions").child(t.getTransactionId()).setValue(t);

                finish();
            }

        });

    }
}
