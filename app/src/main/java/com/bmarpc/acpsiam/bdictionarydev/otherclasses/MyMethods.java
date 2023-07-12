package com.bmarpc.acpsiam.bdictionarydev.otherclasses;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MyMethods {




    public static void simpleMaterialDialogue(Context context, int drawable, int title, int message){
        new MaterialAlertDialogBuilder(context)
                .setIcon(drawable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.okay, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    public static void animateButtonVisibility(View view, int visibility) {
        if (view.getVisibility() != visibility) {
            if (visibility == View.VISIBLE) {
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(300);
                view.startAnimation(fadeIn);
            } else {
                AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                fadeOut.setDuration(300);
                view.startAnimation(fadeOut);
            }
            view.setVisibility(visibility);
        }
    }






    public static void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("BDictionary", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
    }



    public static void makeStatusBarTransparent(Activity activity) {
        Window window = activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        View decorView = window.getDecorView();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(flags);

        // Add the following flags to prevent the navigation buttons from overlapping the app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        } else {
            flags |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        decorView.setSystemUiVisibility(flags);

        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }





    public static void restartActivity(Activity activity) {
        Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anime_fade_in, R.anim.anime_fade_out);
        activity.finish();
    }

    public static void restartActivityLongTransition(Activity activity) {
        Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anime_fade_in_long, R.anim.anime_fade_out_long);
        activity.finish();
    }


}
