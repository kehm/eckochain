/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

/**
 * Enum class for licenses
 */
public enum License {

    CCBY40("CC BY 4.0"),
    CCBYSA40("CC BY-SA 4.0"),
    CCBYND40("CC BY-ND 4.0"),
    CCBYNC40("CC BY-NC 4.0"),
    CCBYNCSA40("CC BY-NC-SA 4.0"),
    CCBYNCND40("CC BY-NC-ND 4.0");

    private final String license;

    License(String license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return this.license;
    }
}
