package com.inspierra.fishapp.HelpingClasses;

public class ProfileClass
{
    public String userId ;
    public String region ;
    public String firstName ;
    public String middleName ;
    public String lastName ;
    public String email ;
    public String phoneNumber ;
    public String farmName ;
    public int role ;
    public String location ;
    public String aboutFarm ;
    public String nextMarketDate ;
    public int noOfPonds ;
    public int noOfHatcheries ;
    public int noOfCages ;
    public String profilePicture;

    public String getFullName() {
        String fullName ="";
        fullName+=firstName;
        if(middleName!=null){
            fullName+= " "+middleName;
        }
        if(lastName!=null){
            fullName+=" "+lastName;
        }
        return fullName;
    }


}
