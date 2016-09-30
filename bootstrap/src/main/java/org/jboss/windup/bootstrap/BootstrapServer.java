/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.windup.bootstrap;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Semaphore;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.jboss.windup.bootstrap.server.*;

/**
 * A class with a main method to bootstrap Windup and leave it running.
 */
public class BootstrapServer extends Bootstrap {
	private static BootstrapServer bootstrap;
	public static BootstrapServer getBootstrap() {
		return bootstrap;
	}
    public static void main(final String[] args)
    {
    	
    	bootstrap = new BootstrapServer();

    	// First register the server mbean, so something can poll and see where we're at
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
        ObjectName name;
        Exception exc = null;
		try {
			name = new ObjectName("org.jboss.windup.bootstrap.server:type=WindupServer");
	        WindupServer mbean = new WindupServer(); 
			mbs.registerMBean(mbean, name);
		} catch (MalformedObjectNameException e) {
			exc = e;
		} catch (InstanceAlreadyExistsException e) {
			exc = e;
		} catch (MBeanRegistrationException e) {
			exc = e;
		} catch (NotCompliantMBeanException e) {
			exc = e;
		}    	
    	
		// Now let the superclass do the heavy lifting
    	mainImpl(args, false, bootstrap);

		// If there was an error registering the shutdown, terminate now
		if( exc != null ) {
			exc.printStackTrace();
        	bootstrap.stop();
        	return;
		}
    	
		// Polls on the mbean will now return true
    	bootstrap.setStarted(true);

		
		// Otherwise wait for ever
        System.out.println("Waiting forever..."); 
        try {
        	bootstrap.terminateSemaphore.acquire();
        } catch(InterruptedException ie) {
        	System.out.println("interrupted");
        }

    	// If interrupted somehow, terminate
    	bootstrap.stop();
    }
    
    private Semaphore terminateSemaphore = new Semaphore(0);
    private boolean started;
    public BootstrapServer() {
    	super();
    	started = false;
    }
    public void shutdown() {
    	System.out.println("Shutdown called");
    	terminateSemaphore.release();
    }
    public synchronized boolean getStarted() {
    	return started;
    }
    public synchronized void setStarted(boolean val) {
    	started = val;
    }
}
