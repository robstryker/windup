package org.jboss.windup.tooling;

import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;

/**
 * Execute the given execution builder. 
 * 
 * The result object returned contains all {@link Classification}s, {@link Hint}s, 
 * and information about how to find the reports that were produced.
 *
 * @author <a href="mailto:rob@oxbeef.net">Rob Stryker</a>
 */
public interface ExecutionRunner {

    /**
     * Execute the given request on windup.
     */
    ExecutionResults execute(ExecutionBuilderSetOptions builder);
}
