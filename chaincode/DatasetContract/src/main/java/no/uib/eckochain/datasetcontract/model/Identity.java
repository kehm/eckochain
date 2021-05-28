/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Class representing a user's identity
 */
public class Identity {

    private String id;
    private String mspId;

    /**
     * Constructor for Identity object
     */
    public Identity() {
    }

    /**
     * Constructor for Identity object
     *
     * @param id User ID
     * @param mspId MSP ID
     */
    public Identity(String id, String mspId) {
        this.id = id;
        this.mspId = mspId;
    }

    /**
     * Get rights-holder ID
     *
     * @return Rights-holder ID
     */
    public String getId() {
        return id;
    }

    /**
     * Set rights-holder ID
     *
     * @param id User ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get MSP ID
     *
     * @return MSP ID
     */
    public String getMspId() {
        return mspId;
    }

    /**
     * Set MSP ID
     *
     * @param mspId MSP ID
     */
    public void setMspId(String mspId) {
        this.mspId = mspId;
    }
}
