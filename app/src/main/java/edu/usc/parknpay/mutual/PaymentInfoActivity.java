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
import android.widget.Spinner;
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

    final static int ADD_PAYMENT_METHOD = 0;

    private TextView balanceTextView;
    private Button addFundsButton;
    private Button withdrawFundsButton;

    private ListView paymentInfoList;
    private TextView addPaymentMethodButton;

    private ArrayList<String> paymentMethods;
    private ArrayAdapter<String> paymentInfoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mutual_payment_info);

        setUpToolbar("Payment");

        // Get references to UI views
        paymentInfoList = (ListView) findViewById(R.id.paymentInfoList);
        addPaymentMethodButton = (TextView) findViewById(R.id.addPaymentMethodButton);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        balanceTextView.setText(Double.toString(User.getInstance().getBalance()));
        addFundsButton = (Button) findViewById(R.id.addFundsButton);
        withdrawFundsButton = (Button) findViewById(R.id.withdrawFundsButton);

        // Set payment info list adapter
        paymentMethods = new ArrayList<String>();
        paymentInfoListAdapter = new ArrayAdapter<String>
                (PaymentInfoActivity.this, android.R.layout.simple_list_item_1, paymentMethods);
        paymentInfoList.setAdapter(paymentInfoListAdapter);

        // Add view listeners
        addListeners();
    }

    private void addFunds(double amount) {

        // Update database
        User.getInstance().changeBalance(amount);
        Toast.makeText(PaymentInfoActivity.this, "You've added $" + amount + " to your balance", Toast.LENGTH_SHORT).show();

        // Update user's balance in UI
        balanceTextView.setText(Double.toString(User.getInstance().getBalance()));
    }

    private void withdrawFunds(double amount) {

        // Update database
        User.getInstance().changeBalance(-1 * amount);
        Toast.makeText(PaymentInfoActivity.this, "You withdrew $" + balance + " from your account", Toast.LENGTH_SHORT).show();

        // Update user's balance in UI
        balanceTextView.setText(Double.toString(User.getInstance().getBalance()));
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
                startActivityForResult(intent, ADD_PAYMENT_METHOD);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_PAYMENT_METHOD && resultCode == RESULT_OK && data != null) {
            String paymentMethod = data.getStringExtra("payment method");

            // Add new payment method to list view
            paymentMethods.add(paymentMethod);
            paymentInfoListAdapter.notifyDataSetChanged();
        }
    }

    private void showAddFundsDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mutual_payment_dialog, null);
        dialogBuilder.setView(dialogView);

        // Set up spinner
        Spinner paymentMethodSpinner = (Spinner) dialogView.findViewById(R.id.paymentMethodSpinner);
        ArrayList<String> paymentMethods = new ArrayList<String>();
        paymentMethods.add("Credit Card");
        paymentMethods.add("ParkNPay Gift Card");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, paymentMethods);
        paymentMethodSpinner.setAdapter(spinnerAdapter);

        final EditText amountEditText = (EditText) dialogView.findViewById(R.id.amountEditText);

        dialogBuilder.setTitle("Enter transaction information");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (amountEditText.getText().toString().matches("")) {
                    Toast.makeText(PaymentInfoActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();;
                    return;
                }
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

        // Set up spinner
        Spinner paymentMethodSpinner = (Spinner) dialogView.findViewById(R.id.paymentMethodSpinner);
        ArrayList<String> paymentMethods = new ArrayList<String>();
        paymentMethods.add("Credit Card");
        paymentMethods.add("ParkNPay Gift Card");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, paymentMethods);
        paymentMethodSpinner.setAdapter(spinnerAdapter);

        final EditText amountEditText = (EditText) dialogView.findViewById(R.id.amountEditText);

        dialogBuilder.setTitle("Enter transaction information");
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (amountEditText.getText().toString().matches("")) {
                    Toast.makeText(PaymentInfoActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                double amount = Double.parseDouble(amountEditText.getText().toString());
                PaymentInfoActivity.this.withdrawFunds(amount);
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }
}
