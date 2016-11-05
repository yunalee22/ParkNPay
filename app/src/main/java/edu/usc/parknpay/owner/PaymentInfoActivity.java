package edu.usc.parknpay.owner;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.User;

//import android.widget.Toolbar;

/**
 * Created by Bobo on 10/14/2016.
 */

public class PaymentInfoActivity extends TemplateActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_payment_info);
        super.onCreateDrawer();
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

    public void addMoney(View view) {
        User.getInstance().changeBalance(100);
    }

    public void cashOut(View view) {
        double balance = User.getInstance().getBalance();
        User.getInstance().changeBalance(-1 * balance);
    }
}
