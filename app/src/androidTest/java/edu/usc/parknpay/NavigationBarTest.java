package edu.usc.parknpay;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import edu.usc.parknpay.database.User;
import edu.usc.parknpay.seeker.SeekerMainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        DrawerActions.openDrawer(R.id.drawer_layout);
        onView(withText("Reservations"))
                .perform(click());
        onView(withId(R.id.seeker_view_spot)).check(matches(isDisplayed()));
    }
    @Test
    public void TestPaymentTab() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        DrawerActions.openDrawer(R.id.drawer_layout);
        onView(withText("Payment"))
                .perform(click());
        onView(withId(R.id.paymentMethods)).check(matches(isDisplayed()));
    }
    @Test
    public void TestHistoryTab() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        DrawerActions.openDrawer(R.id.drawer_layout);
        onView(withText("History"))
                .perform(click());
        onView(withId(R.layout.seeker_history)).check(matches(isDisplayed()));
    }
    @Test
    public void TestSettingsTab() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        DrawerActions.openDrawer(R.id.drawer_layout);
        onView(withText("Settings"))
                .perform(click());
        onView(withId(R.id.settings)).check(matches(isDisplayed()));
    }
    @Test
    public void TestHelpTab() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        DrawerActions.openDrawer(R.id.drawer_layout);
        onView(withText("Help"))
                .perform(click());
        onView(withId(R.id.help)).check(matches(isDisplayed()));
    }
    @Test
    public void TestLogoutTab() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        DrawerActions.openDrawer(R.id.drawer_layout);
        onView(withText("Log Out"))
                .perform(click());
        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));
    }
}



