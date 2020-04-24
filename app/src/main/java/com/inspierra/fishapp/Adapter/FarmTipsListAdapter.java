package com.inspierra.fishapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inspierra.fishapp.HelpingClasses.FarmTipsClass;
import com.inspierra.fishapp.HelpingClasses.FarmerInfoItem;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.BusStation;

import java.util.ArrayList;

public class FarmTipsListAdapter extends RecyclerView.Adapter
{
    private ArrayList<FarmTipsClass> farmTip;
    private Context mContext;


    public FarmTipsListAdapter(Context context, ArrayList<FarmTipsClass> farmTip)
    {
        BusStation.getBus().register(this);
        this.mContext = context;
        this.farmTip = farmTip;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        try
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.farm_tip, parent, false);

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
        FarmTipsClass pp = farmTip.get(position);
        com.adammcneilly.ActionCardView actionCardView;
        com.adammcneilly.ActionButton actionButton;
         try
        {
            actionCardView = holder.itemView.findViewById(R.id.action_card);
            actionButton = holder.itemView.findViewById(R.id.action_button);

             actionCardView.setTitle(pp.title);
             actionCardView.setDescription(pp.description);
             actionCardView.setDividerColor(R.color.blueGray200);
             actionButton.setText("Read More..");

            actionButton.setOnClickListener(v -> {
                Uri uri = Uri.parse(pp.mediaLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
                //BusStation.getBus().post(pp);
            });


        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }
    }

    @Override
    public int getItemCount()
    {
        return farmTip.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder
    {


        com.adammcneilly.ActionCardView actionCardView;
        com.adammcneilly.ActionButton actionButton;
        ViewHolder(View itemView)
        {
            super(itemView);
            try
            {
                actionCardView = itemView.findViewById(R.id.action_card);
                actionButton = itemView.findViewById(R.id.action_button);
            }
            catch (Exception ex)
            {
                String error = ex.getMessage();
            }

        }
    }


}
