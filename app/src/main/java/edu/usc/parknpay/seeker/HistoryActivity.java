package edu.usc.parknpay.seeker;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpotPost;

public class HistoryActivity extends TemplateActivity {

    private ListView historyList;
    private ArrayList<ParkingSpotPost> history;
    private HistoryListAdapter historyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_history);

        // Get references to UI views
        historyList = (ListView) findViewById(R.id.history_list);

        // Add adapter to ListView
        // Add adapter to ListView
        history = new ArrayList<ParkingSpotPost>();
        historyListAdapter = new HistoryListAdapter(HistoryActivity.this, history);
        historyList.setAdapter(historyListAdapter);
    }

    // Call this function to update the history view
    private void loadReservations(ArrayList<ParkingSpotPost> history) {

        historyListAdapter.clear();
        historyListAdapter.addAll(history);
        historyListAdapter.notifyDataSetChanged();
    }

    protected class HistoryListAdapter extends ArrayAdapter<ParkingSpotPost> {

        public HistoryListAdapter(Context context, ArrayList<ParkingSpotPost> results) {
            super(context, 0, results);
        }

        // View lookup cache
        private class ViewHolder {
            TextView address;
            TextView date;
            TextView price;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Get data item
            ParkingSpotPost parkingSpotPost = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            HistoryActivity.HistoryListAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new HistoryActivity.HistoryListAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.seeker_history_item, parent, false);
                viewHolder.address = (TextView) findViewById(R.id.address);
                viewHolder.date = (TextView) findViewById(R.id.date);
                viewHolder.price = (TextView) findViewById(R.id.price);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (HistoryActivity.HistoryListAdapter.ViewHolder) convertView.getTag();
            }

            // Populate data into the views
//            viewHolder.address.setText(parkingSpotPost.get);
//            viewHolder.date.setText(parkingSpotPost.get);
//            viewHolder.price.setText(parkingSpotPost.get);

            return convertView;
        }
    }
}