package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by @s.t.o.k.a.t.o on 14.11.2016.
 *
 * Фрагмент для отображения списка преступлений
 */

public class CrimeListFragment extends Fragment{

    private static final String CRIME_INDEX = "CRIME_INDEX";
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private String mActiveCrimeId;
    private boolean mSubtitleVisible;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView)v.findViewById(R.id.crime_recycler_view);

        // Менеджер необъодим для работы RecyclerView, отвечает за размещение элементов на экране
        // LinearLayoutManager - строит вертиальный список
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null) {
            mActiveCrimeId = savedInstanceState.getString(CRIME_INDEX);
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString(CRIME_INDEX, mActiveCrimeId);

        savedInstanceState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else  {
            mAdapter.notifyDataSetChanged();

            updateSubtitle();
//            if(mActiveCrimeId == null) {
//                mAdapter.notifyDataSetChanged();
//                return;
//            }
//
//            UUID crimeId = UUID.fromString(mActiveCrimeId);
//
//            int pos = mAdapter.getPosById(crimeId);
//
//            if(pos < 0) {
//                mAdapter.notifyDataSetChanged();
//            } else {
//                mAdapter.notifyItemChanged(pos);
//
//                Log.d("CrimeListFragment", "byPos");
//            }
        }
    }

    // Создаем меню
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);

        // Сообщаем, что мы должны получать обратные вызовы командного меню
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime  crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);

                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);

                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                // Если идентификтаор команды неизвестен нашей реализации возвращаем реализацию суперкласса
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());

        int crimeCount = crimeLab.getCrimes().size();

        // Геренируем строку подзаголовка
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Crime mCrime;



        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            // ViewHolder сохраняет результаты вызовов, поэтому эти обращения будут просходить один раз
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);

        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked", Toast.LENGTH_SHORT).show();

            // Данный способ привязвает фрагмент к конкретной активности, лючше использовать пакет аргументов (arg. bundle)
            mActiveCrimeId = mCrime.getId().toString();
//            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());

            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }


    //----------------------------------------------------------------------------------------------
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private  List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        // Вызывается виджетом RecyclerView, когда ему потребуется новое представление для отображения элемента
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            // Получаем макет из стандартной библиотеки
            // android.R.layout.simple_expandable_list_item_1
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);

            // Связываем представление с объектом модели
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
//            holder.mTitleTextView.setText(crime.getTitle());
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }

//        public int getPosById(UUID id) {
//
//            for(int i = 0; i < mCrimes.size(); i++) {
//                if(mCrimes.get(i).getId().equals(id)) {
//                    return i;
//                }
//            }
//
//            return -1;
//        }
    }

}
