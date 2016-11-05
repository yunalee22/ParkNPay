package edu.usc.parknpay.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.usc.parknpay.R;
import edu.usc.parknpay.utility.TemplateActivity;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.owner.OwnerMainActivity;
import edu.usc.parknpay.seeker.SeekerMainActivity;

public class SetDefaultModeActivity extends TemplateActivity {

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
        User.getInstance().setIsCurrentlySeeker(true);
        finish();
    }

    /** Called when the user clicks the owner button. */
    public void setDefaultToOwner(View view) {
        Intent intent = new Intent(this, OwnerMainActivity.class);
        startActivity(intent);

        // Set user's default mode to owner
        User.getInstance().setSeeker(false);
        User.getInstance().setIsCurrentlySeeker(false);
        finish();

    }
}
