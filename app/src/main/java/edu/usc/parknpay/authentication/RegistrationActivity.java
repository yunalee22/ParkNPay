package edu.usc.parknpay.authentication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.usc.parknpay.R;

public class RegistrationActivity extends AppCompatActivity {

    static final int PICK_PHOTO = 1;

    ImageView profilePicture;
    TextView editButton;
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPassword;
    EditText editConfirmPassword;
    EditText editPhoneNumber;
    EditText editDriversLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

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
    }

    /** Called when the user clicks the register account button. */
    public void registerUser(View view) {

        // Communicate with Firebase to authenticate the user.
        String firstName = editFirstName.getText().toString();
        String lastName = editFirstName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        String phoneNumber = editPhoneNumber.getText().toString();
        String driversLicense = editDriversLicense.getText().toString();

        // If registration is successful, proceed to owner/seeker selection view.
        Intent intent = new Intent(this, SetDefaultModeActivity.class);
        startActivity(intent);

        // If registration is unsuccessful, display an error message.
        TextView errorMessage = new TextView(this);
        errorMessage.setText("Failed to register user.");
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_registration);
        layout.addView(errorMessage);
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
                    Uri selectedImage = imageReturnedIntent.getData();
                    profilePicture.setImageURI(selectedImage);
                }
                break;
        }
    }
}
