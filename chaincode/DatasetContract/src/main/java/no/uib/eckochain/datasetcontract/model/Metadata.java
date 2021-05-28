/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent metadata of a dataset
 */
public class Metadata {

    private String datasetId;
    private String description;
    private Survey survey;
    private String parentEventId;
    private String collectionId;
    private String measurementRemarks;
    private SamplingProtocol samplingProtocol;
    private String samplingProtocolReference;
    private String sampleSizeUnit;
    private String geodeticDatum;
    private String locationRemarks;
    private String institutionId;
    private String[] references;
    private String[] continents;
    private String[] countries;
    private String[] languages;
    private String[] habitats;
    private String[] taxa;
    private int latestYearCollected;
    private int earliestYearCollected;
    private int plotNumber;
    private int speciesNumber;
    private float sampleSizeValue;
    private float[] spatialExtent;
    private Map<String, String> dynamicProperties;
    private Policy policy;
    private Map<String, Contract> contracts;
    private DatasetStatus status;
    private FileInfo fileInfo;
    private String createdAt;
    private String modified;
    private ArrayList<Event> events;

    /**
     * Constructor for Dataset object
     */
    public Metadata() {
        this.contracts = new HashMap<>();
        this.events = new ArrayList<>();
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
     * Get description
     *
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     *
     * @param description Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get survey type
     *
     * @return Survey type
     */
    public Survey getSurvey() {
        return survey;
    }

    /**
     * Set survey type
     *
     * @param survey Survey type
     */
    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    /**
     * Get parent event ID
     *
     * @return Parent event ID
     */
    public String getParentEventId() {
        return parentEventId;
    }

    /**
     * Set parent event ID
     *
     * @param parentEventId Parent event ID
     */
    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    /**
     * Get collection ID
     *
     * @return Collection ID
     */
    public String getCollectionId() {
        return collectionId;
    }

    /**
     * Set collection ID
     *
     * @param collectionId Collection ID
     */
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    /**
     * Get measurement remarks
     *
     * @return Measurement remarks
     */
    public String getMeasurementRemarks() {
        return measurementRemarks;
    }

    /**
     * Set measurement remarks
     *
     * @param measurementRemarks Measurement remarks
     */
    public void setMeasurementRemarks(String measurementRemarks) {
        this.measurementRemarks = measurementRemarks;
    }

    /**
     * Get sampling protocol
     *
     * @return Sampling protocol
     */
    public SamplingProtocol getSamplingProtocol() {
        return samplingProtocol;
    }

    /**
     * Set sampling protocol
     *
     * @param samplingProtocol Sampling protocol
     */
    public void setSamplingProtocol(SamplingProtocol samplingProtocol) {
        this.samplingProtocol = samplingProtocol;
    }

    /**
     * Get sampling protocol reference
     *
     * @return Sampling protocol reference
     */
    public String getSamplingProtocolReference() {
        return samplingProtocolReference;
    }

    /**
     * Set sampling protocol reference
     *
     * @param samplingProtocolReference Sampling protocol reference
     */
    public void setSamplingProtocolReference(String samplingProtocolReference) {
        this.samplingProtocolReference = samplingProtocolReference;
    }

    /**
     * Get sample size unit
     *
     * @return Sample size unit
     */
    public String getSampleSizeUnit() {
        return sampleSizeUnit;
    }

    /**
     * Set sample size unit
     *
     * @param sampleSizeUnit Sample size unit
     */
    public void setSampleSizeUnit(String sampleSizeUnit) {
        this.sampleSizeUnit = sampleSizeUnit;
    }

    /**
     * Get geodetic datum
     *
     * @return Geodetic datum
     */
    public String getGeodeticDatum() {
        return geodeticDatum;
    }

    /**
     * Set geodetic datum
     *
     * @param geodeticDatum Geodetic datum
     */
    public void setGeodeticDatum(String geodeticDatum) {
        this.geodeticDatum = geodeticDatum;
    }

    /**
     * Get location remarks
     *
     * @return Location remarks
     */
    public String getLocationRemarks() {
        return locationRemarks;
    }

    /**
     * Set location remarks
     *
     * @param locationRemarks Location remarks
     */
    public void setLocationRemarks(String locationRemarks) {
        this.locationRemarks = locationRemarks;
    }

    /**
     * Get institution ID
     *
     * @return Institution ID
     */
    public String getInstitutionId() {
        return institutionId;
    }

    /**
     * Set institution ID
     *
     * @param institutionId Institution ID
     */
    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    /**
     * Get references (DOI URLs)
     *
     * @return References (DOI URLs)
     */
    public String[] getReferences() {
        return references;
    }

    /**
     * Set references (DOI URLs)
     *
     * @param references References (DOI URLs)
     */
    public void setReferences(String[] references) {
        this.references = references;
    }

    /**
     * Get continents
     *
     * @return Continents
     */
    public String[] getContinents() {
        return continents;
    }

    /**
     * Set continents
     *
     * @param continents Continents
     */
    public void setContinents(String[] continents) {
        this.continents = continents;
    }

    /**
     * Get countries
     *
     * @return Countries
     */
    public String[] getCountries() {
        return countries;
    }

    /**
     * Set countries
     *
     * @param countries Countries
     */
    public void setCountries(String[] countries) {
        this.countries = countries;
    }

    /**
     * Get languages
     *
     * @return Languages
     */
    public String[] getLanguages() {
        return languages;
    }

    /**
     * Set languages
     *
     * @param languages Languages
     */
    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    /**
     * Get habitats
     *
     * @return Habitats
     */
    public String[] getHabitats() {
        return habitats;
    }

    /**
     * Set habitats
     *
     * @param habitats Habitats
     */
    public void setHabitats(String[] habitats) {
        this.habitats = habitats;
    }

    /**
     * Get taxa
     *
     * @return Taxa
     */
    public String[] getTaxa() {
        return taxa;
    }

    /**
     * Set taxa
     *
     * @param taxa Taxa
     */
    public void setTaxa(String[] taxa) {
        this.taxa = taxa;
    }

    /**
     * Get latest year collected
     *
     * @return Latest year collected
     */
    public int getLatestYearCollected() {
        return latestYearCollected;
    }

    /**
     * Set latest year collected
     *
     * @param latestYearCollected Latest year collected
     */
    public void setLatestYearCollected(int latestYearCollected) {
        this.latestYearCollected = latestYearCollected;
    }

    /**
     * Get earliest year collected
     *
     * @return Earliest year collected
     */
    public int getEarliestYearCollected() {
        return earliestYearCollected;
    }

    /**
     * Set earliest year collected
     *
     * @param earliestYearCollected Earliest year collected
     */
    public void setEarliestYearCollected(int earliestYearCollected) {
        this.earliestYearCollected = earliestYearCollected;
    }

    /**
     * Get number of plots
     *
     * @return Number of plots
     */
    public int getPlotNumber() {
        return plotNumber;
    }

    /**
     * Set number of plots
     *
     * @param plotNumber Number of plots
     */
    public void setPlotNumber(int plotNumber) {
        this.plotNumber = plotNumber;
    }

    /**
     * Get number of species
     *
     * @return Number of species
     */
    public int getSpeciesNumber() {
        return speciesNumber;
    }

    /**
     * Set number of species
     *
     * @param speciesNumber Number of species
     */
    public void setSpeciesNumber(int speciesNumber) {
        this.speciesNumber = speciesNumber;
    }

    /**
     * Get sample size value
     *
     * @return Sample size value
     */
    public float getSampleSizeValue() {
        return sampleSizeValue;
    }

    /**
     * Set sample size value
     *
     * @param sampleSizeValue Sample size value
     */
    public void setSampleSizeValue(float sampleSizeValue) {
        this.sampleSizeValue = sampleSizeValue;
    }

    /**
     * Get spatial extent
     *
     * @return Spatial extent
     */
    public float[] getSpatialExtent() {
        return spatialExtent;
    }

    /**
     * Set spatial extent
     *
     * @param spatialExtent Spatial extent
     */
    public void setSpatialExtent(float[] spatialExtent) {
        this.spatialExtent = spatialExtent;
    }

    /**
     * Get dynamic properties
     *
     * @return Dynamic properties
     */
    public Map<String, String> getDynamicProperties() {
        return dynamicProperties;
    }

    /**
     * Set dynamic properties
     *
     * @param dynamicProperties Dynamic properties
     */
    public void setDynamicProperties(Map<String, String> dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
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
     * Get contract hashes and status
     *
     * @return Contract hashes and object
     */
    public Map<String, Contract> getContracts() {
        return contracts;
    }

    /**
     * Set contract hashes and status
     *
     * @param contracts Contract hashes and object
     */
    public void setContracts(Map<String, Contract> contracts) {
        this.contracts = contracts;
    }

    /**
     * Get status
     *
     * @return Status
     */
    public DatasetStatus getStatus() {
        return status;
    }

    /**
     * Set status
     *
     * @param status Status
     */
    public void setStatus(DatasetStatus status) {
        this.status = status;
    }

    /**
     * Get file info
     *
     * @return File info
     */
    public FileInfo getFileInfo() {
        return fileInfo;
    }

    /**
     * Set file info
     *
     * @param fileInfo File info
     */
    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
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

    /**
     * Get last modified timestamp
     *
     * @return Last modified timestamp
     */
    public String getModified() {
        return modified;
    }

    /**
     * Set last modified timestamp
     *
     * @param modified Last modified timestamp
     */
    public void setModified(String modified) {
        this.modified = modified;
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
     * Set events log
     *
     * @param events Events log
     */
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
