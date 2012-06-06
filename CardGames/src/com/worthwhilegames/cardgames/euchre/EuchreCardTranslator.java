package com.worthwhilegames.cardgames.euchre;

import com.worthwhilegames.cardgames.R;
import com.worthwhilegames.cardgames.shared.CardTranslator;

public class EuchreCardTranslator implements CardTranslator{

	@Override
	public int getResourceForCardWithId(int cardId) {
		if (cardId == 0) {
			return R.drawable.clubs_a;
		} else if (cardId == 8) {
			return R.drawable.clubs_9;
		} else if (cardId == 9) {
			return R.drawable.clubs_10;
		} else if (cardId == 10) {
			return R.drawable.clubs_j;
		} else if (cardId == 11) {
			return R.drawable.clubs_q;
		} else if (cardId == 12) {
			return R.drawable.clubs_k;
		} else if (cardId == 13) {
			return R.drawable.diamonds_a;
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
		} 

		return 0;
	}

}
