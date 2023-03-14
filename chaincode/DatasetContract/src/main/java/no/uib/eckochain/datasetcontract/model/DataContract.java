/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Class for data contracts
 */
public class DataContract {

    private String detailsHash;
    private ContractStatus status;
    private String resolvedAt;

    /**
     * Constructor for DataContract object
     */
    public DataContract() {
    }

    /**
     * Constructor for Contract object
     *
     * @param detailsHash Contract details hash
     * @param status      Contract status
     * @param resolvedAt  Resolved at timestamp
     */
    public DataContract(String detailsHash, ContractStatus status, String resolvedAt) {
        this.detailsHash = detailsHash;
        this.status = status;
        this.resolvedAt = resolvedAt;
    }

    /**
     * Get contract details hash
     *
     * @return Contract details hash
     */
    public String getDetailsHash() {
        return detailsHash;
    }

    /**
     * Set contract details hash
     *
     * @param detailsHash Contract details hash
     */
    public void setDetailsHash(String detailsHash) {
        this.detailsHash = detailsHash;
    }

    /**
     * Get contract status
     *
     * @return Contract status
     */
    public ContractStatus getStatus() {
        return status;
    }

    /**
     * Set contract status
     *
     * @param status Contract status
     */
    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    /**
     * Get resolved at timestamp
     *
     * @return Resolved at timestamp
     */
    public String getResolvedAt() {
        return resolvedAt;
    }

    /**
     * Set resolved at timestamp
     *
     * @param resolvedAt Resolved at timestamp
     */
    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
