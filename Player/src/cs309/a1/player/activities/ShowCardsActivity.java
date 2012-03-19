package cs309.a1.player.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cs309.a1.player.R;
import cs309.a1.shared.Card;

public class ShowCardsActivity extends Activity{

	private static final int QUIT_GAME = "QUIT_GAME".hashCode();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_hand);
		addCard(R.drawable.clubs_2);
		addCard(R.drawable.clubs_3);
		addCard(R.drawable.clubs_4);
		addCard(R.drawable.clubs_5);
		addCard(R.drawable.clubs_6);
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
	
	void addCard(int resourceID) {
		ImageView toAdd = new ImageView(this);
		toAdd.setImageResource(resourceID);
		LinearLayout ll = (LinearLayout) findViewById(R.id.playerCardContainer);
		ll.addView(toAdd);
	}
}
