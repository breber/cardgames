package cs309.a1.shared;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextView extends android.widget.TextView {

	private Context mContext;

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "helsinki.ttf");
		setTypeface(typeface);
	}
}
