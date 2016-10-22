package edu.usc.parknpay.owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

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

    protected void addListeners() {
        parkingSpotPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }

        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            //Submitting all the data that user entered for parking spot here.
            @Override
            public void onClick(View arg0) {
                //grabbing all the values from the inputs (if all inputs are valid)
                String streetFinal = street.getText().toString();
                String cityFinal = street.getText().toString();
                String stateFinal = street.getText().toString();
                String zipFinal = street.getText().toString();
                String notesFinal = street.getText().toString();
                String sizeFinal = size.getSelectedItem().toString();
                String cancelFinal = cancel.getSelectedItem().toString();
                boolean handicappedFinal = handicapped.isChecked();
                //should be sending to database here
                Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }

        });
    }
}
