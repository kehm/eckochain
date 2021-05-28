/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Class for contract details. Contracts details are stored off-chain.
 */
public class ContractDetails {

    private String datasetId;
    private Policy policy;
    private String proposal; // intended use (or 'LICENSED')
    private String createdBy;
    private String createdAt;

    /**
     * Constructor for ContractDetails object
     */
    public ContractDetails() {
    }

    /**
     * Constructor for ContractDetails object
     *
     * @param datasetId  Dataset ID
     */
    public ContractDetails(String datasetId) {
        this.datasetId = datasetId;
    }

    /**
     * Constructor for ContractDetails object
     *
     * @param datasetId  Dataset ID
     * @param proposal    The intended use
     */
    public ContractDetails(String datasetId, String proposal) {
        this.datasetId = datasetId;
        this.proposal = proposal;
    }

    /**
     * Get dataset ID
     *
     * @return Dataset ID
     */
    public String getDatasetId() {
        return datasetId;
    }

    /**
     * Set dataset ID
     *
     * @param datasetId Dataset ID
     */
    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    /**
     * Get policy
     *
     * @return Policy
     */
    public Policy getPolicy() {
        return policy;
    }

    /**
     * Set policy
     *
     * @param policy Policy
     */
    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    /**
     * Get proposal
     *
     * @return Proposal
     */
    public String getProposal() {
        return proposal;
    }

    /**
     * Set proposal
     *
     * @param proposal Proposal
     */
    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    /**
     * Get created by
     *
     * @return Created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Set created by
     *
     * @param createdBy Created by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
