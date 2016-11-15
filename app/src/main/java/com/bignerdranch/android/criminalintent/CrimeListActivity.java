package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by @s.t.o.k.a.t.o on 14.11.2016.
 *
 * Контейнер для списка престплений
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {

        return new CrimeListFragment();
    }


}
