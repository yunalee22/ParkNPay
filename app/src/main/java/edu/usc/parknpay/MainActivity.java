package edu.usc.parknpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user clicks the login button. */
    public void authenticateUser(View view) {

        // Communicate with Firebase to authenticate the user.
        EditText editEmail = (EditText) findViewById(R.id.edit_email);
        String email = editEmail.getText().toString();
        EditText editPassword = (EditText) findViewById(R.id.edit_password);
        String password = editPassword.getText().toString();

        // If authentication is successful, proceed to owner/seeker main view
        // (whichever is user's default).
        Intent seekerIntent = new Intent(this, SeekerMainActivity.class);
        Intent ownerIntent = new Intent(this, OwnerMainActivity.class);
        startActivity(seekerIntent);

        // If authentication is unsuccessful, display an error message.
        TextView errorMessage = new TextView(this);
        errorMessage.setText("Failed to authenticate user.");
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_main);
        layout.addView(errorMessage);
    }

    /** Called when the user selects the registration option. */
    public void displayRegistrationScreen(View view) {

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);


    }
}
