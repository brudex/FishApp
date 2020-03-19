package com.inspierra.fishapp.Utilities;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusStation
{
    private static Bus _bus = new Bus(ThreadEnforcer.ANY);

    //create getter for the buss to allow others to access it

    public static Bus getBus()
    {
        return _bus;
    }
}