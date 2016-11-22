package com.bignerdranch.android.criminalintent.database;

/**
 * Created by @s.t.o.k.a.t.o on 16.11.2016.
 */

public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID     = "uuid";
            public static final String TITLE    = "title";
            public static final String DATE     = "date";
            public static final String SOLVED   = "solved";
            public static final String SUSPECT  = "suspect";
        }
    }
}
