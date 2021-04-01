package com.example.dogcare;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewEvent extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newEventText;
    private Button newEventSaveButton;

    private DatabaseHandler db;

    public static AddNewEvent newInstance(){
        return new AddNewEvent();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_event, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newEventText = Objects.requireNonNull(getView()).findViewById(R.id.newEventText);
        newEventSaveButton = getView().findViewById(R.id.newEventButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String event = bundle.getString("event");
            newEventText.setText(event);
            assert event != null;
            if(event.length()>0)
                newEventSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.design_default_color_primary_dark));
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newEventText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newEventSaveButton.setEnabled(false);
                    newEventSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newEventSaveButton.setEnabled(true);
                    newEventSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.design_default_color_primary_dark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newEventSaveButton.setOnClickListener(v -> {
            String text = newEventText.getText().toString();
            if(finalIsUpdate){
                db.updateEvent(bundle.getInt("id"), text);
            }
            else {
                EventModel event = new EventModel();
                event.setEvent(text);
                event.setStatus(0);
                db.insertEvent(event);
            }
            dismiss();
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}