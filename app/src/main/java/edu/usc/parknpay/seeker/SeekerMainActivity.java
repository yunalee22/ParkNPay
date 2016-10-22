package edu.usc.parknpay.seeker;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;
import edu.usc.parknpay.owner.OwnerMainActivity;

public class SeekerMainActivity extends TemplateActivity {

    ImageView filterButton;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_main);

        // Get references to UI views
        filterButton = (ImageView) findViewById(R.id.filter_button);
        searchText = (EditText) findViewById(R.id.search_text);

        // Add view listeners
        addListeners();
    }

    private void executeSearch(String searchText) {

        // Get search parameters
        String address = searchText;

        // Perform search and load results

    }

    private void loadSearchResults() {

    }

    protected void addListeners() {
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(SeekerMainActivity.this, SearchFilterActivity.class);
                startActivity(intent);
            }

        });

        searchText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    executeSearch(searchText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }
}
