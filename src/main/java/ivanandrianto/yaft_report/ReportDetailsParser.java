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

import ivanandrianto.yaft_report.models.ReportDetailsContent;
import ivanandrianto.yaft_report.models.ReportInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Report Details Html Generator Class.
 * @author ivanandrianto
 */
public class ReportDetailsParser {
    private String reportDir;
    private String jenkinsReportsDir;
    private ArrayList<ReportInfo> allReportInfo;

    /**
     * Constructor.
     * @param reportDir
     *      Directory of a report session
     * @param jenkinsReportsDir
     *      Directory where html reports created
     */
    public ReportDetailsParser(String reportDir, String
            jenkinsReportsDir) {
        this.reportDir = reportDir;
        this.jenkinsReportsDir = jenkinsReportsDir;
        allReportInfo = new ArrayList<ReportInfo>();
    }

    /**
     * Generate the HTMLs.
     */
    public void generateHtmls() {
        File f = new File(jenkinsReportsDir);
        if (!f.exists()) {
            f.mkdir();
        }

        ArrayList<String> reportFiles = FileUtil.getFileList(reportDir,
                ".json");
        for (int i = 0; i < reportFiles.size(); i++) {
            String fileName = reportFiles.get(i);
            if (!fileName.matches("summary.json")) {
                ReportInfo reportInfo = new ReportInfo();
                if (parseReport(fileName, reportInfo)) {
                    allReportInfo.add(reportInfo);
                }
            }
        }
    }

    /**
     * Parse the report.
     * @param fileName
     *      Name of the report file
     * @param reportInfo
     *      ReportInfo instance
     * @return boolean
     *      Success or not
     */
    private boolean parseReport(String fileName, ReportInfo reportInfo) {
        try {
            String path = reportDir + fileName;
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(path));
            JSONObject root = (JSONObject) obj;
            fileName = fileName.replaceAll("\\.json$", ".html");

            String request = root.containsKey("request")
                    ? (String) root.get("request") : null;
            String response = root.containsKey("response")
                    ? (String) root.get("response") : null;
            String responseBody = root.containsKey("responseBody")
                    ? (String) root.get("responseBody") : null;
            Boolean isAnalyzeSuccess = (Boolean) root
                    .containsKey("isAnalyzeError") ? !(Boolean)
                    root.get("isAnalyzeError") : false;
            Boolean isVulnerabilityFound = root
                    .containsKey("isVulnerabilityFound") ? (Boolean)
                    root.get("isVulnerabilityFound") : false;
            String lastUri = root.containsKey("lastURI")
                    ? (String) root.get("lastURI") : null;
            String description = root.containsKey("description")
                    ? (String) root.get("description") : null;
            String vulnerabilityType = root.containsKey("vulnerability")
                    ? (String) root.get("vulnerability") : "none";

//            String requestLine = request.split("\\n")[0];
            reportInfo.setRequest(replaceNewline(request));
            reportInfo.setIsVulnerabilityFound(isVulnerabilityFound);
            reportInfo.setFileName(fileName);
            reportInfo.setVulnerabilityType(replaceNewline(vulnerabilityType));

//            fileName, request, response, responseBody,
//                    isAnalyzeSuccess, isVulnerabilityFound, vulnerabilityType,
//                    lastUri, description

            ReportDetailsContent rdc = new ReportDetailsContent();
            rdc.setFileName(fileName);
            rdc.setRequest(request);
            rdc.setResponse(response);
            rdc.setResponseBody(responseBody);
            rdc.setIsAnalyzeSuccess(isAnalyzeSuccess);
            rdc.setIsVulnerabilityFound(isVulnerabilityFound);
            rdc.setVulnerabilityType(vulnerabilityType);
            rdc.setLastUri(lastUri);
            rdc.setDescription(description);
            return createHtml(rdc);
        } catch (Exception ex) {
            Logger.getLogger(ReportSummaryParser.class.getName())
                .log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Replace newline with <br>
     * @param str
     * @return String
     *      Return string where newline has been replaced by <br>
     */
    private String replaceNewline(String str) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("\\n", "<br>");
    }

    /**
     * Create the HTML.
     * @param request
     *      Request
     * @param response
     *      Response
     * @param responseBody
     *      Response body
     * @param isAnalyzeSuccess
     *      Is Analyze Error
     * @param isVulnerabilityFound
     *      Is Vulnerability Found
     * @param vulnerabilityType
     *      Vulnerability Type
     * @param lastUri
     *      Last URI
     * @param description
     *      Description
     * @return boolean
     *      Success or not
     */
    private boolean createHtml(ReportDetailsContent rdc) {
        System.out.println("createHtml fileName: " + rdc.getFileName());
        String html = getTemplateFile();
        if (html == null) {
            System.out.println("HTML template is null");
            return false;
        }

        String[] values = {
            replaceNewline(rdc.getRequest()), rdc.getLastUri(),
            rdc.getIsAnalyzeSuccess().toString(),
            rdc.getIsVulnerabilityFound().toString(),
            rdc.getVulnerabilityType(),
            replaceNewline(rdc.getDescription()),
            replaceNewline(rdc.getResponse()), rdc.getResponseBody()
        };

        html = String.format(html, (Object[]) values);
        String reportFilePath = jenkinsReportsDir + File.separatorChar
                + rdc.getFileName();
        FileUtil.createNewFile(reportFilePath, html, false);
        return true;
    }

    /**
     * Get the template file.
     * @return String
     *      The template file content
     */
    private String getTemplateFile() {
        String fileName = "reportTemplate.html";

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
     * Get the short information of each report.
     * @return ArrayList<ReportInfo>
     *      All report info
     */
    public ArrayList<ReportInfo> getAllReportInfo() {
        return allReportInfo;
    }
}
