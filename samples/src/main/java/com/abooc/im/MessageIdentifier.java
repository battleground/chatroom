package com.abooc.im;

import android.os.CountDownTimer;

import com.abooc.im.message.GiftMessage;
import com.abooc.util.Debug;

/**
 * Created by dayu on 2017/5/31.
 */

public class MessageIdentifier {

    private String identifier;
    private int index = 0;
    private int timerTotal = 5;

    private boolean isRunning = false;

    public GiftMessage eat(GiftMessage message) {
        identifier = identifier == null ? createIdentifier() : identifier;
        message.setGiftId(identifier);
        if (message.getMoney() < 1000) {
            message.setGiftIndex(++index);
        }
        return message;
    }

    String createIdentifier() {
        return "GIFT-ID:" + String.valueOf(System.currentTimeMillis());
    }

    private OnTimer iOnTimer;

    public void setTimerListener(OnTimer timerListener) {
        iOnTimer = timerListener;
    }

    /**
     * 计时开始
     */
    public void start() {
        if (isRunning()) {
            cancel();
        }
        timer.start();
        isRunning = true;

        if (iOnTimer != null) {
            iOnTimer.onStart();
        }
    }

    /**
     * 计时状态
     *
     * @return true 计时中，false 没有计时任务
     */
    public boolean isRunning() {
        return isRunning;
    }

    public void clear() {
        identifier = null;
        index = 0;
    }

    public void cancel() {
        timer.cancel();

        if (iOnTimer != null) {
            iOnTimer.onCancelled();
        }
    }

    CountDownTimer timer = new CountDownTimer(timerTotal * 1000, 990) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (iOnTimer != null) {
                iOnTimer.onTick(millisUntilFinished);
            }
            Debug.anchor(millisUntilFinished + ", " + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            Debug.anchor();
            isRunning = false;
            clear();

            if (iOnTimer != null) {
                iOnTimer.onFinish();
            }
        }

    };

    public interface OnTimer {
        void onStart();

        void onTick(long millisUntilFinished);

        void onFinish();

        void onCancelled();
    }

    public static class SimpleOnTimer implements OnTimer {
        @Override
        public void onStart() {

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {

        }

        @Override
        public void onCancelled() {

        }
    }

}
