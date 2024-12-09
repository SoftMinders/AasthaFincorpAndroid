package com.af.aasthafincorp.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.af.aasthafincorp.R;
import com.bumptech.glide.Glide;

public class UtilityClass {

    Context context;
    private ProgressDialog pdialog;

    public UtilityClass(Context context) {
        this.context = context;
    }

    public void processDialogStart() {
        try {
            if (pdialog != null) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        pdialog = new ProgressDialog(context, R.style.Theme_MyDialog);
        //  pdialog = new ProgressDialog(context,R.style.DialogSlideAnim);
        pdialog.setMessage("Loading...");
        pdialog.setIndeterminate(false);
        pdialog.setCancelable(false);
        if(!((Activity) context).isFinishing()) {

            pdialog.show();
        }
//    pdialog.show();
        pdialog.setContentView(R.layout.view_progress);
        //pdialog.getWindow().setLayout(300,300);
        pdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        // pdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //pdialog.getWindow().setStatusBarColor(context.getResources().getColor(R.color.white));
        // pdialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        pdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#99ffffff")));
        ImageView ivLoad = pdialog.findViewById(R.id.ivFLoading);
        Glide.with(context).asGif().load(R.drawable.loading).into(ivLoad);

    }

    public void processDialogStop() {
        try {
            if (pdialog != null) {
                if (pdialog.isShowing()) {
                    pdialog.dismiss();
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
