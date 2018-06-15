package com.sandraprog.popularmovies2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sandraprog.popularmovies2.R;
import com.sandraprog.popularmovies2.model.Trailer;

import java.util.List;

/**
 * Created by sandrapog on 11.06.2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {
    private List<Trailer> mTrailerList;
    private Context mContext;
    private final String YOUTUBE_PATH = "https://www.youtube.com/watch?v=";

    public TrailersAdapter(Context context, List<Trailer> mTrailerList) {
        this.mTrailerList = mTrailerList;
        mContext = context;
    }

    @Override
    public TrailersAdapter.TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row, parent, false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapter.TrailersViewHolder viewHolder, int position) {
        viewHolder.mTrailerName.setText(mTrailerList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder {
        private TextView mTrailerName;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            mTrailerName = (TextView) itemView.findViewById(R.id.trailer_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Trailer itemTrailer = mTrailerList.get(position);
                        String videoKey = itemTrailer.getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_PATH + videoKey));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("VIDEO_ID", videoKey);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
