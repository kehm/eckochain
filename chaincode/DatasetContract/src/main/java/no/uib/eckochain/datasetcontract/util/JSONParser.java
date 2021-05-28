/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.no.uib.eckochain.datasetcontract.model.*;

/**
 * JSON Parser utilities
 */
public class JSONParser {

    /**
     * Get JSON representation of the object
     *
     * @param object Object to parse
     * @return JSON representation of the object
     * @throws JsonProcessingException If cannot parse JSON
     */
    public static String getJSON(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * Get metadata object from JSON string
     *
     * @param json JSON string
     * @return Object
     * @throws JsonProcessingException If cannot parse JSON
     */
    public static Metadata getMetadata(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.readValue(json, Metadata.class);
        return mapper.readValue(json, Metadata.class);
    }

    /**
     * Get policy object from JSON string
     *
     * @param json JSON string
     * @return Object
     * @throws JsonProcessingException If cannot parse JSON
     */
    public static Policy getPolicy(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.readValue(json, Policy.class);
        return mapper.readValue(json, Policy.class);
    }

    /**
     * Get file info object from JSON string
     *
     * @param json JSON string
     * @return Object
     * @throws JsonProcessingException If cannot parse JSON
     */
    public static FileInfo getFileInfo(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.readValue(json, FileInfo.class);
        return mapper.readValue(json, FileInfo.class);
    }
}
