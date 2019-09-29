package de.mindyourbyte.alphalapse;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import android.text.format.DateFormat;

import java.util.Calendar;

public class NonTouchTimePicker extends LinearLayout {

    NonTouchNumberPicker hourPicker;
    NonTouchNumberPicker minutePicker;
    NonTouchNumberPicker secondPicker;
    NonTouchListPicker ampmPicker;
    boolean isDatePicker = false;
    int textSize = 0;
    int padding = 0;
    private TimePickerChanged callback;

    public TimePickerChanged getCallback() {
        return callback;
    }

    public void setCallback(TimePickerChanged callback) {
        this.callback = callback;
    }

    private void onPickerValueChanged(String value, int valueIndex) {
        onValueChanged();
    }

    private void onPickerValueChanged(int value) {
        onValueChanged();
    }

    public interface TimePickerChanged {
        void onTimeChanged(NonTouchTimePicker picker);
    }

    public void saveChanges(SharedPreferences preferences, String prefix) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(prefix + "hours", hourPicker.getValue());
        editor.putInt(prefix + "minutes", minutePicker.getValue());
        editor.putInt(prefix + "seconds", secondPicker.getValue());
        editor.putInt(prefix + "ampm", ampmPicker.getIndex());
        editor.apply();
    }

    public static Calendar loadTimeFromPreferences(SharedPreferences preferences, String prefix){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, preferences.getInt(prefix + "ampm", calendar.get(Calendar.AM_PM)));
        calendar.set(Calendar.HOUR, preferences.getInt(prefix + "hours", calendar.get(Calendar.HOUR)));
        calendar.set(Calendar.MINUTE, preferences.getInt(prefix + "minutes", calendar.get(Calendar.MINUTE)));
        calendar.set(Calendar.SECOND, preferences.getInt(prefix + "seconds", calendar.get(Calendar.SECOND)));
        return  calendar;
    }

    public static long loadIntervalFromPreferences(SharedPreferences preferences, String prefix){
        return (((preferences.getInt(prefix + "hours", 0)) * 60 +
                (preferences.getInt(prefix + "minutes", 0)) * 60) +
                preferences.getInt(prefix + "seconds", 0)) * 1000;
    }

    public void loadData(SharedPreferences preferences, String prefix){
        Calendar calendar = Calendar.getInstance();
        hourPicker.setValue(preferences.getInt(prefix + "hours", calendar.get(Calendar.HOUR)));
        minutePicker.setValue(preferences.getInt(prefix + "minutes", calendar.get(Calendar.MINUTE)));
        secondPicker.setValue(preferences.getInt(prefix + "seconds", calendar.get(Calendar.SECOND)));
        ampmPicker.setIndex(preferences.getInt(prefix + "ampm", calendar.get(Calendar.AM_PM)));
    }

    public NonTouchTimePicker(Context context) {
        super(context);
        hourPicker = new NonTouchNumberPicker(context);
        minutePicker = new NonTouchNumberPicker(context);
        secondPicker = new NonTouchNumberPicker(context);
        ampmPicker = new NonTouchListPicker(context);
        initialize(context, null);
    }


    public Calendar getDate() {
        Calendar result = Calendar.getInstance();
        boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        if (is24HourFormat){
            result.set(Calendar.AM_PM, ampmPicker.getIndex() == 0 ? Calendar.AM: Calendar.PM);
            result.set(Calendar.HOUR_OF_DAY, hourPicker.getValue());
        }
        else {
            result.set(Calendar.HOUR, hourPicker.getValue());
        }
        result.set(Calendar.MINUTE, minutePicker.getValue());
        result.set(Calendar.SECOND, secondPicker.getValue());
        return  result;
    }

    public long getMillisecondsInterval() {
        return  (((hourPicker.getValue() * 60) + minutePicker.getValue()) * 60 + secondPicker.getValue()) * 1000;
    }

    private void initialize(Context context, TypedArray attrs) {
        if (attrs != null) {
            isDatePicker = attrs.getBoolean(R.styleable.NonTouchTimePicker_isDatePicker, false);
            textSize = attrs.getDimensionPixelSize(R.styleable.NonTouchTimePicker_textSize, 10);
            padding = attrs.getDimensionPixelSize(R.styleable.NonTouchTimePicker_padding, 0);
        }
        hourPicker.setPostfix(context.getString(R.string.hours_postfix));
        minutePicker.setPostfix(context.getString(R.string.minutes_postfix));
        secondPicker.setPostfix(context.getString(R.string.seconds_postfix));
        hourPicker.setMinValue(0);
        minutePicker.setMinValue(0);
        secondPicker.setMinValue(0);

        minutePicker.setMaxValue(59);
        secondPicker.setMaxValue(59);


        setupButtonStyle(hourPicker);
        setupButtonStyle(minutePicker);
        setupButtonStyle(secondPicker);
        setupButtonStyle(ampmPicker);

        setOrientation(LinearLayout.HORIZONTAL);
        addView(hourPicker);
        addView(minutePicker);
        addView(secondPicker);
        if (isDatePicker) {
            boolean is24HourFormat = DateFormat.is24HourFormat(context);
            hourPicker.setMaxValue(is24HourFormat ? 23 : 12);
            if (!is24HourFormat){
                ampmPicker.setValues(new String[]{
                        context.getString(R.string.am),
                        context.getString(R.string.pm),
                });
                addView(ampmPicker);
            }
        }
        else {
            hourPicker.setMaxValue(9999);
        }
        hourPicker.setCallback(this::onPickerValueChanged);
        minutePicker.setCallback(this::onPickerValueChanged);
        secondPicker.setCallback(this::onPickerValueChanged);
        ampmPicker.setCallback(this::onPickerValueChanged);

    }

    private void onValueChanged() {
        if (callback != null) {
            callback.onTimeChanged(this);
        }
    }

    private void setupButtonStyle(Button picker) {
        picker.setTextSize(textSize);
        picker.setMinWidth(textSize);
        picker.setMinimumWidth(textSize);
    }

    public NonTouchTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        hourPicker = new NonTouchNumberPicker(context, attrs);
        minutePicker = new NonTouchNumberPicker(context, attrs);
        secondPicker = new NonTouchNumberPicker(context, attrs);
        ampmPicker = new NonTouchListPicker(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NonTouchTimePicker);
        initialize(context, attributes);
    }

    public NonTouchTimePicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        hourPicker = new NonTouchNumberPicker(context, attrs, defStyle);
        minutePicker = new NonTouchNumberPicker(context, attrs, defStyle);
        secondPicker = new NonTouchNumberPicker(context, attrs, defStyle);
        ampmPicker = new NonTouchListPicker(context, attrs, defStyle);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NonTouchTimePicker, defStyle, 0);
        initialize(context, attributes);
    }
}
