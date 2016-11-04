package edu.usc.parknpay.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import edu.usc.parknpay.Manifest;
import edu.usc.parknpay.R;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.seeker.ReservationsActivity;

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_PHOTO = 1;
    private Uri selectedImage;
    private ImageView profilePicture;
    private EditText editFirstName, editLastName, editEmail,
        editPassword, editConfirmPassword,
        editPhoneNumber, editDriversLicense;
    private TextInputLayout firstNameInputLayout, lastNameInputLayout,
        emailInputLayout, passwordInputLayout, confirmPasswordInputLayout,
        phoneInputLayout, licenseInputLayout;
    private Button registerButton;
    private User user;
    DatabaseReference Ref;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_registration);

        // Get references to UI views
        initializeViews();

        // Initialize progress dialog
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait logging in...");
        progress.setCancelable(false);

        // Add view listeners
        addListeners();
    }

    private void initializeViews() {
        profilePicture = (ImageView) findViewById(R.id.pic);
        registerButton = (Button) findViewById(R.id.register_button);

        // Text fields
        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editConfirmPassword = (EditText) findViewById(R.id.edit_confirm_password);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editDriversLicense = (EditText) findViewById(R.id.edit_drivers_license);

        // Text input layouts
        firstNameInputLayout = (TextInputLayout) findViewById(R.id.first_name_input_layout);
        lastNameInputLayout = (TextInputLayout) findViewById(R.id.last_name_input_layout);
        emailInputLayout = (TextInputLayout) findViewById(R.id.email_input_layout);
        passwordInputLayout = (TextInputLayout) findViewById(R.id.password_input_layout);
        confirmPasswordInputLayout = (TextInputLayout) findViewById(R.id.confirm_password_input_layout);
        phoneInputLayout = (TextInputLayout) findViewById(R.id.phone_input_layout);
        licenseInputLayout = (TextInputLayout) findViewById(R.id.license_input_layout);
    }

    // Called when the user clicks the register account button.
    private void submitForm() {
        if(!validateFirstName(true)) return;
        if(!validateLastName(true)) return;
        if (!validateEmail(true)) return;
        if (!validatePassword(true)) return;
        if (!validateConfirmPassword(true)) return;
        if (!validatePhoneNumber(true)) return;
        if (!validateDriversLicense(true)) return;
        registerUser();
    }

    private void registerUser() {
        progress.show();

        // Communicate with Firebase to authenticate the user.
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        String driversLicense = editDriversLicense.getText().toString().trim();

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
                            progress.dismiss();
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

        registerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    submitForm();
                }
            }
        );

        // Add text changed listeners
        editFirstName.addTextChangedListener(new RegistrationTextWatcher(editFirstName));
        editLastName.addTextChangedListener(new RegistrationTextWatcher(editLastName));
        editEmail.addTextChangedListener(new RegistrationTextWatcher(editEmail));
        editPassword.addTextChangedListener(new RegistrationTextWatcher(editPassword));
        editConfirmPassword.addTextChangedListener(new RegistrationTextWatcher(editConfirmPassword));
        editPhoneNumber.addTextChangedListener(new RegistrationTextWatcher(editPhoneNumber));
        editDriversLicense.addTextChangedListener(new RegistrationTextWatcher(editDriversLicense));

        // Add on focus listeners
        editFirstName.setOnFocusChangeListener(new RegistrationOnFocusChangeListener(editFirstName));
        editLastName.setOnFocusChangeListener(new RegistrationOnFocusChangeListener(editLastName));
        editEmail.setOnFocusChangeListener(new RegistrationOnFocusChangeListener(editEmail));
        editPassword.setOnFocusChangeListener(new RegistrationOnFocusChangeListener(editPassword));
        editConfirmPassword.setOnFocusChangeListener(new RegistrationOnFocusChangeListener(editConfirmPassword));
        editPhoneNumber.setOnFocusChangeListener(new RegistrationOnFocusChangeListener(editPhoneNumber));
        editDriversLicense.setOnFocusChangeListener(new RegistrationOnFocusChangeListener(editDriversLicense));
    }

    private boolean validateFirstName(boolean onFocusChange) {
        if (editFirstName.getText().toString().trim().isEmpty()) {
            firstNameInputLayout.setError(getString(R.string.err_msg_first_name));
            if (!onFocusChange) requestFocus(editFirstName);
            return false;
        }
        editFirstName.getBackground().clearColorFilter();
        firstNameInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateLastName(boolean onFocusChange) {
        if (editLastName.getText().toString().trim().isEmpty()) {
            lastNameInputLayout.setError(getString(R.string.err_msg_last_name));
            if (!onFocusChange) requestFocus(editLastName);
            return false;
        }
        editLastName.getBackground().clearColorFilter();
        lastNameInputLayout.setErrorEnabled(false);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail(boolean onFocusChange) {
        String email = editEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            emailInputLayout.setError(getString(R.string.err_msg_email));
            if (!onFocusChange) requestFocus(editEmail);
            return false;
        }
        editEmail.getBackground().clearColorFilter();
        emailInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validatePassword(boolean onFocusChange) {

        String password = editPassword.getText().toString().trim();
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        boolean b = m.find();

        // Check if field is empty
        if (password.isEmpty()) {
            passwordInputLayout.setError(getString(R.string.err_msg_password_1));
            if (!onFocusChange) requestFocus(editPassword);
            return false;
        }

        // Check for special character
        if(!b) {
            passwordInputLayout.setError(getString(R.string.err_msg_password_2));
            if (!onFocusChange) requestFocus(editPassword);
            return false;
        }

        // Check for password length
        if(password.length() < 10) {
            passwordInputLayout.setError(getString(R.string.err_msg_password_3));
            if (!onFocusChange) requestFocus(editPassword);
            return false;
        }

        editPassword.getBackground().clearColorFilter();
        passwordInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateConfirmPassword(boolean onFocusChange) {
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError(getString(R.string.err_msg_confirm_password));
            if (!onFocusChange) requestFocus(editConfirmPassword);
            return false;
        }
        editConfirmPassword.getBackground().clearColorFilter();
        confirmPasswordInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validatePhoneNumber(boolean onFocusChange) {
        String phoneNumber = editPhoneNumber.getText().toString().trim();
        if (phoneNumber.length() != 10) {
            phoneInputLayout.setError(getString(R.string.err_msg_phone_number));
            if (!onFocusChange) requestFocus(editPhoneNumber);
            return false;
        }
        editPhoneNumber.getBackground().clearColorFilter();
        phoneInputLayout.setErrorEnabled(false);
        return true;
    }

    private boolean validateDriversLicense(boolean onFocusChange) {
        if (editDriversLicense.getText().toString().trim().isEmpty()) {
            licenseInputLayout.setError(getString(R.string.err_msg_license));
            if (!onFocusChange) requestFocus(editDriversLicense);
            return false;
        }
        editDriversLicense.getBackground().clearColorFilter();
        licenseInputLayout.setErrorEnabled(false);
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class RegistrationTextWatcher implements TextWatcher {

        private View view;

        private RegistrationTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_first_name:
                    validateFirstName(false);
                    break;
                case R.id.edit_last_name:
                    validateLastName(false);
                    break;
                case R.id.edit_email:
                    validateEmail(false);
                    break;
                case R.id.edit_password:
                    validatePassword(false);
                    break;
                case R.id.edit_confirm_password:
                    validateConfirmPassword(false);
                    break;
                case R.id.edit_phone_number:
                    validatePhoneNumber(false);
                    break;
                case R.id.edit_drivers_license:
                    validateDriversLicense(false);
                    break;
            }
        }
    }

    private class RegistrationOnFocusChangeListener implements View.OnFocusChangeListener {

        private View view;

        private RegistrationOnFocusChangeListener(View view) { this.view = view; }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) return;

            switch (view.getId()) {
                case R.id.edit_first_name:
                    validateFirstName(true);
                    break;
                case R.id.edit_last_name:
                    validateLastName(true);
                    break;
                case R.id.edit_email:
                    validateEmail(true);
                    break;
                case R.id.edit_password:
                    validatePassword(true);
                    break;
                case R.id.edit_confirm_password:
                    validateConfirmPassword(true);
                    break;
                case R.id.edit_phone_number:
                    validatePhoneNumber(true);
                    break;
                case R.id.edit_drivers_license:
                    validateDriversLicense(true);
                    break;
            }
        }
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