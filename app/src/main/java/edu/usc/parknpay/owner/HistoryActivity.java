package edu.usc.parknpay.owner;

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

import java.util.ArrayList;
import java.util.Map;

import edu.usc.parknpay.R;
import edu.usc.parknpay.utility.TemplateActivity;
import edu.usc.parknpay.database.Transaction;
import edu.usc.parknpay.database.User;

/**
 * Created by Bobo on 10/31/2016.
 */

public class HistoryActivity extends TemplateActivity {
    private ListView historyList;
    private ArrayList<Transaction> history;
    private edu.usc.parknpay.owner.HistoryActivity.HistoryListAdapter historyListAdapter;
    private DatabaseReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_history);

        setUpToolbar("History");

        // Add adapter to ListView
        historyList = (ListView) findViewById(R.id.history_list);
        history = new ArrayList<Transaction>();
        historyListAdapter = new edu.usc.parknpay.owner.HistoryActivity.HistoryListAdapter(edu.usc.parknpay.owner.HistoryActivity.this, history);
        historyList.setAdapter(historyListAdapter);

        String userId = User.getInstance().getId();
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.child("Transactions").orderByChild("ownerId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.owner_history_item, parent, false);
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

            TextView seekerText = (TextView) convertView.findViewById(R.id.history_seeker);
            seekerText.setText(transaction.getSeekerName());


            return convertView;
        }
    }
}
