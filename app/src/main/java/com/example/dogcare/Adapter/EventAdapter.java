package com.example.dogcare.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dogcare.Utility.DatabaseHelper;
import com.example.dogcare.MainActivity;
import com.example.dogcare.Model.EventModel;
import com.example.dogcare.Dialog.NewEventDialog;
import com.example.dogcare.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventModel> eventList;
    private final DatabaseHelper db;
    private final MainActivity activity;

    public EventAdapter(DatabaseHelper db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView dogName, eventType, eventDateTextView, eventTimeTextView, eventNotes;

        EventViewHolder(View view) {
            super(view);
            dogName = view.findViewById(R.id.dogNameTextView);
            eventType = view.findViewById(R.id.eventTypeTextView);
            eventDateTextView = view.findViewById(R.id.eventDateTextView);
            eventTimeTextView = view.findViewById(R.id.eventTimeTextView);
            eventNotes = view.findViewById(R.id.eventNotesTextView);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View eventView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event, parent, false);
        return new EventViewHolder(eventView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventViewHolder viewHolder, int position) { // new dialog?
        db.openDatabase();

        final EventModel event = eventList.get(position);
        viewHolder.dogName.setText(event.getDogName());
        viewHolder.eventType.setText(event.getTypeText());
        viewHolder.eventDateTextView.setText(event.getDateText());
        viewHolder.eventTimeTextView.setText(event.getTimeText());
        viewHolder.eventNotes.setText(event.getNotes());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setEvents(List<EventModel> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        EventModel item = eventList.get(position);
        db.deleteEvent(item.getId());
        eventList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) { // for an edit dialog?
        EventModel event = eventList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", event.getId());
        bundle.putString("dogName", event.getDogName());
        bundle.putString("eventType", event.getType());
        bundle.putString("eventDate", event.getDate());
        bundle.putString("eventTime", event.getTime());
        bundle.putString("eventNotes", event.getNotes());
        bundle.putLong("eventDateTime", event.getDateTime());
        bundle.putInt("eventYear", event.getYear());
        bundle.putInt("eventMonth", event.getMonth());
        bundle.putInt("eventDay", event.getDay());
        bundle.putInt("eventHour", event.getHour());
        bundle.putInt("eventMinute", event.getMinute());
        NewEventDialog dialog = new NewEventDialog();
        dialog.setArguments(bundle);
        dialog.show(activity.getSupportFragmentManager(), NewEventDialog.TAG);
    }

}
