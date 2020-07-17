package com.example.ghoudanlibrary.Commun;

import android.net.Uri;

import com.example.ghoudanlibrary.API.FireBaseClient;

public abstract class Commun {

    //Data Retrieved From Email Account
    public static final String Current_Client_Id = FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getId();
    public static final String Current_Client_IdToken = FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getIdToken();
    public static final String Current_Client_DisplayName = FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getDisplayName();
    public static final String Current_Client_FamilyName = FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getFamilyName();
    public static final String Current_Client_GivenName = FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getGivenName();
    public static final String Current_Client_Email = FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getEmail();
    public static final Uri Current_Client_PhotoUrl = FireBaseClient.getFireBaseClient().getUserLogEdInAccount().getPhotoUrl();

    //DataBase Tables Names
    public static final String Favor_DataBase_Table = "Favor";
    public static final String Chauffeur_DataBase_Table = "Chauffeur";
    public static final String Demand_DataBase_Table = "Demand";
    public static final String Pickup_DataBase_Table = "PickUp";
    public static final String Client_DataBase_Table = "Client";
    public static final String Users_DataBase_Table = "Users";

    //Usual String Used
    public static final String Email_String = "Email";
}
