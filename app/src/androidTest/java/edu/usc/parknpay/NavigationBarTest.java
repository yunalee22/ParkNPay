package edu.usc.parknpay;

import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.usc.parknpay.database.User;
import edu.usc.parknpay.seeker.SeekerMainActivity;

/**
 * Created by Avery on 11/5/2016.
 */

public class NavigationBarTest {
    @Rule
    public ActivityTestRule<SeekerMainActivity> mActivityRule = new ActivityTestRule<>(
            SeekerMainActivity.class);

    @BeforeClass
    public static void setUpBeforeClass() {
        // Initialize User
        User.createUser(new User(
                "S",
                "S",
                "seeker@seeker.com",
                "ITTwYsLBiza905uAomt9iacFm6R2",
                "1234567890",
                "none",
                5,
                1,
                true,
                500,
                "https://firebasestorage.googleapis.com/v0/b/parknpay-4c06e.appspot.com/o/ITTwYsLBiza905uAomt9iacFm6R2%2Fprofile?alt=media&token=69aa020a-51b2-4509-ae15-d7e24bd2ea84"
        ));


    }

    @Test
    public void TestReservationsTab() {
        // HOW DO I OPEN THE DRAWER?????

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
