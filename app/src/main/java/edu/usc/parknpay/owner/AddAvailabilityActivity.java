package edu.usc.parknpay.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

/**
 * Created by Bobo on 10/22/2016.
 */

public class AddAvailabilityActivity extends TemplateActivity {
    CalendarView calendar;
    Spinner from, to;
    EditText editFrom, editTo, prices;
    Button doneButton;
    String date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_availability);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        setSpinners();
        addListeners();
    }

    protected void initializeEdits() {
        calendar = (CalendarView) findViewById(R.id.calendar);
        from = (Spinner) findViewById(R.id.spinnerFrom);
        to = (Spinner) findViewById(R.id.spinnerTo);
        editFrom = (EditText) findViewById(R.id.editFrom);
        editTo = (EditText) findViewById(R.id.editTo);
        prices = (EditText) findViewById(R.id.prices);
        doneButton = (Button) findViewById(R.id.done);
    }

    protected void setSpinners() {
        List<String> fromArray =  new ArrayList<>();
        fromArray.add("AM");
        fromArray.add("PM");
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, fromArray);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(fromAdapter);

        List<String> toArray =  new ArrayList<>();
        toArray.add("AM");
        toArray.add("PM");
        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, fromArray);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        to.setAdapter(toAdapter);
    }

    protected void addListeners () {
        doneButton.setOnClickListener(new View.OnClickListener() {
            //Submitting all the data that user entered for parking spot here.
            @Override
            public void onClick(View arg0) {
                //checking inputs


                //grabbing all the values from the inputs (if all inputs are valid)
                int fromFinal;
                int toFinal;
                int priceFinal;

                //checking if input is numerical
                try {
                    fromFinal = Integer.parseInt(editFrom.getText().toString());
                    toFinal = Integer.parseInt(editTo.getText().toString());
                    priceFinal = Integer.parseInt(prices.getText().toString());
                } catch (NumberFormatException e) {
                    //error message for bad format input
                    return;
                }
                if (fromFinal < 0 || fromFinal >12 ||
                        toFinal < 0 || toFinal > 12 ||
                        priceFinal < 0 || priceFinal >12 ) {
                    //error message for bad input out of range
                    return;
                }
                int tempFrom = 0, tempTo = 0;
                String fromTimeFinal = from.getSelectedItem().toString();
                String toTimeFinal = to.getSelectedItem().toString();
                if(fromTimeFinal == "PM")
                    tempFrom = fromFinal +12;
                if(toTimeFinal == "PM")
                    tempTo = toFinal +12;
                if(tempFrom > tempTo)
                    //invalid time range
                    return;


                //should be sending to database here
                Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }

        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String m = String.valueOf(month+1);
                if(month<9)
                    m = "0" + m;
                String d = String.valueOf(dayOfMonth);
                if(dayOfMonth<10)
                    d = "0" + d;
                date = String.valueOf(year) + "-" + m + "-" + d;
            }
        });
    }

    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add an Availability");
    }

}
