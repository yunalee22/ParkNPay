package edu.usc.parknpay.seeker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.Transaction;
import edu.usc.parknpay.database.User;

public class ReservationsActivity extends TemplateActivity {

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private ListView reservationsList;
    private ArrayList<Transaction> reservations;
    private ReservationsListAdapter reservationsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_reservations);
        super.onCreateDrawer();

        // Get references to UI views
        reservationsList = (ListView) findViewById(R.id.reservations_list);

        // Add adapter to ListView
        reservations = new ArrayList<Transaction>();
        reservationsListAdapter = new ReservationsListAdapter(ReservationsActivity.this, reservations);
        reservationsList.setAdapter(reservationsListAdapter);

        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.child("Transactions").orderByChild("seekerId").equalTo(User.getInstance().getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> spots = (Map<String,Object>)dataSnapshot.getValue();
                if (spots == null) {return;}
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Transaction t = snapshot.getValue(Transaction.class);
                    processTransaction(t);
                    reservationsListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
    private void processTransaction(Transaction t) {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currTime = dateFormat.format(today);
        if (currTime.compareTo(t.getEndTime()) > 0) {
            return;
        }

        for (int i = 0; i < reservations.size(); ++i) {
            // If item exists, replace it
            if (reservations.get(i).getTransactionId().equals(t.getTransactionId()))
            {
                reservations.set(i, t);
                return;
            }
        }
        // transaction was not part of array
        reservations.add(t);
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

    protected class ReservationsListAdapter extends ArrayAdapter<Transaction> {

        public ReservationsListAdapter(Context context, ArrayList<Transaction> results) {
            super(context, 0, results);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.seeker_reservation_item, parent, false);
            }

            // Get data item
            Transaction transaction = getItem(position);

            // Set image
            ImageView spotImageView = (ImageView) convertView.findViewById(R.id.parking_spot_image);
            spotImageView.setImageResource(0);

            Picasso.with(getContext())
                    .load(transaction.getPhotoUrl())
                    .placeholder(R.drawable.progress_animation)
                    .resize(150, 150)
                    .centerCrop()
                    .into(spotImageView);

            // Set date/time
            TextView dateText = (TextView) convertView.findViewById(R.id.date_time_range);
            dateText.setText(transaction.getStartTime() + " - " + transaction.getEndTime());

            // Set Address
            TextView addrText = (TextView) convertView.findViewById(R.id.address);
            addrText.setText(transaction.getAddress());

            return convertView;
        }
    }
}