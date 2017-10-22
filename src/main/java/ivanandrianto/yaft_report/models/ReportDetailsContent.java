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
 * Report Details Content class.
 * @author ivanandrianto
 */
public class ReportDetailsContent {
    private String fileName;
    private String request;
    private String generationType;
    private String modifications;
    private String response;
    private String responseBody;
    private Boolean isAnalyzeSuccess;
    private Boolean isVulnerabilityFound;
    private String vulnerabilityType;
    private String lastUri;
    private String description;

    /**
     * Constructor.
     */
    public ReportDetailsContent() {

    }

    /**
     * Set the file name.
     * @param fileName
     *      The file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the file name.
     * @return String
     *      The file name
     */
    public String getFileName() {
        return fileName;
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
    public String getRequest() {
        return request;
    }

    /**
     * Set the generation type.
     * @param generationType
     *      The generation type
     */
    public void setGenerationType(String generationType) {
        this.generationType = generationType;
    }

    /**
     * Get the generation type.
     * @return String
     *      The generation type
     */
    public String getGenerationType() {
        return generationType;
    }

    /**
     * Set the modifications.
     * @param modifications
     *      The modifications
     */
    public void setModifications(String modifications) {
        this.modifications = modifications;
    }

    /**
     * Get the modifications.
     * @return String
     *      The modifications
     */
    public String getModifications() {
        return modifications;
    }

    /**
     * Set the response.
     * @param response
     *      The response
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * Get the response.
     * @return String
     *      The response
     */
    public String getResponse() {
        return response;
    }

    /**
     * Set the response.
     * @param responseBody
     *      The responseBody
     */
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    /**
     * Get the responseBody.
     * @return String
     *      The responseBody
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * Set is analyze success.
     * @param isAnalyzeSuccess
     *      Is analyze success
     */
    public void setIsAnalyzeSuccess(Boolean isAnalyzeSuccess) {
        this.isAnalyzeSuccess = isAnalyzeSuccess;
    }

    /**
     * Get is analyze success.
     * @return Boolean
     *      Is analyze success
     */
    public Boolean getIsAnalyzeSuccess() {
        return isAnalyzeSuccess;
    }

    /**
     * Set is vulnerability found.
     * @param isVulnerabilityFound
     *      Is vulnerability found
     */
    public void setIsVulnerabilityFound(Boolean isVulnerabilityFound) {
        this.isVulnerabilityFound = isVulnerabilityFound;
    }

    /**
     * Get is vulnerability found.
     * @return Boolean
     *      Is vulnerability found
     */
    public Boolean getIsVulnerabilityFound() {
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
     * Set the last URI.
     * @param lastUri
     *      The last URI
     */
    public void setLastUri(String lastUri) {
        this.lastUri = lastUri;
    }

    /**
     * Get the last URI.
     * @return String
     *      The last URI
     */
    public String getLastUri() {
        return lastUri;
    }

    /**
     * Set the description.
     * @param description
     *      The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the description.
     * @return String
     *      The description
     */
    public String getDescription() {
        return description;
    }

}
    