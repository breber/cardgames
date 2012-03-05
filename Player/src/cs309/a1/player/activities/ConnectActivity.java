package cs309.a1.player.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import cs309.a1.player.R;
import cs309.a1.shared.bluetooth.DeviceListActivity;

public class ConnectActivity extends Activity{

	private static int DEVICE_LIST_RESULT = DeviceListActivity.class.getName().hashCode();

	private String tabletMacAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.informationTitle);
		title.setText(R.string.ConnectActivity_title);

		if (tabletMacAddress == null || "".equals(tabletMacAddress)) {
			Intent showDeviceList = new Intent(this, DeviceListActivity.class);
			startActivityForResult(showDeviceList, DEVICE_LIST_RESULT);
		} else {
			// Start connecting to device
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();
		if (requestCode == DEVICE_LIST_RESULT && resultCode != RESULT_CANCELED) {
			// TODO: start connecting to device
			Intent showCards = new Intent(this, ShowCardsActivity.class);
			startActivity(showCards);

			Toast.makeText(this, "Connect to: " + data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS), Toast.LENGTH_SHORT).show();
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}
