package cs309.a1.player.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cs309.a1.player.R;

public class GameResultsActivity extends Activity{
	private static final int MAIN_MENU = Math.abs("MAIN_MENU".hashCode());
	
	public static final String IS_WINNER = "iswinner";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TextView title = (TextView) findViewById(R.id.title);
		Intent isWinner = getIntent();
		if(isWinner.getBooleanExtra(IS_WINNER, false)){
			title.setText(R.string.winner);
			
		}else{
			title.setText(R.string.GameResultsActivity_title);
			
		}
		
		setContentView(R.layout.winlose);
				
		Button mainMenu = (Button) findViewById(R.id.btMainMenu);

		mainMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mainMenu = new Intent(GameResultsActivity.this, QuitGameActivity.class);
				startActivityForResult(mainMenu, MAIN_MENU);
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MAIN_MENU && resultCode == RESULT_OK) {
			setResult(RESULT_OK);
			finish();
		}
	}

}
	
	
