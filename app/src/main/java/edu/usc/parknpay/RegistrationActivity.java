package edu.usc.parknpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    /** Called when the user clicks the register account button. */
    public void registerUser(View view) {

        // Communicate with Firebase to authenticate the user.
        EditText editFirstName = (EditText) findViewById(R.id.edit_first_name);
        String firstName = editFirstName.getText().toString();
        EditText editLastName = (EditText) findViewById(R.id.edit_last_name);
        String lastName = editFirstName.getText().toString();
        EditText editEmail = (EditText) findViewById(R.id.edit_email);
        String email = editEmail.getText().toString();
        EditText editPassword = (EditText) findViewById(R.id.edit_password);
        String password = editPassword.getText().toString();
        EditText editConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);
        String confirmPassword = editConfirmPassword.getText().toString();
        EditText editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        String phoneNumber = editPhoneNumber.getText().toString();
        EditText editDriversLicense = (EditText) findViewById(R.id.edit_drivers_license);
        String driversLicense = editDriversLicense.getText().toString();

        // If registration is successful, proceed to owner/seeker selection view.
        Intent seekerIntent = new Intent(this, SeekerMainActivity.class);
        Intent ownerIntent = new Intent(this, OwnerMainActivity.class);
        startActivity(seekerIntent);

        // If registration is unsuccessful, display an error message.
        TextView errorMessage = new TextView(this);
        errorMessage.setText("Failed to register user.");
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_main);
        layout.addView(errorMessage);
    }
}
