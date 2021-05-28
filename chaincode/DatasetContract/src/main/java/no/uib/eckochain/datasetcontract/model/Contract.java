/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

import java.util.ArrayList;

/**
 * Class for contracts
 */
public class Contract {

    private String detailsHash;
    private ContractStatus status;
    private String resolvedAt;
    private ArrayList<Event> events;

    /**
     * Constructor for Request object
     */
    public Contract() {
        this.events = new ArrayList<>();
    }

    /**
     * Constructor for Contract object
     *
     * @param detailsHash Contract details hash
     * @param status Contract status
     * @param resolvedAt  Resolved at timestamp
     */
    public Contract(String detailsHash, ContractStatus status, String resolvedAt) {
        this.detailsHash = detailsHash;
        this.status = status;
        this.resolvedAt = resolvedAt;
        this.events = new ArrayList<>();
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

    /**
     * Get events log
     *
     * @return Events log
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Set events lot
     *
     * @param events Events log
     */
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
