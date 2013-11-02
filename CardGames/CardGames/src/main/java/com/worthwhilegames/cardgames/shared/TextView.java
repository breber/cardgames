package com.worthwhilegames.cardgames.shared;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * A Custom TextView for this Application
 * 
 * On Production builds, it uses a custom font
 */
public class TextView extends android.widget.TextView {

    /**
     * The context of this TextView
     */
    private Context mContext;

    /**
     * Create a TextView with the given Context and Attributes
     * 
     * @param context
     * @param attrs
     */
    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /* (non-Javadoc)
     * @see android.view.View#onFinishInflate()
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // If this is not a debug build, use the custom font
        if (!Util.isDebugBuild()) {
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),
                    Constants.FONT_NAME);
            setTypeface(typeface);
        }
    }
}
