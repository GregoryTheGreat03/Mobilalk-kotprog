package com.application.yogaandmeditation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MySessionAdapter extends RecyclerView.Adapter<MySessionAdapter.ViewHolder> implements Filterable {
    private ArrayList<OnlineSession> mOnlineSessionData;
    private ArrayList<OnlineSession> mOnlineSessionDataAll;
    private Context mContext;
    private int lastPosition = -1;

    MySessionAdapter(Context context, ArrayList<OnlineSession> sessionData){
        this.mOnlineSessionData = sessionData;
        this.mOnlineSessionDataAll = sessionData;
        this.mContext = context;
    }


    @NonNull
    @Override
    public MySessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MySessionAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_my_session, parent, false));
    }

    @Override
    public void onBindViewHolder(MySessionAdapter.ViewHolder holder, int position) {
        OnlineSession currentSession = mOnlineSessionData.get(position);

        holder.bindTo(currentSession);

        if (holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mOnlineSessionData.size();
    }

    @Override
    public Filter getFilter() {
        return sessionFilter;
    }

    private Filter sessionFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<OnlineSession> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0){
                results.count = mOnlineSessionDataAll.size();
                results.values = mOnlineSessionDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (OnlineSession session : mOnlineSessionDataAll){
                    if (session.getTitle().toLowerCase().contains(filterPattern) ||
                            session.getCreatorName().toLowerCase().contains(filterPattern) ||
                            session.getDescription().toLowerCase().contains(filterPattern)){
                        filteredList.add(session);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mOnlineSessionData = (ArrayList<OnlineSession>) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userNameTV;
        private TextView userTitleTV;
        private TextView sessionTitleTV;
        private TextView sessionGenderTV;
        private TextView sessionDescriptionTV;
        private TextView sessionPriceTV;
        private ImageView sessionImageIV;

        private Button deleteButton;
        private Button updateButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userNameTV = itemView.findViewById(R.id.nameOfPoster);
            userTitleTV = itemView.findViewById(R.id.titleOfPoster);
            sessionTitleTV = itemView.findViewById(R.id.sessionTitle);
            sessionGenderTV = itemView.findViewById(R.id.sessionGender);
            sessionDescriptionTV = itemView.findViewById(R.id.sessionDescription);
            sessionPriceTV = itemView.findViewById(R.id.sessionPrice);
            sessionImageIV = itemView.findViewById(R.id.sessionImage);

            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bindTo(OnlineSession currentSession) {
            userNameTV.setText(currentSession.getCreatorName());
            userTitleTV.setText(currentSession.getCreatorId());
            sessionTitleTV.setText(currentSession.getTitle());
            sessionGenderTV.setText(currentSession.getForGender());
            sessionDescriptionTV.setText(currentSession.getDescription());
            sessionPriceTV.setText(currentSession.getPrice() + " Ft");

            //Glide.with(mContext).load(currentSession.getImageResource()).into(sessionImageIV);

            updateButton.setOnClickListener(view -> ((ListMySessionsActivity)mContext).updateSession(currentSession));
            deleteButton.setOnClickListener(view -> ((ListMySessionsActivity)mContext).deleteSession(currentSession));
        }
    }
}


