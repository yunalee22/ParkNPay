package edu.usc.parknpay;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TemplateActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ListView drawerList;
    public ArrayAdapter<String> mAdapter;

    private void addDrawerItems() {
        String[] menuItems = {"Reservations", "History", "Payment", "Settings", "Use App as Owner", "Log Out"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems);
        drawerList.setAdapter(mAdapter);
    }

    protected void onCreateDrawer()  {
        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        //adding drawer functionality
        drawerList = (ListView) findViewById(R.id.left_drawer);
        addDrawerItems();
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //code for the menu buttons
            Intent intent;
            switch(position) {
                case 0:
                    //Reservations
                    break;
                case 1:
                    //History
                    break;
                case 2:
                    intent = new Intent(getApplicationContext(), edu.usc.parknpay.mutual.PaymentInfoActivity.class);
                    //intent.putExtra("shopCash", cash);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(getApplicationContext(), edu.usc.parknpay.mutual.AccountSettingsActivity.class);
                    //intent.putExtra("shopCash", cash);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    break;
                case 4:
                    //Swap to owner/seeker
                    break;
                case 5:
                    //logout
                    break;
            }

        }
    }



}
