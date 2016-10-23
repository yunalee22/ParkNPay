package edu.usc.parknpay.owner;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

public class OwnerMainActivity extends TemplateActivity {
    GridView parkingSpots;
    ImageView addSpotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_main);
        super.onCreateDrawer();
        initializeComponents();
        addListeners();
    }

    protected void initializeComponents() {
        parkingSpots = (GridView) findViewById(R.id.gridView);
        addSpotButton = (ImageView) findViewById(R.id.addSpot);
    }

    protected void addListeners() {
        addSpotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), AddSpotActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

        });
    }
}
