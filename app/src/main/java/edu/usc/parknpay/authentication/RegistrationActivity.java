package edu.usc.parknpay.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.DatabaseTalker;
import edu.usc.parknpay.database.User;

public class RegistrationActivity extends AppCompatActivity {

    static final int PICK_PHOTO = 1;
    Uri selectedImage;
    ImageView profilePicture;
    TextView editButton;
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPassword;
    EditText editConfirmPassword;
    EditText editPhoneNumber;
    EditText editDriversLicense;

    DatabaseTalker databaseTalker;
    User user;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        databaseTalker = DatabaseTalker.getInstance();

        // Get references to UI views
        profilePicture = (ImageView) findViewById(R.id.pic);
        editButton = (TextView) findViewById(R.id.edit_button);
        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editDriversLicense = (EditText) findViewById(R.id.edit_drivers_license);

        // Add view listeners
        addListeners();
    }

    /** Called when the user clicks the register account button. */
    public void registerUser(View view) {
        // Communicate with Firebase to authenticate the user.
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();
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

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber.length() != 10) {
            Toast.makeText(RegistrationActivity.this, "Please enter a phone number of length 10", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImage == null) {
            Toast.makeText(RegistrationActivity.this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
            return;
        }

        user = new User(
                firstName,
                lastName,
                email,
                phoneNumber,
                driversLicense,
                5,
                1,
                true
        );

        // If registration is successful, proceed to owner/seeker selection view.
        intent = new Intent(this, SetDefaultModeActivity.class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
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


                            // Put the profile photo into firebase
                            StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child(userId).child("profile");
                            firebaseStorage.putFile(selectedImage);

                            User.createUser(user);
                            Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void addListeners() {
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , PICK_PHOTO);
            }

        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case PICK_PHOTO:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    profilePicture.setImageURI(selectedImage);
                }
                break;
        }
    }
}
