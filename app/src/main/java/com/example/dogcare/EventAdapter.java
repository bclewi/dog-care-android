package com.example.dogcare;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> eventList;
    private DatabaseHandler db;
    private MainActivity activity;

    public EventAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final EventModel item = eventList.get(position);
        holder.event.setText(item.getEvent());
        holder.event.setChecked(toBoolean(item.getStatus()));
        holder.event.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.updateStatus(item.getId(), 1);
            } else {
                db.updateStatus(item.getId(), 0);
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
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

    public void editItem(int position) {
        EventModel item = eventList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("event", item.getEvent());
        AddNewEvent fragment = new AddNewEvent();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewEvent.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox event;

        ViewHolder(View view) {
            super(view);
            event = view.findViewById(R.id.eventCheckBox);
        }
    }
}
