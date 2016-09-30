package org.jboss.windup.bootstrap.server;

import org.jboss.windup.bootstrap.BootstrapServer;

public class WindupServer implements WindupServerMBean {
	@Override
	public boolean shutdownServer() {
		BootstrapServer server = BootstrapServer.getBootstrap();
		if( server != null ) {
			server.shutdown();
			return true;
		}
		return false;
	}

	@Override
	public boolean started() {
		BootstrapServer server = BootstrapServer.getBootstrap();
		if( server != null ) {
			return server.getStarted();
		}
		return false;
	}
}
