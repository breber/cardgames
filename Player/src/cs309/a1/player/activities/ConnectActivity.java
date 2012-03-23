package cs309.a1.player.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
import cs309.a1.player.R;
import cs309.a1.shared.Util;
import cs309.a1.shared.activities.DeviceListActivity;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothConstants;

public class ConnectActivity extends Activity {

	private static int DEVICE_LIST_RESULT = Math.abs(DeviceListActivity.class.getName().hashCode());

	private Context mContext;

	private ProgressDialog dlg;
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int currentState = intent.getIntExtra(BluetoothConstants.KEY_STATE_MESSAGE, -1);

			if (Util.isDebugBuild()) {
				Toast.makeText(mContext, "onReceive " + currentState, Toast.LENGTH_LONG).show();
			}

			if (currentState == BluetoothConstants.STATE_CONNECTED) {
				// We connected just fine, so bring them to the ShowCardsActivity, and close
				// this activity out.
				dlg.dismiss();
				Intent showCards = new Intent(ConnectActivity.this, ShowCardsActivity.class);
				startActivity(showCards);

				ConnectActivity.this.setResult(RESULT_OK);
				ConnectActivity.this.finish();
			} else if (currentState == BluetoothConstants.STATE_LISTEN) {
				// If we make it back to listening state, we weren't able to connect,
				// so bring them back to the device list
				dlg.dismiss();

				Intent showDeviceList = new Intent(ConnectActivity.this, DeviceListActivity.class);
				startActivityForResult(showDeviceList, DEVICE_LIST_RESULT);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_hand);

		mContext = this;

		// Show the device list
		Intent showDeviceList = new Intent(this, DeviceListActivity.class);
		startActivityForResult(showDeviceList, DEVICE_LIST_RESULT);
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(receiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DEVICE_LIST_RESULT && resultCode != RESULT_CANCELED) {
			dlg = new ProgressDialog(mContext);
			dlg.setMessage("Waiting for connection...");
			dlg.show();

			String macAddress = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
			BluetoothClient client = BluetoothClient.getInstance(getApplicationContext());
			client.connect(macAddress);

			registerReceiver(receiver, new IntentFilter(BluetoothConstants.STATE_CHANGE_INTENT));
		} else {
			// The user cancelled out of the device list, so return them to the main menu
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}
