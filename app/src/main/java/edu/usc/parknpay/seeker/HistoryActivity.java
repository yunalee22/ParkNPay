package edu.usc.parknpay.seeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class HistoryActivity extends TemplateActivity {

    private ListView historyList;
    private ArrayList<Transaction> history;
    private HistoryListAdapter historyListAdapter;
    private DatabaseReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_history);
        super.onCreateDrawer();

        // Get references to UI views
        historyList = (ListView) findViewById(R.id.history_list);

        // Add adapter to ListView
        // Add adapter to ListView
        history = new ArrayList<Transaction>();
        historyListAdapter = new HistoryListAdapter(HistoryActivity.this, history);
        historyList.setAdapter(historyListAdapter);

        String userId = User.getInstance().getId();
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.child("Transactions").orderByChild("seekerId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> spots = (Map<String,Object>)dataSnapshot.getValue();
                if (spots == null) {return;}
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Transaction t = snapshot.getValue(Transaction.class);
                    processTransaction(t);
                    historyListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction t = history.get(position);

                // Get current date and time
                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String currTime = dateFormat.format(today);

                if(!t.isRated() && currTime.compareTo(t.getEndTime()) >= 0) {
                    Intent intent = new Intent(getApplicationContext(), edu.usc.parknpay.seeker.RateActivity.class);
                    intent.putExtra("transaction", t);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });
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


    private void processTransaction(Transaction t) {
        for (int i = 0; i < history.size(); ++i) {
            // If item exists, replace it
            if (history.get(i).getTransactionId().equals(t.getTransactionId()))
            {
                history.set(i, t);
                return;
            }
        }
        // transaction was not part of array
        history.add(t);
    }

    protected class HistoryListAdapter extends ArrayAdapter<Transaction> {

        public HistoryListAdapter(Context context, ArrayList<Transaction> results) {
            super(context, 0, results);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.seeker_history_item, parent, false);
            }
            // Get data item
            Transaction transaction = getItem(position);

            // Set image
            ImageView spotImageView = (ImageView) convertView.findViewById(R.id.history_image);
            spotImageView.setImageResource(0);
            Picasso.with(getContext())
                    .load(transaction.getPhotoUrl())
                    .placeholder(R.drawable.progress_animation)
                    .resize(150, 150)
                    .centerCrop()
                    .into(spotImageView);

            // Set Address
            TextView addrText = (TextView) convertView.findViewById(R.id.history_address);
            addrText.setText(transaction.getAddress());

            // Set Price
            TextView priceText = (TextView) convertView.findViewById(R.id.history_price);
            priceText.setText("$" + transaction.getPrice());

            // Set Date
            TextView dateText = (TextView) convertView.findViewById(R.id.history_dates);
            dateText.setText(transaction.getStartTime() + " - " + transaction.getEndTime());

            TextView ownerText = (TextView) convertView.findViewById(R.id.history_ownerr);
            ownerText.setText(transaction.getOwnerName());

            // Get current date and time
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currTime = dateFormat.format(today);

            // If transaction is rated or has not expired, hide (tap to rate)
            if(transaction.isRated() || currTime.compareTo(transaction.getEndTime()) < 0) {
                TextView rateText = (TextView) convertView.findViewById(R.id.history_rate);
                rateText.setVisibility(View.GONE);
            }

            return convertView;
        }
    }
}