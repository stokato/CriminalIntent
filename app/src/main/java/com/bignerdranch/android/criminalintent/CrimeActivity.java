package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class CrimeActivity extends FragmentActivity {

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
            fragment = new CrimeFragment();

            // Впоследствии он будет идентифицироваться по ид представления
            fm.beginTransaction().add(R.id.content_crime, fragment).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

    }
}
