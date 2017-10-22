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
package ivanandrianto.yaft_report.models;

/**
 * ReportInfo Class.
 * @author ivanandrianto
 */
public class ReportInfo {
    private String request;
    private boolean isVulnerabilityFound;
    private String vulnerabilityType;
    private String fileName;

    /**
     * Constructor.
     */
    public ReportInfo() {

    }

    /**
     * Constructor.
     * @param request
     *      Request
     * @param isVulnerabilityFound
     *      Is vulnerability found
     * @param vulnerabilityType
     *      The vulnerability type
     * @param fileName
     *      The file name of detailed report
     */
    public ReportInfo(String request, boolean isVulnerabilityFound,
            String vulnerabilityType, String fileName) {
        this.request = request;
        this.isVulnerabilityFound = isVulnerabilityFound;
        this.vulnerabilityType = vulnerabilityType;
        this.fileName = fileName;
    }

    /**
     * Set the request.
     * @param request
     *      The request
     */
    public void setRequest(String request) {
        this.request = request;
    }

    /**
     * Get the request.
     * @return String
     *      The request
     */
    public String getReqeust() {
        return request;
    }

    /**
     * Set whether or not vulnerability found.
     * @param isVulnerabilityFound
     *      Is vulnerability found
     */
    public void setIsVulnerabilityFound(boolean isVulnerabilityFound) {
        this.isVulnerabilityFound = isVulnerabilityFound;
    }

    /**
     * Get whether or not vulnerability found.
     * @return boolean
     *      Is vulnerability found
     */
    public boolean getIsVulnerabilityFound() {
        return isVulnerabilityFound;
    }

    /**
     * Set the vulnerability type.
     * @param vulnerabilityType
     *      The vulnerability type
     */
    public void setVulnerabilityType(String vulnerabilityType) {
        this.vulnerabilityType = vulnerabilityType;
    }

    /**
     * Get the vulnerability type.
     * @return String
     *      The vulnerability type
     */
    public String getVulnerabilityType() {
        return vulnerabilityType;
    }

    /**
     * Set the vulnerability file name.
     * @param fileName
     *      The file name of detailed report
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the file name.
     * @return String
     *      The file name of detailed report
     */
    public String getFileName() {
        return fileName;
    }
}
