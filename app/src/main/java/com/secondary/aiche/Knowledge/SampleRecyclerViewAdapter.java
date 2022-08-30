package com.secondary.aiche.Knowledge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secondary.aiche.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


// Adapter for your courses


public class SampleRecyclerViewAdapter extends RecyclerView.Adapter<SampleViewHolders>
{
    private List<ItemObject> itemList;
    private Context context;

    public SampleRecyclerViewAdapter(Context context,
                                     List<ItemObject> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SampleViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.course_list_item, null);
        SampleViewHolders rcv = new SampleViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SampleViewHolders holder, int position)
    {
        holder.courseImage.setImageResource(itemList.get(position).getName());
        holder.courseTitle.setText(itemList.get(position).getImage());
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }
}