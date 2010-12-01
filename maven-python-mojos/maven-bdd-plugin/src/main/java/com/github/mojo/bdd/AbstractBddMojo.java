package com.github.mojo.bdd;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Common class for BDD test runs
 * 
 * @author Jacek Furmankiewicz
 */
@EqualsAndHashCode(of = "toolName", callSuper = false)
public abstract class AbstractBddMojo extends AbstractMojo {

	@Getter
	@Setter
	private String toolName;
	@Getter
	@Setter
	private String testReportName;
	@Getter
	@Setter
	private String workingDirectory;
	@Getter
	@Setter
	private String testDirectory;
	@Getter
	@Setter
	private String[] testCommands;

	protected AbstractBddMojo(String toolName, String testReportName, String workingDirectory, String testDirectory,
			String... testCommands) {
		this.toolName = toolName;
		this.testReportName = testReportName;
		this.workingDirectory = workingDirectory;
		this.testDirectory = testDirectory;
		this.testCommands = testCommands;
	}

	/**
	 * Can be overriden in descendants to do Mojo-specific stuff
	 */
	protected void preExecute() throws MojoExecutionException, MojoFailureException {}
	
	/**
	 * Can be overriden in descendants for any post-processing
	 * Only called upon success
	 * @param output
	 */
	protected void postExecute(StringBuilder output) throws MojoExecutionException, MojoFailureException {}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		BddUtils.createReportsFolder();

		try {
			
			preExecute();

			if (new File(testDirectory).exists()) {

				getLog().info("");
				getLog().info("Running " + toolName + " from " + workingDirectory);
				getLog().info("");

				StringBuilder bld = new StringBuilder();

				ProcessBuilder t = new ProcessBuilder(testCommands);
				t.directory(new File(workingDirectory));
				t.redirectErrorStream(true);

				Process pr = t.start();
				int exitCode = pr.waitFor();
				BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line = "";
				while ((line = buf.readLine()) != null) {
					bld.append(line).append("\n");
					getLog().info(line);
				}

				BddUtils.writeReport(testReportName, bld.toString(), getLog());

				if (bld.length() == 0) {
					getLog().warn(toolName + " did not return any output. No unit test(s) found?");
					throw new MojoFailureException(toolName + " did not return any output. No unit test(s) found?");
				}

				if (exitCode != 0) {
					throw new MojoFailureException(toolName + " unit test(s) failed");
				}

			} else {

				getLog().warn("No " + toolName + " unit test(s) found. Please create some in " + testDirectory);
				throw new MojoFailureException("No " + toolName + " unit test(s) found. Please create some in " + testDirectory);

			}
		} catch (MojoFailureException ex) {
			throw ex;
		} catch (MojoExecutionException ex) {
			throw ex;
		} catch (Exception ex) {
			getLog().error(ex);
			throw new MojoExecutionException("Failed to run " + toolName + " unit test(s)", ex);
		} finally {
			// TODO: cleanup
		}

	}

}
