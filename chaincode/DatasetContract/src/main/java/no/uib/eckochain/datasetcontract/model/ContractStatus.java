/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Enum class for contract status constants
 */
public enum ContractStatus {

    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private final String contractStatus;

    ContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    @Override
    public String toString() {
        return this.contractStatus;
    }
}
