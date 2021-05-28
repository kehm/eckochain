/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Class for access policies
 */
public class Policy {

    private String policyId;
    private License license;
    private String terms; // overrides license
    private String createdAt;

    /**
     * Constructor for Policy object
     */
    public Policy() {
    }

    /**
     * Get policy ID
     *
     * @return Policy ID
     */
    public String getPolicyId() {
        return policyId;
    }

    /**
     * Set policy ID
     *
     * @param policyId Policy ID
     */
    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    /**
     * Get license
     *
     * @return License
     */
    public License getLicense() {
        return license;
    }

    /**
     * Set license
     *
     * @param license License
     */
    public void setLicense(License license) {
        this.license = license;
    }

    /**
     * Get terms
     *
     * @return Terms
     */
    public String getTerms() {
        return terms;
    }

    /**
     * Set terms
     *
     * @param terms Terms
     */
    public void setTerms(String terms) {
        this.terms = terms;
    }

    /**
     * Get created at timestamp
     *
     * @return Created at timestamp
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Set created at timestamp
     *
     * @param createdAt Created at timestamp
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
