package edu.usc.parknpay.owner;

import android.os.Bundle;
import android.view.MenuItem;

import edu.usc.parknpay.R;
import edu.usc.parknpay.utility.TemplateActivity;

/**
 * Created by Bobo on 11/5/2016.
 */

public class HelpActivity extends TemplateActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_menu);

        setUpToolbar("Help");
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
}
