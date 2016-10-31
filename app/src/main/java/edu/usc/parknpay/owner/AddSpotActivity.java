package edu.usc.parknpay.owner;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.seeker.SeekerMainActivity;

public class AddSpotActivity extends TemplateActivity implements PlaceSelectionListener {

    //EditText street, city, state, zipCode, notes;
    EditText notes;

    CheckBox handicapped;
    Spinner size, cancel;
    Button doneButton;
    ImageView parkingSpotPhoto;
    Uri selectedImage;
    ParkingSpot spot;
    DatabaseReference Ref;

    // Search autocomplete text field
    private PlaceAutocompleteFragment autocompleteFragment;
    private Geocoder coder;
    private static final String LOG_TAG = "PlaceSelectionListener";

    // Search results list view
    private ListView searchList;
    private ArrayList<ParkingSpot> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_add_spot);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        //addListeners();
        setSpinners();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Add search text field and geocoder
        coder = new Geocoder(this);
        autocompleteFragment.setHint("Enter an address");
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(34.0224, -118.2851),
                new LatLng(34.0224, -118.2851)
        ));

        // Add view listeners
        addListeners();
        autocompleteFragment.setOnPlaceSelectedListener(this);
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
//        street = (EditText) findViewById(R.id.streetEdit);
//        city = (EditText) findViewById(R.id.cityEdit);
//        state = (EditText) findViewById(R.id.stateEdit);
//        zipCode = (EditText) findViewById(R.id.zipEdit);
        notes = (EditText) findViewById(R.id.notesEdit);
        handicapped = (CheckBox) findViewById(R.id.checkBox);
        size = (Spinner) findViewById(R.id.sizeSpinner);
        cancel = (Spinner) findViewById(R.id.cancelSpinner);
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
        if (selectedImage == null) {
            Toast.makeText(AddSpotActivity.this, "Please upload a photo.", Toast.LENGTH_SHORT).show();
            return;
        }

        String notesFinal = notes.getText().toString();
        String sizeFinal = size.getSelectedItem().toString();
        String cancelFinal = cancel.getSelectedItem().toString();
        boolean handicappedFinal = handicapped.isChecked();

        Ref = FirebaseDatabase.getInstance().getReference();
        String parkingSpotID = UUID.randomUUID().toString();
        String userId = User.getInstance().getId();


        // Create parking spot
        spot = new ParkingSpot(userId, "address-here", sizeFinal, 0, handicappedFinal, notesFinal, cancelFinal);
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
    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place.getName());

        List<Address> address;

        String str = place.getName().toString();

        try {
            address = coder.getFromLocationName(str, 5);
            Address location =address.get(0);
            location.getLatitude(); //shows you how to get it
            location.getLongitude();
            Log.i(LOG_TAG, "SHOW LAT: "+location.getLatitude());

            Toast.makeText(this, "Latitude is "+location.getLatitude(), Toast.LENGTH_LONG).show();
        } catch (Exception IOException) {
            Toast.makeText(this, "IOEXCEPTION", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = "+status.toString());
        Toast.makeText(this, "Place selection failed: "+status.getStatusMessage(), Toast.LENGTH_LONG).show();
    }
}
