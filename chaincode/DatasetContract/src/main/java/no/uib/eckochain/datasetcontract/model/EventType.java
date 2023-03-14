/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Enum class for event types
 */
public enum EventType {

    ACCEPT_CONTRACT("ACCEPT_CONTRACT"),
    REJECT_CONTRACT("REJECT_CONTRACT"),
    REMOVE_FILE("REMOVE_FILE"),
    CHANGE_POLICY("CHANGE_POLICY"),
    CHANGE_METADATA("CHANGE_METADATA");

    private final String eventType;

    EventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return this.eventType;
    }
}
