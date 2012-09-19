package com.worthwhilegames.cardgames.shared.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.euchre.EuchreTabletGame;
import com.worthwhilegames.cardgames.shared.Player;
import com.worthwhilegames.cardgames.shared.TextView;

/**
 * This will have a pop-up dialog that shows the score
 *
 */
public class RoundScoresActivity extends Activity {

	/**
	 * the list of current active players
	 */
	private List<Player> players;

	/**
	 * This game object will keep track of the current state of the game and be
	 * used to manage player hands and draw and discard piles
	 */
	private static EuchreTabletGame game = null;


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_dialog);

		game = EuchreTabletGame.getInstance();

		players = game.getPlayers();

		// Update the prompt dialog title text
		TextView tv = (TextView) findViewById(R.id.notificationDialogTitle);

		tv.setText(players.get(0).getName() + " & " + players.get(2).getName()
				+ ": " + game.getMatchScores()[0] +
				" \n" + players.get(1).getName() + " & " + players.get(3).getName()
				+ ": " + game.getMatchScores()[1]);

		// Add handlers to affirmative and negative buttons
		Button affirmative = (Button) findViewById(R.id.notificationAffirmative);
		affirmative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		//leave activity
		setResult(RESULT_OK);
		finish();
	}
}
