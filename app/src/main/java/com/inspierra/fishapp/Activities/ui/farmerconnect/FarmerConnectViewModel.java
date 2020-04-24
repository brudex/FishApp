package com.inspierra.fishapp.Activities.ui.farmerconnect;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FarmerConnectViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FarmerConnectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}