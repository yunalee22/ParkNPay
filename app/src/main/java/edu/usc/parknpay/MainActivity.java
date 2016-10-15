package edu.usc.parknpay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MainActivity extends AppCompatActivity {
//    TextView mTextFieldCondition;
//    Button mButtonSunny;
//    Button mButtonFoggy;
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int test = 7;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mTextFieldCondition = (TextView) findViewById(R.id.textView42);
//        mButtonSunny = (Button) findViewById(R.id.buttonSunny);
//        mButtonFoggy = (Button) findViewById(R.id.buttonFoggy);
        mRef = new Firebase("https://parknpay-4c06e.firebaseio.com/condition");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String text = dataSnapshot.getValue(String.class);
//                System.out.println(text);
//                mTextFieldCondition.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("error");
            }
        });
    }
}
