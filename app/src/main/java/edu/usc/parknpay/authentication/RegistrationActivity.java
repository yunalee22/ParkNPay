package edu.usc.parknpay.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.DatabaseTalker;
import edu.usc.parknpay.database.User;

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_PHOTO = 1;
    private Uri selectedImage;
    private ImageView profilePicture;
    private EditText editFirstName, editLastName, editEmail,
            editPassword, editConfirmPassword,
            editPhoneNumber, editDriversLicense;

    private DatabaseTalker databaseTalker;
    private User user;
    DatabaseReference Ref;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_registration);

        // Get database talker instance
        databaseTalker = DatabaseTalker.getInstance();

        // Get references to UI views
        profilePicture = (ImageView) findViewById(R.id.pic);
        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editDriversLicense = (EditText) findViewById(R.id.edit_drivers_license);

        // Add text changed listeners
        editEmail.addTextChangedListener(new LoginTextWatcher(editEmail));
        editPassword.addTextChangedListener(new LoginTextWatcher(editPassword));

        // Initialize progress dialog
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait logging in...");
        progress.setCancelable(false);

        // Add view listeners
        addListeners();
    }

    /** Called when the user clicks the register account button. */
    public void registerUser(View view) {

        progress.show();

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
            || TextUtils.isEmpty(driversLicense)) {
            Toast.makeText(RegistrationActivity.this, "Please do not leave any fields empty", Toast.LENGTH_SHORT).show();
            progress.dismiss();
            return;
        }

        // Check for password reqs
        // 10 char, 1 special char
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        boolean b = m.find();
        //check for special character
        if(!b) {
            Toast.makeText(RegistrationActivity.this, "Password requires a special non-alphanumerical character", Toast.LENGTH_SHORT).show();
            progress.dismiss();
            return;
        }

        // Check for password length
        if(password.length() < 10) {
            Toast.makeText(RegistrationActivity.this, "Password should be 10 or more characters long", Toast.LENGTH_SHORT).show();
            progress.dismiss();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            progress.dismiss();
            return;
        }

        if (phoneNumber.length() != 10) {
            Toast.makeText(RegistrationActivity.this, "Please enter a phone number of length 10", Toast.LENGTH_SHORT).show();
            progress.dismiss();
            return;
        }

        if (selectedImage == null) {
            Toast.makeText(RegistrationActivity.this, "Please upload a profile picture", Toast.LENGTH_SHORT).show();
            progress.dismiss();
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
                true,
                500.00
        );

        // If authentication_registration is successful, proceed to owner/seeker selection view.
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Ref = FirebaseDatabase.getInstance().getReference();

                    // Get the newly generated user
                    String userId = task.getResult().getUser().getUid();

                    // Add newly generated user id to the user passed in
                    user.setId(userId);

                    // Insert profile photo into database
                    StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child(userId).child("profile");
                    firebaseStorage.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            user.setProfilePhotoURL(taskSnapshot.getDownloadUrl().toString());

                            // Get correct firebase ref
                            Ref.child("Users").child(user.getId()).setValue(user);

                            User.createUser(user);

                            // Proceed to default mode selection view
                            Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this, SetDefaultModeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
                }
            });
    }

    protected void addListeners() {
        profilePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_PHOTO);
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

    private boolean validateEmail() {
        return true;
    }

    private boolean validatePassword() {
        return true;
    }

    private class LoginTextWatcher implements TextWatcher {

        private View view;

        private LoginTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_email:
                    validateEmail();
                    break;
                case R.id.edit_password:
                    validatePassword();
                    break;
            }
        }
    }
}
