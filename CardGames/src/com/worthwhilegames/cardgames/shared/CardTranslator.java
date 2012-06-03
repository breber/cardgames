package com.worthwhilegames.cardgames.shared;

/**
 * An interface that will allow games to translate a card id
 * to an image resource.
 * 
 * This is necessary because of the shared project setup. The two
 * projects have access to the same resources, but each project generates
 * its own resource ids, which will possibly not match what the other
 * project generates. So we can't just send resource ids back and forth.
 * We will need to come up with our own system.
 */
public interface CardTranslator {

	/**
	 * Get the appropriate resource id for the card with the
	 * given id.
	 * 
	 * @param id the id of the card
	 * @return the resource id to display
	 */
	int getResourceForCardWithId(int id);
}
