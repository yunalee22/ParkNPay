package edu.usc.parknpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import edu.usc.parknpay.database.User;
import edu.usc.parknpay.owner.AccountSettingsActivity;
import edu.usc.parknpay.owner.PaymentInfoActivity;

public class TemplateActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    private TextView userName, balance;
    private ImageView userPic;
    private ListView drawerList;
    protected LinearLayout drawer;
    private ArrayAdapter<String> drawerAdapter;
    private User u;
//    private ImageView menuButton;

    @Override
    protected void onNewIntent(Intent intent)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        balance.setText("$ " + df.format(u.getBalance()));
    }

    private void refreshBalanceView()
    {
        DecimalFormat df = new DecimalFormat("#.00");
        balance.setText("$ " + df.format(u.getBalance()));
    }

    protected void onCreateDrawer() {
        u = User.getInstance();

        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (LinearLayout) findViewById(R.id.drawerll);
        userName = (TextView) findViewById(R.id.drawer_name);
        balance = (TextView) findViewById(R.id.drawer_balance);
        userPic = (ImageView) findViewById(R.id.drawer_pic);
//        menuButton = (ImageView) findViewById(R.id.menu);
//        menuButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v)
//            {
//                drawerLayout.openDrawer(drawer);
//            }
//
//        });

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
                "Home",
                "Reservations",
                "History",
                "Payment",
                "Settings",
                "Switch Role",
                "Log Out"
        };
        drawerAdapter = new ArrayAdapter<String>(TemplateActivity.this, R.layout.drawer_item, menuItems);
        drawerList.setAdapter(drawerAdapter);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerLayout.closeDrawer(drawer);
            Intent intent;
            switch(position) {
                case 0: //Home
                {
                    if (u.getIsCurrentlySeeker()) {
                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.seeker.SeekerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.owner.OwnerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                    break;
                }
                case 1:     // Reservations
                {
                    if (u.getIsCurrentlySeeker()) {
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
                case 2:     // History
                {
                    if (u.getIsCurrentlySeeker()) {
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
                case 3:     // Payment
                {
                    intent = new Intent(getApplicationContext(), PaymentInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                }
                case 4:     // Settings
                {
                    intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                }
                case 5:     // Use App as Owner
                {
                    //u.changeBalance(10);
                    //refreshBalanceView();


                    if (u.getIsCurrentlySeeker()) {
                        //Toast.makeText(TemplateActivity.this, "currentlySeeker True to False", Toast.LENGTH_SHORT).show();
                        u.setIsCurrentlySeeker(false);
                        //drawerLayout.closeDrawers();
                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.owner.OwnerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        u.setIsCurrentlySeeker(true);
                        //Toast.makeText(TemplateActivity.this, "currentlySeeker False to True", Toast.LENGTH_SHORT).show();
                        //drawerLayout.closeDrawers();
                        intent = new Intent(getApplicationContext(), edu.usc.parknpay.seeker.SeekerMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    break;
                }
                case 6:     // Log Out
                {
                    User.setInstance(null);
                    intent = new Intent(getApplicationContext(), edu.usc.parknpay.authentication.LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                }
            }

        }
    }

    // Calculates four corners of view
    private Rect getLocationOnScreen(View view) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        view.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + view.getWidth();
        mRect.bottom = location[1] + view.getHeight();

        return mRect;
    }

    // Hides the soft keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        boolean handleReturn = super.dispatchTouchEvent(ev);
        View view = getCurrentFocus();
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if(view instanceof EditText){
            View innerView = getCurrentFocus();

            if (ev.getAction() == MotionEvent.ACTION_UP &&
                    !getLocationOnScreen(innerView).contains(x, y)) {

                InputMethodManager input = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }

        return handleReturn;
    }

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

        // Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
}
