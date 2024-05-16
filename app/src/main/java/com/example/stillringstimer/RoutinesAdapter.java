package com.example.stillringstimer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoutinesAdapter extends RecyclerView.Adapter<RoutinesAdapter.ViewHolder> {

    private ArrayList<intervalsModel> routinesList;

    public RoutinesAdapter(ArrayList<intervalsModel> routinesList) {
        this.routinesList = routinesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        intervalsModel interval = routinesList.get(position);
        holder.routineButton.setText(interval.getTitle());
        holder.routineButton.setOnClickListener(new View.OnClickListener() {
            @Override // tu sem se spravla delat da te premakne na rutino ko jo klikneš v seznamu ampak nisem končala ker bi morala bit shranjena rutina
            public void onClick(View v) {
                //intervalsModel selectedInterval = routinesList.get(position);
                //Intent intent = new Intent(v.getContext(), YourActivity.class);
                //intent.putExtra("routineName", selectedInterval.getTitle());
                //v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routinesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button routineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routineButton = itemView.findViewById(R.id.routineButton);
        }
    }
}
