package edu.usc.parknpay.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.owner.OwnerMainActivity;
import edu.usc.parknpay.seeker.SeekerMainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private TextView registerButton;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_login);

        // Get references to UI views
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        registerButton = (TextView) findViewById(R.id.register_button);

        // Add register button listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Proceed to registration screen
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        // Initialize progress dialog
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please wait logging in...");
        progress.setCancelable(false);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        progress.dismiss();
    }

    /** Called when the user clicks the authentication_login button. */
    public void authenticateUser(View view) {

        // Communicate with Firebase to authenticate the user.
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        // Verify input
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter your email and/or password", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        // Authenticate user through database
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    // Get the newly generated user
                    String userId = task.getResult().getUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // Create user
                            User user = snapshot.getValue(User.class);
                            User.createUser(user);

                            // If authentication is successful, proceed to default (owner/seeker) main view.
                            if (User.getInstance().isSeeker()) {
                                User.getInstance().setIsCurrentlySeeker(true);
                                progress.dismiss();
                                Intent seekerIntent = new Intent(LoginActivity.this, SeekerMainActivity.class);
                                startActivity(seekerIntent);
                            } else {
                                User.getInstance().setIsCurrentlySeeker(false);
                                progress.dismiss();
                                Intent ownerIntent = new Intent(LoginActivity.this, OwnerMainActivity.class);
                                startActivity(ownerIntent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    // TESTING
    public void SeekerTestLogin(View view){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        progress.show();

        firebaseAuth.signInWithEmailAndPassword("seeker@seeker.com", "1234567890!").addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    // Get the newly generated user
                    String userId = task.getResult().getUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // Create user
                            User user = snapshot.getValue(User.class);
                            User.createUser(user);

                            (User.getInstance()).setIsCurrentlySeeker(true);

                            // If authentication is successful, proceed to default (owner/seeker) main view.

                            //if (User.getInstance().isSeeker()) {
                            progress.dismiss();
                            Intent seekerIntent = new Intent(LoginActivity.this, SeekerMainActivity.class);
                            startActivity(seekerIntent);
                            //} else {
                            //    Intent ownerIntent = new Intent(LoginActivity.this, OwnerMainActivity.class);
                            //    startActivity(ownerIntent);
                            //}
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    public void HostTestLogin(View view){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        progress.show();


        firebaseAuth.signInWithEmailAndPassword("host@host.com", "1234567890!").addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()) {

                    // Get the newly generated user
                    String userId = task.getResult().getUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            // Create user
                            User user = snapshot.getValue(User.class);
                            User.createUser(user);

                            User.getInstance().setIsCurrentlySeeker(false);

                            // If authentication is successful, proceed to default (owner/seeker) main view.
                            //if (User.getInstance().isSeeker()) {
                            //    Intent seekerIntent = new Intent(LoginActivity.this, SeekerMainActivity.class);
                            //    startActivity(seekerIntent);
                            //} else {
                            progress.dismiss();
                                Intent ownerIntent = new Intent(LoginActivity.this, OwnerMainActivity.class);
                                startActivity(ownerIntent);
                            //}
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to authenticate user", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }
}
