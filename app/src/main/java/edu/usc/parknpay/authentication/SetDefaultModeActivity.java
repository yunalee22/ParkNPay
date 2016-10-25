package edu.usc.parknpay.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.usc.parknpay.R;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.owner.OwnerMainActivity;
import edu.usc.parknpay.seeker.SeekerMainActivity;

public class SetDefaultModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_set_default_mode);
    }

    /** Called when the user clicks the seeker button. */
    public void setDefaultToSeeker(View view) {
        Intent intent = new Intent(this, SeekerMainActivity.class);
        startActivity(intent);

        // Set user's default mode to seeker
        User.getInstance().setSeeker(true);
    }

    /** Called when the user clicks the owner button. */
    public void setDefaultToOwner(View view) {
        Intent intent = new Intent(this, OwnerMainActivity.class);
        startActivity(intent);

        // Set user's default mode to owner
        User.getInstance().setSeeker(false);

    }
}
