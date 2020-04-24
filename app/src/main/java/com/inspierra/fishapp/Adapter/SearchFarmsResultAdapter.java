package com.inspierra.fishapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inspierra.fishapp.HelpingClasses.FarmerInfoItem;
import com.inspierra.fishapp.R;
import com.inspierra.fishapp.Utilities.BusStation;

import java.util.ArrayList;

public class SearchFarmsResultAdapter extends RecyclerView.Adapter
{
    private ArrayList<FarmerInfoItem> farmItem;
    private Context mContext;


    public SearchFarmsResultAdapter(Context context, ArrayList<FarmerInfoItem> farmInfo)
    {
        BusStation.getBus().register(this);
        this.mContext = context;
        this.farmItem = farmInfo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        try
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.farm_item, parent, false);

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
        FarmerInfoItem pp = farmItem.get(position);
        TextView tvFarmName,tvNextMarketDate,txtPhone,txtEmail,txtLocation;
        RelativeLayout rel3, rel4;
        try
        {
            tvFarmName = holder.itemView.findViewById(R.id.tvFarmName);
            tvNextMarketDate = holder.itemView.findViewById(R.id.tvNextMarketDate);
            txtPhone = holder.itemView.findViewById(R.id.txtPhone);
            txtEmail = holder.itemView.findViewById(R.id.txtEmail);
            txtLocation = holder.itemView.findViewById(R.id.txtLocation);
            tvFarmName.setText(pp.farmName);
            if(pp.nextMarketDate==null){
                pp.nextMarketDate="2020-05-20";
            }
            tvNextMarketDate.setText(pp.nextMarketDate);
            txtPhone.setText(pp.phoneNumber);
            txtEmail.setText(pp.email);
            txtLocation.setText(pp.location);

//            rel3 = holder.itemView.findViewById(R.id.rel3);
//            rel4 = holder.itemView.findViewById(R.id.rel4);

//            rel3.setOnClickListener(v -> {
//                pp.identity = 1;
//                BusStation.getBus().post(pp);
//            });
//
//            rel4.setOnClickListener(vv ->
//            {
//                farmItem.get(position).identity = 2;
//                BusStation.getBus().post(farmItem.get(position));
//            });
//            tvFarmName,tvNextMarketDate,txtPhone,txtEmail,txtLocation
//            txtName.setText(pp.pondName);
        }
        catch (Exception ex)
        {
            String error = ex.getMessage();
        }
    }

    @Override
    public int getItemCount()
    {
        return farmItem.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder
    {

//        TextView txtName;
//        RelativeLayout rel3, rel4;
        TextView tvFarmName,tvNextMarketDate,txtPhone,txtEmail,txtLocation;
        ViewHolder(View itemView)
        {
            super(itemView);
            try
            {

                tvFarmName = itemView.findViewById(R.id.tvFarmName);
                tvNextMarketDate = itemView.findViewById(R.id.tvNextMarketDate);
                txtPhone = itemView.findViewById(R.id.txtPhone);
                txtEmail = itemView.findViewById(R.id.txtEmail);
                txtLocation = itemView.findViewById(R.id.txtLocation);

//                txtName = itemView.findViewById(R.id.tvFarmName);
//                rel3 = itemView.findViewById(R.id.rel3);
//                rel4 = itemView.findViewById(R.id.rel4);
            }
            catch (Exception ex)
            {
                String error = ex.getMessage();
            }

        }
    }


}
