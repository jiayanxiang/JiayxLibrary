package com.jyx.mylibrary.widget.txt;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyx.mylibrary.R;


/**
 * @author jyx
 * @ctime 2017/12/15:17:41
 * @explain
 */

public class ExpandTextView extends LinearLayout {
    public int DEFAULT_MAX_LINES = 10;
    private TextView contentText;
    private TextView textPlus;

    private int showLines;

    private ExpandStatusListener expandStatusListener;
    private boolean isExpand;
    private Boolean isHaveExpand = true;

    public ExpandTextView(Context context) {
        super(context);
        initView();
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView();

    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_magic_text, this);
        contentText = (TextView) findViewById(R.id.contentText);
        if (showLines > 0) {
            if (isHaveExpand) {
                contentText.setMaxLines(showLines);
            } else {
                contentText.setMaxLines(Integer.MAX_VALUE);
            }
        }

        textPlus = (TextView) findViewById(R.id.textPlus);
        textPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String textStr = textPlus.getText().toString().trim();
                if ("全文".equals(textStr)) {
                    contentText.setMaxLines(Integer.MAX_VALUE);
                    textPlus.setText("收起");
                    setExpand(true);
                } else {
                    contentText.setMaxLines(showLines);
                    textPlus.setText("全文");
                    setExpand(false);
                }
                //通知外部状态已变更
                if (expandStatusListener != null) {
                    expandStatusListener.statusChange(isExpand());
                }
            }
        });


        contentText.setEnabled(true);
        contentText.setTextIsSelectable(false);
        contentText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandTextView, 0, 0);
        try {
            showLines = typedArray.getInt(R.styleable.ExpandTextView_showLines, DEFAULT_MAX_LINES);
        } finally {
            typedArray.recycle();
        }
    }

    public void setText(final CharSequence content) {
        contentText.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                // 避免重复监听
                contentText.getViewTreeObserver().removeOnPreDrawListener(this);

                int linCount = contentText.getLineCount();
                if (showLines == 0) {
                    textPlus.setVisibility(GONE);
                    return true;
                }
                if (linCount > showLines) {

                    if (isExpand) {
                        contentText.setMaxLines(Integer.MAX_VALUE);
                        textPlus.setText("收起");
                    } else {
                        contentText.setMaxLines(showLines);
                        textPlus.setText("全文");
                    }
                    textPlus.setVisibility(View.VISIBLE);
                } else {
                    textPlus.setVisibility(View.GONE);
                }

                return true;
            }


        });

        contentText.setText(content);

        contentText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickExpandListenner != null) {
                    onClickExpandListenner.OnClick(v);
                }
            }
        });

//        contentText.setMovementMethod(new CircleMovementMethod(getResources().getColor(R.color.list_item_selected_color)));
        /*contentText.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "这里是要复制的文字");
// 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                MyToast.getInstence().showConfusingToast(mClipData.toString());
                return false;
            }
        });*/
    }

    public void setText(final CharSequence content, final Boolean isHaveExpands) {
        contentText.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                // 避免重复监听
                contentText.getViewTreeObserver().removeOnPreDrawListener(this);

                int linCount = contentText.getLineCount();
                isHaveExpand = isHaveExpands;
                if (!isHaveExpand) {
                    contentText.setMaxLines(Integer.MAX_VALUE);
                    textPlus.setVisibility(GONE);
                    return true;
                }
                if (showLines == 0) {
                    textPlus.setVisibility(GONE);
                    return true;
                }
                if (linCount > showLines) {

                    if (isExpand) {
                        contentText.setMaxLines(Integer.MAX_VALUE);
                        textPlus.setText("收起");
                    } else {
                        contentText.setMaxLines(showLines);
                        textPlus.setText("全文");
                    }
                    textPlus.setVisibility(View.VISIBLE);
                } else {
                    textPlus.setVisibility(View.GONE);
                }

                return true;
            }


        });

        contentText.setText(content);

        contentText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickExpandListenner != null) {
                    onClickExpandListenner.OnClick(v);
                }
            }
        });

//        contentText.setMovementMethod(new CircleMovementMethod(getResources().getColor(R.color.list_item_selected_color)));
        /*contentText.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "这里是要复制的文字");
// 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                MyToast.getInstence().showConfusingToast(mClipData.toString());
                return false;
            }
        });*/
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public boolean isExpand() {
        return this.isExpand;
    }

    public void setExpandStatusListener(ExpandStatusListener listener) {
        this.expandStatusListener = listener;
    }

    public static interface ExpandStatusListener {

        void statusChange(boolean isExpand);
    }

    public interface OnClickExpandListenner {
        void OnClick(View view);
    }

    private OnClickExpandListenner onClickExpandListenner;

    public void setOnClickExpandListenner(OnClickExpandListenner onClickExpandListenner) {
        this.onClickExpandListenner = onClickExpandListenner;
    }
}