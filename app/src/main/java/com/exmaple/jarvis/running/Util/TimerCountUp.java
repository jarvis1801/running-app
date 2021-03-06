package com.exmaple.jarvis.running.Util;

import android.os.CountDownTimer;
import android.util.Log;

public abstract class TimerCountUp extends CountDownTimer {
    private static final long INTERVAL_MS = 10;
    private long duration;

    protected TimerCountUp(long durationMs) {
        super(durationMs, INTERVAL_MS);
        this.duration = durationMs;
    }

    public abstract void onTick(int second);

    @Override
    public void onTick(long msUntilFinished) {
        int second = (int) ((duration - msUntilFinished) / 10);
        onTick(second);
    }

    public void resume(long duration) {
        start();
    }

    public long getDuration() {
        return this.duration;
    }

    @Override
    public void onFinish() {
    }
}