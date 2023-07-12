package com.bmarpc.acpsiam.bdictionarydev.backgroundtasks;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.util.Log;

import com.bmarpc.acpsiam.bdictionarydev.R;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;


public class ServiceBackgroundJob extends JobService {
    private static final String TAG = "ExampleJobService";
//    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    DatabaseReference databaseReference = firebaseDatabase.getReference();
//    DatabaseReference installedDevicesDatabaseReference = databaseReference.child("INSTALLED_DEVICES");
    private boolean jobCancelled = false;



    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFERENCES_APP_PROCESS), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String UNIQUE_ID = sharedPreferences.getString(getString(R.string.DEVICE_UID), "");
        String DEVICE_INFO = sharedPreferences.getString(getString(R.string.DEVICE_INFO), "");
        checkIfOldUser(jobParameters, UNIQUE_ID, DEVICE_INFO);
        editor.apply();
        return true;
    }

    private void checkIfOldUser(JobParameters jobParameters, String UNIQUE_ID, String DEVICE_INFO) {
        Log.d(TAG, getString(R.string.job_started));

//        installedDevicesDatabaseReference.addValueEventListener(new ValueEventListener() {
//            boolean oldUser = false;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    String device = snapshot1.child("uid").getValue().toString();
//                    if (device.equals(UNIQUE_ID)) {
//                        Log.d(TAG, "Match Found");
//                        oldUser = true;
//                        break;
//                    }
//                }
//                setupUser(UNIQUE_ID, DEVICE_INFO, oldUser);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });


        jobFinished(jobParameters, false);

    }


    public void setupUser(String UNIQUE_ID, String DEVICE_INFO, boolean oldUser){


        if(oldUser){
//            installedDevicesDatabaseReference.child(UNIQUE_ID).child("RATING").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.d(TAG, snapshot.child("rating_date").toString());
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                }
//            });

//            installedDevicesDatabaseReference.child(UNIQUE_ID).child("RATING")


//            editor.putString(getString(R.string.firebase_rating_date), )
        }
        else {
//            DatabaseReference deviceIdRef = installedDevicesDatabaseReference.child(UNIQUE_ID);
//            HashMap<String, String> hashMap = new HashMap<>();
//
//            hashMap.put("uid", UNIQUE_ID);
//            hashMap.put("device", DEVICE_INFO);
//            deviceIdRef.setValue(hashMap);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, getString(R.string.job_cancelled_before_completion));
        jobCancelled = true;
        return true;
    }
}
