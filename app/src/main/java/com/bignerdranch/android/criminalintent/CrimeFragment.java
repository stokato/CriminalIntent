package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by @s.t.o.k.a.t.o on 11.11.2016.
 */

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Crime mCrime;

    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mCrime = new Crime();

//        UUID crimeID = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeID = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    private String getCrimeDate() {

            Date date = mCrime.getDate();

            String ds = new SimpleDateFormat("EEE, MMM d, ''yyyy", Locale.ENGLISH).format(date);

//            return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(date);
        return ds;
    }

    private String getCrimeTime() {
        Date date = mCrime.getDate();

        String ts = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(date);

        return ts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // При создании представления получаем инфлейтор, к нему затем и обращаемся
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        // Обработка ввода пользователя
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);
//        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setText(getCrimeDate());
//        mDateButton.setEnabled(false);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
//                DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());

                // Для связи между фрагментами с целью получения результата
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);

                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        mTimeButton.setText(getCrimeTime());
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());

                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });

        return v;
    }

    // Обработка результаов диалогов
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        // Выбор даты
        if(requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            Calendar reqCal = Calendar.getInstance();
            reqCal.setTime(date);

            Calendar curCal = Calendar.getInstance();
            curCal.setTime(mCrime.getDate());

            curCal.set(Calendar.YEAR, reqCal.get(Calendar.YEAR));
            curCal.set(Calendar.MONTH, reqCal.get(Calendar.MONTH));
            curCal.set(Calendar.DAY_OF_MONTH, reqCal.get(Calendar.DAY_OF_MONTH));

            mCrime.setDate(curCal.getTime());
            mDateButton.setText(getCrimeDate());
        }

        if(requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DATE);

            Calendar reqCal = Calendar.getInstance();
            reqCal.setTime(date);

            Calendar curCal = Calendar.getInstance();
            curCal.setTime(mCrime.getDate());

            curCal.set(Calendar.HOUR, reqCal.get(Calendar.HOUR));
            curCal.set(Calendar.MINUTE, reqCal.get(Calendar.MINUTE));
            curCal.set(Calendar.SECOND, reqCal.get(Calendar.SECOND));

            mCrime.setDate(curCal.getTime());

            mTimeButton.setText(getCrimeTime());
        }
    }

    public static CrimeFragment newInstance(UUID crimeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeID);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return  fragment;
    }

}
