package edu.usc.parknpay.owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.User;

public class AddSpotActivity extends TemplateActivity {

    EditText notes;
    CheckBox handicapped;
    Spinner size;
    Button doneButton;
    ImageView parkingSpotPhoto;
    Uri selectedImage;
    ParkingSpot spot;
    DatabaseReference Ref;
    String address;
    double latitude, longitude;
    ProgressDialog progress;
    // Search autocomplete text field
    private PlaceAutocompleteFragment autocompleteFragment;
    private Geocoder coder;
    private static final String LOG_TAG = "PlaceSelectionListener";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_add_spot);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        addListeners();
        setSpinners();

        //progress dialog
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait logging in...");
        progress.setCancelable(false);

        //address bar stuff

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        // Add search text field and geocoder
        coder = new Geocoder(this);
        autocompleteFragment.setHint("Enter an address");
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(34.0224, -118.2851),
                new LatLng(34.0224, -118.2851)
        ));

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
        notes = (EditText) findViewById(R.id.notesEdit);
        handicapped = (CheckBox) findViewById(R.id.checkBox);
        size = (Spinner) findViewById(R.id.sizeSpinner);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        doneButton = (Button) findViewById(R.id.button);
        parkingSpotPhoto = (ImageView) findViewById(R.id.spotPhoto);
        // Clear the photo
        Picasso.with(AddSpotActivity.this)
                .load(R.drawable.add_photo)
                .into(parkingSpotPhoto);
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
                    selectedImage = imageReturnedIntent.getData();
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
    }

    public void addSpot(View view) {
        if (selectedImage == null) {
            Toast.makeText(AddSpotActivity.this, "Please upload a photo.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address == null) {
            Toast.makeText(AddSpotActivity.this, "Please enter an address.", Toast.LENGTH_SHORT).show();
            return;
        }
        progress.show();
        //address string should hold the address typed into the google search bar
        String notesFinal = notes.getText().toString().trim();
        String sizeFinal = size.getSelectedItem().toString();
        boolean handicappedFinal = handicapped.isChecked();

        Ref = FirebaseDatabase.getInstance().getReference();
        String parkingSpotID = UUID.randomUUID().toString();
        User user = User.getInstance();
        String userId = user.getId();
        String userFullName = user.getFullName();
        String userPhoneNumber = user.getPhoneNumber();

        // Create parking spot
        spot = new ParkingSpot(user.getId(), user.getFullName(), userPhoneNumber, address, sizeFinal, 0, handicappedFinal, notesFinal, latitude, longitude, 0);
        spot.setParkingId(parkingSpotID);

        // handle image
        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child(userId + "/Spots/" + parkingSpotID);
        firebaseStorage.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                spot.setPhotoURL(taskSnapshot.getDownloadUrl().toString());
                Ref.child("Parking-Spots").child(spot.getParkingId()).setValue(spot);

                // Add to User with list of parking spots table
                Ref.child("Owner-To-Spots").child(spot.getOwnerUserId()).child(spot.getParkingId()).setValue(true);

                Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddSpotActivity.this, "Failed to create parking spot.", Toast.LENGTH_SHORT).show();
            }
        });



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

        // Called when user types into search field
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                address = place.getName().toString();

                List<Address> addr;
                try {
                    addr = coder.getFromLocationName(address, 5);
                    Address location = addr.get(0);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Status status) {
                Log.e(LOG_TAG, "onError: Status = " + status.toString());
            }
        });
    }
}
