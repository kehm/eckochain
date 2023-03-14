/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Class to represent file info
 */
public class FileInfo {

    private String fileName;
    private String fileType;
    private String fileHash;

    /**
     * Constructor for Dataset object
     */
    public FileInfo() {
    }

    /**
     * Constructor for FileInfo object
     *
     * @param fileName File name
     * @param fileType File type
     */
    public FileInfo(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }

    /**
     * Get file name
     *
     * @return File name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set file name
     *
     * @param fileName File name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get file type
     *
     * @return File type
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Set file type
     *
     * @param fileType File type
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * Get file hash
     *
     * @return File hash
     */
    public String getFileHash() {
        return fileHash;
    }

    /**
     * Set file hash
     *
     * @param fileHash File hash
     */
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}
