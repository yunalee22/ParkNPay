package edu.usc.parknpay.owner;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.R;

/**
 * Created by Bobo on 10/14/2016.
 */

public class PaymentInfoActivity extends TemplateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_payment_info);

        setUpToolbar();
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

    private void setUpToolbar() {

        // Set toolbar as action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Customize toolbar
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Payment");

        // Enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
}
