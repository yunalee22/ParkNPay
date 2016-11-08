    package edu.usc.parknpay;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.usc.parknpay.authentication.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LocationTest2 {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void locationTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.SEEKER), withText("SEEKER"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.place_autocomplete_search_input),
                        withParent(allOf(withId(R.id.place_autocomplete_fragment),
                                withParent(withId(R.id.search_layout)))),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.place_autocomplete_search_input), withText("University of Southern California"),
                        withParent(allOf(withId(R.id.place_autocomplete_fragment),
                                withParent(withId(R.id.search_layout)))),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.place_autocomplete_search_input), withText("New York"),
                        withParent(allOf(withId(R.id.place_autocomplete_fragment),
                                withParent(withId(R.id.search_layout)))),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.place_autocomplete_search_input), withText("Hawaii"),
                        withParent(allOf(withId(R.id.place_autocomplete_fragment),
                                withParent(withId(R.id.search_layout)))),
                        isDisplayed()));
        appCompatEditText4.perform(click());

    }

}
