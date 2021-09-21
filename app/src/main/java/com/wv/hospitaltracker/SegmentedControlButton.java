package com.wv.hospitaltracker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by wolfgangvogl on 22/11/15.
 */
public class SegmentedControlButton extends RadioButton {


    private boolean mTextAllCaps;

    private int mLineHeightSelected;
    private int mLineHeightUnselected;

    private Paint mLinePaint;

    public SegmentedControlButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.segmentedControlButtonStyle);
    }

    public SegmentedControlButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.SegmentedControlButton, defStyle, R.style.Widget_Holo_SegmentedControl);

//            mTextAllCaps = a.getBoolean(R.styleable.SegmentedControlButton_textAllCaps, false);
            mTextAllCaps = true;
            setTextCompat(getText());

            int lineColor = a.getColor(R.styleable.SegmentedControlButton_lineColor, 0);
            mLineHeightUnselected = a.getDimensionPixelSize(R.styleable.SegmentedControlButton_lineHeightUnselected, 0);
            mLineHeightSelected = a.getDimensionPixelSize(R.styleable.SegmentedControlButton_lineHeightSelected, 0);

            mLinePaint = new Paint();
            mLinePaint.setColor(lineColor);
            mLinePaint.setStyle(Paint.Style.FILL);

            a.recycle();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the line
        if (mLinePaint.getColor() != 0 && (mLineHeightSelected > 0 || mLineHeightUnselected > 0)) {
            int lineHeight;
            if (isChecked()) {
                lineHeight = mLineHeightSelected;
            } else {
                lineHeight = mLineHeightUnselected;
            }

            if (lineHeight > 0) {
                Rect rect = new Rect(0, this.getHeight() - lineHeight, getWidth(), this.getHeight());
                canvas.drawRect(rect, mLinePaint);
            }
        }
    }

    // Used for android:textAllCaps
    public void setTextCompat(CharSequence text) {
        if (mTextAllCaps) {
            setText(text.toString().toUpperCase());
        }
        else {
            setText(text);
        }
    }

    public int getLineColor() {
        return mLinePaint.getColor();
    }

    public int getLineHeightUnselected() {
        return mLineHeightUnselected;
    }

    public void setLineColor(int lineColor) {
        mLinePaint.setColor(lineColor);
    }
}
