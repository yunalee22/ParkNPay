package edu.usc.parknpay.mutual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.usc.parknpay.R;
import edu.usc.parknpay.utility.TemplateActivity;

public class AddPaymentMethodActivity extends TemplateActivity {

    EditText paymentInformationField;
    Button addPaymentMethodButton;

    Spinner paymentMethodTypeSpinner;
    ArrayList<String> paymentMethods;
    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mutual_add_payment_method);

        setUpToolbar("Add Payment Method");

        // Get references to UI views
        paymentMethodTypeSpinner = (Spinner) findViewById(R.id.paymentMethodTypeSpinner);
        paymentInformationField = (EditText) findViewById(R.id.paymentInformationField);
        addPaymentMethodButton = (Button) findViewById(R.id.addPaymentMethodButton);

        // Set up spinner
        paymentMethods = new ArrayList<String>();
        paymentMethods.add("Credit Card");
        paymentMethods.add("ParkNPay Gift Card");
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentMethods);
        paymentMethodTypeSpinner.setAdapter(spinnerAdapter);

        // Add button listener
        addPaymentMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get spinner choice
                String paymentMethodType = paymentMethodTypeSpinner.getSelectedItem().toString();
                String paymentInformation = paymentInformationField.getText().toString();
                String paymentMethod = paymentMethodType + ": " + paymentInformation;

                // Return to payment info screen
                Intent intent = new Intent(AddPaymentMethodActivity.this, PaymentInfoActivity.class);
                intent.putExtra("payment method", paymentMethod);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
