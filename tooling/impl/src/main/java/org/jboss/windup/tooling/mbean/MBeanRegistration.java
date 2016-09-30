package org.jboss.windup.tooling.mbean;

import java.lang.management.ManagementFactory;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.jboss.forge.furnace.event.PostStartup;

@Singleton
public class MBeanRegistration {

	private boolean initialized = false;
	
	 public void perform(@Observes PostStartup event) {
		 String addonStarted = event.getAddon().getId().getName();
		 if( "org.jboss.windup:windup-tooling".equals(addonStarted)) {
			 synchronized(this) {
				 boolean shouldInitialize = !getInitialized();
				 if( shouldInitialize) {
					 initialize();
				 }
			 }
		 }
	 }
	 
	 private synchronized boolean getInitialized() {
		 return initialized;
	 }
	 
	 private synchronized void setInitialized(boolean val) {
		 initialized = val;
	 }
	 
	 private void initialize() {
	    	// First register the server mbean, so something can poll and see where we're at
	        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	        ObjectName name;
	        Exception exc = null;
			try {
				name = new ObjectName("org.jboss.windup.tooling:type=Tooling");
		        Tooling mbean = new Tooling(); 
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
			// TODO log the exception
			 setInitialized(true);
	 }
}
