package org.jboss.windup.tooling;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.windup.config.SkipReportsRenderingOption;
import org.jboss.windup.rules.apps.java.config.SourceModeOption;

/**
 * @author <a href="mailto:jesse.sightler@gmail.com">Jesse Sightler</a>
 */
public class ExecutionBuilderImpl implements ExecutionBuilder, ExecutionBuilderSetInput, 
			ExecutionBuilderSetOutput, ExecutionBuilderSetOptions, ExecutionBuilderSetOptionsAndProgressMonitor {

    Path windupHome;
    ToolingProgressMonitor progressMonitor;
    Path input;
    Path output;
    Set<String> ignorePathPatterns = new HashSet<>();
    Set<String> includePackagePrefixSet = new HashSet<>();
    Set<String> excludePackagePrefixSet = new HashSet<>();
    Set<String> userRulesPathSet = new HashSet<>();
    Map<String, Object> options = new HashMap<>();
    boolean skipReportsRendering;

    /**
     * Is the option to skip Report preparing and generation set?
     * 
     * @return the skipReportsRendering
     */
    public boolean isSkipReportsRendering()
    {
        return skipReportsRendering;
    }

    /**
     * Sets the option to skip Report preparing and generation
     * 
     * @param skipReportsRendering the skipReportsRendering to set
     */
    public void setSkipReportsRendering(boolean skipReportsRendering)
    {
        this.skipReportsRendering = skipReportsRendering;
    }

    @Override
    public ExecutionBuilderSetInput begin(Path windupHome)
    {
        this.windupHome = windupHome;
        return this;
    }

    @Override
    public ExecutionBuilderSetOutput setInput(Path input)
    {
        this.input = input;
        return this;
    }

    @Override
    public ExecutionBuilderSetOptionsAndProgressMonitor setOutput(Path output)
    {
        this.output = output;
        return this;
    }

    @Override
    public ExecutionBuilderSetOptions ignore(String ignorePattern)
    {
        this.ignorePathPatterns.add(ignorePattern);
        return this;
    }

    @Override
    public ExecutionBuilderSetOptions includePackage(String packagePrefix)
    {
        this.includePackagePrefixSet.add(packagePrefix);
        return this;
    }

    @Override
    public ExecutionBuilderSetOptions includePackages(Collection<String> includePackagePrefixes)
    {
        if (includePackagePrefixes != null)
            this.includePackagePrefixSet.addAll(includePackagePrefixes);

        return this;
    }

    @Override
    public ExecutionBuilderSetOptions excludePackage(String packagePrefix)
    {
        this.excludePackagePrefixSet.add(packagePrefix);
        return this;
    }

    @Override
    public ExecutionBuilderSetOptions excludePackages(Collection<String> excludePackagePrefixes)
    {
        if (excludePackagePrefixes != null)
            this.excludePackagePrefixSet.addAll(excludePackagePrefixes);
        return this;
    }

    @Override
    public ExecutionBuilderSetOptions setProgressMonitor(ToolingProgressMonitor monitor)
    {
        this.progressMonitor = monitor;
        return this;
    }

    @Override
    public ExecutionBuilderSetOptions sourceOnlyMode()
    {
        options.put(SourceModeOption.NAME, true);
        return this;
    }

    @Override
    public ExecutionBuilderSetOptions skipReportGeneration()
    {
        options.put(SkipReportsRenderingOption.NAME, true);
        return this;
    }

    @Override
    public ExecutionBuilderSetOptions addUserRulesPath(Path rulesPath)
    {
        if (rulesPath == null)
            return this;

        String pathString = rulesPath.normalize().toAbsolutePath().toString();
        this.userRulesPathSet.add(pathString);

        return this;
    }

    @Override
    public ExecutionBuilderSetOptions addUserRulesPaths(Iterable<Path> rulesPaths)
    {
        if (rulesPaths == null)
            return this;

        for (Path rulesPath : rulesPaths) {
            this.addUserRulesPath(rulesPath);
        }

        return this;
    }

    @Override
    public ExecutionBuilderSetOptions setOption(String name, Object value)
    {
        this.options.put(name, value);
        return this;
    }
}
