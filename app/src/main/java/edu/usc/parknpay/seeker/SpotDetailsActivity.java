package edu.usc.parknpay.seeker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

public class SpotDetailsActivity extends TemplateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_details);
    }
}
