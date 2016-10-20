package edu.usc.parknpay.mutual;

import android.os.Bundle;
import android.view.MenuItem;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.R;

/**
 * Created by Bobo on 10/14/2016.
 */

public class PaymentInfoActivity extends TemplateActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_info);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


}
