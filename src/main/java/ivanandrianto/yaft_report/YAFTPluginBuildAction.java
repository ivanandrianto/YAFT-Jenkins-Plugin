/*
 * The MIT License
 *
 * Copyright 2017 ivanandrianto.
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

import hudson.FilePath;
import hudson.model.Action;
import hudson.model.DirectoryBrowserSupport;
import hudson.model.Run;
import ivanandrianto.yaft_report.models.Report;
import ivanandrianto.yaft_report.models.ReportInfo;
import hudson.util.HttpResponses;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections4.CollectionUtils;
import org.kohsuke.stapler.StaplerProxy;
import org.xml.sax.SAXException;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
/**
 * Class describing action performed on build page.
 */
public class YAFTPluginBuildAction implements Action,
				Serializable, StaplerProxy {

	/**
	 * Id of the class.
	 */
	private static final long serialVersionUID = 1L;
        private static final String ICON_NAME = "/plugin/yaft_report/icons/"
                + "yaft_report.png";
        private static final String INDEX_FILE = "reports/list.html";
        private static final String TITLE = "YAFT REPORTS";
//        private static final String WEB_RESOURCES_ZIP = "webResources.zip";

	/**
	 * URL to access data.
	 */
	public static final String URL_NAME = "yaftResult";
        private static final String REPORTS_FOLDER_NAME = "reports";
	private Run<?, ?> build;
	private String result;
        private File rootDir;
        private String urlName;
        private final String indexFile;
        private final String iconName;
        private final String title;
	private Report report;
	private ArrayList<ArrayList<String>> fileError;
        private final List<String> safeExtensions;
        private Set<File> safeDirectories;

	/**
	 * Constructor.
	 * @param rootDir
         *            The root dir
	 * @param build
	 *            The current build
	 * @param files
	 *            The current files
         * @param safeExtensions
         *            The safe extensions
	 * @throws InterruptedException
	 *             Interruption
	 * @throws ParserConfigurationException
	 *             Exception in parser configuration
	 * @throws SAXException
	 *             Exception in XML parser
	 * @throws URISyntaxException
	 *             Exception in URL
	 * @throws IOException
	 *             Exception with I/Os
	 */
	public YAFTPluginBuildAction(final File rootDir, final Run<?, ?> build,
			final ArrayList<String> files, final String...
                        safeExtensions)
                        throws InterruptedException,
                        ParserConfigurationException, SAXException,
                        URISyntaxException, IOException {
		this.build = build;
                this.rootDir = rootDir;
                this.urlName = URL_NAME;
                this.indexFile = INDEX_FILE;
                this.iconName = ICON_NAME;
                this.title = TITLE;
		this.report = new Report();
		this.fileError = new ArrayList<ArrayList<String>>();

                this.safeExtensions = unmodifiableList(asList(safeExtensions));
                this.safeDirectories = unmodifiableSet(new HashSet<File>(asList(
                rootDir,
                        new File(rootDir, "css"),
                        new File(rootDir, "fonts"),
                        new File(rootDir, "js"),
                        new File(rootDir, "images")
                )));

                prepareDir();

//		for (int i = 0; i < files.size(); i++) {
                    String currentReport = files.get(0);
                    String path = currentReport + File.separatorChar
                            + "summary.json";

                    ReportSummaryParser parser = new ReportSummaryParser(path);
                    parser.parse();
                    this.report.addSection(parser.result());
                    System.out.println("build URL: " + build.getUrl());
                    this.report.setBuildUrl(build.getUrl());

                    String jenkinsReportsPath = rootDir.getAbsolutePath()
                            + File.separatorChar + REPORTS_FOLDER_NAME;
                    ReportDetailsParser rdhg = new
                            ReportDetailsParser(currentReport,
                            jenkinsReportsPath);
                    rdhg.generateHtmls();
                    ArrayList<ReportInfo> allReportInfo = rdhg
                            .getAllReportInfo();
                    generateReportListPage(allReportInfo);
//		}
	}

        /**
         * Prepare directory.
         */
        private void prepareDir() {
            File folder = new File(rootDir.getAbsolutePath());
            if (!folder.exists()) {
                folder.mkdir();
            }

//            InputStream is = YAFTPluginBuildAction.class
//                    .getResourceAsStream(WEB_RESOURCES_ZIP);
//            ZipInputStream zis = new ZipInputStream(is);
//            ZipEntry entry = null;
//
//            try {
//                while ((entry = zis.getNextEntry()) != null) {
//                    File entryFile = new File(rootDir.getAbsolutePath(),
//                            entry.getName());
//                    if (entry.isDirectory()) {
//                        if (entryFile.exists()) {
//                            System.out.println("Dir already exists");
//                        } else {
//                            System.out.println("Buat directory");
//                            entryFile.mkdirs();
//                        }
//                    } else {
//                        if (entryFile.getParentFile() != null && !entryFile
//                                .getParentFile().exists()) {
//                            entryFile.getParentFile().mkdirs();
//                        }
//
//                        if (!entryFile.exists()) {
//                            entryFile.createNewFile();
//                        }
//
//                        OutputStream os = null;
//                        try {
//                            os = new FileOutputStream(entryFile);
//                            IOUtils.copy(zis, os);
//                            System.out.println("after copy");
//                        } finally {
//                            IOUtils.closeQuietly(os);
//                        }
//                    }
//                }
//
//                zis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        private void generateReportListPage(ArrayList<ReportInfo>
                allReportInfo) {
            String reportListTable = "";
            for (int i = 0; i < allReportInfo.size(); i++) {
                ReportInfo reportInfo = allReportInfo.get(i);
                reportListTable += "<tr><td>";
                reportListTable += reportInfo.getReqeust() + "</td><td>";
                reportListTable += String.valueOf(reportInfo
                        .getIsVulnerabilityFound()) + "</td><td>";
                reportListTable += reportInfo.getVulnerabilityType()
                        + "</td><td>";
                reportListTable += "<a href=\"" + reportInfo.getFileName()
                        + "\">Details</a></td></tr>";
            }

            String template = getReportListTemplateFile();
            if (template == null) {
                return;
            }

            String[] values = {reportListTable};
            template = String.format(template, (Object[]) values);
            String filePath = rootDir.getAbsolutePath() + File.separatorChar
                    + REPORTS_FOLDER_NAME + File.separatorChar + "list.html";
            FileUtil.createNewFile(filePath, template, false);
        }

        /**
        * Get the template file.
        * @return String
        *      The template file content
        */
        private String getReportListTemplateFile() {
           String fileName = "reportListTemplate.html";

           InputStream in = ReportDetailsParser
                   .class.getResourceAsStream(fileName);
           if (in == null) {
               System.out.println("null");
               return null;
           }

           return new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));

        }

	/**
	 * Get Report.
	 */
	public Report getReport() {
		return report;
	}

	/**
	 * Get current build.
	 */
	Run<?, ?> getBuild() {
		return this.build;
	}

	/**
	 * Get Target.
	 */
	public Object getTarget() {
		return this.result;
	}

	/**
	 * Get Project Name.
	 */
	public String getProjectName() {
		String str = build.getParent().getName();
		str = str.replace(".", "dot");
		return str;
	}

	/**
	 * Get Build Number.
	 */
	public int getBuildNumber() {
		return build.getNumber();
	}

	/**
	 * The three functions getIconFileName,getDisplayName,getUrlName create a
	 * link to a new page with url : http://{root}/job/{job name}/URL_NAME for
	 * the page of the build.
	 */
	public String getIconFileName() {
		return ICON_NAME;
	}

	/**
	 * The three functions getIconFileName,getDisplayName,getUrlName create a
	 * link to a new page with url : http://{root}/job/{job name}/URL_NAME for
	 * the page of the build.
	 */
	public String getDisplayName() {
		return TITLE;
	}

        /**
         * Get the root dir.
         * @return File
         *      rootDir
         */
        public File getRootDir() {
            return rootDir;
        }

        /**
         * Set the root dir.
         * @param rootDir
         *      The root dir to set
         */
        public void setRootDir(final File rootDir) {
            this.rootDir = rootDir;
        }

        /**
         * Get the url name.
         * @return
         *      The url name
         */
        public String getUrlName() {
            return urlName;
        }

        /**
         * Set the url name.
         * @param urlName
         *      The url anem to set
         */
        public void setUrlName(final String urlName) {
            this.urlName = urlName;
        }

        private static final class UnsafeDirectoryBrowserSupport
                implements HttpResponse {

            private final File file;

            UnsafeDirectoryBrowserSupport(final File file) {
                this.file = file;
            }

            @Override
            public void generateResponse(final StaplerRequest req, final
                    StaplerResponse rsp, final Object node)
                    throws IOException, ServletException {
                // serve the file without Content-Security-Policy
                long lastModified = file.lastModified();
                long length = file.length();
                try {
                    InputStream in = new FileInputStream(file);
                    rsp.serveFile(req, in, lastModified, -1, length,
                            file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Serve file.
         * @param file
         *         The file
         * @return HttpResponse
         *         The HttpResponse
         * @throws IOException
         *      In case can't read file
         * @throws ServletException
         *      In case Servlet error
         */
        private HttpResponse serveFile(final File file) throws IOException,
                ServletException {
            if (CollectionUtils.isEmpty(safeDirectories)) {
                // this is to keep compatibility with older reports for which
                // the collection might not be initiated
                return new UnsafeDirectoryBrowserSupport(file);
            } else if (safeDirectories.contains(file.getParentFile())) {
                // Reports in safe directories can be trusted and must be served
                // without Content-Security-Policy to display reports properly
                return new UnsafeDirectoryBrowserSupport(file);
            } else {
                // Other can (such as embeddings) can not be trusted,and must
                // be served with Content-Security-Policy
                return new DirectoryBrowserSupport(this, new FilePath(rootDir),
                        title, iconName, false);
            }
        }

        /**
         * Do dynamic.
         * @param req
         *      The request
         * @param rsp
         *      The response
         * @return HttpResponse
         *      HttpResponse
         * @throws IOException
         *      In case can't read file
         * @throws ServletException
         *      In case Servlet error
         */
        public HttpResponse doDynamic(final StaplerRequest req,
            final StaplerResponse rsp)
            throws IOException, ServletException {
            if (req.getRestOfPath().isEmpty()) {
                // serve the index page
                throw HttpResponses.redirectTo(indexFile);
            }

            String fileName = req.getRestOfPath();
            if (fileName.startsWith("/")) {
                fileName = fileName.substring(1);
            }

            File file = new File(getRootDir(), fileName);

            if (!new File(getRootDir(), fileName).exists()) {
                throw HttpResponses.notFound();
            }
            return serveFile(file);
        }
}
