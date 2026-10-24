package com.example.dmitro.weatherapp.screen.select_city.list;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.geo.places.Prediction;

import java.util.ArrayList;

/**
 * Created by dmitro on 26.09.17.
 */

public class CityChoiceRecyclerAdapter extends RecyclerView.Adapter<CityChoiceRecyclerAdapter.PlaceHolder> {
    private ArrayList<Prediction> predictions;
    private RecyclerListener recyclerViewListener;
    private int lastCheckedPosition = -1;


    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

    public CityChoiceRecyclerAdapter(RecyclerListener recyclerViewListener, ArrayList<Prediction> predictions) {
        this.recyclerViewListener = recyclerViewListener;
        this.predictions = predictions;
    }

    public void updateData(ArrayList<Prediction> places) {
        predictions = places;
        notifyDataSetChanged();

    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.place_item_view, parent, false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceHolder holder, int position) {
        holder.bindView(predictions.get(position));


    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    class PlaceHolder extends RecyclerView.ViewHolder {
        TextView tvCountry;
        TextView tvCity;

        public PlaceHolder(View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvCity = itemView.findViewById(R.id.tvCity);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                lastCheckedPosition = position;
                notifyItemRangeChanged(0, predictions.size());
                recyclerViewListener.onClick(predictions.get(position));
            });
        }

        public void bindView(Prediction p) {
            tvCountry.setText(p.getTerms().get(p.getTerms().size() - 1).getValue());
            tvCity.setText(p.getDescription());


        }
    }


}