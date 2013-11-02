package com.worthwhilegames.cardgames.shared;

import android.app.Activity;
import android.view.View;

import com.worthwhilegames.cardgames.R;

/**
 * All activities should extend AdActivity, so that
 * it is trivial to disable ads on an install.
 * 
 * @author breber
 */
public abstract class AdActivity extends Activity {

    private boolean mAdsHidden = false;

    @Override
    protected void onStart() {
        super.onStart();

        if (mAdsHidden) {
            View v = findViewById(R.id.adView);

            if (v != null) {
                v.setVisibility(View.GONE);
            }
        }
    }
}
