package edu.usc.parknpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

//    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // REFERENCE FOR USING FIREBASE
//        Firebase.setAndroidContext(this);
//        mRef = new Firebase("https://parknpay-4c06e.firebaseio.com/Users/47fsSEGu3WOzQ1UkkzBkEj8jGHD3");
//
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Object _data = dataSnapshot.getValue();
//                System.out.println("DATA");
//                System.out.println(_data.toString());
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("Error in fetching data");
//            }
//        });
    }

    /** Called when the user clicks the login button. */
    public void authenticateUser(View view) {

        // Communicate with Firebase to authenticate the user.
        EditText editEmail = (EditText) findViewById(R.id.edit_email);
        String email = editEmail.getText().toString();
        EditText editPassword = (EditText) findViewById(R.id.edit_password);
        String password = editPassword.getText().toString();

        // If authentication is successful, proceed to owner/seeker main view
        // (whichever is user's default).
        Intent seekerIntent = new Intent(this, SeekerMainActivity.class);
        Intent ownerIntent = new Intent(this, OwnerMainActivity.class);
        startActivity(seekerIntent);

        // If authentication is unsuccessful, display an error message.
        TextView errorMessage = new TextView(this);
        errorMessage.setText("Failed to authenticate user.");
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_login);
        layout.addView(errorMessage);
    }

    /** Called when the user selects the registration option. */
    public void displayRegistrationScreen(View view) {

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);


    }
}
