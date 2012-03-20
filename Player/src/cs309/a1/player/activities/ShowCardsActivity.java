package cs309.a1.player.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cs309.a1.player.R;
import cs309.a1.shared.Card;

public class ShowCardsActivity extends Activity{

	private static final int QUIT_GAME = "QUIT_GAME".hashCode();
	
	private ArrayList<Card> cardHand;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_hand);
		cardHand = new ArrayList<Card>();
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
	
	public void addCard(Card newCard) {
		
		cardHand.add(newCard);
		
		Collections.sort(cardHand, new CompareIdNums());
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.playerCardContainer);
		ll.removeAllViews();
		// convert dip to pixels
		final float dpsToPixScale = getApplicationContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (125 * dpsToPixScale + 0.5f);
					
		// edit layout attributes
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		for(int i = 0; i < cardHand.size(); i++) {
		
			// create ImageView to hold Card
			ImageView toAdd = new ImageView(this);
			toAdd.setImageResource(cardHand.get(i).getResourceId());
			toAdd.setId(cardHand.get(i).getIdNum());
			toAdd.setAdjustViewBounds(true);
			ll.addView(toAdd, lp);
		}
	}
	
	public void removeFromHand(int idNum) {
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.playerCardContainer);
		ll.removeView((ImageView) findViewById(idNum));
		
		// remove card from list
		Card current = cardHand.get(0);
		int i = 0;
		
		while(current.getIdNum() != idNum) {
			i++;
			current = cardHand.get(i);
		}
		
		cardHand.remove(current);
	}
	
	private class CompareIdNums implements Comparator<Card> {
		
		public int compare(Card card1, Card card2) {
			return card1.getIdNum() - card2.getIdNum();
		}
	}
}
