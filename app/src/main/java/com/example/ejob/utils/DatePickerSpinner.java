package com.example.ejob.utils;

/*--------------------------------------------------------------------------*
 | Copyright (C) 2012 Maximilian Lenkeit                                    |
 |                                                                          |
 | This program is free software; you can redistribute it and/or modify     |
 | it under the terms of the GNU General Public License as published by the |
 | Free Software Foundation. A copy of the license has been included with   |
 | these distribution in the COPYING file, if not go to www.fsf.org         |
 |                                                                          |
 | As a special exception, you are granted the permissions to link this     |
 | program with every library, which license fulfills the Open Source       |
 | Definition as published by the Open Source Initiative (OSI).             |
 *--------------------------------------------------------------------------*/

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

/**
 * This widget combines a spinner widget with a date picker dialog. Clicking the
 * spinner widget opens the date picker dialog for selecting a date. Pressing ok
 * on the dialog puts the selected date into the spinner widget as selected
 * value.
 *
 * @author Maximilian Lenkeit <dev@lenki.com>
 */
public class DatePickerSpinner extends Spinner {

    private DatePickerDialog dialog;
    private Calendar selectedDate;
    private DateFormat format;

    /**
     * Constructor with date. If date is null, the date will be set to the
     * current date.
     *
     * @param context
     *                     Current context
     * @param selectedDate
     *                     Default date
     */
    public DatePickerSpinner(Context context, Date selectedDate) {
        super(context);

        // Prepare input date
        if (selectedDate == null) {
            selectedDate = new Date();
        }

        this.selectedDate = Calendar.getInstance();
        this.selectedDate.setTime(selectedDate);
        this.format = android.text.format.DateFormat.getDateFormat(this
                .getContext());

        this.setOnTouchListener(new SpinnerOnTouchListener(this.getContext(),
                this, this.dialog, this.selectedDate));
        this.refresh();
    }

    /**
     * Constructor without date, date defaults to the device's date
     *
     * @param context
     *                Current context
     */
    public DatePickerSpinner(Context context) {
        // Set date to current date on device
        this(context, null);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        // By returning false, the default behavoir of the spinner widget is
        // overwritten and the standard item choice dialog is shown. This allows
        // for displaying a custom dialog.
        return false;
    }

    /**
     * @return Currently displayed date
     */
    public Date getDate() {
        return this.selectedDate.getTime();
    }

    /**
     * Set date and refresh spinner
     *
     * @param date
     *             Date to be set
     */
    public void setDate(Date date) {
        this.refresh(date);
    }

    /**
     * Refresh spinner with last selected date formatted according to the user's
     * locale settings
     */
    public void refresh() {
        this.refresh(this.selectedDate.getTime());
    }

    /**
     * Refresh spinner with specified date formatted according to the user's
     * locale settings
     *
     * @param date
     *             Date to be displayed
     */
    public void refresh(Date date) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getContext(), android.R.layout.simple_spinner_item,
                new String[] { this.format.format(date) });
        this.setAdapter(adapter);
    }

    /**
     * Refresh spinner with specified year, month and day formatted according to
     * the user's locale settings.
     *
     * @param year
     *                    Year
     * @param monthOfYear
     *                    Month of the year
     * @param dayOfMonth
     *                    Day of the month
     */
    public void refresh(int year, int monthOfYear, int dayOfMonth) {
        this.selectedDate.set(year, monthOfYear, dayOfMonth);
        this.refresh(this.selectedDate.getTime());
    }

    /**
     * This listener listens to touch events on the spinner widget and displays
     * the date picker dialog.
     *
     * @author Maximilian Lenkeit <dev@lenki.com>
     *
     */
    public static class SpinnerOnTouchListener implements OnTouchListener {

        private Context context;
        private DatePickerSpinner spinner;
        private DatePickerDialog dialog;
        private Calendar selectedDate;

        /**
         * @param context
         *                     Current context
         * @param spinner
         *                     Spinner reference
         * @param dialog
         *                     Date picker dialog reference
         * @param selectedDate
         *                     Default date
         */
        public SpinnerOnTouchListener(Context context,
                DatePickerSpinner spinner, DatePickerDialog dialog,
                Calendar selectedDate) {
            this.context = context;
            this.spinner = spinner;
            this.dialog = dialog;
            this.selectedDate = selectedDate;
        }

        public boolean onTouch(View v, MotionEvent event) {
            // Check whether dialog has already been created and is still
            // showing, then do not create a new dialog
            if (this.dialog != null && this.dialog.isShowing()) {
                return false;
            }

            // Create new date picker dialog with current date
            DatePickerOnDateSetListener listener = new DatePickerOnDateSetListener(
                    this.spinner);

            this.dialog = new DatePickerDialog(this.context, listener,
                    this.selectedDate.get(Calendar.YEAR),
                    this.selectedDate.get(Calendar.MONTH),
                    this.selectedDate.get(Calendar.DAY_OF_MONTH));

            this.dialog.show();
            return true;
        }

    }

    /**
     * This listener listens to the save button of the date picker dialog and
     * refreshes the spinner with the selected date.
     *
     * @author Maximilian Lenkeit <dev@lenki.com>
     *
     */
    public static class DatePickerOnDateSetListener implements
            OnDateSetListener {

        private DatePickerSpinner spinner;

        /**
         * @param spinner
         *                Spinner reference for refresh
         */
        public DatePickerOnDateSetListener(DatePickerSpinner spinner) {
            this.spinner = spinner;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            this.spinner.refresh(year, monthOfYear, dayOfMonth);
        }

    }

}
