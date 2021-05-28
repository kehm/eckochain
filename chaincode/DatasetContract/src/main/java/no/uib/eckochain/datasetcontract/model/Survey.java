/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

public enum Survey {

    ORIGINAL("ORIGINAL"),
    RESURVEY("RESURVEY"),
    COMBINATION("COMBINATION");

    private final String survey;

    Survey(String survey) {
        this.survey = survey;
    }

    @Override
    public String toString() {
        return this.survey;
    }
}
