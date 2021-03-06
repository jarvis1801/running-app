package com.exmaple.jarvis.running;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.exmaple.jarvis.running.AsyncTask.HistoryLoader;
import com.exmaple.jarvis.running.Util.TimerCountUp;


public class MainActivity extends FragmentActivity {
    private static String mainTag = "MAIN";

    private TextView tv_timer;
    private Button btn_startTimer, btn_endTimer, btn_pauseTimer ,btn_record;

    private Boolean isStarted = false;
    private Boolean isPause = false;
    private Boolean isPosted = false;
    private long tempDuration = 0;

    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_timer = findViewById(R.id.tv_timer);

        btn_startTimer = findViewById(R.id.btn_startTimer);
        btn_endTimer = findViewById(R.id.btn_endTimer);
        btn_pauseTimer = findViewById(R.id.btn_pauseTimer);
        btn_record = findViewById(R.id.btn_record);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, mainTag);
        wl.acquire();

        wl.release();

        loaderManager = getSupportLoaderManager();

        final TimerCountUp timer = new TimerCountUp(3599 * 1000) {
            @Override
            public void onTick(int durationSeconds) {
                String formattedTimer = String.format("%02d:%02d:%02d",
                        (durationSeconds % (3600 * 100)) / (60 * 100), (durationSeconds % (60 * 100)) / (100),
                        (durationSeconds % 100));
                tv_timer.setText(String.valueOf(formattedTimer));
            }
        };

        btn_startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStarted) {
                    timer.start();
                } else {
                    timer.resume(tempDuration);
                }
            }
        });

        btn_pauseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                isPause = true;
                isStarted = true;
                tempDuration = timer.getDuration();
            }
        });

        btn_endTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                loaderManager.initLoader(4, null, postHistory);

                isStarted = false;
            }
        });

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
    }

    private LoaderManager.LoaderCallbacks<Object> postHistory
            = new LoaderManager.LoaderCallbacks<Object>() {
        @Override
        public Loader<Object> onCreateLoader(int id, Bundle args) {
            String url = Config.url + "history";

            return new HistoryLoader(MainActivity.this, url, tv_timer.getText().toString());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Object> loader, Object o) {
            loaderManager.destroyLoader(4);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Object> loader) {
        }
    };
}
