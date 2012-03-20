package cs309.a1.gameboard.activities;

import static cs309.a1.shared.CardGame.CRAZY_EIGHTS;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cs309.a1.crazyeights.CrazyEightGameRules;
import cs309.a1.crazyeights.CrazyEightsTabletGame;
import cs309.a1.gameboard.R;
import cs309.a1.shared.Deck;
import cs309.a1.shared.Game;
import cs309.a1.shared.Player;
import cs309.a1.shared.Rules;
import cs309.a1.shared.bluetooth.BluetoothServer;

public class GameboardActivity extends Activity {

	private static final int QUIT_GAME = "QUIT_GAME".hashCode();
	private static Game game = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameboard);
		BluetoothServer bts = BluetoothServer.getInstance(this);
		
		int numOfConnections = bts.getConnectedDeviceCount();
		List<Player> players = new ArrayList<Player>();
		List<String> devices = bts.getConnectedDevices();
		
		for(int i = 0; i < numOfConnections; i++){
			Player p = new Player();
			p.setId(devices.get(i));
			p.setName("Player "+i);
			players.add(p);
		}
		
		Rules rules = new CrazyEightGameRules();
		Deck deck = new Deck(CRAZY_EIGHTS);
		Game game = CrazyEightsTabletGame.getInstance(players, deck, rules);
		game.setup();
		
		for(int i = 0; i < players.size(); i++){
			bts.write(players.get(i), players.get(i).getId());
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, QuitGameActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIT_GAME) {
			if (resultCode == RESULT_OK) {
				// Start the Main Menu
				Intent intent = new Intent(GameboardActivity.this, MainMenu.class);
				startActivity(intent);

				// Finish this activity
				finishActivity(RESULT_OK);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
