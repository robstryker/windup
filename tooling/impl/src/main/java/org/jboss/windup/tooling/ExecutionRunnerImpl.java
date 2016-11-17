package org.jboss.windup.tooling;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.forge.furnace.util.Lists;
import org.jboss.windup.config.SkipReportsRenderingOption;
import org.jboss.windup.exec.WindupProcessor;
import org.jboss.windup.exec.configuration.WindupConfiguration;
import org.jboss.windup.graph.GraphContext;
import org.jboss.windup.graph.GraphContextFactory;
import org.jboss.windup.graph.model.report.IgnoredFileRegexModel;
import org.jboss.windup.graph.service.GraphService;
import org.jboss.windup.rules.apps.java.config.ExcludePackagesOption;
import org.jboss.windup.rules.apps.java.config.ScanPackagesOption;
import org.jboss.windup.rules.apps.java.model.WindupJavaConfigurationModel;
import org.jboss.windup.rules.apps.java.service.WindupJavaConfigurationService;
import org.jboss.windup.util.PathUtil;
import org.jboss.windup.util.exception.WindupException;

/**
 * @author <a href="mailto:jesse.sightler@gmail.com">Jesse Sightler</a>
 */
public class ExecutionRunnerImpl implements ExecutionRunner
{
    @Inject
    private GraphContextFactory graphContextFactory;

    @Inject
    private ToolingXMLService toolingXMLService;

    @Inject
    private WindupProcessor processor;

    

	@Override
	public ExecutionResults execute(ExecutionBuilderSetOptions builder) {
		
		ExecutionBuilderImpl impl = (ExecutionBuilderImpl)builder;
		
        PathUtil.setWindupHome(impl.windupHome);
        WindupConfiguration windupConfiguration = new WindupConfiguration();
        try
        {
            windupConfiguration.useDefaultDirectories();
        }
        catch (IOException e)
        {
            throw new WindupException("Failed to configure windup due to: " + e.getMessage(), e);
        }
        windupConfiguration.addInputPath(impl.input);
        windupConfiguration.setOutputDirectory(impl.output);
        windupConfiguration.setProgressMonitor(new ProgressMonitorWrapper(impl.progressMonitor));
        windupConfiguration.setOptionValue(SkipReportsRenderingOption.NAME, impl.skipReportsRendering);

        Path graphPath = impl.output.resolve(GraphContextFactory.DEFAULT_GRAPH_SUBDIRECTORY);

        Logger globalLogger = Logger.getLogger("");
        WindupProgressLoggingHandler loggingHandler = null;
        if (impl.progressMonitor instanceof WindupToolingProgressMonitor) {
            loggingHandler = new WindupProgressLoggingHandler((WindupToolingProgressMonitor)impl.progressMonitor);
            globalLogger.addHandler(loggingHandler);
        }

        try (final GraphContext graphContext = graphContextFactory.create(graphPath))
        {

            GraphService<IgnoredFileRegexModel> graphService = new GraphService<>(graphContext, IgnoredFileRegexModel.class);
            for (String ignorePattern : impl.ignorePathPatterns)
            {
                IgnoredFileRegexModel ignored = graphService.create();
                ignored.setRegex(ignorePattern);

                WindupJavaConfigurationModel javaCfg = WindupJavaConfigurationService.getJavaConfigurationModel(graphContext);
                javaCfg.addIgnoredFileRegex(ignored);
            }

            windupConfiguration.setOptionValue(ScanPackagesOption.NAME, Lists.toList(impl.includePackagePrefixSet));
            windupConfiguration.setOptionValue(ExcludePackagesOption.NAME, Lists.toList(impl.excludePackagePrefixSet));

            for (Map.Entry<String, Object> option : impl.options.entrySet())
            {
                windupConfiguration.setOptionValue(option.getKey(), option.getValue());
            }
            
            windupConfiguration
                        .setProgressMonitor(new ProgressMonitorWrapper(impl.progressMonitor))
                        .setGraphContext(graphContext);
            
            processor.execute(windupConfiguration);

            return new ExecutionResultsImpl(graphContext, toolingXMLService);
        }
        catch (IOException e)
        {
            throw new WindupException("Failed to instantiate graph due to: " + e.getMessage(), e);
        } finally
        {
            if (loggingHandler != null)
                globalLogger.removeHandler(loggingHandler);
        }
    }

	
	private class ProgressMonitorWrapper implements WindupToolingProgressMonitor {
		private ToolingProgressMonitor delegate;
		public ProgressMonitorWrapper(ToolingProgressMonitor mon) {
			this.delegate = mon;
		}
		@Override
		public void beginTask(String name, int totalWork) {
			delegate.beginTask(name, totalWork);
		}

		@Override
		public void done() {
			delegate.done();
		}

		@Override
		public boolean isCancelled() {
			return delegate.isCancelled();
		}

		@Override
		public void setCancelled(boolean value) {
			delegate.setCancelled(value);
		}

		@Override
		public void setTaskName(String name) {
			delegate.setTaskName(name);
		}

		@Override
		public void subTask(String name) {
			delegate.subTask(name);
		}

		@Override
		public void worked(int work) {
			delegate.worked(work);
		}

		@Override
		public void logMessage(LogRecord logRecord) {
			delegate.logMessage(logRecord);
		}
	}
	
	
	
    private class WindupProgressLoggingHandler extends Handler
    {
        private final WindupToolingProgressMonitor monitor;

        public WindupProgressLoggingHandler(WindupToolingProgressMonitor monitor)
        {
            this.monitor = monitor;
        }

        @Override
        public void publish(LogRecord record)
        {
            if (this.monitor == null)
                return;

            this.monitor.logMessage(record);
        }

        @Override
        public void flush()
        {

        }

        @Override
        public void close() throws SecurityException
        {

        }
    }

}
