package cs309.a1.gameboard.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cs309.a1.crazyeights.CrazyEightsCardTranslator;
import cs309.a1.crazyeights.CrazyEightsGameController;
import cs309.a1.gameboard.R;
import cs309.a1.shared.Card;
import cs309.a1.shared.CardTranslator;
import cs309.a1.shared.GameController;
import cs309.a1.shared.Player;
import cs309.a1.shared.bluetooth.BluetoothConstants;
import cs309.a1.shared.bluetooth.BluetoothServer;

public class GameboardActivity extends Activity {
	
	/**
	 * The Logcat Debug tag
	 */
	private static final String TAG = GameboardActivity.class.getName();
	
	/**
	 * The request code to keep track of the Pause Menu activity
	 */
	private static final int PAUSE_GAME = Math.abs("PAUSE_GAME".hashCode());

	/**
	 * The request code to keep track of the "Are you sure you want to quit" activity
	 */
	private static final int QUIT_GAME = Math.abs("QUIT_GAME".hashCode());

	/**
	 * The request code to keep track of the "You have been disconnected" activity
	 */
	private static final int DISCONNECTED = Math.abs("DISCONNECTED".hashCode());
	
	private static final int DECLARE_WINNER = Math.abs("DECLARE_WINNER".hashCode());
	
	private BluetoothServer bts;
	
	List<Player> players;
	
	private static final int MAX_DISPLAYED = 13;
	
	private static final int MAX_DIS_SIDES = 7;
	
	private int player1cards;
	
	private int player2cards;
	
	private int player3cards;
	
	private int player4cards;
	
	CardTranslator ct;
	
	GameController gameController;
	
	
	/**
	 * This will be 0 to 3 to indicate the spot in the players array for the player currently taking their turn
	 */
	private int whoseTurn = 0; 

