package cs309.a1.crazyeights;

import cs309.a1.shared.CardTranslator;
import cs309.a1.shared.R;

/**
 * The Crazy Eights implementation of a CardTranslator
 */
public class CrazyEightsCardTranslator implements CardTranslator {

	/* (non-Javadoc)
	 * @see cs309.a1.shared.CardTranslator#getResourceForCardWithId(int)
	 */
	@Override
	public int getResourceForCardWithId(int cardId) {
		if (cardId == 0) {
			return R.drawable.clubs_a;
		} else if (cardId == 1) {
			return R.drawable.clubs_2;
		} else if (cardId == 2) {
			return R.drawable.clubs_3;
		} else if (cardId == 3) {
			return R.drawable.clubs_4;
		} else if (cardId == 4) {
			return R.drawable.clubs_5;
		} else if (cardId == 5) {
			return R.drawable.clubs_6;
		} else if (cardId == 6) {
			return R.drawable.clubs_7;
		} else if (cardId == 7) {
			return R.drawable.clubs_8;
		} else if (cardId == 8) {
			return R.drawable.clubs_9;
		} else if (cardId == 9) {
			return R.drawable.clubs_10_;
		} else if (cardId == 10) {
			return R.drawable.clubs_j;
		} else if (cardId == 11) {
			return R.drawable.clubs_q;
		} else if (cardId == 12) {
			return R.drawable.clubs_k;
		} else if (cardId == 13) {
			return R.drawable.diamonds_a;
		} else if (cardId == 14) {
			return R.drawable.diamonds_2;
		} else if (cardId == 15) {
			return R.drawable.diamonds_3;
		} else if (cardId == 16) {
			return R.drawable.diamonds_4;
		} else if (cardId == 17) {
			return R.drawable.diamonds_5;
		} else if (cardId == 18) {
			return R.drawable.diamonds_6;
		} else if (cardId == 19) {
			return R.drawable.diamonds_7;
		} else if (cardId == 20) {
			return R.drawable.diamonds_8;
		} else if (cardId == 21) {
			return R.drawable.diamonds_9;
		} else if (cardId == 22) {
			return R.drawable.diamonds_10;
		} else if (cardId == 23) {
			return R.drawable.diamonds_j;
		} else if (cardId == 24) {
			return R.drawable.diamonds_q;
		} else if (cardId == 25) {
			return R.drawable.diamonds_k;
		} else if (cardId == 26) {
			return R.drawable.hearts_a;
		} else if (cardId == 27) {
			return R.drawable.hearts_2;
		} else if (cardId == 28) {
			return R.drawable.hearts_3;
		} else if (cardId == 29) {
			return R.drawable.hearts_4;
		} else if (cardId == 30) {
			return R.drawable.hearts_5;
		} else if (cardId == 31) {
			return R.drawable.hearts_6;
		} else if (cardId == 32) {
			return R.drawable.hearts_7;
		} else if (cardId == 33) {
			return R.drawable.hearts_8;
		} else if (cardId == 34) {
			return R.drawable.hearts_9;
		} else if (cardId == 35) {
			return R.drawable.hearts_10;
		} else if (cardId == 36) {
			return R.drawable.hearts_j;
		} else if (cardId == 37) {
			return R.drawable.hearts_q;
		} else if (cardId == 38) {
			return R.drawable.hearts_k;
		} else if (cardId == 39) {
			return R.drawable.spades_a;
		} else if (cardId == 40) {
			return R.drawable.spades_2;
		} else if (cardId == 41) {
			return R.drawable.spades_3;
		} else if (cardId == 42) {
			return R.drawable.spades_4;
		} else if (cardId == 43) {
			return R.drawable.spades_5;
		} else if (cardId == 44) {
			return R.drawable.spades_6;
		} else if (cardId == 45) {
			return R.drawable.spades_7;
		} else if (cardId == 46) {
			return R.drawable.spades_8;
		} else if (cardId == 47) {
			return R.drawable.spades_9;
		} else if (cardId == 48) {
			return R.drawable.spades_10;
		} else if (cardId == 49) {
			return R.drawable.spades_j;
		} else if (cardId == 50) {
			return R.drawable.spades_q;
		} else if (cardId == 51) {
			return R.drawable.spades_k;
		} else if (cardId == 52) {
			return R.drawable.joker_b;
		} else if (cardId == 53) {
			return R.drawable.joker_r;
		}

		return 0;
	}

}
