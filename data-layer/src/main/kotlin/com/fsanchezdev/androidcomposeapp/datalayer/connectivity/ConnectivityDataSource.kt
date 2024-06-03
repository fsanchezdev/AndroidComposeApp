package com.fsanchezdev.androidcomposeapp.datalayer.connectivity

/**
 * This interface represents the contract to be complied by an entity to fit in as the connectivity
 * state provider
 */
public interface ConnectivityDataSource {
    /**
     * Returns the current state of the connection availability
     */
    public fun checkNetworkConnectionAvailability(): Boolean
}
