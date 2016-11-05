package edu.usc.parknpay.owner;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.User;

public class OwnerMainActivity extends TemplateActivity {
    GridView parkingSpots;
    ArrayList<ParkingSpot> parkingSpotArray;
    ImageView addSpotButton;
    OwnerMainSpotAdapter parkingSpotAdapter;
    DatabaseReference parkingSpotRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_main);

        setUpToolbar("ParkNPay");
        initializeComponents();
        addListeners();

        parkingSpotArray = new ArrayList<ParkingSpot>();
        parkingSpotAdapter = new OwnerMainSpotAdapter(this, parkingSpotArray);
        parkingSpots.setAdapter(parkingSpotAdapter);

        String userId = User.getInstance().getId();
        parkingSpotRef = FirebaseDatabase.getInstance().getReference();
        parkingSpotRef.child("Owner-To-Spots").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> spots = (Map<String,Object>)dataSnapshot.getValue();
                if (spots == null) return;
                for (Map.Entry<String, Object> entry : spots.entrySet()) {
                    final boolean exists = (boolean) entry.getValue();
                    parkingSpotRef.child("Parking-Spots").child(entry.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ParkingSpot spot = dataSnapshot.getValue(ParkingSpot.class);
                            processSpot(spot, exists);
                            parkingSpotAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    protected void setUpToolbar(String toolbarTitle) {

        // Set toolbar as action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Customize toolbar
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(toolbarTitle);

        // Enable navigation icon
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Add navigation drawer
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(OwnerMainActivity.this,
                drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        super.onCreateDrawer();
    }

    private void initializeComponents() {
        parkingSpots = (GridView) findViewById(R.id.gridView);
        addSpotButton = (ImageView) findViewById(R.id.addSpot);
    }

    protected void addListeners() {
        addSpotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), AddSpotActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

        });

        parkingSpots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewSpotActivity.class);
                intent.putExtra("parkingSpot", parkingSpotArray.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

    }

    // Proccess parking spot - checks if parking spot already exists on UI
    // If so, replace it
    // If not, add the spot
    // Ask Avery for more details
    public void processSpot(ParkingSpot spot, boolean exists) {
        for (int i = 0; i < parkingSpotArray.size(); ++i) {
            // If item exists, replace it
            if (parkingSpotArray.get(i).getParkingId().equals(spot.getParkingId()))
            {
                if (exists) {
                    parkingSpotArray.set(i, spot);
                } else {
                    parkingSpotArray.remove(i);
                }
                return;
            }
        }
        if (!exists) {return;}
        // Spot was not part of array
        parkingSpotArray.add(spot);
    }
}
