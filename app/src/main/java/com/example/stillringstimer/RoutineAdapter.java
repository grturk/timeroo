package com.example.stillringstimer;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    private List<Training> trainings;
    private Context context;
    private Gson gson;

    public RoutineAdapter(List<Training> trainings, Context context) {
        this.trainings = trainings;
        this.context = context;
        this.gson = new Gson();
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        Training training = trainings.get(position);
        holder.routineButton.setText(training.getName());
        holder.routineButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayTraining.class);
            intent.putExtra("selectedTraining", gson.toJson(training));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trainings.size();
    }

    public static class RoutineViewHolder extends RecyclerView.ViewHolder {
        Button routineButton;

        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            routineButton = itemView.findViewById(R.id.routineButton);
        }
    }
}