	/**
	 * The BroadcastReceiver for handling messages from the Bluetooth connection
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			gameController.handleBroadcastReceive(context, intent);
			
			if (BluetoothConstants.STATE_CHANGE_INTENT.equals(action)) {
				// Handle a state change
				int newState = intent.getIntExtra(BluetoothConstants.KEY_STATE_MESSAGE, BluetoothConstants.STATE_NONE);

				// If the new state is anything but connected, display the "You have been disconnected" screen
				if (newState != BluetoothConstants.STATE_CONNECTED) {
					Intent i = new Intent(GameboardActivity.this, ConnectionFailActivity.class);
					startActivityForResult(i, DISCONNECTED);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameboard);
		
		ImageButton pause = (ImageButton) findViewById(R.id.gameboard_pause);
		
		pause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent pauseButtonClick = new Intent(GameboardActivity.this, PauseMenuActivity.class);
				startActivityForResult(pauseButtonClick, PAUSE_GAME);
			}
		});
		
		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.MESSAGE_RX_INTENT));
		registerReceiver(receiver, new IntentFilter(BluetoothConstants.STATE_CHANGE_INTENT));

		bts = BluetoothServer.getInstance(this);

		int numOfConnections = bts.getConnectedDeviceCount();
		players = new ArrayList<Player>(); 
		List<String> devices = bts.getConnectedDevices();
		
		for (int i = 0; i < numOfConnections; i++){
			Player p = new Player();
			p.setId(devices.get(i));
			p.setName("Player "+i);
			players.add(p);
		}

		//the GameController now handles the setup of the game.
		gameController = new CrazyEightsGameController(this, bts, players);
		ct = new CrazyEightsCardTranslator();
	}
	
	

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, QuitGameActivity.class);
		startActivityForResult(intent, QUIT_GAME);
	}

	@Override
	protected void onDestroy() {
		// Disconnect Bluetooth connection
		BluetoothServer.getInstance(this).disconnect();

		// Unregister the receiver
		unregisterReceiver(receiver);

		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK) {
			// Finish this activity
			setResult(RESULT_OK);
			finish();
		} else if (requestCode == DISCONNECTED) {
			if (requestCode == RESULT_OK) {
				// TODO: DROP PLAYER
			} else {
				// TODO: CONNECT DIFFERENT PLAYER
			}
		} else if (requestCode == PAUSE_GAME && resultCode == RESULT_CANCELED) {
			// Finish this activity
			setResult(RESULT_OK);
			finish();
		} else if (requestCode == DECLARE_WINNER && resultCode == RESULT_OK)

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void placeCard(int location, Card newCard) {

		LinearLayout ll;
		LinearLayout.LayoutParams lp;
		int handSize;

		// convert dip to pixels
		final float dpsToPixScale = getApplicationContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (125 * dpsToPixScale + 0.5f);

		// place in discard pile
		if(location == 0) {
			ImageView discard = (ImageView) findViewById(R.id.discardpile);
			discard.setImageResource(newCard.getResourceId());
		}

		// if Player 1 or Player 3
		else if(location == 1 || location == 3) {

			if(location == 1) {
				ll = (LinearLayout) findViewById(R.id.player1ll);
				handSize = ++player1cards;
			}
			else {
				ll = (LinearLayout) findViewById(R.id.player3ll);
				handSize = ++player3cards;
			}
			
			if(handSize == 1) {
				lp = new LinearLayout.LayoutParams(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);
	
				ImageView toAdd = new ImageView(this);
				toAdd.setImageResource(newCard.getResourceId());
				if(location == 1) toAdd.setId(handSize);
				else toAdd.setId(2*MAX_DISPLAYED + handSize);
				toAdd.setAdjustViewBounds(true);
				ll.addView(toAdd, lp);
			}
			
			else if(handSize <= MAX_DISPLAYED) {
				
				Bitmap verticalCard = BitmapFactory.decodeResource(getResources(), newCard.getResourceId());
				Matrix tempMatrix = new Matrix();
				
				if(location == 3) {
					Bitmap halfCard = Bitmap.createBitmap(verticalCard, verticalCard.getWidth()/2, 0, verticalCard.getWidth()/2, verticalCard.getHeight(), tempMatrix, true);
					ImageView toAdd = new ImageView(this);
					toAdd.setId(2*MAX_DISPLAYED + handSize);
					toAdd.setImageBitmap(halfCard);
		
					lp = new LinearLayout.LayoutParams(pixels/2, LinearLayout.LayoutParams.WRAP_CONTENT);
					toAdd.setAdjustViewBounds(true);
					ll.addView(toAdd, lp);
				}
				
				else {
					Bitmap horCard = Bitmap.createBitmap(verticalCard, 0, 0, verticalCard.getWidth()/2, verticalCard.getHeight(), tempMatrix, true);
					ll.removeAllViews();
					for(int i = 1; i < handSize; i++) {
						ImageView toAdd = new ImageView(this);
						toAdd.setId(i+1);
						toAdd.setImageBitmap(horCard);
			
						lp = new LinearLayout.LayoutParams(pixels/2, LinearLayout.LayoutParams.WRAP_CONTENT);
						toAdd.setAdjustViewBounds(true);
						ll.addView(toAdd, lp);
					}
					
					ImageView toAdd = new ImageView(this);
					toAdd.setId(1);
					toAdd.setImageResource(newCard.getResourceId());
		
					lp = new LinearLayout.LayoutParams(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);
					toAdd.setAdjustViewBounds(true);
					ll.addView(toAdd, lp);
				}
			}
			
			else {
				//TODO: display counter of cards not shown
			}
		}

		// if Player 2 or Player 4
		else if(location == 2 || location == 4) {

			if(location == 2) {
				ll = (LinearLayout) findViewById(R.id.player2ll);
				handSize = ++player2cards;
			}
			else {
				ll = (LinearLayout) findViewById(R.id.player4ll);
				handSize = ++player4cards;
			}
			
			if(handSize == 1) {

				// rotate vertical card image 90 degrees
				Bitmap verticalCard = BitmapFactory.decodeResource(getResources(), newCard.getResourceId());
				Matrix tempMatrix = new Matrix();
				tempMatrix.postRotate(90);
				Bitmap horCard = Bitmap.createBitmap(verticalCard, 0, 0, verticalCard.getWidth(), verticalCard.getHeight(), tempMatrix, true);
	
				ImageView toAdd = new ImageView(this);
				if(location == 2) toAdd.setId(MAX_DISPLAYED + handSize);
				else toAdd.setId(3*MAX_DISPLAYED + handSize);
				toAdd.setImageBitmap(horCard);
	
				lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, pixels);
				toAdd.setAdjustViewBounds(true);
				ll.addView(toAdd, lp);
			}
			
			else if(handSize <= MAX_DIS_SIDES) {
				
				Bitmap horCard;
				
				Bitmap verticalCard = BitmapFactory.decodeResource(getResources(), newCard.getResourceId());
				double conversion = verticalCard.getHeight()*(((double)pixels/(double)verticalCard.getWidth()));
				
				Matrix tempMatrix = new Matrix();
				tempMatrix.postRotate(90);
				
				if(location == 4) {
					horCard = Bitmap.createBitmap(verticalCard, 0, 0, verticalCard.getWidth()/2, verticalCard.getHeight(), tempMatrix, true);
					ll.removeAllViews();
					for(int i = 1; i < handSize; i++) {
						ImageView toAdd = new ImageView(this);
						toAdd.setId(3*MAX_DISPLAYED + i+1);
						toAdd.setImageBitmap(horCard);
			
						lp = new LinearLayout.LayoutParams((int)conversion, LinearLayout.LayoutParams.WRAP_CONTENT);
						toAdd.setAdjustViewBounds(true);
						ll.addView(toAdd, lp);
					}
					
					Bitmap verticalCard2 = BitmapFactory.decodeResource(getResources(), newCard.getResourceId());
					Matrix tempMatrix2 = new Matrix();
					tempMatrix2.postRotate(90);
					Bitmap horCard2 = Bitmap.createBitmap(verticalCard2, 0, 0, verticalCard2.getWidth(), verticalCard2.getHeight(), tempMatrix2, true);
					
					ImageView toAdd = new ImageView(this);
					toAdd.setId(3*MAX_DISPLAYED + 1);
					toAdd.setImageBitmap(horCard2);
		
					lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, pixels);
					toAdd.setAdjustViewBounds(true);
					ll.addView(toAdd, lp);
				}
				else {
					horCard = Bitmap.createBitmap(verticalCard, verticalCard.getWidth()/2, 0, verticalCard.getWidth()/2, verticalCard.getHeight(), tempMatrix, true);
					ImageView toAdd = new ImageView(this);
					toAdd.setId(MAX_DISPLAYED + handSize);
					toAdd.setImageBitmap(horCard);
		
					lp = new LinearLayout.LayoutParams((int)conversion, LinearLayout.LayoutParams.WRAP_CONTENT);
					toAdd.setAdjustViewBounds(true);
					ll.addView(toAdd, lp);
				}
			}
			
			else {
				//TODO: display counter of cards not shown
			}
		}

		else {
			ImageView draw = (ImageView) findViewById(R.id.drawpile);
			draw.setImageResource(newCard.getResourceId());
		}
	}
	
	public void removeCard(int location) {
		
		LinearLayout ll;
		int handSize;
		
		if(location == 1) {
			ll = (LinearLayout) findViewById(R.id.player1ll);
			handSize = --player1cards;
			if(handSize < MAX_DISPLAYED) {
				if(handSize == 0) {
					ll.removeView(findViewById(1));
				}
				else {
					ll.removeView(findViewById(handSize + 1));
				}
			}
		}
		else if(location == 2) {
			ll = (LinearLayout) findViewById(R.id.player2ll);
			handSize = --player2cards;
			if(handSize < MAX_DIS_SIDES) {
				ll.removeView(findViewById(MAX_DISPLAYED +handSize+1));
			}
		}
		else if(location == 3) {
			ll = (LinearLayout) findViewById(R.id.player3ll);
			handSize = --player3cards;
			if(handSize < MAX_DISPLAYED) {
				ll.removeView(findViewById(2*MAX_DISPLAYED + handSize+1));
			}
		}
		else {
			ll = (LinearLayout) findViewById(R.id.player4ll);
			handSize = --player4cards;
			if(handSize < MAX_DIS_SIDES) {
				if(handSize == 0) {
					ll.removeView(findViewById(3*MAX_DISPLAYED + 1));
				}
				else {
					ll.removeView(findViewById(3*MAX_DISPLAYED + handSize + 1));
				}
			}
		}
	}
	
}
