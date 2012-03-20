package cs309.a1.player.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cs309.a1.player.R;
import cs309.a1.shared.Card;

public class ShowCardsActivity extends Activity{

	private static final int QUIT_GAME = "QUIT_GAME".hashCode();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_hand);
		addCard(new Card(0, 1, R.drawable.clubs_2, 1));
		addCard(new Card(0, 2, R.drawable.clubs_3, 2));
		addCard(new Card(0, 3, R.drawable.clubs_4, 3));
		addCard(new Card(0, 4, R.drawable.clubs_5, 4));
		addCard(new Card(0, 5, R.drawable.clubs_6, 5));
		addCard(new Card(0, 6, R.drawable.clubs_7, 6));
		removeCard(3);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, QuitGameActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIT_GAME) {
			if (resultCode == RESULT_OK) {
				// Finish this activity - if everything goes right, we
				// should be back at the main menu
				setResult(RESULT_OK);
				finish();
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	void addCard(Card newCard) {
		
		// create ImageView to hold Card
		ImageView toAdd = new ImageView(this);
		toAdd.setImageResource(newCard.getResourceId());
		toAdd.setId(newCard.getIdNum());
		LinearLayout ll = (LinearLayout) findViewById(R.id.playerCardContainer);
		
		// convert dip to pixels
		final float dpsToPixScale = getApplicationContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (125 * dpsToPixScale + 0.5f);
		
		// edit layout attributes
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);
		toAdd.setAdjustViewBounds(true);
		ll.addView(toAdd, lp);
	}
	
	void removeCard(int idNum) {
		ImageView toRemove = (ImageView) findViewById(idNum);
		toRemove.setVisibility(8); // set visibility attribute to GONE
	}
}
