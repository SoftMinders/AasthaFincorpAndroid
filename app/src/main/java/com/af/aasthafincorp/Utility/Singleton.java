package com.af.aasthafincorp.Utility;

import android.app.Activity;
import android.widget.ImageView;

public class Singleton {
    private static Singleton mInstance = null;

    public static Singleton GetInstance() {
        if (mInstance == null)
            mInstance = new Singleton();
        return mInstance;
    }


    int fragmentPosition = -1, nearPosition = -1;
    ImageView ivUserImage;
    Activity activity;


    public int getFragmentPosition() {
        return fragmentPosition;
    }

    public void setFragmentPosition(int fragmentPosition) {
        this.fragmentPosition = fragmentPosition;
    }

    public ImageView getIvUserImage() {
        return ivUserImage;
    }

    public void setIvUserImage(ImageView ivUserImage) {
        this.ivUserImage = ivUserImage;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
