package cs309.a1.player.activities;

import static cs309.a1.crazyeights.Constants.SUIT_CLUBS;
import static cs309.a1.crazyeights.Constants.SUIT_DIAMONDS;
import static cs309.a1.crazyeights.Constants.SUIT_HEARTS;
import static cs309.a1.crazyeights.Constants.SUIT_SPADES;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.player.R;

public class SelectSuitActivity extends Activity{
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectsuit);
		
		//set the listener for the spade button
		Button spade = (Button) findViewById(R.id.Spades);
		spade.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(SUIT_SPADES);
				finish();
			}
		});
		
		//set the listener for the heart button
		Button heart = (Button) findViewById(R.id.Hearts);
		heart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(SUIT_HEARTS);
				finish();
			}
		});
		
		//set the listener for the club button
		Button club = (Button) findViewById(R.id.Clubs);
		club.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(SUIT_CLUBS);
				finish();
			}
		});
		
		//set the listener for the diamond button
		Button diamond = (Button) findViewById(R.id.Diamonds);
		diamond.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(SUIT_DIAMONDS);
				finish();
			}
		});
	}

}
