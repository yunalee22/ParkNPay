package edu.usc.parknpay.mutual;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.R;
import edu.usc.parknpay.owner.OwnerMainActivity;

/**
 * Created by Bobo on 10/16/2016.
 */

public class AccountSettingsActivity extends TemplateActivity {
    TextView edit;
    EditText firstName, lastName, email, phoneNum;
    Spinner defaultLogin;
    ImageView profPic;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        setSpinners();
        addListeners();


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

    protected void initializeEdits(){
        edit = (TextView) findViewById(R.id.editView);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        defaultLogin = (Spinner) findViewById(R.id.loginSpinner);
        profPic = (ImageView) findViewById(R.id.profPic);
        saveButton = (Button) findViewById(R.id.saveButton);

    }

    protected void setSpinners() {
        List<String> sizeArray =  new ArrayList<>();
        sizeArray.add("Seeker");
        sizeArray.add("Owner");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, sizeArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaultLogin.setAdapter(adapter);
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
                //grabbing all the values from the inputs (if all inputs are valid)
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String mail = email.getText().toString();
                String phoneNumber = phoneNum.getText().toString();
                String loginSpinner = defaultLogin.getSelectedItem().toString();
                //should be sending to database here
                Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }

        });
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
                    Uri selectedImage = imageReturnedIntent.getData();
                    profPic.setImageURI(selectedImage);
                }
                break;
        }
    }


}
