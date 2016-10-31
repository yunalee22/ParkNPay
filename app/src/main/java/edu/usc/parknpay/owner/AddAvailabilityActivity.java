package edu.usc.parknpay.owner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Date;

import edu.usc.parknpay.R;
import edu.usc.parknpay.TemplateActivity;

public class AddAvailabilityActivity extends TemplateActivity {
    TextView startDate, endDate;
    EditText prices;
    Button doneButton;
    Spinner cancellationSpinner, endSpinner, startSpinner;
    Calendar startCalendar;
    Calendar endCalendar;
    DatePickerDialog.OnDateSetListener dateStart, dateEnd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_add_availability);
        super.onCreateDrawer();
        toolbarSetup();
        initializeEdits();
        setSpinners();
        addListeners();

    }

    protected void initializeEdits() {
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        cancellationSpinner = (Spinner) findViewById(R.id.cancelSpinner);
        endSpinner = (Spinner) findViewById(R.id.spinnerEnd);
        startSpinner = (Spinner) findViewById(R.id.spinnerStart);
        prices = (EditText) findViewById(R.id.prices);
        doneButton = (Button) findViewById(R.id.done);
        startDate = (TextView) findViewById(R.id.startDate);
        updateLabel("start");
        endDate = (TextView) findViewById(R.id.endDate);
        updateLabel("end");
    }


    protected void addListeners () {
        dateStart = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel("start");
            }

        };
        dateEnd = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel("end");
            }

        };
        doneButton.setOnClickListener(new View.OnClickListener() {
            //Submitting all the data that user entered for parking spot here.
            @Override
            public void onClick(View arg0) {
                int priceFinal;
                String startString, endString;
                String cancellation;
                //checking if input is numerical
                try {
                    priceFinal = Integer.parseInt(prices.getText().toString());
                } catch (NumberFormatException e) {
                    //error message for bad format input
                    Toast.makeText(AddAvailabilityActivity.this, "Please enter a numerical value for price.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(priceFinal < 0) {
                    //error for negative number
                    Toast.makeText(AddAvailabilityActivity.this, "Price cannot be negative.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                startString = startDate.getText().toString() + "T" + startSpinner.getSelectedItem().toString() + ":00-7:00";
                endString = endDate.getText().toString() + "T" + endSpinner.getSelectedItem().toString() + ":00-7:00";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
                Date date1, date2;
                try {
                    date1 = sdf.parse(startString);
                    date2 = sdf.parse(endString);
                    if(!date1.before(date2)) {
                        Toast.makeText(AddAvailabilityActivity.this, "Please enter valid dates",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch(ParseException e) {
                    //Exception handling
                    Toast.makeText(AddAvailabilityActivity.this, "Parsing Error!",
                            Toast.LENGTH_SHORT).show();
                }


                cancellation = cancellationSpinner.getSelectedItem().toString();
                //should be sending to database here
                //String date is the date on the calendar in iso-8601
                //int priceFinal is price for the reservation
                Intent intent = new Intent(getApplicationContext(), OwnerMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }

        });

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAvailabilityActivity.this, dateStart, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAvailabilityActivity.this, dateEnd, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel(String end) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(end == "end")
            endDate.setText(sdf.format(endCalendar.getTime()));
        else
            startDate.setText(sdf.format(startCalendar.getTime()));
    }

    protected void setSpinners() {
        List<String> cancelSpinner =  new ArrayList<>();
        cancelSpinner.add("Cancellation Policy 1");
        cancelSpinner.add("Cancellation Policy 2");
        cancelSpinner.add("Cancellation Policy 3");
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, cancelSpinner);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cancellationSpinner.setAdapter(sizeAdapter);
        List<String> timeSpinner =  new ArrayList<>();
        for(int i=0; i<24; i++) {
            if(i <10)
                timeSpinner.add("0"+Integer.toString(i));
            else
                timeSpinner.add(Integer.toString(i));
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, timeSpinner);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endSpinner.setAdapter(timeAdapter);
        startSpinner.setAdapter(timeAdapter);

    }

    protected void toolbarSetup() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add an Availability");
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
