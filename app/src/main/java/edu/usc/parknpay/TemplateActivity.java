package edu.usc.parknpay;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import edu.usc.parknpay.owner.AccountSettingsActivity;
import edu.usc.parknpay.owner.PaymentInfoActivity;
import edu.usc.parknpay.database.User;

public class TemplateActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView userName, balance;
    private ImageView userPic;
    private ListView drawerList;
    private ArrayAdapter<String> drawerAdapter;
    private User u;

    protected void onCreateDrawer() {
        u = User.getInstance();
        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        userName = (TextView) findViewById(R.id.drawer_name);
        balance = (TextView) findViewById(R.id.drawer_balance);
        userPic = (ImageView) findViewById(R.id.drawer_pic);

        // Add drawer functionality
        drawerList = (ListView) findViewById(R.id.left_drawer);
        addDrawerItems();
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        //Toast.makeText(TemplateActivity.this,"Temp Activity being added",Toast.LENGTH_LONG).show();
        userName.setText(u.getFirstName() + " " + u.getLastName());
        DecimalFormat df = new DecimalFormat("#.00");
        balance.setText("$ " + df.format(u.getBalance()));

        // Load profile image
        Picasso.with(this)
                .load(u.getProfilePhotoURL())
                .placeholder(R.drawable.progress_animation)
                .into(userPic);
    }

    private void addDrawerItems() {
        String[] menuItems = {
                "Reservations",
                "History",
                "Payment",
                "Settings",
                (User.getInstance().getIsCurrentlySeeker()) ? "Switch to Owner" : "Switch to Seeker",
                "Log Out"
        };
        drawerAdapter = new ArrayAdapter<String>(this, R.layout.drawer_item, menuItems);
        drawerList.setAdapter(drawerAdapter);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            switch(position) {
                case 0:     // Reservations
                {
                    if (u.isSeeker()) {
                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.seeker.ReservationsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.owner.ReservationsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                    break;
                }
                case 1:     // History
                {
                    if (u.isSeeker()) {
                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.seeker.HistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.owner.HistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                    break;
                }
                case 2:     // Payment
                {
                    intent = new Intent(getApplicationContext(), PaymentInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                }
                case 3:     // Settings
                {
                    intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);

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
