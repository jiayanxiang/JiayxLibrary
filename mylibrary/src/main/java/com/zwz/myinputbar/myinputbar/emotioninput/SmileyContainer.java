
package com.zwz.myinputbar.myinputbar.emotioninput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zwz.myinputbar.myinputbar.utils.KeyboardUtil;


public class SmileyContainer extends FrameLayout {

    boolean isVisible = false;
    boolean isKeyboardShowing;
    private SmileyView smileyView;
    private EditText editText;
    private int savedHeight = 0;

    private Paint paint = new Paint();

    public SmileyContainer(Context context) {
        super(context);
        init();
    }

    private void init() {
        savedHeight = KeyBoardHeightPreference.get(getContext(), 200);
        setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, savedHeight));
        paint.setColor(Color.parseColor("#d5d3d5"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.0f);

        smileyView = new SmileyView(getContext());
        smileyView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        addView(smileyView);
    }

    public void init(EditText editText, View smileyBtn, final View sendBtn) {
        sendBtn.setEnabled(false);
        EmotionInputHandler handler = new EmotionInputHandler(editText, new EmotionInputHandler.TextChangeListener() {
            @Override
            public void onTextChange(boolean enable, String s) {
                sendBtn.setEnabled(enable);
                if(s.length()>50){
                    Toast.makeText(getContext(), "对不起,最多只能输入50个字符", Toast.LENGTH_SHORT).show();
                }
            }
        });

        smileyView.setInputView(handler);


        this.editText = editText;
        this.editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideContainer(true);
                return false;
            }
        });
//        setVisibility(GONE);
        setSmileyView(smileyBtn);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isVisible) {
            setVisibility(GONE);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void setSmileyView(View smileyBtn) {
        if (this.getVisibility() == View.VISIBLE){
            if (smileyView.getVisibility() == View.VISIBLE){
                hideContainer(true);
            }
        }
        smileyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getVisibility() != VISIBLE) {
                    smileyView.setVisibility(VISIBLE);
                    showContainer();
                } else {
                    if (smileyView.getVisibility() == VISIBLE) {
                        hideContainer(true);
                        KeyboardUtil.showKeyboard(editText);
                    } else {
                        smileyView.setVisibility(VISIBLE);
                    }
                }
            }
        });
    }



    public void showContainer() {
        if (isVisible) return;
        isVisible = true;
        if (isKeyboardShowing) KeyboardUtil.hideKeyboard(editText);
        if (getVisibility() == GONE) {
            setVisibility(VISIBLE);
        }
    }


    //参数代表是否由键盘弹起
    public void hideContainer(boolean isCauseByKeyboard) {
        isVisible = false;
        if (!isCauseByKeyboard) {
            setVisibility(GONE);
        }
    }


    //offset > 0 可能时键盘弹起
    void onMainViewSizeChange(int offset) {
        if (offset > 0) {//键盘弹起
            isVisible = false;
            this.isKeyboardShowing = true;
            if (offset != savedHeight) {
                KeyBoardHeightPreference.save(getContext(), offset);
                savedHeight = offset;
                setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, offset));
            }
            hideContainer(true);
        } else if (offset < 0) {
            Log.e("______", "keyboard hide :" + offset);
            this.isKeyboardShowing = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 0.5f, getMeasuredWidth(), 0.5f, paint);
    }
}
