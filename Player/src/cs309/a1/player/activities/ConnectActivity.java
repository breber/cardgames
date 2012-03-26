package cs309.a1.player.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
import cs309.a1.player.R;
import cs309.a1.shared.TextView;
import cs309.a1.shared.Util;
import cs309.a1.shared.activities.DeviceListActivity;
import cs309.a1.shared.bluetooth.BluetoothClient;
import cs309.a1.shared.bluetooth.BluetoothConstants;

public class ConnectActivity extends Activity {

	private static int DEVICE_LIST_RESULT = Math.abs(DeviceListActivity.class.getName().hashCode());

	private Context mContext;

	private boolean readyToStart = false;

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int currentState = intent.getIntExtra(BluetoothConstants.KEY_STATE_MESSAGE, -1);

			if (Util.isDebugBuild()) {
				Toast.makeText(mContext, "onReceive " + currentState, Toast.LENGTH_LONG).show();
			}

			if (currentState == BluetoothConstants.STATE_CONNECTED) {
				readyToStart = true;

				TextView tv = (TextView) findViewById(R.id.connectingText);
				tv.setText(getResources().getString(R.string.waitingForGame));

				// Register the receiver for receiving messages from Bluetooth
				registerReceiver(gameStartReceiver, new IntentFilter(BluetoothConstants.MESSAGE_RX_INTENT));
			} else if (currentState == BluetoothConstants.STATE_LISTEN) {
				Intent showDeviceList = new Intent(ConnectActivity.this, DeviceListActivity.class);
				startActivityForResult(showDeviceList, DEVICE_LIST_RESULT);
			}
		}
	};


	private BroadcastReceiver gameStartReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int messageType = intent.getIntExtra(BluetoothConstants.KEY_MESSAGE_TYPE, 0);

			if (Util.isDebugBuild()) {
				Toast.makeText(mContext, "messageType = " + messageType, Toast.LENGTH_SHORT).show();
			}

			if (readyToStart && messageType == BluetoothConstants.MSG_TYPE_INIT) {
				// We connected just fine, so bring them to the ShowCardsActivity, and close
				// this activity out.
				Intent showCards = new Intent(ConnectActivity.this, ShowCardsActivity.class);
				startActivity(showCards);

				ConnectActivity.this.setResult(RESULT_OK);
				ConnectActivity.this.finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

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

		try {
			unregisterReceiver(gameStartReceiver);
		} catch (IllegalArgumentException e) {
			// We didn't get far enough to register the receiver
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DEVICE_LIST_RESULT && resultCode != RESULT_CANCELED) {
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
