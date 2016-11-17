package org.jboss.windup.tooling;

import org.jboss.windup.exec.WindupProgressMonitor;

/**
 * Allows setting windup options and a progress monitor. 
 * The progress monitor is an instance of {@link ToolingProgressMonitor}, 
 * which is a clone of {@link WindupProgressMonitor} and will be wrapped
 * in the implementation classes of the builder later to be passed
 * to windup properly. 
 *
 * @author <a href="mailto:jesse.sightler@gmail.com">Jesse Sightler</a>
 */
public interface ExecutionBuilderSetOptionsAndProgressMonitor extends ExecutionBuilderSetOptions
{
    /**
     * Sets the callback that will be used for monitoring progress.
     */
    ExecutionBuilderSetOptions setProgressMonitor(ToolingProgressMonitor monitor);
}
