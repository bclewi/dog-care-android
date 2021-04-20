package com.example.dogcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dogcare.Adapter.EventAdapter;
import com.example.dogcare.Dialog.DialogCloseListener;
import com.example.dogcare.Dialog.NewEventDialog;
import com.example.dogcare.Model.EventModel;
import com.example.dogcare.Utility.DatabaseHelper;
import com.example.dogcare.Utility.RecyclerViewTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private DatabaseHelper db;
    private EventAdapter eventAdapter;
    private List<EventModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHelper(this);
        db.openDatabase();

        RecyclerView eventRecyclerView = findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(db,MainActivity.this);
        eventRecyclerView.setAdapter(eventAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerViewTouchHelper(eventAdapter));
        itemTouchHelper.attachToRecyclerView(eventRecyclerView);

        eventList = db.getAllEvents();
        Collections.reverse(eventList);
        eventAdapter.setEvents(eventList);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v ->
                NewEventDialog.newInstance().show(getSupportFragmentManager(),
                        NewEventDialog.TAG)
        );
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        eventList = db.getAllEvents();
        Collections.reverse(eventList);
        eventAdapter.setEvents(eventList);
        eventAdapter.notifyDataSetChanged();
    }

}