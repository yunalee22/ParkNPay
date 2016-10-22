package edu.usc.parknpay.owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.authentication.RegistrationActivity;
import edu.usc.parknpay.authentication.SetDefaultModeActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.User;

/**
 * Created by Bobo on 10/19/2016.
 */

public class AddSpotActivity extends TemplateActivity {

    EditText street, city, state, zipCode, notes;
    CheckBox handicapped;
    Spinner size, cancel;
    Button doneButton;
    ImageView parkingSpotPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spot);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        addListeners();
        setSpinners();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a Space");
    }

    protected void initializeEdits() {
        street = (EditText) findViewById(R.id.streetEdit);
        city = (EditText) findViewById(R.id.cityEdit);
        state = (EditText) findViewById(R.id.stateEdit);
        zipCode = (EditText) findViewById(R.id.zipEdit);
        notes = (EditText) findViewById(R.id.notesEdit);
        handicapped = (CheckBox) findViewById(R.id.checkBox);
        size = (Spinner) findViewById(R.id.sizeSpinner);
        cancel = (Spinner) findViewById(R.id.cancelSpinner);
        doneButton = (Button) findViewById(R.id.button);
        parkingSpotPhoto = (ImageView) findViewById(R.id.spotPhoto);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    parkingSpotPhoto.setImageURI(selectedImage);
                }
                break;
        }
    }

    protected void setSpinners(){
        List<String> sizeArray =  new ArrayList<>();
        sizeArray.add("Normal");
        sizeArray.add("Compact");
        sizeArray.add("SUV");
        sizeArray.add("Truck");
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, sizeArray);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size.setAdapter(sizeAdapter);

        List<String> cancelArray =  new ArrayList<>();
        cancelArray.add("Policy 1");
        cancelArray.add("Policy 2");
        cancelArray.add("Policy 3");
        ArrayAdapter<String> cancelAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, cancelArray);
        cancelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cancel.setAdapter(cancelAdapter);
    }

    public void addSpot(View view) {
        // If registration is successful, proceed to owner/seeker selection view.
        // intent = new Intent(this, SetDefaultModeActivity.class);

        // Ref.child("Users").child(userId).setValue(user);

        //grabbing all the values from the inputs (if all inputs are valid)
        String notesFinal = notes.getText().toString();
        String sizeFinal = size.getSelectedItem().toString();
        String cancelFinal = cancel.getSelectedItem().toString();
        boolean handicappedFinal = handicapped.isChecked();

        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();

        String parkingSpotID = UUID.randomUUID().toString();

        String userId = User.getInstance().getId();

        // Parking-Spots table
        ParkingSpot spot = new ParkingSpot("Swaggin spot", userId, sizeFinal, 0, handicappedFinal, notesFinal, cancelFinal);
        Ref.child("Parking-Spots").child(parkingSpotID).setValue(spot);

        // Add to User with list of parking spots table

        // Add to Browse table



        Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    protected void addListeners() {
        parkingSpotPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }

        });
    }
}
