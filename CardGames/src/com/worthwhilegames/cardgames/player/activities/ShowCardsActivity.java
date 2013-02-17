package com.worthwhilegames.cardgames.player.activities;

import static com.worthwhilegames.cardgames.shared.Constants.PREFERENCES;
import static com.worthwhilegames.cardgames.shared.Constants.fourthCard;
import static com.worthwhilegames.cardgames.shared.Constants.fullCard;
import static com.worthwhilegames.cardgames.shared.Constants.halfCard;

import java.util.Collections;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.gameboard.activities.PauseMenuActivity;
import com.worthwhilegames.cardgames.shared.Card;
import com.worthwhilegames.cardgames.shared.Constants;
import com.worthwhilegames.cardgames.shared.GameFactory;
import com.worthwhilegames.cardgames.shared.PlayerState;
import com.worthwhilegames.cardgames.shared.activities.GameViewActivity;
import com.worthwhilegames.cardgames.shared.connection.ConnectionClient;
import com.worthwhilegames.cardgames.shared.connection.ConnectionConstants;

/**
 * This is the Activity that handles Game Play
 */
public class ShowCardsActivity extends GameViewActivity {


	/**
	 * Returns BroadcastReceiver for handling messages from the connection module
	 * @return BroadcastReceiver for handling messages from the connection module
	 */
	private BroadcastReceiver getBroadCastReceiver(){

		return new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				int messageType = intent.getIntExtra(ConnectionConstants.KEY_MESSAGE_TYPE, -1);
				if (ConnectionConstants.STATE_CHANGE_INTENT.equals(action)) {
					// Handle a state change
					int newState = intent.getIntExtra(ConnectionConstants.KEY_STATE_MESSAGE, ConnectionConstants.STATE_NONE);

					// If the new state is anything but connected, display the "You have been disconnected" screen
					if (newState != ConnectionConstants.STATE_CONNECTED) {
						Intent i = new Intent(ShowCardsActivity.this, ConnectionFailActivity.class);
						startActivityForResult(i, DISCONNECTED);
					}
				} else if (messageType == Constants.MSG_PAUSE) {
					Intent pause = new Intent(ShowCardsActivity.this, PauseMenuActivity.class);
					ShowCardsActivity.this.startActivityForResult(pause, PAUSE_GAME);
				} else if (messageType == Constants.MSG_END_GAME) {
					unregisterReceiver();
					ShowCardsActivity.this.setResult(RESULT_OK);
					finish();
				} else {
					if (playerController == null) {
						setupGame();
					}

					// Give it up to the player controller to deal with
					playerController.handleBroadcastReceive(context, intent);
				}
			}
		};
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		receiver = getBroadCastReceiver();
		setContentView(R.layout.gameboardplayer);
		initUIElements();

		sharedPreferences = getSharedPreferences(PREFERENCES, 0);

		// Get the image to use for the back of a card
		CARD_BACK = sharedPreferences.getInt(Constants.PREF_CARD_BACK, R.drawable.back_blue_1);

		// Register the receiver for message/state change intents
		registerReceiver(receiver, new IntentFilter(ConnectionConstants.MESSAGE_RX_INTENT));

		// Get an instance of the ConnectionClient so that we can
		// send messages back to the tablet
		connection = ConnectionClient.getInstance(this);

		// Start the connection screen from here so that we can register the message receive
		// broadcast receiver so that we don't miss any messages
		Intent i = new Intent(this, ConnectActivity.class);
		startActivityForResult(i, CONNECT_DEVICE);
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == QUIT_GAME && resultCode == RESULT_OK) {
			// Finish this activity - if everything goes right, we
			// should be back at the main menu
			this.unregisterReceiver();
			setResult(RESULT_OK);
			finish();
		} else if (requestCode == DISCONNECTED) {
			// Whatever result we get from the disconnected activity,
			// just finish this activity since they will need to reconnect anyways.
			setResult(RESULT_CANCELED);
			finish();
		} else if (requestCode == CONNECT_DEVICE) {
			// If the user cancelled the device list, then bring them back to the main menu
			if (resultCode == RESULT_CANCELED) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				setupGame();

				// Register the state change receiver
				registerReceiver(receiver, new IntentFilter(ConnectionConstants.STATE_CHANGE_INTENT));
			}
		} else if (requestCode == PAUSE_GAME && resultCode == RESULT_CANCELED ) {
			// Finish this activity - if everything goes right, we
			// should be back at the main menu
			setResult(RESULT_OK);
			finish();
		} else {
			// If it isn't anything we know how to handle, pass it on to the
			// playerController to try and handle it
			// Quit game and replay will be handled here
			playerController.handleActivityResult(requestCode, resultCode, data);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Setup the game information
	 */
	private void setupGame() {
		synchronized (this) {
			if (playerController == null) {
				// Initialize the buttons
				ViewStub buttonLayout = (ViewStub) findViewById(R.id.playerHandButtonView);
				buttonLayout.setLayoutResource(GameFactory.getPlayerButtonViewLayout(this));
				buttonLayout.inflate();

				// Get the player controller instance
				playerController = GameFactory.getPlayerControllerInstance(this);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.activities.GameViewActivity#updateNamesOnGameboard()
	 */
	@Override
	public void updateNamesOnGameboard() {
		PlayerState pStateFull = playerController.getPlayerState();

		int curPlayerIndex = 0;
		for (int i = 0; i < 4; i++) {
			curPlayerIndex = (4 + i - pStateFull.playerIndex) % 4;
			if (pStateFull.playerNames[curPlayerIndex] != null) {
				playerTextViews[i].setVisibility(View.VISIBLE);
				playerTextViews[i].setText(pStateFull.playerNames[curPlayerIndex]);
			} else {
				playerTextViews[i].setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * Set the selected card. This will highlight the selected
	 * card, and clear the highlight from any other cards.
	 *
	 * @param cardId - the currently selected card
	 */
	public void setSuggested(int cardId) {
		for (Card c : playerController.getPlayerState().cards) {
			if (c.getIdNum() == cardId) {
				ImageView iv = (ImageView) findViewById(c.getIdNum());
				iv.setBackgroundColor(getResources().getColor(R.color.gold));
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.worthwhilegames.cardgames.shared.activities.GameViewActivity#updateUi()
	 */
	@Override
	public void updateUi() {
		PlayerState pStateFull = playerController.getPlayerState();

		// Place images for all player's cards
		for (int i = 0; i < Constants.DEFAULT_MAX_PLAYERS; i++) {

			playerLinearLayouts[i].removeAllViews();

			/*
			 * curPlayerIndex is the index of the player in pStateFull based on this players perspective
			 * i is the view that we are updating
			 */
			int curPlayerIndex = (i + 4 - pStateFull.playerIndex) % 4;

			if(i > 0){
				// Display for other players
				for (int j = 0; j < pStateFull.numCards[curPlayerIndex]; j++) {
					ImageView image = new ImageView(this);
					image.setId(CARD_BACK);
					image.setScaleType(ScaleType.FIT_CENTER);
					int resId = CARD_BACK;

					int cardsToDisplay = pStateFull.numCards[curPlayerIndex];
					if (cardsToDisplay > maxDisplayed[i]) {
						cardsToDisplay = maxDisplayed[i];
					}

					// Scale card
					Bitmap scaledCard = scaleCard(resId, (j < (cardsToDisplay - 1)) ? fourthCard : halfCard);
					image.setImageBitmap(scaledCard);

					// Check for max displayed
					if (j < maxDisplayed[i]) {
						playerLinearLayouts[i].addView(image, cardParams);
						playerRemainingCards[i].setVisibility(View.INVISIBLE);
					} else {
						// Display how many cards are remaining that aren't displayed
						playerRemainingCards[i].setText("+" + Math.abs(maxDisplayed[i] - pStateFull.cards.size()));
						playerRemainingCards[i].setVisibility(View.VISIBLE);
						break;
					}
				}
			} else {
				// Sort the cards
				Collections.sort(pStateFull.cards);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, cardHeight);
				int j = 0;
				// Display for this player
				for (Card c : pStateFull.cards) {
					ImageView image = new ImageView(this);
					image.setId(c.getIdNum());
					image.setImageBitmap(scaleCard(c.getResourceId(), fullCard));

					// Set the onClick Listener for selecting this card
					image.setOnClickListener(playerController.getCardClickListener());

					// Add a 5px border around the image
					image.setPadding(5, 5, 5, 5);

					// Check for max displayed
					if (j < maxDisplayed[i]) {
						playerLinearLayouts[i].addView(image, params);
						playerRemainingCards[i].setVisibility(View.INVISIBLE);
					} else {
						// Display how many cards are remaining that aren't displayed
						playerRemainingCards[i].setText("+" + Math.abs(maxDisplayed[i] - pStateFull.cards.size()));
						playerRemainingCards[i].setVisibility(View.VISIBLE);
						break;
					}
					j++;
				}
			}
		}

		// Set all the cards in the center of the screen
		for (int j = 0; j < 4; j++) {
			int curPlayerIndex = (j + 4 - pStateFull.playerIndex) % 4;

			Card c = pStateFull.cardsPlayed[curPlayerIndex];
			if (!c.isNullCard()) {
				Bitmap scaledCard = scaleCard(c.getResourceId(), fullCard);

				centerCards[j].setImageBitmap(scaledCard);
				centerCards[j].setVisibility(View.VISIBLE);
			} else {
				centerCards[j].setVisibility(View.INVISIBLE);
			}
		}

		// Update suit and highlight whose turn it is
		updateSuit(pStateFull.suitDisplay);
		highlightPlayer(pStateFull.whoseTurn);

		// TODO Set up buttons enabled or not
	}

}
