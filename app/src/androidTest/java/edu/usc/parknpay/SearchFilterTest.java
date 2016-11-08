package edu.usc.parknpay;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.parknpay.R;
import edu.usc.parknpay.authentication.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchFilterTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void searchFilterTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.SEEKER), withText("SEEKER"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.filter_button),
                        withParent(withId(R.id.search_layout)),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                withId(R.id.handicap_only_checkbox));
        appCompatCheckBox.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.search_button), withText("Search")));
        appCompatButton2.perform(scrollTo(), click());

    }

}
