package edu.usc.parknpay.mutual;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        // Set payment info list adapter
        paymentMethods = new ArrayList<PaymentMethod>();
        paymentInfoListAdapter = new PaymentInfoListAdapter(PaymentInfoActivity.this, paymentMethods);
        paymentInfoList.setAdapter(paymentInfoListAdapter);

        // Add button listener
        addPaymentMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Proceed to registration screen
                Intent intent = new Intent(PaymentInfoActivity.this, AddPaymentMethodActivity.class);
                startActivity(intent);
            }
        });
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
}
