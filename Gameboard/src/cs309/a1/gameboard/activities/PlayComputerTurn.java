package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import cs309.a1.crazyeights.Constants;
import cs309.a1.shared.Util;

public class PlayComputerTurn extends Activity{

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
		finish();
	}
}
