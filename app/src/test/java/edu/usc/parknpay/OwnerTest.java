package edu.usc.parknpay;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Timothy on 11/5/2016.
 */

public class OwnerTest {

    @Test
    public void testDateBeforeValid() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date1 = df.parse("2000-01-31 02:00");
            Date date2 = df.parse("");

            boolean b = !date1.before(date2);

            assertFalse(b);
        } catch(ParseException e) {
        }
    }

    @Test
    public void testDateBeforeInvalid() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date1 = df.parse("2000-02-15 01:00");
            Date date2 = df.parse("2000-01-31 02:00");

            boolean b = !date1.before(date2);

            assertTrue(b);
        } catch(ParseException e) {
        }
    }

    @Test
    public void testDateBeforeEqual() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date1 = df.parse("2000-02-15 01:00");
            Date date2 = df.parse("2000-02-15 01:00");

            boolean b = !date1.before(date2);

            assertTrue(b);
        } catch(ParseException e) {
        }
    }
}
