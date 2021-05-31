package com.example.parkinglotapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinglotapp.R;
import com.example.parkinglotapp.model.TimeIO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<TimeIO> list;
    private Context context;
    private boolean isAdmin=false;
    private int lastPosition = -1;
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public RecyclerViewAdapter(List<TimeIO> list, Context context){
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_on_line,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tvTime.setText(formatDate(list.get(position).getTime()));
        Drawable img=context.getResources().getDrawable(R.drawable.ic_out);
        if(list.get(position).isCheckin()){
            img=context.getResources().getDrawable(R.drawable.ic_in);
            Log.d("AAA1","checkin : "+list.get(position).isCheckin()+"   pos : "+position);
        }
        img.setBounds(0,0,60,60);
        holder.tvIcon.setCompoundDrawables(img,null,null,null);
        holder.tvName.setText(list.get(position).getName());
        if(isAdmin){
            holder.linearLayout.setVisibility(LinearLayout.VISIBLE);
        }
        else{
            holder.linearLayout.setVisibility(LinearLayout.GONE);
        }
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime,tvName;
        private TextView tvIcon;
        private LinearLayout linearLayout;
        private View v;
        private BlurView blurView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v=itemView;
            linearLayout=v.findViewById(R.id.layout_expand);
            tvTime=v.findViewById(R.id.tv_time);
            tvName=v.findViewById(R.id.tv_name);
            tvIcon=v.findViewById(R.id.tv_icon);
            blurView=v.findViewById(R.id.blurview);
        }
    }
    public String formatDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
