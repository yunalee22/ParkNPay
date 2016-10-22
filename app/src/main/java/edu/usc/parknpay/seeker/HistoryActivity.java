package edu.usc.parknpay.seeker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

public class HistoryActivity extends TemplateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
    }
}
