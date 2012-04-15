package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import cs309.a1.shared.Constants;
import cs309.a1.shared.Util;

/**
 * This class will basically just wait a specified amount of time in the
 * Constants class then when the time is up the GameController class will play
 * for the computer using the information available in the GameController
 */
public class PlayComputerTurnActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Thread.sleep(Constants.COMPUTER_WAIT_TIME);
		} catch (InterruptedException e) {
			if (Util.isDebugBuild()) {
				Toast.makeText(this, "Sleep interrupted", Toast.LENGTH_SHORT);
			}
		}

		setResult(RESULT_OK);
		finish();
	}
}
