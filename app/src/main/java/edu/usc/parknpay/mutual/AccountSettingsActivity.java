package edu.usc.parknpay.mutual;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.owner.OwnerMainActivity;
import edu.usc.parknpay.seeker.SeekerMainActivity;
import edu.usc.parknpay.utility.TemplateActivity;
import edu.usc.parknpay.database.User;

public class AccountSettingsActivity extends TemplateActivity {
    TextView edit;
    EditText firstName, lastName, email, phoneNum;
    Spinner defaultLogin;
    ImageView profPic;
    Button saveButton;
    Uri selectedImage;
    User u;
    User tempUser;
    boolean pictureChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mutual_account_settings);

        setUpToolbar("Settings");
        u = User.getInstance();
        setSpinners();
        initializeEdits();
        addListeners();
    }

    protected void initializeEdits(){
        edit = (TextView) findViewById(R.id.editView);
        firstName = (EditText) findViewById(R.id.firstName);
        firstName.setText(u.getFirstName());
        lastName = (EditText) findViewById(R.id.lastName);
        lastName.setText(u.getLastName());
        email = (EditText) findViewById(R.id.email);
        email.setText(u.getLicenseNumber());
        email.setKeyListener(null);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        phoneNum.setText(u.getPhoneNumber());
        profPic = (ImageView) findViewById(R.id.profPic);
        saveButton = (Button) findViewById(R.id.saveButton);

        // Load profile image
        Picasso.with(AccountSettingsActivity.this)
                .load(u.getProfilePhotoURL())
                .placeholder(R.drawable.progress_animation)
                .into(profPic);
    }

    protected void setSpinners() {
        List<String> sizeArray =  new ArrayList<>();
        sizeArray.add("Seeker");
        sizeArray.add("Owner");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, sizeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaultLogin = (Spinner) findViewById(R.id.loginSpinner);
        defaultLogin.setAdapter(adapter);
        if(u.isSeeker()) {
            defaultLogin.setSelection(0);
        }
        else {
            defaultLogin.setSelection(1);
        }
    }

    protected void addListeners() {

        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }

        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            //Submitting all the data that user entered for parking spot here.
            @Override
            public void onClick(View arg0) {

                // Get all edit text strings
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String license = email.getText().toString();
                String phoneNumber = phoneNum.getText().toString();
                int role = defaultLogin.getSelectedItemPosition();

                // Check if all inputs are valid
                if (!validateFirstName(fName)) return;
                if (!validateLastName(lName)) return;
                if (!validateLicense(license)) return;
                if (!validatePhoneNumber(phoneNumber)) return;

                tempUser = new User(
                        fName,
                        lName,
                        u.getEmail(),
                        phoneNumber,
                        license,
                        u.getRawRating(),
                        u.getNumRatings(),
                        role == 0, // seeker or not
                        u.getBalance()
                );
                tempUser.setId(u.getId());

                if (pictureChanged) {
                    // Insert profile photo into database
                    StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child(u.getId()).child("profile");
                    firebaseStorage.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            tempUser.setProfilePhotoURL(taskSnapshot.getDownloadUrl().toString());

                            // Get correct firebase ref
                            FirebaseDatabase.getInstance().getReference().child("Users").child(u.getId()).setValue(tempUser);

                            User.createUser(tempUser);

                            Toast.makeText(AccountSettingsActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                            if(u.getIsCurrentlySeeker()) {
                                Intent intent = new Intent(getApplicationContext(), SeekerMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AccountSettingsActivity.this, "Failed to save changes", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(u.getId()).setValue(tempUser);

                    // Use original profile photo url
                    tempUser.setProfilePhotoURL(u.getProfilePhotoURL());
                    // Get correct firebase ref
                    FirebaseDatabase.getInstance().getReference().child("Users").child(u.getId()).setValue(tempUser);
                    User.createUser(tempUser);

                    Toast.makeText(AccountSettingsActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }


            }

        });
    }

    private boolean validateFirstName(String firstName) {
        if (firstName.isEmpty()) {
            Toast.makeText(AccountSettingsActivity.this, "Please enter your first name.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateLastName(String lastName) {
        if (lastName.isEmpty()) {
            Toast.makeText(AccountSettingsActivity.this, "Please enter your last name.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateLicense(String license) {
        if (license.isEmpty()) {
            Toast.makeText(AccountSettingsActivity.this, "Please enter your license number.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 10) {
            Toast.makeText(AccountSettingsActivity.this, "Please enter a valid 10-digit phone number.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //function needed to grab photo from phone's gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    profPic.setImageURI(selectedImage);
                    pictureChanged = true;
                }
                break;
        }
    }
}
