package com.haksoy.pertem.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.haksoy.pertem.R;
import com.haksoy.pertem.firebase.FirebaseClient;
import com.haksoy.pertem.service.DataUpdateHelper;
import com.haksoy.pertem.tools.ConnectivityHelper;
import com.haksoy.pertem.tools.Constant;
import com.haksoy.pertem.tools.Enums;
import com.haksoy.pertem.tools.INotifyAction;
import com.haksoy.pertem.tools.MyDoubleArcProgress;

public class SplashActivity extends AppCompatActivity {

    MyDoubleArcProgress progress;
    private String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progress = findViewById(R.id.progress3);
        progress.animate();
        if (!ConnectivityHelper.isConnectedToNetwork(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_warn), Toast.LENGTH_LONG).show();
            startMainActivity(1000);
        } else {
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            FirebaseMessaging.getInstance().subscribeToTopic(Constant.NEWS);
            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    FirebaseClient.getInstance().getProcurementList(null);
                    FirebaseClient.getInstance().getAnnounceList(null);
                    DataUpdateHelper updateService = new DataUpdateHelper(new INotifyAction() {
                        @Override
                        public void onNotified(Object key, Object value) {
                            if (key == Enums.UpdateCompleted) {
                                startMainActivity(0);
                            }
                        }
                    });
                    updateService.controlAndUpdate(getApplicationContext());

                }
            });
        }

    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_version_title))
                .setMessage(getString(R.string.new_version_message))
                .setPositiveButton(getString(R.string.new_version_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openStorePage();
                    }
                }).setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openStorePage() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void startMainActivity(long duration) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.clearAnimation();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, duration);
    }


}
