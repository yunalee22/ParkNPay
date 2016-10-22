package edu.usc.parknpay.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.DatabaseTalker;
import edu.usc.parknpay.database.User;

public class RegistrationActivity extends AppCompatActivity {
    DatabaseTalker databaseTalker;
    User user;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        databaseTalker = DatabaseTalker.getInstance();
    }

    /** Called when the user clicks the register account button. */
    public void registerUser(View view) {

        // Communicate with Firebase to authenticate the user.
        EditText editFirstName = (EditText) findViewById(R.id.edit_first_name);
        String firstName = editFirstName.getText().toString().trim();
        EditText editLastName = (EditText) findViewById(R.id.edit_last_name);
        String lastName = editLastName.getText().toString().trim();
        EditText editEmail = (EditText) findViewById(R.id.edit_email);
        String email = editEmail.getText().toString().trim();
        EditText editPassword = (EditText) findViewById(R.id.edit_password);
        String password = editPassword.getText().toString();
        EditText editConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);
        String confirmPassword = editConfirmPassword.getText().toString();
        EditText editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        EditText editDriversLicense = (EditText) findViewById(R.id.edit_drivers_license);
        String driversLicense = editDriversLicense.getText().toString().trim();

        // Check if inputs are entered
        if (TextUtils.isEmpty(email)
            || TextUtils.isEmpty(password)
            || TextUtils.isEmpty(firstName)
            || TextUtils.isEmpty(lastName)
            || TextUtils.isEmpty(confirmPassword)
            || TextUtils.isEmpty(phoneNumber)
            || TextUtils.isEmpty(driversLicense)
        ) {
            Toast.makeText(RegistrationActivity.this, "Please do not leave any fields empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirmPassword)) {
            Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if(phoneNumber.length() != 10) {
            Toast.makeText(RegistrationActivity.this, "Please enter a phone number of length 10", Toast.LENGTH_SHORT).show();
            return;
        }

        user = new User(
                firstName,
                lastName,
                email,
                phoneNumber,
                driversLicense,
                5,
                1
        );

        // If registration is successful, proceed to owner/seeker selection view.
        intent = new Intent(this, SetDefaultModeActivity.class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        Task<AuthResult> result = firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
                            // Get the newly generated user
                            String userId = task.getResult().getUser().getUid();
                            // Add newly generated user id to the user passed in
                            user.setId(userId);

                            // Get correct firebase ref
                            Ref.child("Users").child(userId).setValue(user);
                            Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
