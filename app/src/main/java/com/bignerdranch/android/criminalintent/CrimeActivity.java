package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return  intent;
    }

    @Override
    protected Fragment createFragment() {
//        return new CrimeFragment();

        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        return  CrimeFragment.newInstance(crimeId);
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_crime);
//
//        // Получаем менеджер фрагметнов
//        FragmentManager fm = getSupportFragmentManager();
//
//        // При воссоздании активности получаем фрагмент из сохраненного списка
//        Fragment fragment = fm.findFragmentById(R.id.content_crime);
//
//        // Если фрагмента в списке нет
//        if(fragment == null) {
//            // Создаем новый
//            fragment = new CrimeFragment();
//
//            // Впоследствии он будет идентифицироваться по ид представления
//            fm.beginTransaction().add(R.id.content_crime, fragment).commit();
//        }
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        // setSupportActionBar(toolbar);
//
//    }
}
