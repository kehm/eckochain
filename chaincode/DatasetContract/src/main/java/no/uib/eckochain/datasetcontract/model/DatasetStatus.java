/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Enum class for dataset status constants
 */
public enum DatasetStatus {

    ACTIVE("ACTIVE"),
    REMOVED("REMOVED");

    private final String datasetStatus;

    DatasetStatus(String datasetStatus) {
        this.datasetStatus = datasetStatus;
    }

    @Override
    public String toString() {
        return this.datasetStatus;
    }
}
