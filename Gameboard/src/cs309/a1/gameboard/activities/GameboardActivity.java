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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cs309.a1.crazyeights.CrazyEightsGameController;
import cs309.a1.gameboard.R;
import cs309.a1.shared.Card;
import cs309.a1.shared.GameController;
import cs309.a1.shared.Player;
import cs309.a1.shared.Util;
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
	public static final int DISCONNECTED = Math.abs("DISCONNECTED".hashCode());

	/**
	 * The request code to keep track of the "Player N Won!" activity
	 */
	private static final int DECLARE_WINNER = Math.abs("DECLARE_WINNER".hashCode());

	/**
	 * The BluetoothServer that sends and receives messages from other devices
	 */
	private BluetoothServer bts;

	/**
	 * The maximum number of cards to be displayed on longest sides of tablet
	 */
	private static final int MAX_DISPLAYED = 13;

	/**
	 * The maximum number of cards to be displayed on shortest sides of tablet
	 */
	private static final int MAX_DIS_SIDES = 7;

	/**
	 * The number of cards in player 1's hand
	 */
	private int player1cards;

	/**
	 * The number of cards in player 2's hand
	 */
	private int player2cards;

	/**
	 * The number of cards in player 3's hand
	 */
	private int player3cards;

	/**
	 * The number of cards in player 4's hand
	 */
	private int player4cards;

	private GameController gameController;

	/**
	 * The BroadcastReceiver for handling messages from the Bluetooth connection
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (Util.isDebugBuild()) {
				Log.d(TAG, "onReceive: " + action);
			}

			if (BluetoothConstants.STATE_CHANGE_INTENT.equals(action)) {
				// Handle a state change
				int newState = intent.getIntExtra(BluetoothConstants.KEY_STATE_MESSAGE, BluetoothConstants.STATE_NONE);

				// If the new state is anything but connected, display the "You have been disconnected" screen
				if (newState != BluetoothConstants.STATE_CONNECTED) {
					Intent i = new Intent(GameboardActivity.this, ConnectionFailActivity.class);
					i.putExtra(BluetoothConstants.KEY_DEVICE_ID, intent.getStringExtra(BluetoothConstants.KEY_DEVICE_ID));
					startActivityForResult(i, DISCONNECTED);
				}
			} else {
				// We didn't handle the Broadcast message here, so pass it on to the GameController
				gameController.handleBroadcastReceive(context, intent);
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
		List<Player> players = new ArrayList<Player>();
		List<String> devices = bts.getConnectedDevices();

		for (int i = 0; i < numOfConnections; i++){
			Player p = new Player();
			p.setId(devices.get(i));
			p.setName("Player " + i);
			players.add(p);
		}

		ImageButton refresh = (ImageButton) findViewById(R.id.gameboard_refresh);

		//the GameController now handles the setup of the game.
		gameController = new CrazyEightsGameController(this, bts, players, refresh);
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
		} else if (requestCode == PAUSE_GAME && resultCode == RESULT_CANCELED) {
			// Finish this activity
			setResult(RESULT_OK);
			finish();
		} else if (requestCode == DECLARE_WINNER && resultCode == RESULT_OK) {
			// TODO: what do we do here?
		} else {
			// If we didn't handle the result here, try handling it in the GameController
			// If they don't handle it, pass it on to the default onActivityResult
			if (!gameController.handleActivityResult(requestCode, resultCode, data)) {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

	/**
	 * Places a card in the specified location on the game board
	 * 
	 * @param location Location to place the card
	 * @param newCard Card to be placed on the game board
	 */
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

			// create full-sized card image if first card in hand
			if(handSize == 1) {
				lp = new LinearLayout.LayoutParams(pixels, LinearLayout.LayoutParams.WRAP_CONTENT);

				ImageView toAdd = new ImageView(this);
				toAdd.setImageResource(newCard.getResourceId());
				if(location == 1) toAdd.setId(handSize);
				else toAdd.setId(2*MAX_DISPLAYED + handSize);
				toAdd.setAdjustViewBounds(true);
				ll.addView(toAdd, lp);
			}

			// create half-sized card image to add to hand if current card count is less than display limit
			else if(handSize <= MAX_DISPLAYED) {

				Bitmap verticalCard = BitmapFactory.decodeResource(getResources(), newCard.getResourceId());
				Matrix tempMatrix = new Matrix();

				// if player 3, add new image to linear layout of player 3's hand
				if(location == 3) {
					Bitmap halfCard = Bitmap.createBitmap(verticalCard, verticalCard.getWidth()/2, 0, verticalCard.getWidth()/2, verticalCard.getHeight(), tempMatrix, true);
					ImageView toAdd = new ImageView(this);
					toAdd.setId(2*MAX_DISPLAYED + handSize);
					toAdd.setImageBitmap(halfCard);

					lp = new LinearLayout.LayoutParams(pixels/2, LinearLayout.LayoutParams.WRAP_CONTENT);
					toAdd.setAdjustViewBounds(true);
					ll.addView(toAdd, lp);
				}

				// if player 1, remove and re-add all views so new card displays in correct order
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

			// create full-sized horizontal card if first card in hand
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

			// create horizontal half-cards to display if maximum display count has not been reached
			else if(handSize <= MAX_DIS_SIDES) {

				Bitmap horCard;

				Bitmap verticalCard = BitmapFactory.decodeResource(getResources(), newCard.getResourceId());
				double conversion = verticalCard.getHeight()*(((double)pixels/(double)verticalCard.getWidth()));

				Matrix tempMatrix = new Matrix();
				tempMatrix.postRotate(90);

				// if player 4, remove all views and re-add to player 4's linear layout to display in correct order
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

				// if player 2, add new card view to player 2's linear layout
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

		// set draw pile image
		else {
			ImageView draw = (ImageView) findViewById(R.id.drawpile);
			draw.setImageResource(newCard.getResourceId());
		}
	}

	/**
	 * Removes a card from specified location on the game board
	 * 
	 * @param location Location from which card should be removed
	 */
	public void removeCard(int location) {

		LinearLayout ll;
		int handSize;

		// remove card from player 1's hand
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

		// remove card from player 2's hand
		else if(location == 2) {
			ll = (LinearLayout) findViewById(R.id.player2ll);
			handSize = --player2cards;
			if(handSize < MAX_DIS_SIDES) {
				ll.removeView(findViewById(MAX_DISPLAYED +handSize+1));
			}
		}

		// remove card from player 3's hand
		else if(location == 3) {
			ll = (LinearLayout) findViewById(R.id.player3ll);
			handSize = --player3cards;
			if(handSize < MAX_DISPLAYED) {
				ll.removeView(findViewById(2*MAX_DISPLAYED + handSize+1));
			}
		}

		// remove card from player 4's hand
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
