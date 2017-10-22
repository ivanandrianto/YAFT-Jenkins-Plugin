/*
 * The MIT License
 *
 * Copyright (c) 2012, Thomas Deruyter, Raynald Briand
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ivanandrianto.yaft_report;

import hudson.AbortException;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import jenkins.tasks.SimpleBuildStep;

import java.io.File;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.Project;

import org.kohsuke.stapler.DataBoundConstructor;
import org.xml.sax.SAXException;

/**
 * YAFTPluginPublisher Is the main class for
 * publishing data over build & project page.
 */
@SuppressWarnings("unchecked")
public class YAFTPluginPublisher extends Recorder implements SimpleBuildStep {

	/* The list name of the files to parse */
	private String name;

        /* The directory of YAFT */
	private String directory;

	/**
        * Whether the reports are shown on the project page or not.
        * By default, the reports are shown on the project page as
        * well as on the results page. When the reports are very long
        * the user can chose to display them only on the results page.
        */
       private boolean shownOnProjectPage = true;

       /**
	 * Set the name of the publisher.
	 * @param name
	 * 		The name of the published report
         * @param directory
	 * 		The directory of YAFT
	 * @param shownOnProjectPage
	 * 		Whether or not to display the report on the Project page
	 */
	@DataBoundConstructor
       public YAFTPluginPublisher(final String name, final String directory,
               final boolean shownOnProjectPage) {
                this.name = name;
                this.directory = directory;
                this.shownOnProjectPage = shownOnProjectPage;
	}

	/**
	 * Get the name of the publisher.
	 * @return string
	 * 		The name of the published report
	 */
	public String getName() {
		return name;
	}

        /**
	 * Get the directory of YAFT.
	 * @return string
	 * 		The absolute path to YAFT
	 */
	public String getDirectory() {
		return directory;
	}

	/**
        * Get whether or not to display the report on the Project page.
        * @return boolean
        *      Whether or not to display the report on the Project page
        */
       public boolean isShownOnProjectPage() {
               return shownOnProjectPage;
       }

       /**
	 * Get the action associated to the publisher.
	 * @param project
	 * 		Project on which to apply publication
	 */
	@Override
	public Action getProjectAction(final AbstractProject<?, ?> project) {
		return new YAFTPluginProjectAction(project);
	}

	/**
	 * Perform the publication.
	 * @param build
	 * 		Build on which to apply publication
	 * @param workspacePath
	 *      Unused
	 * @param launcher
	 * 		Unused
	 * @param listener
	 * 		Unused
	 * @throws IOException
	 * 		In case of file IO mismatch
	 * @throws InterruptedException
	 * 		In case of interuption
	 */
	public void perform(final Run<?, ?> build, final FilePath workspacePath,
			final Launcher launcher, final TaskListener listener)
			throws InterruptedException, IOException {

		/**
		 * Define if we must parse multiple file by searching for ','
		 * in the name var.
		 */
		String[] files = name.split(",");

//                System.out.println("directory: " + directory);
//                File yaftDirectory = new File(directory);
//                ArrayList<String> fileNames = new ArrayList<String>();
//                if (yaftDirectory.exists()) {
//                    File[] fileList = yaftDirectory.listFiles();
//                    for (int i = 0; i < fileList.length; i++) {
//                        String fileName = fileList[i].getName();
//                        System.out.println(fileName);
//                        fileNames.add(fileName);
//                    }
//                }
//                System.out.println(fileNames);

		ArrayList<String> outputFiles = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			FileSet fileSet = new FileSet();
			File workspace = build.getArtifactsDir();
			fileSet.setDir(workspace);
			fileSet.setIncludes(files[i].trim());
			Project antProject = new Project();
			fileSet.setProject(antProject);
			String[] tmpFiles = new String[] {};
			try {
				tmpFiles = fileSet.getDirectoryScanner(antProject)
					.getIncludedFiles();
			} catch (Exception ex) {
				listener.getLogger().println(ex.toString());
				throw new AbortException(ex.getMessage());
			}
			for (int j = 0; j < tmpFiles.length; j++) {
				outputFiles.add(tmpFiles[j]);
			}

		}

                ArrayList<String> reportPaths = new ArrayList<String>();
                for (int i = 0; i < outputFiles.size(); i++) {
                        String filePath = build.getArtifactsDir()
                                .getAbsolutePath() + File.separatorChar
                                + outputFiles.get(i);
                        System.out.println("filePath: " + filePath);
                        String path = directory + File.separatorChar
                                + FileUtil.read(filePath)
                                .trim();
                        System.out.println("path: " + path);
                        reportPaths.add(path);
                }

		try {
			YAFTPluginBuildAction buildAction;
			buildAction = new YAFTPluginBuildAction(new File(
                                build.getRootDir(), YAFTPluginBuildAction
                                .URL_NAME), build, reportPaths);
			build.addAction(buildAction);
		} catch (ParserConfigurationException ex) {
			listener.getLogger().println(ex.toString());
			throw new AbortException(ex.getMessage());
		} catch (SAXException ex) {
			listener.getLogger().println(ex.toString());
			throw new AbortException(ex.getMessage());
		} catch (URISyntaxException ex) {
			listener.getLogger().println(ex.toString());
			throw new AbortException(ex.getMessage());
		}
	}

	/**
	 * Method that returns the status of the service required.
	 */
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
}
