package org.jboss.windup.bootstrap.server;


public interface WindupServerMBean { 
	/**
	 * Is the server started completely?
	 * @return
	 */
	public boolean started();
	
	/**
	 * Shutdown the server
	 * @return
	 */
    public boolean shutdownServer(); 
} 
