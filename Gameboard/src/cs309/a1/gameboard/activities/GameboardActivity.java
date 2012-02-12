package cs309.a1.gameboard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.TextView;
import cs309.a1.gameboard.R;

public class GameboardActivity extends Activity {

	@Override
	protected void onStart() {
		super.onStart();
		setContentView(R.layout.information);

		TextView title = (TextView) findViewById(R.id.informationTitle);
		title.setText("Gameboard");
	}

	@Override
	public void onBackPressed() {
		AlertDialog dlg = new AlertDialog.Builder(this).create();
		dlg.setTitle(R.string.quit);
		dlg.setButton(getResources().getString(R.string.yes), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Start the Main Menu
				Intent intent = new Intent(GameboardActivity.this, MainMenu.class);
				startActivity(intent);

				// Finish this activity
				finishActivity(RESULT_OK);
			}
		});
		dlg.setButton2(getResources().getString(R.string.cancel), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Don't do anything if they cancel
			}
		});

		dlg.show();
	}

}
