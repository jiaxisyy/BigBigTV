package com.share_will.mobile.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.share_will.mobile.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoLayoutHelper;

/**
 * Created by Chenguandu on 2018/3/31.
 */

public class RowItemView extends LinearLayout {

    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    private Context context;
    private ImageView mWidgetRowActionImg;
    private TextView mWidgetRowLabel;
    private ImageView mWidgetRowIconImg;
    private int mTextColor;
    private float mTextSize;
    private int mDrawableIcon;
    private String mTitleText;


    public RowItemView(Context context) {
        super(context);
        init(context);
    }

    public RowItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initAttr(attrs);
    }

    public RowItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.RowItemView);

        mTitleText = a.getString(R.styleable.RowItemView_text);
        mDrawableIcon = a.getResourceId(R.styleable.RowItemView_iconDrawable, 0);
        mWidgetRowLabel.setText(mTitleText);
        mWidgetRowIconImg.setBackgroundResource(mDrawableIcon);

        a.recycle();
    }

    private void init(final Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.widget_row_item, this);
        mWidgetRowIconImg = findViewById(R.id.mWidgetRowIconImg);
        mWidgetRowLabel = findViewById(R.id.mWidgetRowLabel);
        mWidgetRowActionImg = findViewById(R.id.mWidgetRowActionImg);
        mWidgetRowActionImg.setImageResource(R.drawable.btn_arrow_right_gray);
    }

    @Override
    public AutoLinearLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AutoLinearLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
