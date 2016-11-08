package edu.usc.parknpay;

import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import edu.usc.parknpay.authentication.LoginActivity;
import edu.usc.parknpay.authentication.RegistrationActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

/**
 * Created by yunalee on 11/7/16.
 */

public class DisablingBackButtonTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void BackAfterRegistration() {

        // Move to registration activity
        onView(withId(R.id.register_button)).perform(click());

        // Register a new user
        onView(withId(R.id.edit_first_name)).perform(typeText("Billy"), closeSoftKeyboard());
        onView(withId(R.id.edit_last_name)).perform(typeText("Bruin"), closeSoftKeyboard());
        onView(withId(R.id.edit_email)).perform(typeText("billybruin123@hotmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_password)).perform(typeText("ihatetrojans!"), closeSoftKeyboard());
        onView(withId(R.id.edit_confirm_password)).perform(typeText("ihatetrojans!"), closeSoftKeyboard());
        onView(withId(R.id.edit_phone_number)).perform(typeText("0123456789"), closeSoftKeyboard());
        onView(withId(R.id.edit_drivers_license)).perform(typeText("24242424242424"), closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Press the back button
        pressBack();
    }

    @Test
    public void BackAfterSignOut() {

        // Log into Billy Bruin's account
        onView(withId(R.id.edit_email)).perform(typeText("billybruin123@hotmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_password)).perform(typeText("ihatetrojans!"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Sign out Billy Bruin
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        DrawerActions.openDrawer(R.id.drawer_layout);
        onView(withText("Log Out")).perform(click());
        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Press the back button
        pressBack();
    }

    @Test
    public void BackAfterLogIn() {

        // Log into Billy Bruin's account
        onView(withId(R.id.edit_email)).perform(typeText("billybruin123@hotmail.com"), closeSoftKeyboard());
        onView(withId(R.id.edit_password)).perform(typeText("ihatetrojans!"), closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Press the back button
        pressBack();
    }

}
