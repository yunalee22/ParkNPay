package edu.usc.parknpay;


        import android.location.Address;
        import android.location.Geocoder;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.text.Html;
        import android.text.TextUtils;
        import android.util.Log;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.location.places.Place;
        import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
        import com.google.android.gms.location.places.ui.PlaceSelectionListener;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;

        import java.util.List;

public class LoginActivity extends AppCompatActivity implements PlaceSelectionListener {

    private static final String LOG_TAG = "PlaceSelectionListener";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private TextView locationTextView;
    private TextView attributionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Method #1
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Search a Location");
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(34.0224, -118.2851),
                new LatLng(34.0224, -118.2851)));

        //new LatLng(34.0224, 118.2851),
        //new LatLng(-33.880490, 151.184363),
        //new LatLng(-33.858754, 151.229596))

    }

    @Override
    public void onPlaceSelected(Place place) {
        Log.i(LOG_TAG, "Place Selected: " + place.getName());

        //Toast.makeText(this, "TIM: "+ place.getName(), Toast.LENGTH_LONG).show();

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        String str = place.getName().toString();

        try {
            address = coder.getFromLocationName(str, 5);
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
            Log.i(LOG_TAG, "SHOW LAT: " + location.getLatitude());

            Toast.makeText(this, "Latititude is "+location.getLatitude(), Toast.LENGTH_LONG);
        } catch (Exception IOException) {
            Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onError(Status status) {
        Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }
}