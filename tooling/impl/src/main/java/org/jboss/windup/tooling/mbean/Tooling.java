package org.jboss.windup.tooling.mbean;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.windup.tooling.DefaultToolingXMLService;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.jboss.windup.tooling.ExecutionBuilderSetOptions;
import org.jboss.windup.tooling.ExecutionResults;
import org.jboss.windup.tooling.ExecutionRunner;

public class Tooling implements ToolingMBean {

	@Inject
	private ExecutionBuilder builder;
	@Inject
	private ExecutionRunner runner;

	public String execute(String windupHome, String projectPath, String outputPath, Map<String, Object> options,
			IProgressMonitor monitor) {
		ExecutionBuilderSetOptions options2 = builder.begin(new File(windupHome).toPath())
              .setInput(new File(projectPath).toPath())
              .setOutput(new File(outputPath).toPath());

		// Todo progmon, in non-jmx impl
//              .setProgressMonitor(windupProgressMonitor);
      
      Iterator<String> optsIt = options.keySet().iterator();
      while(optsIt.hasNext()) {
      	String k = optsIt.next();
      	
      	
      	// This API solution is deficient because we are not able to easily send over
      	// other parts of the builder, such as the ignore methods or any of the others. 
      	// This may need to be abstracted out so it could be part of the options map? 
      	
      	// But since this is only proof of concept, it should be ok 
      	
      	options2 = options2.setOption(k, options.get(k));
      	

      	// Conditional implementation, but skipping for now
//      	if( !k.startsWith(IWindupOptions.TOOLING_PREFIX)) {
//      		options2 = options2.setOption(k, options.get(k));
//      	} else {
//      		if( IWindupOptions.T_IGNORE.equals(k)) {
//      			options2 = options2.ignore((String)options.get(k));
//      		}
//      	}
      }
      
      // Hard-coding our common ignore
      options2 = options2.ignore("\\.class$");
      
      
      ExecutionResults results = runner.execute(options2);
      if( results != null ) {
    	  ByteArrayOutputStream os = new ByteArrayOutputStream();
    	  new DefaultToolingXMLService().serializeResults(results, os);
    	  String ret = new String(os.toByteArray());
    	  System.out.println(ret);
    	  return ret;
      }
      return null;
	}
}
