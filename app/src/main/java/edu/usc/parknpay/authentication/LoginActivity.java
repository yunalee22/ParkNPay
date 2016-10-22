package edu.usc.parknpay.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.owner.OwnerMainActivity;
import edu.usc.parknpay.seeker.SeekerMainActivity;

public class LoginActivity extends AppCompatActivity {
    EditText editEmail;
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Get references to UI views
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /** Called when the user clicks the login button. */
    public void authenticateUser(View view) {

        // Communicate with Firebase to authenticate the user.
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // If authentication is successful, proceed to owner/seeker main view
        // (whichever is user's default).
        final Intent seekerIntent = new Intent(this, SeekerMainActivity.class);
        final Intent ownerIntent = new Intent(this, OwnerMainActivity.class);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter your email and/or password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // Get the newly generated user
                    String userId = task.getResult().getUser().getUid();

                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);

                                    // Create the user
                                    User.createUser(user);

                                    // Am I a seeker or an owner?
                                    if (User.getInstance().isSeeker()) {
                                        startActivity(seekerIntent);
                                    } else {
                                        startActivity(ownerIntent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });


                    //startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /** Called when the user selects the registration option. */
    public void displayRegistrationScreen(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
