package com.haksoy.pertem.model;

import android.util.Log;

import com.haksoy.pertem.tools.BaseModelPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Announce implements BaseModelPresenter {
    private String title;
    private String desc;
    private String date;
    private String TAG = "Announce";

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return date.split("\\.")[0];
    }

    public String getMoon() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            Log.e(TAG, e.getLocalizedMessage());

        }
        return (new SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.getTime()));
    }

    public String getDayString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(date));
        } catch (ParseException e) {
            Log.e(TAG, e.getLocalizedMessage());

        }
        return (new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String onTitle() {
        return getTitle();
    }

    @Override
    public String onDesc() {
        return "Detaylar için tıklayın !";
    }
}
