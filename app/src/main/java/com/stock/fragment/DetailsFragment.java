package com.stock.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.stock.R;
import com.stock.dao.MeatDao;
import com.stock.entity.Meat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by m on 29.07.2017.
 */

public class DetailsFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.date_coming)
    EditText dateComing;
    @BindView(R.id.weigth_coming)
    EditText weightComing;
    @BindView(R.id.price_coming)
    EditText priceComing;
    @BindView(R.id.date_output)
    EditText dateOutput;
    @BindView(R.id.weigth_output)
    EditText weightOutput;
    @BindView(R.id.price_output)
    EditText priceOutput;
    @BindView(R.id.textProfit)
    TextView profit;
    @BindView(R.id.textShrinkage)
    TextView shrinkage;

    private Calendar calendar;
    private int year, month, day;
    private String dateField;
    private static Meat meat;
    private ProgressDialog progressDialog;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static DetailsFragment newInstans(Meat meat) {
        DetailsFragment frg = new DetailsFragment();
        frg.meat = meat;
        return frg;
    }

    public DetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (meat == null)
            meat = new Meat();
        priceOutput.addTextChangedListener(watcher);
        weightOutput.addTextChangedListener(watcher);
        weightComing.addTextChangedListener(watcher);
        weightComing.addTextChangedListener(watcher);
        dateComing.setOnClickListener(this);
        dateOutput.setOnClickListener(this);
        return view;
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year, month, dayOfMonth);
            if (dateField.equals("coming")) {
                meat.setDateComing(calendar.getTimeInMillis());
                dateComing.setText(sdf.format(calendar.getTime()));
            }
            if (dateField.equals("output")) {
                meat.setDateOutput(calendar.getTimeInMillis());
                dateOutput.setText(sdf.format(calendar.getTime()));
            }
        }
    };

    public void saveMeat() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        showProgressDialog();
        MeatDao dao = new MeatDao(handler);
        if (meat.getId() == null) {
            dao.save(createMeat());
            return;
        }
        dao.updateByObject(createMeat());

    }

    @Override
    public void onClick(View v) {
        new DatePickerDialog(getActivity(), dateSetListener, year, month, day).show();
        switch (v.getId()) {
            case R.id.date_coming:
                dateField = "coming";
                break;
            case R.id.date_output:
                dateField = "output";
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void fieldFilling() {
        if (meat == null)
            return;
        if (meat.getDateComing() > 0)
            dateComing.setText(sdf.format(new Date(meat.getDateComing())));
        if (meat.getWeigthComing() > 0)
            weightComing.setText(meat.getWeigthComing() + "");
        if (meat.getPriceComing() > 0)
            priceComing.setText(meat.getPriceComing() + "");
        if (meat.getDateOutput() > 0)
            dateOutput.setText(sdf.format(new Date(meat.getDateOutput())));
        if (meat.getWeigthOutput() > 0)
            weightOutput.setText(meat.getWeigthOutput() + "");
        if (meat.getPriceOutput() > 0)
            priceOutput.setText(meat.getPriceOutput() + "");
    }

    private Meat createMeat() {
        meat.setWeigthComing(convertToDouble(weightComing.getText().toString()));
        meat.setPriceComing(convertToDouble(priceComing.getText().toString()));
        meat.setWeigthOutput(convertToDouble(weightOutput.getText().toString()));
        meat.setPriceOutput(convertToDouble(priceOutput.getText().toString()));
        return meat;
    }

    private double convertToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            Log.i("log_tag", "convert wrong");
        }
        return 0;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MeatDao.HANDLER_RESULT_OK:
                    progressDialog.dismiss();
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    break;
                case MeatDao.HANDLER_RESULT_ERR:
                    progressDialog.dismiss();
                    break;
            }

        }
    };

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Сохранение");
        progressDialog.show();
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            calculate();
        }
    };

    private void calculate() {
        if (weightComing.getText().length() != 0 && weightOutput.getText().length() != 0) {
            double weightLost = convertToDouble(weightComing.getText().toString())
                    - convertToDouble(weightOutput.getText().toString());
            double result = (weightLost * 100) / convertToDouble(weightComing.getText().toString());
            shrinkage.setText(String.format("Усушка: %.2f ", result) + "%");
        } else {
            return;
        }

        if (priceComing.getText().length() != 0 && priceOutput.getText().length() != 0) {
            double coming = convertToDouble(priceComing.getText().toString())
                    * convertToDouble(weightComing.getText().toString());
            double output = convertToDouble(priceOutput.getText().toString())
                    * convertToDouble(weightOutput.getText().toString());
            double prof = output - coming;
            profit.setText(String.format("Прибыль: %.2f грн", prof));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fieldFilling();
    }

    @Override
    public void onPause() {
        super.onPause();
        createMeat();
    }

}
