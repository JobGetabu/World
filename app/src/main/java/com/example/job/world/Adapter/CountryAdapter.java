package com.example.job.world.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.job.world.R;
import com.example.job.world.model.Country;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by JOB on 8/21/2017.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryAdapterViewHolder> {

    private List<Country> countryList;
    private Context mContext;

    public CountryAdapter( Context mContext, List<Country> countryList) {
        this.countryList = countryList;
        this.mContext = mContext;
    }

    @Override
    public CountryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.country_item,parent,false);
        CountryAdapterViewHolder countryAdapterViewHolder = new CountryAdapterViewHolder(view);


        return countryAdapterViewHolder;

    }

    @Override
    public void onBindViewHolder(final CountryAdapterViewHolder holder, int position) {

        Country countryObj = countryList.get(position);

        holder.tvRank.setText(String.valueOf(countryObj.getRank()));
        holder.tvCountry_name.setText(countryObj.getName());
        holder.tvPopulation.setText(countryObj.getPopulation());

        ImageLoader.getInstance().displayImage(countryObj.getFlag(), holder.countryFlag, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class CountryAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView countryFlag;
        TextView tvRank ;
        TextView tvCountry_name ;
        TextView tvPopulation ;
        ProgressBar progressBar;


        public CountryAdapterViewHolder(View itemView) {
            super(itemView);

            countryFlag = (ImageView) itemView.findViewById(R.id.country_image);
            tvRank = (TextView) itemView.findViewById(R.id.rank);
            tvCountry_name = (TextView) itemView.findViewById(R.id.country_name);
            tvPopulation = (TextView) itemView.findViewById(R.id.population);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
