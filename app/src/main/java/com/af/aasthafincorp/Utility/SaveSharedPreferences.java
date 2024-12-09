package com.af.aasthafincorp.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {

    static final String UserObject = "UserObject";
    static final String UniqueID = "uniqueID";
    static final String FIREBASE_TOKEN = "firebaseToken";
    static final String CurrentVersionCode = "CurrentVersionCode";
    static final String UserAccessToken = "UserAccessToken";
    static final String User_id = "User_id";
    static final String loginId= "loginId";
    static final String loginPwd= "loginPwd";
    static final String E_id = "E_id";
    static final String Uname = "Uname";
    static final String Branch = "Branch";
    static final String Email = "Email";
    static final String Mobile = "Mobile";
    static final String Depart = "Depart";
    static final String Verif = "Verif";
    static final String Redi_Url = "Redi_Url";
    static final String click_action = "click_action";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUniqueID(Context ctx, String uniqueID) {
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(UniqueID, uniqueID);
        WelComeEditor.commit();
    }

    public static String getUniqueID(Context ctx) {
        return getSharedPreferences(ctx).getString(UniqueID, "");
    }
    public static void setClickAction(Context ctx, String click_actions) {
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(click_action, click_actions);
        WelComeEditor.commit();
    }

    public static String getClickAction(Context ctx) {
        return getSharedPreferences(ctx).getString(click_action, "");
    }

    public static void setUserObject(Context ctx, String s) {
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(UserObject, s);
        WelComeEditor.commit();
    }

    public static String getUserObject(Context ctx) {
        return getSharedPreferences(ctx).getString(UserObject, "");
    }

    public static void setUser_id(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(User_id, l);
        WelComeEditor.commit();
    }


    public static String getUser_id(Context ctx){
        return getSharedPreferences(ctx).getString(User_id,"");
    }
    public static void set_loginId(Context ctx,String l)

    {
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(loginId, l);
        WelComeEditor.commit();
    }


    public static String getloginId(Context ctx){
        return getSharedPreferences(ctx).getString(loginId,"");
    }
    public static void set_loginPwd(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(loginPwd, l);
        WelComeEditor.commit();
    }


    public static String getloginPwd(Context ctx){
        return getSharedPreferences(ctx).getString(loginPwd,"");
    }

    public static String getE_id(Context ctx){
        return getSharedPreferences(ctx).getString(E_id,"");
    }

    public static void setE_id(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(E_id, l);
        WelComeEditor.commit();
    }

    public static String getUname(Context ctx){
        return getSharedPreferences(ctx).getString(Uname,"");
    }

    public static void setUname(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(Uname, l);
        WelComeEditor.commit();
    }

    public static String getBranch(Context ctx){
        return getSharedPreferences(ctx).getString(Branch,"");
    }

    public static void setBranch(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(Branch, l);
        WelComeEditor.commit();
    }

    public static String getEmail(Context ctx){
        return getSharedPreferences(ctx).getString(Email,"");
    }

    public static void setEmail(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(Email, l);
        WelComeEditor.commit();
    }

    public static String getMobile(Context ctx){
        return getSharedPreferences(ctx).getString(Mobile,"");
    }

    public static void setMobile(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(Mobile, l);
        WelComeEditor.commit();
    }

    public static String getDepart(Context ctx){
        return getSharedPreferences(ctx).getString(Depart,"");
    }

    public static void setDepart(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(Depart, l);
        WelComeEditor.commit();
    }

    public static String getVerif(Context ctx){
        return getSharedPreferences(ctx).getString(Verif,"");
    }

    public static void setVerif(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(Verif, l);
        WelComeEditor.commit();
    }

    public static String getRedi_Url(Context ctx){
        return getSharedPreferences(ctx).getString(Redi_Url,"");
    }

    public static void setRedi_Url(Context ctx,String l){
        SharedPreferences.Editor WelComeEditor = getSharedPreferences(ctx).edit();
        WelComeEditor.putString(Redi_Url, l);
        WelComeEditor.commit();
    }
}
