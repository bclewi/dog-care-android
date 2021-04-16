package com.example.dogcare.Dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.dogcare.Model.EventModel;
import com.example.dogcare.R;
import com.example.dogcare.Utility.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewEventDialog extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "ActionBottomDialog";
    private EditText
            dogName,
            //eventType,
            eventNotes;
    private TextView
            eventDateTextView,
            eventTimeTextView;
    private Calendar cal = Calendar.getInstance(Locale.US);

    // TODO: move instantiations to "SaveButton".onClickListener()
    private int
            eventYear = cal.get(Calendar.YEAR),
            eventMonth = cal.get(Calendar.MONTH),
            eventDay = cal.get(Calendar.DAY_OF_MONTH),
            eventHour = cal.get(Calendar.HOUR_OF_DAY),
            eventMinute = cal.get(Calendar.MINUTE);
    private long eventDateTime = cal.getTimeInMillis();
    // TODO: ends here

    private Button eventSaveButton;
    private AtomicBoolean
            hasDogName = new AtomicBoolean(false),
            hasEventType = new AtomicBoolean(false),
            hasEventDate = new AtomicBoolean(false),
            hasEventTime = new AtomicBoolean(false);

    private DatabaseHelper db;






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_event_new, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }



    public static NewEventDialog newInstance(){
        return new NewEventDialog();
    }

    private void updateSaveButton() {
        if (hasDogName.get()) {
            eventSaveButton.setEnabled(true);
            eventSaveButton.setTextColor(ContextCompat.getColor(
                    Objects.requireNonNull(getContext()),
                    R.color.design_default_color_primary_dark));
        } else {
            eventSaveButton.setEnabled(false);
            eventSaveButton.setTextColor(Color.GRAY);
        }
    }

    private void validateText(@NonNull CharSequence s, AtomicBoolean b) {
        b.set(!s.toString().equals(""));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // added the spinner class
        Spinner spinner = (Spinner) view.findViewById(R.id.staticSpinner);


        dogName = Objects.requireNonNull(getView()).findViewById(R.id.dogNameEditText);
        //eventType = Objects.requireNonNull(getView()).findViewById(R.id.staticSpinner);
        eventDateTextView = Objects.requireNonNull(getView()).findViewById(R.id.eventDateTextView);
        eventTimeTextView = Objects.requireNonNull(getView()).findViewById(R.id.eventTimeTextView);
        eventNotes = Objects.requireNonNull(getView()).findViewById(R.id.eventNotesEditText);
        eventSaveButton = getView().findViewById(R.id.newEventButton);

        boolean hasNewUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            hasNewUpdate = true;

            String dogNameString = bundle.getString("dogName");
            dogName.setText(dogNameString);
            validateText(dogNameString, hasDogName);
            // commented out this to make it run as you cant get string from spinner with getstring
            //
           // String eventTypeString = spinner.getSelectedItem().toString();
           // eventType.setText(eventTypeString);
           // validateText(eventTypeString, hasEventType);

            String eventDateString = bundle.getString("eventDate");
            eventDateTextView.setText(eventDateString);
            validateText(eventDateString, hasEventDate);

            String eventTimeString = bundle.getString("eventTime");
            eventTimeTextView.setText(eventTimeString);
            validateText(eventTimeString, hasEventTime);

            eventNotes.setText(bundle.getString("eventNotes"));

            eventDateTime = bundle.getLong("eventDateTime");

            eventYear = bundle.getInt("eventYear");
            eventMonth = bundle.getInt("eventMonth");
            eventDay = bundle.getInt("eventDay");
            eventHour = bundle.getInt("eventHour");
            eventMinute = bundle.getInt("eventMinute");

            updateSaveButton();

        }

        db = new DatabaseHelper(getActivity());
        db.openDatabase();

        dogName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateText(s, hasDogName);
                updateSaveButton();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

       /* eventType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateText(s, hasEventType);
                updateSaveButton();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        }); */

        // https://www.geeksforgeeks.org/datepickerdialog-in-android/
        // https://developer.android.com/guide/fragments/fragmentmanager
        // https://www.journaldev.com/9976/android-date-time-picker-dialog
        eventDateTextView.setOnClickListener(v -> {
            //DatePicker datePickerDialogFragment = new DatePicker();
            //datePickerDialogFragment.show(getChildFragmentManager(), "DATE PICK");
            final Calendar cal = Calendar.getInstance(Locale.US);
            int currYear = cal.get(Calendar.YEAR);
            int currMonth = cal.get(Calendar.MONTH); // DatePicker(currMonth) and Calender.MONTH are both 0 indexed
            int currDay = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    // onDateSetListener
                    (datePickerView, yearPicked, monthPicked, dayOfMonthPicked) -> {
                        cal.set(Calendar.YEAR, yearPicked);
                        cal.set(Calendar.MONTH, monthPicked); // cal and monthPicked are both 0 indexed
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonthPicked);

                        eventYear = yearPicked;
                        eventMonth = monthPicked; // both 0 indexed
                        eventDay = dayOfMonthPicked;

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        String datePickedText = sdf.format(cal.getTime());
                        eventDateTextView.setText(datePickedText);

                        validateText(datePickedText, hasEventDate);
                        updateSaveButton();
                    },
                    // set datePickerDialog to initially show the current date selected
                    currYear, currMonth, currDay);
            datePickerDialog.show();

        });

        // https://www.journaldev.com/9976/android-date-time-picker-dialog
        eventTimeTextView.setOnClickListener(v -> {
            final Calendar cal = Calendar.getInstance(Locale.US);
            int currHour = cal.get(Calendar.HOUR_OF_DAY);
            int currMinute = cal.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(),
                    // onTimeSetListener
                    (timePickerView, hourOfDayPicked, minutePicked) -> {
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDayPicked);
                        cal.set(Calendar.MINUTE, minutePicked);

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
                        String timePickedText = sdf.format(cal.getTime());
                        eventTimeTextView.setText(timePickedText);

                        eventHour = hourOfDayPicked;
                        eventMinute = minutePicked;

                        validateText(timePickedText, hasEventTime);
                        updateSaveButton();
                    },
                    currHour, currMinute, false);
            timePickerDialog.show();

        });

        final boolean hasNewUpdateFinal = hasNewUpdate;


        eventSaveButton.setOnClickListener(v -> {
            String dogNameInput = dogName.getText().toString();
            //below is the way to get the new string from the spinner to add to database
            String eventTypeInput  = spinner.getSelectedItem().toString();
            //String eventTypeInput = eventType.getText().toString();
            String eventNotesInput = eventNotes.getText().toString();

            Calendar cal = Calendar.getInstance(Locale.US);
            cal.set(eventYear, eventMonth, eventDay, eventHour, eventMinute);
            eventDateTime = cal.getTimeInMillis();
            if (hasNewUpdateFinal) { // if event update?
                db.updateName(bundle.getInt("id"), dogNameInput);
                db.updateType(bundle.getInt("id"), eventTypeInput);
                db.updateDateTime(bundle.getInt("id"), eventDateTime);
                db.updateNotes(bundle.getInt("id"), eventNotesInput);
            } else { // if new event?
                EventModel event = new EventModel();
                event.setDogName(dogNameInput);
                event.setType(eventTypeInput);
                event.setDateTime(eventDateTime);
                event.setNotes(eventNotesInput);
                db.insertEvent(event);
            }
            dismiss();
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}