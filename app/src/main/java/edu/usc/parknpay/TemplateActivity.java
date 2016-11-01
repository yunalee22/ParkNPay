package edu.usc.parknpay;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import edu.usc.parknpay.owner.AccountSettingsActivity;
import edu.usc.parknpay.owner.PaymentInfoActivity;
import edu.usc.parknpay.database.User;

public class TemplateActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArrayAdapter<String> drawerAdapter;
    private User u;

    protected void onCreateDrawer() {
        u = User.getInstance();
        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Add drawer functionality
        drawerList = (ListView) findViewById(R.id.left_drawer);
        addDrawerItems();
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        //Toast.makeText(TemplateActivity.this,"Temp Activity being added",Toast.LENGTH_LONG).show();
    }

    private void addDrawerItems() {
        String[] menuItems = {
                "Reservations",
                "History",
                "Payment",
                "Settings",
                "Switch Role",
                "Log Out"
        };
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
                    if (u.getIsCurrentlySeeker()) {
                        //Toast.makeText(TemplateActivity.this, "currentlySeeker True to False", Toast.LENGTH_SHORT).show();
                        u.setIsCurrentlySeeker(false);
                        drawerLayout.closeDrawers();

                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.owner.OwnerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    } else {
                        u.setIsCurrentlySeeker(true);
                        //Toast.makeText(TemplateActivity.this, "currentlySeeker False to True", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();

                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.seeker.SeekerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                    break;
                }
                case 5:     // Log Out
                {
                    User.setInstance(null);
                    intent = new Intent(getApplicationContext(), edu.usc.parknpay.authentication.LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                }
            }

        }
    }



}
