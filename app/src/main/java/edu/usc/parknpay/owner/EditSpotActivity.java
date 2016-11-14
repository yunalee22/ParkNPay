package edu.usc.parknpay.owner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.ParkingSpot;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.utility.TemplateActivity;

/**
 * Created by Bobo on 11/13/2016.
 */

public class EditSpotActivity extends TemplateActivity {

    private ImageView parkingSpotPhoto;
    private Button doneButton, deleteButton;
    private TextView notes, spotType, handicap, address;
    Uri selectedImage;
    ParkingSpot parkingSpot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_edit_spot);

        //setting up elements
        parkingSpot = (ParkingSpot) getIntent().getSerializableExtra("parkingSpot");
        parkingSpotPhoto = (ImageView) findViewById(R.id.parkingSpotImage);
        doneButton = (Button) findViewById(R.id.doneButton);
        deleteButton = (Button) findViewById(R.id.dbutton);
        notes = (TextView)findViewById(R.id.notes);
        spotType = (TextView)findViewById(R.id.spotType);
        handicap = (TextView)findViewById(R.id.handicap);
        address = (TextView) findViewById(R.id.address);

//        initialize values
        address.setText(parkingSpot.getAddress());
        Picasso.with(EditSpotActivity.this)
                .load(parkingSpot.getPhotoURL())
                .placeholder(R.drawable.progress_animation)
                .resize(450, 450)
                .centerCrop()
                .into(parkingSpotPhoto);
        notes.setText(parkingSpot.getDescription());
        spotType.setText(parkingSpot.getSize());
        if(parkingSpot.isHandicap()) {
            handicap.setText("Handicapped Only");
        }

        //add listeners
        parkingSpotPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }

        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //firebase: save edits
                Intent intent = new Intent(getApplicationContext(), ViewSpotActivity.class);
                intent.putExtra("newChanges", "yes");
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                new AlertDialog.Builder(EditSpotActivity.this)
                        .setTitle( "Delete Spot" )
                        .setMessage( "Are you sure you want to delete your spot?" )
                        .setPositiveButton( "No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( "AlertDialog", "Negative" );
                            }
                        } )
                        .setNegativeButton( "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference().child("Owner-To-Spots").child(User.getInstance().getId()).child(parkingSpot.getParkingId()).setValue(false)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(EditSpotActivity.this, "Parking spot successfully deleted.",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), edu.usc.parknpay.owner.OwnerMainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditSpotActivity.this, "Could not delete parking spot.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Log.d( "AlertDialog", "Positive" );
                            }
                        })
                        .show();
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
                    selectedImage = imageReturnedIntent.getData();
                    parkingSpotPhoto.setImageURI(selectedImage);
                }
                break;
        }
    }
}
