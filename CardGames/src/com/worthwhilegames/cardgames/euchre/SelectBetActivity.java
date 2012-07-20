package com.worthwhilegames.cardgames.euchre;

import static com.worthwhilegames.cardgames.euchre.EuchreConstants.BET;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.BET_ROUND;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.FIRST_ROUND_BETTING;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.GO_ALONE;
import static com.worthwhilegames.cardgames.euchre.EuchreConstants.TRUMP;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_CLUBS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_DIAMONDS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_HEARTS;
import static com.worthwhilegames.cardgames.shared.Constants.SUIT_SPADES;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.player.activities.SelectSuitActivity;
import com.worthwhilegames.cardgames.shared.Button;


public class SelectBetActivity extends Activity {

	/**
	 * intent code for choosing suit
	 */
	private static final int CHOOSE_SUIT = Math.abs("CHOOSE_SUIT".hashCode());

	/**
	 * This intent will be set to the result of this activity bet object will
	 * be returned
	 */
	private Intent returnIntent = new Intent();

	/**
	 * the trump suit
	 */
	private int trump;

	/**
	 * the round of betting
	 */
	private int round;

	/**
	 * The trump suit button
	 */
	private ImageView trumpSuit;


	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO: fix this! All the functionality in this Activity
		//       should be moved to the EuchrePlayerController since
		//       these buttons are now displayed on the ShowCardsActivity
		setContentView(R.layout.euchrebuttons);

		trumpSuit = (ImageView) findViewById(R.id.betTrumpSuit);
		//		TextView title = (TextView) findViewById(R.id.betTitle);
		round = getIntent().getIntExtra(BET_ROUND, FIRST_ROUND_BETTING);
		if( round == FIRST_ROUND_BETTING ){
			trump = getIntent().getIntExtra(TRUMP, SUIT_SPADES);
			trumpSuit.setEnabled(false);
			//			title.setText(R.string.bet_round_one_title);
		} else {
			trump = -1;
			trumpSuit.setEnabled(true);
			//			title.setText(R.string.bet_round_two_title);
		}
		updateTrumpSuit();

		// Set the listener for the spade button
		Button pass = (Button) findViewById(R.id.passOption);
		pass.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				returnIntent.putExtra(TRUMP, SUIT_SPADES);
				returnIntent.putExtra(BET, false);
				returnIntent.putExtra(GO_ALONE, false);
				SelectBetActivity.this.setResult(RESULT_OK, returnIntent);
				SelectBetActivity.this.finish();
			}
		});

		// Set the listener for the heart button
		Button bet = (Button) findViewById(R.id.betOption);
		bet.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				if(trump == -1){
					ScaleAnimation scale = new ScaleAnimation((float) 1.1, (float) 1.1,	(float) 1.2, (float) 1.2);
					scale.scaleCurrentDuration(5);
					trumpSuit.startAnimation(scale);
					return;
				}
				returnIntent.putExtra(TRUMP, trump);
				returnIntent.putExtra(BET, true);
				returnIntent.putExtra(GO_ALONE, false);
				SelectBetActivity.this.setResult(RESULT_OK, returnIntent);
				SelectBetActivity.this.finish();
			}
		});

		// Set the listener for the club button
		Button goAlone = (Button) findViewById(R.id.goAloneOption);
		goAlone.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				if(trump == -1){
					ScaleAnimation scale = new ScaleAnimation((float) 1.1, (float) 1.1,	(float) 1.2, (float) 1.2);
					scale.scaleCurrentDuration(5);
					trumpSuit.startAnimation(scale);
					return;
				}
				returnIntent.putExtra(TRUMP, trump);
				returnIntent.putExtra(BET, true);
				returnIntent.putExtra(GO_ALONE, true);
				SelectBetActivity.this.setResult(RESULT_OK, returnIntent);
				SelectBetActivity.this.finish();
			}
		});

		trumpSuit.setOnClickListener(new OnClickListener() {
			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				if( round == FIRST_ROUND_BETTING ){
					//do nothing
				} else {
					v.setEnabled(false);
					Intent selectSuit = new Intent(SelectBetActivity.this, SelectSuitActivity.class);
					SelectBetActivity.this.startActivityForResult(selectSuit, CHOOSE_SUIT);
					v.setEnabled(true);
				}
			}
		});

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		//try to pass??
		returnIntent.putExtra(TRUMP, SUIT_SPADES);
		returnIntent.putExtra(BET, false);
		returnIntent.putExtra(GO_ALONE, false);
		SelectBetActivity.this.setResult(RESULT_OK, returnIntent);
		SelectBetActivity.this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHOOSE_SUIT) {
			trump = resultCode;
			updateTrumpSuit();
		}

	}

	private void updateTrumpSuit(){
		switch(trump){

		case SUIT_CLUBS:
			trumpSuit.setBackgroundResource(R.drawable.clubsuitimage);
			break;
		case SUIT_DIAMONDS:
			trumpSuit.setBackgroundResource(R.drawable.diamondsuitimage);
			break;
		case SUIT_HEARTS:
			trumpSuit.setBackgroundResource(R.drawable.heartsuitimage);
			break;
		case SUIT_SPADES:
			trumpSuit.setBackgroundResource(R.drawable.spadesuitimage);
			break;
		case (-1):
		default:
			//TODO set this to  ? image
			trumpSuit.setBackgroundResource(R.drawable.spadesuitimage);
			break;
		}
	}


}
