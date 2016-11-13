package edu.usc.parknpay.mutual;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.ParkingSpotPost;
import edu.usc.parknpay.database.PaymentMethod;
import edu.usc.parknpay.utility.TemplateActivity;
import edu.usc.parknpay.database.User;

public class PaymentInfoActivity extends TemplateActivity {

    private TextView balanceTextView;
    private Button addFundsButton;
    private Button withdrawFundsButton;

    private ListView paymentInfoList;
    private TextView addPaymentMethodButton;

    private ArrayList<PaymentMethod> paymentMethods;
    private PaymentInfoListAdapter paymentInfoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mutual_payment_info);

        setUpToolbar("Payment");

        // Get references to UI views
        paymentInfoList = (ListView) findViewById(R.id.paymentInfoList);
        addPaymentMethodButton = (TextView) findViewById(R.id.addPaymentMethodButton);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        addFundsButton = (Button) findViewById(R.id.addFundsButton);
        withdrawFundsButton = (Button) findViewById(R.id.withdrawFundsButton);

        // Set payment info list adapter
        paymentMethods = new ArrayList<PaymentMethod>();
        paymentInfoListAdapter = new PaymentInfoListAdapter(PaymentInfoActivity.this, paymentMethods);
        paymentInfoList.setAdapter(paymentInfoListAdapter);

        // Add view listeners
        addListeners();
    }

    private void addFunds(double amount) {

        // Update database
        User.getInstance().changeBalance(100);
        Toast.makeText(PaymentInfoActivity.this, "You've added $100 to your balance", Toast.LENGTH_SHORT).show();

        // Update user's balance in UI
//        balanceTextView.setText();
    }

    private void withdrawFunds(double amount) {

        // Update database
        double balance = User.getInstance().getBalance();
        User.getInstance().changeBalance(-1 * balance);
        Toast.makeText(PaymentInfoActivity.this, "You withdrew $" + balance + " from your account", Toast.LENGTH_SHORT).show();

        // Update user's balance in UI
//        balanceTextView.setText();
    }

    private void addListeners() {

        // Called when add funds button is pressed
        addFundsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFundsDialog();
            }
        });

        // Called when withdraw funds button is pressed
        withdrawFundsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWithdrawFundsDialog();
            }
        });

        // Called when add payment method button is pressed
        addPaymentMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Proceed to add payment method activity
                Intent intent = new Intent(PaymentInfoActivity.this, AddPaymentMethodActivity.class);
                startActivity(intent);
            }
        });

    }

    protected class PaymentInfoListAdapter extends ArrayAdapter<PaymentMethod> {

        public PaymentInfoListAdapter(Context context, ArrayList<PaymentMethod> paymentMethods) {
            super(context, 0, paymentMethods);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.mutual_payment_info_list_item, parent, false);
            }

            // Get payment method
            PaymentMethod paymentMethod = getItem(position);

            // Populate list item with data
            ImageView paymentMethodImage = (ImageView) convertView.findViewById(R.id.paymentMethodImage);
            paymentMethodImage.setImageDrawable(ContextCompat.getDrawable(PaymentInfoActivity.this, R.drawable.venmo_logo));

            TextView paymentMethodType = (TextView) convertView.findViewById(R.id.paymentMethodType);
            paymentMethodType.setText(paymentMethod.getPaymentMethodType());

            TextView paymentInformation = (TextView) convertView.findViewById(R.id.paymentInformation);
            paymentInformation.setText(paymentMethod.getPaymentInformation());

            return convertView;
        }
    }

    private void showAddFundsDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mutual_payment_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText amountEditText = (EditText) dialogView.findViewById(R.id.amountEditText);

        dialogBuilder.setTitle("Enter transaction information");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                double amount = Double.parseDouble(amountEditText.getText().toString());
                PaymentInfoActivity.this.addFunds(amount);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void showWithdrawFundsDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mutual_payment_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText amountEditText = (EditText) dialogView.findViewById(R.id.amountEditText);

        dialogBuilder.setTitle("Enter transaction information");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                double amount = Double.parseDouble(amountEditText.getText().toString());
                PaymentInfoActivity.this.withdrawFunds(amount);
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
