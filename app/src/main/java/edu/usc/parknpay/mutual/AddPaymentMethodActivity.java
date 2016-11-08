package edu.usc.parknpay.mutual;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        paymentMethods.add("Venmo");
        paymentMethods.add("Credit Card");
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentMethods);
        paymentMethodTypeSpinner.setAdapter(spinnerAdapter);

        // Add button listener
        addPaymentMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add new payment method to database
                // Get spinner choice
                String paymentInformation = paymentInformationField.getText().toString();

                // Proceed to registration screen
                Intent intent = new Intent(AddPaymentMethodActivity.this, PaymentInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
