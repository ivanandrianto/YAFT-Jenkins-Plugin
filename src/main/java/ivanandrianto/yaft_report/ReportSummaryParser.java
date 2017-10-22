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

import ivanandrianto.yaft_report.models.Section;
import ivanandrianto.yaft_report.models.Vulnerability;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * JSON Parser Class.
 */
public class ReportSummaryParser {
    private Section section;
    private String jsonPath;

    /**
     * JSON Parser.
     * @param jsonFile
     *          Access path to the json file
     */
    public ReportSummaryParser(final String jsonFile) {
        jsonPath = jsonFile;
    }

    /**
     * Parse a JSON file.
     */
    public void parse() {
        section = new Section();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(jsonPath));
            JSONObject root = (JSONObject) obj;
            System.out.println("Parsing");

            section.setSectionName((String) root.get("time"));
            section.setCount((Long) root.get("count"));
            section.setVulnerabilityCount((Long)
                    root.get("vulnerabilityCount"));
            System.out.println("1." + (String) root.get("time"));
            System.out.println("1." + (Long) root.get("count"));
            System.out.println("1." + (Long) root.get("vulnerabilityCount"));

            JSONArray vulnerabilities = (JSONArray) root.get("vulnerabilities");
            System.out.println("2." + vulnerabilities.size());
            for (int i = 0; i < vulnerabilities.size(); ++i) {
                JSONObject vulnerability = (JSONObject) vulnerabilities.get(i);
                String name = (String) vulnerability.get("name");
                long count = (Long) vulnerability.get("count");
                System.out.println("2." + name + " - " + count);
                Vulnerability v = new Vulnerability(name, count);
                section.addVulnerability(v);
            }

        } catch (Exception ex) {
            Logger.getLogger(ReportSummaryParser.class.getName())
                .log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Return result of the section.
     */
    public Section result() {
        return section;
    }
}
