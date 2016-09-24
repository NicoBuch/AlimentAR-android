package com.android.proyectoalimentar.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.proyectoalimentar.R;
import com.android.proyectoalimentar.utils.CountdownTimer;

public class TimerView extends TextView implements CountdownTimer.TimerTickListener {

    private CountdownTimer countDownTimer;

    public TimerView(Context context) {
        super(context);
        init();
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.timer_font));
        setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    public void setTimeLeft(long timeLeft) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountdownTimer(this, timeLeft);
    }

    @Override
    public void onTick(long timeLeft) {
        timeLeft /= 1000;
        long seconds = timeLeft % 60;
        long minutes = timeLeft / 60;
        setText(withTwoDecimals(minutes) + ":" + withTwoDecimals(seconds));
    }

    private String withTwoDecimals(long number) {
        return String.format("%02d", number);
    }

    @Override
    public void onTimerFinished() {
        onTick(0);
    }

}
