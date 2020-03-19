package com.inspierra.fishapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inspierra.fishapp.HelpingClasses.UserPondsClass;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.BusStation;

import java.util.ArrayList;

public class PondsAdapter extends RecyclerView.Adapter
{
    private ArrayList<UserPondsClass> ponds;
    private Context mContext;


    public PondsAdapter(Context context, ArrayList<UserPondsClass> ponds)
    {
        BusStation.getBus().register(this);
        this.mContext = context;
        this.ponds = ponds;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        try
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pond_view, parent, false);

        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        UserPondsClass pp = ponds.get(position);
        TextView txtName;
        RelativeLayout rel3, rel4;
        try
        {
            txtName = holder.itemView.findViewById(R.id.txtName);
            rel3 = holder.itemView.findViewById(R.id.rel3);
            rel4 = holder.itemView.findViewById(R.id.rel4);

            rel3.setOnClickListener(v -> {
                pp.identity = 1;
                BusStation.getBus().post(pp);
            });

            rel4.setOnClickListener(vv ->
            {
                ponds.get(position).identity = 2;
                BusStation.getBus().post(ponds.get(position));
            });

            txtName.setText(pp.pondName);
        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }
    }

    @Override
    public int getItemCount()
    {
        return ponds.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView txtName;
        RelativeLayout rel3, rel4;

        ViewHolder(View itemView)
        {
            super(itemView);
            try
            {
                txtName = itemView.findViewById(R.id.txtName);
                rel3 = itemView.findViewById(R.id.rel3);
                rel4 = itemView.findViewById(R.id.rel4);
            }
            catch (Exception ex)
            {
                String error = ex.getMessage();
            }

        }
    }


}
