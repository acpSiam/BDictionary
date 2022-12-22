package bmarpc.acpsiam.bdictionary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
public class DownloadTask extends AsyncTask<String, Integer, String> {

    private final Context context;
    private PowerManager.WakeLock mWakeLock;


    public DownloadTask(Context context) {
        this.context = context;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
//        MainActivity.mProgressDialog.show();
        MainActivity.updateDiaalogDownloadLottie.playAnimation();
        MainActivity.updateDiaalogDownloadLottie.setProgress(0.0f);
        MainActivity.updateDiaalogDownloadLottie.setMaxFrame(8);

        MainActivity.dialogUpdateDownloadButtonLabel.setText("Downloading...");
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        MainActivity.progressIndicator.setProgress(progress[0]);
        MainActivity.updateDiaalogDownloadLottie.setProgress(progress[0]/100f);
        MainActivity.updateDiaalogDownloadLottie.setMinAndMaxFrame(9, (int) ((progress[0]+9)*1.02f));


    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
//        MainActivity.mProgressDialog.dismiss();
        if (result != null){
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
            Log.d("DownloadError", "Download error: "+result);

            MainActivity.dialogUpdateDownloadButtonLabel.setText("Network Error");
            MainActivity.updateDiaalogDownloadLottie.setVisibility(View.GONE);
        }

        else{
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
            MainActivity.dialogUpdateDownloadButtonLabel.setText("Downloaded");
            MainActivity.updateDiaalogDownloadLottie.setVisibility(View.GONE);


//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/BDictionary GITHUB.apk");
//            Intent promptInstall = new Intent(Intent.ACTION_VIEW)
//                    .setDataAndType(Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/BDictionary GITHUB.apk"),
//                            "application/vnd.android.package-archive");
//            context.startActivity(promptInstall);
        }

    }



    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/BDictionary GITHUB.apk");

            byte[] data = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
}
