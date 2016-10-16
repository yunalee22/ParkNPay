package edu.usc.parknpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SetDefaultModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_default_mode);
    }

    public void setDefaultToSeeker(View view) {
        Intent intent = new Intent(this, SeekerMainActivity.class);
        startActivity(intent);

        // Set user's default mode to seeker

    }

    public void setDefaultToOwner(View view) {
        Intent intent = new Intent(this, OwnerMainActivity.class);
        startActivity(intent);

        // Set user's default mode to owner
        
    }
}
