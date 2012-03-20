package cs309.a1.shared;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class Button extends android.widget.Button {

	private Context mContext;

	public Button(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		//Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "helsinki.ttf");
		//setTypeface(typeface);
	}
}
