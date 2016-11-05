package edu.usc.parknpay.owner;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.User;

public class PaymentInfoActivity extends TemplateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_payment_info);

        setUpToolbar("Payment");
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
        Toast.makeText(PaymentInfoActivity.this, "You've added $100 to your balance", Toast.LENGTH_SHORT).show();
    }

    public void cashOut(View view) {
        double balance = User.getInstance().getBalance();
        User.getInstance().changeBalance(-1 * balance);
        Toast.makeText(PaymentInfoActivity.this, "You withdrew $" + balance + " from your account", Toast.LENGTH_SHORT).show();
    }
}
