package com.example.dmitro.weatherapp.screen.current_weather.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.RecyclerViewEmptySupport;
import com.example.dmitro.weatherapp.screen.select_city.list.RecyclerListener;
import com.example.dmitro.weatherapp.utils.MyUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.dmitro.weatherapp.screen.current_weather.fragment.WeatherDetailsFragment.POSTFIX_BY_ICON_URL;

/**
 * Created by dmitro on 26.09.17.
 */

public class WeatherRecyclerAdapter extends RecyclerViewEmptySupport.Adapter<WeatherRecyclerAdapter.PlaceHolder> {
    private ArrayList<HashMap<String, ArrayList<WeatherResponse>>> weatherResponses;
    private RecyclerListener recyclerViewListener;
    private Context context;


    public WeatherRecyclerAdapter(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> weatherResponses, RecyclerListener listener) {
        this.recyclerViewListener = listener;
        this.weatherResponses = weatherResponses;
    }

    public void updateData(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> weather) {
        weatherResponses = weather;
        notifyDataSetChanged();

    }

    @Override
    public WeatherRecyclerAdapter.PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_weather, parent, false);
        WeatherRecyclerAdapter.PlaceHolder viewHolder = new WeatherRecyclerAdapter.PlaceHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherRecyclerAdapter.PlaceHolder holder, int position) {

        holder.bindView(weatherResponses.get(position));


    }

    @Override
    public int getItemCount() {
        return weatherResponses.size();
    }

    class PlaceHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        TextView tempTextView;
        ImageView iconImageView;

        public PlaceHolder(View itemView) {
            super(itemView);
            dayTextView = (TextView) itemView.findViewById(R.id.day_tv);
            tempTextView = (TextView) itemView.findViewById(R.id.temp_item_tv);
            iconImageView = (ImageView) itemView.findViewById(R.id.weather_icon_for_item_iv);
            itemView.setOnClickListener(view -> {
                recyclerViewListener.onClick(weatherResponses.get(getAdapterPosition()));
            });
        }

        public void bindView(HashMap<String, ArrayList<WeatherResponse>> weatherResponse) {
            Iterator it = weatherResponse.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                dayTextView.setText(pair.getKey().toString());
                String url = WeatherApp.getInstance().getBaseContext().getString(R.string.ICON_WEATHER_URL) + weatherResponse.get(pair.getKey().toString()).get(0).getWeather().get(0).getIcon() + POSTFIX_BY_ICON_URL;
                Picasso.with(context).load(url).into(iconImageView);
                String diapason = getMinAndMaxTemp(weatherResponse.get(pair.getKey().toString()));
                tempTextView.setText(diapason);
            }


        }

        private String getMinAndMaxTemp(ArrayList<WeatherResponse> weatherResponses) {
            double maxT = weatherResponses.get(0).getMain().getTempMax();
            double minT = weatherResponses.get(0).getMain().getTempMin();
            ;
            for (WeatherResponse weatherResponse : weatherResponses) {
                if (weatherResponse.getMain().getTempMin() < minT)
                    minT = weatherResponse.getMain().getTempMin();
                if (weatherResponse.getMain().getTempMax() > maxT)
                    maxT = weatherResponse.getMain().getTempMax();
            }
            return MyUtil.createTemperatureDiapasonString(maxT, minT);
        }
    }
}