package com.thalesgroup.movieproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.RecyclerViewHolder> {

    private List<ResultDisplay> listResult;
    private Context context;

    public RecyclerViewAdapter(List<ResultDisplay> listResult, Context context) {
        this.listResult = listResult;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_result, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        ResultDisplay result = listResult.get(position);

        holder.textTitle.setText(result.getTitle());
        holder.textRelease.setText(result.getRelease());
        holder.textRating.setText(result.getRating());
    }

    @Override
    public int getItemCount() {
        return listResult.size();
    }



    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView textTitle;
        public TextView textRelease;
        public TextView textRating;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textMovieTitle);
            textRelease = itemView.findViewById(R.id.textMovieRelease);
            textRating = itemView.findViewById(R.id.textMovieRating);
        }
    }
}
