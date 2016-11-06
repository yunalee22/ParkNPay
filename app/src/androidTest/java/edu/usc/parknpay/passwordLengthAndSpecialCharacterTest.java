package edu.usc.parknpay;

import android.support.design.widget.TextInputLayout;
import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.usc.parknpay.authentication.RegistrationActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Avery on 11/5/2016.
 */

public class passwordLengthAndSpecialCharacterTest {
    private String NineCharacterString;
    private String OneCharacterString;
    private String TenCharacterStringWithNoSpecialCharacter;
    private String TenCharacterStringWithSpecialCharacter;

    @Rule
    public ActivityTestRule<RegistrationActivity> mActivityRule = new ActivityTestRule<>(
            RegistrationActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        NineCharacterString = "asdfghjkl";
        OneCharacterString = "@";
        TenCharacterStringWithNoSpecialCharacter = "asdfghjkll";
        TenCharacterStringWithSpecialCharacter = "asdfghjkl@";
    }

    @Test
    public void NineCharacterString() {
        onView(withId(R.id.edit_password))
                .perform(typeText(NineCharacterString), closeSoftKeyboard());
        TextInputLayout passwordInputLayout = (TextInputLayout) mActivityRule.getActivity().findViewById(R.id.password_input_layout);
        assertEquals((String) passwordInputLayout.getError(),"Password requires a special non-alphanumerical character");
    }

    @Test
    public void OneCharacterStringSpecial() {
        onView(withId(R.id.edit_password))
                .perform(typeText(OneCharacterString), closeSoftKeyboard());
        TextInputLayout passwordInputLayout = (TextInputLayout) mActivityRule.getActivity().findViewById(R.id.password_input_layout);
        assertEquals((String) passwordInputLayout.getError(),"Password should be 10 or more characters long");
    }

    @Test
    public void TenCharacterStringNoSpecial() {
        onView(withId(R.id.edit_password))
                .perform(typeText(TenCharacterStringWithNoSpecialCharacter), closeSoftKeyboard());
        TextInputLayout passwordInputLayout = (TextInputLayout) mActivityRule.getActivity().findViewById(R.id.password_input_layout);
        assertEquals((String) passwordInputLayout.getError(),"Password requires a special non-alphanumerical character");
    }

    @Test
    public void TenCharactersWithSpecial() {
        onView(withId(R.id.edit_password))
                .perform(typeText(TenCharacterStringWithSpecialCharacter), closeSoftKeyboard());
        TextInputLayout passwordInputLayout = (TextInputLayout) mActivityRule.getActivity().findViewById(R.id.password_input_layout);
        assertEquals((String) passwordInputLayout.getError(), null);
    }

}
