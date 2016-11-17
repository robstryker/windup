package org.jboss.windup.tooling.mbean;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.windup.tooling.ExecutionResults;

public interface ToolingMBean {
	
	/**
	 * Return an xml string representation of {@link ExecutionResults}
	 * 
	 * @param windupHome
	 * @param projectPath
	 * @param outputPath
	 * @param options
	 * @param monitor
	 * @return
	 */
	public String execute(String windupHome, String projectPath, String outputPath, Map<String, Object> options,
			IProgressMonitor monitor);
}
