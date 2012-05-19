package cs309.a1.player.activities;

import static cs309.a1.shared.Constants.SUIT_CLUBS;
import static cs309.a1.shared.Constants.SUIT_DIAMONDS;
import static cs309.a1.shared.Constants.SUIT_HEARTS;
import static cs309.a1.shared.Constants.SUIT_SPADES;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cs309.a1.R;

/**
 * The popup displayed when a user plays an 8-Card.
 * It allows the user to choose which suit they want
 * to change it to.
 *
 * Activity Results:
 * 		Constants.SUIT_SPADES   - If the user chose spades
 * 		Constants.SUIT_HEARTS   - If the user chose hearts
 * 		Constants.SUIT_CLUBS    - If the user chose clubs
 * 		Constants.SUIT_DIAMONDS - If the user chose diamonds
 * 		RESULT_OK				- If the user doesn't choose
 */
public class SelectSuitActivity extends Activity{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectsuit);

		// Set the listener for the spade button
		ImageView spade = (ImageView) findViewById(R.id.spadeSuit);
		spade.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				setResult(SUIT_SPADES);
				finish();
			}
		});

		// Set the listener for the heart button
		ImageView heart = (ImageView) findViewById(R.id.heartSuit);
		heart.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				setResult(SUIT_HEARTS);
				finish();
			}
		});

		// Set the listener for the club button
		ImageView club = (ImageView) findViewById(R.id.clubSuit);
		club.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				setResult(SUIT_CLUBS);
				finish();
			}
		});

		// Set the listener for the diamond button
		ImageView diamond = (ImageView) findViewById(R.id.diamondSuit);
		diamond.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				setResult(SUIT_DIAMONDS);
				finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		finish();
	}
}
