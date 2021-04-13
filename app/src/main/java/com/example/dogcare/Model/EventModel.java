package com.example.dogcare.Model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class EventModel {
    private int id, dogID;
    private String dogName, type, notes;
    private long dateTime;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getDogID() { return dogID; }
    public void setDogID(int dogID) { this.dogID = dogID; }

    public String getDogName() {
        return dogName;
    }
    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getType() {
        return type;
    }
    public String getTypeText() { return " "+type;}
    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // https://stackoverflow.com/questions/7363112/best-way-to-work-with-dates-in-android-sqlite
    public long getDateTime() { return dateTime; }
    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    // http://www.java2s.com/Code/Java/Data-Type/Checksifacalendardateistoday.htm
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
    private boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }
    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }
    public static boolean isWithinDaysPast(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar past = Calendar.getInstance();
        past.add(Calendar.DAY_OF_YEAR, -1*days);
        return (isBeforeDay(cal, today) && ! isBeforeDay(cal, past));
    }
    public static boolean isWithinDaysPast(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysPast(cal, days);
    }

    // https://developer.android.com/reference/java/text/SimpleDateFormat.html
    // https://stackoverflow.com/questions/6537535/check-date-with-todays-date
    public String getDate() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return sdf.format(cal.getTime());
    }
    public String getDateText() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        Date date = cal.getTime();
        if (isToday(date)) {
            return " today";
        }
        if (isWithinDaysPast(date, 1)) {
            return " yesterday";
        }
        if (isWithinDaysPast(date, 2)) {
            return " two days ago";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return " on "+sdf.format(date);
    }

    public String getTime() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        return sdf.format(date);
    }
    public String getTimeText() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
        return " at "+sdf.format(date);
    }

    public int getYear() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        return cal.get(Calendar.YEAR);
    }
    public int getMonth() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        return cal.get(Calendar.MONTH);
    }
    public int getDay() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    public int getHour() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    public int getMinute() {
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(dateTime);
        return cal.get(Calendar.MINUTE);
    }
}
