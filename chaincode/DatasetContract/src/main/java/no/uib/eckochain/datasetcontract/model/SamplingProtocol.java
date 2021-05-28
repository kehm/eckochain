/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

public enum SamplingProtocol {

    PLOTS("PLOTS"),
    TRANSECT("TRANSECT"),
    OTHER("OTHER");

    private final String samplingProtocol;

    SamplingProtocol(String samplingProtocol) {
        this.samplingProtocol = samplingProtocol;
    }

    @Override
    public String toString() {
        return this.samplingProtocol;
    }
}
