package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

/**
 * Created by @s.t.o.k.a.t.o on 14.11.2016.
 *
 * Родительский клас для списка преступлений и единичного их отображения
 */

public abstract class SingleFragmentActivity extends FragmentActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        // Получаем менеджер фрагметнов
        FragmentManager fm = getSupportFragmentManager();

        // При воссоздании активности получаем фрагмент из сохраненного списка
        Fragment fragment = fm.findFragmentById(R.id.content_crime);

        // Если фрагмента в списке нет
        if(fragment == null) {
            // Создаем новый
            fragment = createFragment();

            // Впоследствии он будет идентифицироваться по ид представления
            fm.beginTransaction().add(R.id.content_crime, fragment).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

    }
}
