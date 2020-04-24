package com.inspierra.fishapp.Activities.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    public MutableLiveData<String> get_title() {
        return _title;
    }

    private MutableLiveData<String> _title;
    LiveData<String> title;
    public HomeViewModel() {
        _title = new MutableLiveData<>();
     }





    public void updateActionBarTitle(String title ) {
        this._title.postValue(title) ;
    }
}