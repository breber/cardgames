package cs309.a1.shared.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cs309.a1.R;
import cs309.a1.shared.TextView;

/**
 * This Activity lists all devices that are discoverable, or paired and
 * in range. The user can then select which device they want to connect
 * to, and the address of that device is returned in the result Intent.
 *
 * Activity Results:
 * 		RESULT_OK - If the user chose a device
 * 					The Device's MAC address will be in the result
 * 					Intent with the key DeviceListActivity.EXTRA_DEVICE_ADDRESS
 *
 * 		RESULT_CANCELLED - If no device was chosen
 */
public class WifiConnectActivity extends Activity {

	/**
	 * Return Intent extra
	 */
	public static String EXTRA_DEVICE_ADDRESS = "deviceAddress";


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_dialog);

		TextView title = (TextView) findViewById(R.id.dialogPromptTitle);
		title.setText(R.string.enterIpAddress);

		final EditText textView = (EditText) findViewById(R.id.dialogPromptTextbox);
		textView.setHint(R.string.ipAddress);

		// create button for the view
		Button ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String ipAddress = "";

				// If the user didn't enter anything, send them back to main menu
				if (textView.getText().length() == 0) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					ipAddress = textView.getText().toString();

					// Return the name the player chose with the result of this Activity
					Intent nameToSend = new Intent();
					nameToSend.putExtra(EXTRA_DEVICE_ADDRESS, ipAddress);
					setResult(RESULT_OK, nameToSend);
					finish();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
}

