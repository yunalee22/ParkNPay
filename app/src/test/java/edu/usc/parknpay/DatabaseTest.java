package edu.usc.parknpay;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Timothy on 11/5/2016.
 */

public class DatabaseTest {

    @Test
    public void testSetNumRatings1() {
        int ratingGiven = 4;

        double rating = 2;
        double numRatings = 3;

        double tempRating = rating*numRatings;
        numRatings++;
        rating = (tempRating+(double)ratingGiven)/numRatings;

        assertEquals(rating, 2.5);
    }

    @Test
    public void testSetNumRatings2() {
        int ratingGiven = 1;

        double rating = 5;
        double numRatings =3;

        double tempRating = rating*numRatings;
        numRatings++;
        rating = (tempRating+(double)ratingGiven)/numRatings;

        assertEquals(rating, 4.0);
    }
}
