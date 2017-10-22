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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * FileUtil Class.
 * @author ivanandrianto
 */
final class FileUtil {

    /**
     * Prevent instantiation.
     */
    private FileUtil() {

    }

    /**
     * Read file content.
     * @param path
     *      Path of the file to read
     * @return string
     *      The content of the file
     * @throws IOException
     *      In case something error when reading the file
     */
    public static String read(final String path) throws IOException {
        FileInputStream fstream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String content = "";
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            content += strLine + "\n";
        }
        content = content.substring(0, content.length() - 1);
        br.close();
        return content;
    }

    /**
     * Method ini berfungsi untuk mendapatkan daftar file dalam suatu direktori.
     * Dapat ditambahkan parameter kedua untuk hanya mengambil file dengan
     * format tertentu.
     * @param dirPath
     *      Path menuju direktori
     * @param endsWith
     *      Diakhiri dengan
     * @return ArrayList<String>
     *      Daftar file
     */
    public static ArrayList<String> getFileList(String dirPath,
                String endsWith) {
        File folder = new File(dirPath);
        ArrayList<String> fileNames = new ArrayList<String>();
        if (!folder.exists()) {
            return fileNames;
        }

        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String fileName = files[i].getName();
                if (endsWith != null) {
                    if (fileName.endsWith(endsWith)) {
                        fileNames.add(fileName);
                    }
                }
            }
        }

        return fileNames;
    }

        /**
     * Method ini berfungsi untuk membuat file baru beserta isinya.
     * @param filePath
     *      Path menuju file yang akan dibentuk
     * @param content
     *      Teks yang akan ditambahkan ke file
     * @param format
     *      Apakah perlu diformat
     * @return boolean
     *      Berhasil atau tidak
     */
    public static boolean createNewFile(String filePath,
            String content, boolean format) {
        File file = new File(filePath);
        content = content.replaceAll("\\n", System.lineSeparator());
        if (file.exists()) {
            return false;
        }
        try {
            if (!file.createNewFile()) {
                return false;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(content);
            Files.write(Paths.get(filePath), sb.toString().getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
