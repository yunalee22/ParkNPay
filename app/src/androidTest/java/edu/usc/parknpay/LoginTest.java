package edu.usc.parknpay;
import android.support.test.rule.ActivityTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import edu.usc.parknpay.authentication.LoginActivity;
import edu.usc.parknpay.database.User;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.not;
/**
 * Created by Avery on 11/5/2016.
 */
public class LoginTest {
    private String seekerAccountUsername, seekerAccountPassword;
    private String hostAccountUsername, hostAccountPassword;
    private String invalidAccountUsername, invalidAccountPassword;
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);
    @Before
    public void initValidString() {
        // Specify a valid string.
        seekerAccountUsername = "seeker@seeker.com";
        seekerAccountPassword = "1234567890!";
        hostAccountUsername = "host@host.com";
        hostAccountPassword = "1234567890!";
        invalidAccountUsername = "invalid";
        invalidAccountPassword = "invalid";
    }
    @Test
    public void TestSeekerLogin() {
        onView(withId(R.id.edit_email))
                .perform(typeText(seekerAccountUsername), closeSoftKeyboard());
        onView(withId(R.id.edit_password))
                .perform(typeText(seekerAccountPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals((Boolean) User.getInstance().isSeeker(),(Boolean) true);
        onView(withId(R.id.dates_available_label)).check(matches(withText("Date Range")));
    }
    @Test
    public void TestHostLogin() {
        onView(withId(R.id.edit_email))
                .perform(typeText(hostAccountUsername), closeSoftKeyboard());
        onView(withId(R.id.edit_password))
                .perform(typeText(hostAccountPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals((Boolean) User.getInstance().isSeeker(),(Boolean) false);
        onView(withId(R.id.textView4)).check(matches(withText("My Parking Spots")));
    }
    @Test
    public void invalidLogin() {
        onView(withId(R.id.edit_email))
                .perform(typeText(invalidAccountUsername), closeSoftKeyboard());
        onView(withId(R.id.edit_password))
                .perform(typeText(invalidAccountPassword), closeSoftKeyboard());
        onView(withId(R.id.login_button))
                .perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Failed to authenticate user"))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
}
// this is a loginTest