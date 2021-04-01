package com.example.dogcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private DatabaseHandler db;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private FloatingActionButton fab;
    private List<EventModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(db,MainActivity.this);
        eventRecyclerView.setAdapter(eventAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(eventAdapter));
        itemTouchHelper.attachToRecyclerView(eventRecyclerView);

        eventList = db.getAllEvents();
        Collections.reverse(eventList);
        eventAdapter.setEvents(eventList);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v ->
                AddNewEvent.newInstance().show(getSupportFragmentManager(),
                        AddNewEvent.TAG)
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