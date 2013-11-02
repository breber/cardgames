package com.worthwhilegames.cardgames.shared;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * A Custom RelativeLayout that will rotate vertically
 */
public class VerticalRelativeLayout extends RelativeLayout {

    private int mRotation = 0;

    /**
     * Create a VerticalRelativeLayout with the given Context and Attributes
     * 
     * @param context
     * @param attrs
     */
    public VerticalRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if ("rotation".equals(attrs.getAttributeName(i))) {
                try {
                    mRotation = Integer.parseInt(attrs.getAttributeValue(i));
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see android.view.View#onSizeChanged(int, int, int, int)
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    /* (non-Javadoc)
     * @see android.widget.RelativeLayout#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    /* (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        if (mRotation >= 0 && mRotation < 180) {
            c.rotate(90);
            c.translate(0, -getWidth());
        } else {
            c.rotate(-90);
            c.translate(-getHeight(), 0);
        }
    }
}
