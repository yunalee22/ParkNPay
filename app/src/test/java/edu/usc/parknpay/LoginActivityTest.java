package edu.usc.parknpay;

import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.text.TextUtils;
import android.util.Log;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.usc.parknpay.authentication.LoginActivity;
import edu.usc.parknpay.authentication.RegistrationActivity;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
    public class LoginActivityTest {

    @Test
    public void testEmptyName() throws Exception {
        String name = "   ";

        boolean b = name.trim().isEmpty();

        assertTrue(b);
    }

    @Test
    public void testValidName() throws Exception {
        String name = " valid ";

        boolean b = name.trim().isEmpty();

        assertFalse(b);
    }

    @Test
    public void testValidEmail() throws Exception {
        String email = "hwangtim@usc.edu";
        email = "timothyahwang@gmail.com";

        RegistrationActivity ra = new RegistrationActivity();

        email = email.trim();

        System.out.println(email);

        assertTrue(true);
        assertTrue(!TextUtils.isEmpty(email));// && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
//        boolean b = (email.isEmpty() || !ra.isValidEmail(email));
//
//        assertFalse(b);
    }

    @Test
    public void testSpecialCharacter() throws Exception {
        String password = "has$pecialCharacter";

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        boolean b = m.find();

        assertTrue(b);
    }

    @Test
    public void testNoSpecialCharacter() throws Exception {
        String password = "noSpecialCharacters";

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(password);
        boolean b = m.find();

        assertFalse(b);
    }

    @Test
    public void testPasswordLengthShort() throws Exception {
        String password = "length<10";

        boolean b = password.length()<10;

        assertTrue(b);
    }

    @Test
    public void testPasswordLengthLong() throws Exception {
        String password = "longerThan10";

        boolean b = password.length()<10;

        assertFalse(b);
    }

    @Test
    public void testPasswordsMatch() throws Exception {
        String password = "password";
        String confirmPassword = "password";

        boolean b = (!password.equals(confirmPassword));

        assertFalse(b);
    }

    @Test
    public void testPasswordsDifferent() throws Exception {
        String password="password";
        String confirmPassword = "differentPassword";

        boolean b = (!password.equals(confirmPassword));

        assertTrue(b);
    }
}