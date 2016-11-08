package edu.usc.parknpay;
import android.support.test.rule.ActivityTestRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import edu.usc.parknpay.database.User;
import edu.usc.parknpay.mutual.AccountSettingsActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
/**
 * Created by Avery on 11/5/2016.
 */
public class SettingsTest {
    @Rule
    public ActivityTestRule<AccountSettingsActivity> mActivityRule = new ActivityTestRule<>(
            AccountSettingsActivity.class);
    @BeforeClass
    public static void setUpBeforeClass() {
        // Initialize User
        User.createUser(new User(
                "firstName",
                "lastName",
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
    public void nameCorrect() {
        onView(withId(R.id.firstName))
                .check(matches(withText("firstName")));
    }
    @Test
    public void lastNameCorrect() {
        onView(withId(R.id.lastName))
                .check(matches(withText("lastName")));
    }
    @Test
    public void licenseCorrect() {
        onView(withId(R.id.email))
                .check(matches(withText("none")));
    }
    @Test
    public void phoneCorrect() {
        onView(withId(R.id.phoneNum))
                .check(matches(withText("1234567890")));
    }
}
// this is a Settings test