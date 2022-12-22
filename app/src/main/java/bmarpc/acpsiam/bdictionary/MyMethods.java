package bmarpc.acpsiam.bdictionary;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import java.net.InetAddress;

public class MyMethods {

    public void shareApp(Context context, String appDownloadLink, String message)
    {
        final String appPackageName = context.getString(R.string.app_name);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: "+ appDownloadLink +"   ["+ appPackageName + "]\n\n" + message);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }

}
