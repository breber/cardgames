package com.worthwhilegames.cardgames.shared.connection;

/**
 * A wrapper around a multicast DNS service
 *
 * @author breber
 */
public interface IDnsWrapper {

    /**
     * Setup the broadcasting service
     */
    void setup();

    /**
     * Tear down the broadcasting service
     */
    void close();

}
