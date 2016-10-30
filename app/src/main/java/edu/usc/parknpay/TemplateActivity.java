package edu.usc.parknpay;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.usc.parknpay.owner.AccountSettingsActivity;
import edu.usc.parknpay.owner.PaymentInfoActivity;

public class TemplateActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArrayAdapter<String> drawerAdapter;

    protected void onCreateDrawer() {

        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Add drawer functionality
        drawerList = (ListView) findViewById(R.id.left_drawer);
        addDrawerItems();
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void addDrawerItems() {
        String[] menuItems = {"Reservations", "History", "Payment", "Settings", "Use App as Owner", "Log Out"};
        drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        drawerList.setAdapter(drawerAdapter);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            switch(position) {
                case 0:     // Reservations
                {
                    break;
                }
                case 1:     // History
                {
                    break;
                }
                case 2:     // Payment
                {
                    intent = new Intent(getApplicationContext(), PaymentInfoActivity.class);
                    //intent.putExtra("shopCash", cash);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                }
                case 3:     // Settings
                {
                    intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);
                    //intent.putExtra("shopCash", cash);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                }
                case 4:     // Use App as Owner
                {
                    // Swap to owner/seeker
                    break;
                }
                case 5:     // Log Out
                {
                    break;
                }
            }

        }
    }



}
