package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cs309.a1.gameboard.R;
import cs309.a1.shared.Util;
import cs309.a1.shared.bluetooth.BluetoothServer;

public class ConnectActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 10;

	private Context mContext;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothServer mBluetoothServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

		mContext = this;

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			startListeningForDevices();
		}

		Button connectButton = (Button) findViewById(R.id.connectButton);
		connectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canStartGame()) {
					Intent i = new Intent(ConnectActivity.this, GameboardActivity.class);
					startActivity(i);
				}
			}
		});
	}

	/**
	 * Returns whether or not a game can be started or not
	 * 
	 * - There needs to be at least 2 devices connected
	 * 
	 * @return whether a game can be started or not
	 */
	private boolean canStartGame() {
		return mBluetoothServer.getConnectedDeviceCount() > 1;
	}

	private void startListeningForDevices() {
		mBluetoothServer = BluetoothServer.getInstance();
		Util.ensureDiscoverable(mContext, mBluetoothAdapter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_ENABLE_BT) {
			startListeningForDevices();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}