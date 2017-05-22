package com.nikola.appprotect.services.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.nikola.appprotect.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


public class FakeLockRelativeLayout extends RelativeLayout {

    private boolean isLongPress = false;
    private static final int LONG_PRESS_DURATION = 3000;

    public interface OnClickListener {
        void onIconClick();

        void onButtonClick();
    }

    private OnClickListener onClickListener;

    public FakeLockRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public FakeLockRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FakeLockRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setGravity(Gravity.CENTER);
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fake_lock_layout, this, true);
        ButterKnife.bind(this);
    }

    public void OnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @OnTouch(R.id.tap_area)
    public boolean onTouchIcon(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isLongPress = true;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isLongPress) {
                        onClickListener.onIconClick();
                    }
                }
            }, LONG_PRESS_DURATION);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isLongPress = false;
        }
        return true;
    }

    @OnClick(R.id.btn_ok)
    public void onButtonClick() {
        if (onClickListener != null) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(startMain);
            onClickListener.onButtonClick();
        }
    }
}
