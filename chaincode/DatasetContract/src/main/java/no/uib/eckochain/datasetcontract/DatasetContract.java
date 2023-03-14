/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.java.no.uib.eckochain.datasetcontract.model.*;
import main.java.no.uib.eckochain.datasetcontract.util.JSONParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import main.java.no.uib.eckochain.datasetcontract.util.SHA256Hasher;
import org.apache.commons.codec.binary.Hex;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;

/**
 * Class to handle DatasetContract chaincode invocations
 */
@Contract(name = "DatasetContract",
        info = @Info(title = "Dataset Contract",
                description = "Dataset Contract for ECKO - Ecological Consortium on Resurvey Data",
                version = "0.0.1",
                license =
                @License(name = "SPDX-License-Identifier: Apache-2.0")))
@Default
public class DatasetContract implements ContractInterface {

    private static final Logger LOG = Logger.getLogger(DatasetContract.class.getName());
    private static final String IMPLICIT_ORG = "_implicit_org_";
    private static final String FILE_KEY = "file";
    private static final String CONTRACT_KEY = "contract";
    private static final String RIGHTS_KEY = "rights";
    private static final String INVOKED_BY_KEY = "invokedBy";
    private static final String CONTRACT_ID_KEY = "contractId";

    /**
     * Constructor for DatasetContract
     */
    public DatasetContract() {
    }

    /**
     * Submit a dataset to the organization's private data collection.
     * Must target a peer from the invoker's organization.
     * Transient: invokedBy, file
     *
     * @param ctx       Chaincode context
     * @param datasetId Dataset ID
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void submitDataset(Context ctx, String datasetId) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey(INVOKED_BY_KEY) || !transientData.containsKey(FILE_KEY)) {
            LOG.info("Transient data is missing invokedBy or file");
            throw new RuntimeException("Transient data is missing invokedBy or file");
        }
        Identity invokedBy = new Identity(new String(transientData.get(INVOKED_BY_KEY), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!isPeerFromFromOrganization(stub, invokedBy.getMspId())) {
            LOG.info("Request must be submitted to a peer from the invoker's organization");
            throw new RuntimeException(("Request must be submitted to a peer from the invoker's organization"));
        }
        // Check if metadata already exists
        String state = stub.getStringState(datasetId);
        if (state != null && !state.isEmpty()) {
            LOG.info("A dataset with the supplied ID already exists in the state database");
            throw new RuntimeException(("A dataset with the supplied ID already exists in the state database"));
        }
        String implicitCollection = IMPLICIT_ORG + invokedBy.getMspId();
        CompositeKey fileKey = stub.createCompositeKey(FILE_KEY, datasetId);
        byte[] fileBytes = stub.getPrivateData(implicitCollection, fileKey.toString());
        if (fileBytes != null && fileBytes.length > 0) {
            LOG.info("A dataset with the supplied ID already exists in the private data collection");
            throw new RuntimeException(("A contract with the supplied ID already exists in the private data collection"));
        }
        // Save file and rights holder in private data
        CompositeKey rightsKey = stub.createCompositeKey(RIGHTS_KEY, datasetId);
        LOG.info("Putting " + fileKey + " in collection " + implicitCollection);
        LOG.info("Putting " + rightsKey + " in collection " + implicitCollection);
        stub.putPrivateData(implicitCollection, fileKey.toString(), transientData.get(FILE_KEY));
        stub.putPrivateData(implicitCollection, rightsKey.toString(), JSONParser.getJSON(invokedBy));
        LOG.info("Successfully submitted dataset");
    }

    /**
     * Submit metadata for a new dataset that has been placed in the organization's private data collection.
     * Transient: invokedBy
     *
     * @param ctx          Chaincode context
     * @param metadataJson Metadata JSON
     * @param policyJson   Policy JSON
     * @param fileInfoJson FileInfo JSON
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void submitMetadata(Context ctx, String metadataJson, String policyJson, String fileInfoJson) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey(INVOKED_BY_KEY)) {
            LOG.info("Transient data is missing invokedBy");
            throw new RuntimeException("Transient data is missing invokedBy");
        }
        // Parse parameters
        Identity invokedBy = new Identity(new String(transientData.get(INVOKED_BY_KEY), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        String timestamp = stub.getTxTimestamp().toString();
        Metadata metadata = JSONParser.getMetadata(metadataJson);
        metadata.setCreatedAt(timestamp);
        metadata.setModified(timestamp);
        metadata.setStatus(DatasetStatus.ACTIVE);
        metadata.setInstitutionId(invokedBy.getMspId());
        Policy policy = JSONParser.getPolicy(policyJson);
        policy.setCreatedAt(timestamp);
        metadata.setPolicy(policy);
        FileInfo fileInfo = JSONParser.getFileInfo(fileInfoJson);
        // Check if metadata exists
        String state = stub.getStringState(metadata.getDatasetId());
        if (state != null && !state.isEmpty()) {
            LOG.info("A dataset with the supplied ID already exists in the state database");
            throw new RuntimeException("A dataset with the supplied ID already exists in the state database");
        }
        String implicitCollection = IMPLICIT_ORG + invokedBy.getMspId();
        CompositeKey fileKey = stub.createCompositeKey(FILE_KEY, metadata.getDatasetId());
        byte[] fileHash = stub.getPrivateDataHash(implicitCollection, fileKey.toString());
        if (fileHash == null || fileHash.length == 0) {
            LOG.info("A dataset with the supplied ID does not exist in the private data collection");
            throw new RuntimeException("A dataset with the supplied ID does not exist in the private data collection");
        }
        fileInfo.setFileHash(Hex.encodeHexString(fileHash));
        metadata.setFileInfo(fileInfo);
        if (!isRightsHolder(stub, invokedBy, metadata)) {
            LOG.info("The invoker is not the owner of this dataset");
            throw new RuntimeException("The invoker is not the owner of this dataset");
        }
        // Save metadata to state
        String metadataJSON = JSONParser.getJSON(metadata);
        stub.putStringState(metadata.getDatasetId(), metadataJSON);
        LOG.info("Successfully submitted metadata");
    }

    /**
     * Update metadata for an existing dataset.
     * Transient: invokedBy
     *
     * @param ctx          Chaincode context
     * @param metadataJson Metadata JSON
     * @param policyJson   Policy JSON
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void updateMetadata(Context ctx, String metadataJson, String policyJson) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey(INVOKED_BY_KEY)) {
            LOG.info("Transient data is missing invokedBy");
            throw new RuntimeException("Transient data is missing invokedBy");
        }
        // Parse parameters
        Metadata metadata = JSONParser.getMetadata(metadataJson);
        Policy policy = JSONParser.getPolicy(policyJson);
        metadata.setPolicy(policy);
        String state = stub.getStringState(metadata.getDatasetId());
        if (state == null || state.isEmpty()) {
            LOG.info("A dataset with the supplied ID does not exist in the state database");
            throw new RuntimeException("A dataset with the supplied ID does not exist in the state database");
        }
        // Check institution
        Metadata existingMetadata = JSONParser.getMetadata(state);
        Identity invokedBy = new Identity(new String(transientData.get(INVOKED_BY_KEY), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!isRightsHolder(stub, invokedBy, existingMetadata)) {
            LOG.info("The invoker is not the owner of this dataset");
            throw new RuntimeException("The invoker is not the owner of this dataset");
        }
        if (existingMetadata.getStatus().equals(DatasetStatus.REMOVED)) {
            LOG.info("The dataset has been removed");
            throw new RuntimeException("The dataset has been removed");
        }
        // Add existing file info, contracts and events to new object
        String timestamp = stub.getTxTimestamp().toString();
        metadata.setModified(timestamp);
        metadata.setCreatedAt(existingMetadata.getCreatedAt());
        metadata.setStatus(existingMetadata.getStatus());
        metadata.setFileInfo(existingMetadata.getFileInfo());
        metadata.setDataContracts(existingMetadata.getDataContracts());
        metadata.setEvents(existingMetadata.getEvents());
        metadata.getEvents().add(new Event(EventType.CHANGE_METADATA, timestamp));
        if (!metadata.getPolicy().equals(existingMetadata.getPolicy())) {
            metadata.getEvents().add(new Event(EventType.CHANGE_POLICY, timestamp));
        }
        // Save updated metadata to state
        String metadataJSON = JSONParser.getJSON(metadata);
        stub.putStringState(metadata.getDatasetId(), metadataJSON);
        LOG.info("Successfully updated metadata");
    }

    /**
     * Remove the dataset from the organization's private data collection.
     * Must target a peer from the invoker's organization.
     * Transient: invokedBy
     *
     * @param ctx       Chaincode context
     * @param datasetId Dataset ID
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void removeDataset(Context ctx, String datasetId) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey(INVOKED_BY_KEY)) {
            LOG.info("Transient data is missing invokedBy");
            throw new RuntimeException("Transient data is missing invokedBy");
        }
        // Parse parameters
        String state = stub.getStringState(datasetId);
        if (state == null || state.isEmpty()) {
            LOG.info("A dataset with the supplied ID does not exist in the state database");
            throw new RuntimeException("A dataset with the supplied ID does not exist in the state database");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        // Check institution
        Identity invokedBy = new Identity(new String(transientData.get(INVOKED_BY_KEY), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!isRightsHolder(stub, invokedBy, metadata)) {
            LOG.info("The invoker is not the owner of this dataset");
            throw new RuntimeException("The invoker is not the owner of this dataset");
        }
        // Remove file from private data
        String implicitCollection = IMPLICIT_ORG + invokedBy.getMspId();
        CompositeKey fileKey = stub.createCompositeKey(FILE_KEY, datasetId);
        stub.delPrivateData(implicitCollection, fileKey.toString());
        LOG.info("Successfully removed dataset");
    }

    /**
     * Set dataset status to removed.
     * Transient: invokedBy
     *
     * @param ctx       Chaincode context
     * @param datasetId Dataset ID
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void setDatasetRemoved(Context ctx, String datasetId) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey(INVOKED_BY_KEY)) {
            LOG.info("Transient data is missing invokedBy");
            throw new RuntimeException("Transient data is missing invokedBy");
        }
        // Parse parameters
        String state = stub.getStringState(datasetId);
        if (state == null || state.isEmpty()) {
            LOG.info("A dataset with the supplied ID does not exist in the state database");
            throw new RuntimeException("A dataset with the supplied ID does not exist in the state database");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        // Check institution
        Identity invokedBy = new Identity(new String(transientData.get(INVOKED_BY_KEY), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!isRightsHolder(stub, invokedBy, metadata)) {
            LOG.info("The invoker is not the owner of this dataset");
            throw new RuntimeException("The invoker is not the owner of this dataset");
        }
        // Check if dataset is removed
        String implicitCollection = IMPLICIT_ORG + invokedBy.getMspId();
        CompositeKey fileKey = stub.createCompositeKey(FILE_KEY, datasetId);
        byte[] fileHash = stub.getPrivateDataHash(implicitCollection, fileKey.toString());
        if (fileHash != null && fileHash.length > 0) {
            LOG.info("A dataset with the supplied ID still exists in the private data collection");
            throw new RuntimeException("A contract with the supplied ID still exists in the private data collection");
        }
        // Save updated metadata to state
        String timestamp = stub.getTxTimestamp().toString();
        metadata.setModified(timestamp);
        metadata.setStatus(DatasetStatus.REMOVED);
        metadata.setFileInfo(null);
        metadata.getEvents().add(new Event(EventType.REMOVE_FILE, timestamp));
        stub.putStringState(metadata.getDatasetId(), JSONParser.getJSON(metadata));
        LOG.info("Successfully updated metadata with status removed");
    }

    /**
     * Create a new contract for the dataset and place it in implicit collection of the requester and owner organizations.
     * Transient: invokedBy
     *
     * @param ctx       Chaincode context
     * @param datasetId Dataset ID
     * @param proposal  Proposal message (ignored if no terms)
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void createContract(Context ctx, String datasetId, String proposal) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey(INVOKED_BY_KEY)) {
            LOG.info("Transient data is missing invokedBy");
            throw new RuntimeException("Transient data is missing invokedBy");
        }
        // Check state
        String state = stub.getStringState(datasetId);
        if (state == null || state.isEmpty()) {
            LOG.info("A dataset with the supplied ID does not exist in the state database");
            throw new RuntimeException("A dataset with the supplied ID does not exist in the state database");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        Identity invokedBy = new Identity(new String(transientData.get(INVOKED_BY_KEY), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!isPeerFromFromOrganization(stub, invokedBy.getMspId())) {
            LOG.info("Request must be submitted to a peer from the invoker's organization");
            throw new RuntimeException("Request must be submitted to a peer from the invoker's organization");
        }
        // Check contract
        String contractId = SHA256Hasher.getHash(invokedBy.getId() + metadata.getDatasetId());
        CompositeKey contractKey = stub.createCompositeKey(CONTRACT_KEY, contractId);
        String requesterImplicitCollection = IMPLICIT_ORG + invokedBy.getMspId();
        String ownerImplicitCollection = IMPLICIT_ORG + metadata.getInstitutionId();
        byte[] requesterContractHash = stub.getPrivateDataHash(requesterImplicitCollection, contractKey.toString());
        byte[] ownerContractHash = stub.getPrivateDataHash(ownerImplicitCollection, contractKey.toString());
        if ((requesterContractHash != null && requesterContractHash.length > 0) || ownerContractHash != null && ownerContractHash.length > 0) {
            LOG.info("User already has a contract for this dataset");
            throw new RuntimeException("User already has a contract for this dataset");
        }
        // Set proposal
        Policy policy = metadata.getPolicy();
        ContractDetails contractDetails = new ContractDetails(datasetId);
        if (policy.getTerms() != null) {
            contractDetails.setProposal(proposal);
        }
        // Save contract details in requester and owner organisations' private data
        String timestamp = stub.getTxTimestamp().toString();
        contractDetails.setCreatedAt(timestamp);
        contractDetails.setCreatedBy(invokedBy.getId());
        contractDetails.setPolicy(policy);
        // If peer is from the requester's organization
        if (isPeerFromFromOrganization(stub, invokedBy.getMspId())) {
            LOG.info("Putting " + contractKey + " in collection " + requesterImplicitCollection);
            stub.putPrivateData(requesterImplicitCollection, contractKey.toString(), JSONParser.getJSON(contractDetails));
        }
        // If peer is from the owner's organization
        if (isPeerFromFromOrganization(stub, metadata.getInstitutionId())) {
            LOG.info("Putting " + contractKey + " in collection " + ownerImplicitCollection);
            stub.putPrivateData(ownerImplicitCollection, contractKey.toString(), JSONParser.getJSON(contractDetails));
        }
        LOG.info("Successfully created contract");
    }

    /**
     * Place the contract's private data hash and status in the dataset's metadata object.
     * Must be invoked by the dataset owner if the dataset has custom terms, else the requester can invoke.
     * Transient: invokedBy, contractId
     *
     * @param ctx       Chaincode context
     * @param datasetId Dataset ID
     * @param approve   Boolean approve/reject
     */
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void resolveContract(Context ctx, String datasetId, String approve) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey(INVOKED_BY_KEY) || !transientData.containsKey(CONTRACT_ID_KEY)) {
            LOG.info("Transient data is missing invokedBy or contractId");
            throw new RuntimeException("Transient data is missing invokedBy or contractId");
        }
        // Check state
        String state = stub.getStringState(datasetId);
        if (state == null || state.isEmpty()) {
            LOG.info("A dataset with the supplied ID does not exist in the state database");
            throw new RuntimeException("A dataset with the supplied ID does not exist in the state database");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        // Check if the policy is a license or custom terms
        Policy policy = metadata.getPolicy();
        Identity invokedBy = new Identity(new String(transientData.get(INVOKED_BY_KEY), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        String contractId = SHA256Hasher.getHash(invokedBy.getId() + metadata.getDatasetId());
        if (policy.getTerms() != null && !isRightsHolder(stub, invokedBy, metadata)) {
            LOG.info("The invoker is not the owner of this dataset");
            throw new RuntimeException("The invoker is not the owner of this dataset");
        } else if (!contractId.equals(new String(transientData.get(CONTRACT_ID_KEY), StandardCharsets.UTF_8))) {
            LOG.info("The invoker is not the contract requester");
            throw new RuntimeException("The invoker is not the contract requester");
        }
        // Check contract
        String requesterImplicitCollection = IMPLICIT_ORG + invokedBy.getMspId();
        String ownerImplicitCollection = IMPLICIT_ORG + metadata.getInstitutionId();
        CompositeKey contractKey = stub.createCompositeKey(CONTRACT_KEY, contractId);
        byte[] requesterContractHash = stub.getPrivateDataHash(requesterImplicitCollection, contractKey.toString());
        byte[] ownerContractHash = stub.getPrivateDataHash(ownerImplicitCollection, contractKey.toString());
        if ((requesterContractHash == null || requesterContractHash.length == 0) || (ownerContractHash == null || ownerContractHash.length == 0)) {
            LOG.info("Contract does not exist");
            throw new RuntimeException("Contract does not exist");
        }
        if (!Arrays.equals(requesterContractHash, ownerContractHash)) {
            LOG.info("Contract is invalid");
            throw new RuntimeException("Contract is invalid");
        }

        String contractHash = Hex.encodeHexString(ownerContractHash);
        if (metadata.getDataContracts().get(contractHash) != null) {
            LOG.info("The contract has already been resolved");
            throw new RuntimeException("The contract has already been resolved");
        }
        // Set contract status
        Event event;
        DataContract dataContract;
        String timestamp = stub.getTxTimestamp().toString();
        if (Boolean.parseBoolean(approve)) {
            event = new Event(EventType.ACCEPT_CONTRACT, timestamp);
            dataContract = new DataContract(contractHash, ContractStatus.ACCEPTED, timestamp);
        } else {
            event = new Event(EventType.REJECT_CONTRACT, timestamp);
            dataContract = new DataContract(contractHash, ContractStatus.REJECTED, timestamp);
        }
        metadata.getDataContracts().put(contractHash, dataContract);
        metadata.getEvents().add(event);
        stub.putStringState(metadata.getDatasetId(), JSONParser.getJSON(metadata));
        LOG.info("Successfully resolved contract");
    }

    /**
     * Get dataset file. Must be targeted to a peer from the organization that owns the dataset.
     * Requires that the invoker has an accepted contract for the dataset.
     *
     * @param ctx       Chaincode context
     * @param datasetId Dataset ID
     * @return File bytes
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public byte[] getDataset(Context ctx, String datasetId) throws CertificateException, IOException {
        ChaincodeStub stub = ctx.getStub();
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey(INVOKED_BY_KEY)) {
            LOG.info("Transient data is missing invokedBy");
            throw new RuntimeException("Transient data is missing invokedBy");
        }
        // Check state
        String state = stub.getStringState(datasetId);
        if (state == null || state.isEmpty()) {
            LOG.info("A dataset with the supplied ID does not exist in the state database");
            throw new RuntimeException("A dataset with the supplied ID does not exist in the state database");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        if (!isPeerFromFromOrganization(stub, metadata.getInstitutionId())) {
            LOG.info("Dataset must be submitted to a peer from the dataset owner's organization");
            throw new RuntimeException("Dataset must be submitted to a peer from the dataset owner's organization");
        }
        // Get dataset file from private data
        CompositeKey fileKey = stub.createCompositeKey(FILE_KEY, metadata.getDatasetId());
        byte[] fileBytes = stub.getPrivateData(IMPLICIT_ORG + metadata.getInstitutionId(), fileKey.toString());
        if (fileBytes == null || fileBytes.length == 0) {
            LOG.info("Could not find dataset file");
            throw new RuntimeException("Could not find dataset file");
        }
        // Check contract
        Identity invokedBy = new Identity(new String(transientData.get(INVOKED_BY_KEY), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!isRightsHolder(stub, invokedBy, metadata)) {
            String contractId = SHA256Hasher.getHash(invokedBy.getId() + metadata.getDatasetId());
            String implicitCollection = IMPLICIT_ORG + metadata.getInstitutionId();
            CompositeKey contractKey = stub.createCompositeKey(CONTRACT_KEY, contractId);
            byte[] contractHash = stub.getPrivateDataHash(implicitCollection, contractKey.toString());
            if (contractHash == null || contractHash.length == 0) {
                LOG.info("User does not have a contract for this dataset");
                throw new RuntimeException("User does not have a contract for this dataset");
            }
            DataContract dataContract = metadata.getDataContracts().get(Hex.encodeHexString(contractHash));
            if (dataContract == null || !dataContract.getStatus().equals(ContractStatus.ACCEPTED)) {
                LOG.info("User does not have an accepted contract for this dataset");
                throw new RuntimeException("User does not have an accepted contract for this dataset");
            }
        }
        LOG.info("Successfully retrieved dataset");
        return fileBytes;
    }

    /**
     * Get metadata for all or a specified dataset.
     *
     * @param ctx       Chaincode context
     * @param datasetId A dataset ID or 'null' to get all
     * @return Stringified array of metadata
     */
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String getMetadata(Context ctx, String datasetId) throws Exception {
        ChaincodeStub stub = ctx.getStub();
        JSONArray jsonArray = new JSONArray();
        if (datasetId.equals("null")) {
            try (QueryResultsIterator<KeyValue> iterator = stub.getStateByRange("", "")) {
                while (iterator.iterator().hasNext()) {
                    jsonArray.put(iterator.iterator().next().getStringValue());
                }
            }
        } else {
            String state = stub.getStringState(datasetId);
            if (state == null || state.isEmpty()) {
                LOG.info("A dataset with the supplied ID does not exist in the state database");
                throw new RuntimeException("A dataset with the supplied ID does not exist in the state database");
            }
            jsonArray.put(state);
        }
        LOG.info("Successfully retrieved metadata");
        return jsonArray.toString();
    }

    /**
     * Returns true if the identity is the rights holder of the dataset
     *
     * @param stub     Chaincode interface
     * @param identity Identity
     * @param metadata Dataset metadata
     * @return True if the identity is the rights holder
     */
    private boolean isRightsHolder(ChaincodeStub stub, Identity identity, Metadata metadata) throws JsonProcessingException {
        if (!metadata.getInstitutionId().equals(identity.getMspId())) {
            return false;
        }
        String implicitCollection = IMPLICIT_ORG + identity.getMspId();
        CompositeKey rightsKey = stub.createCompositeKey(RIGHTS_KEY, metadata.getDatasetId());
        byte[] rightsBytes = stub.getPrivateDataHash(implicitCollection, rightsKey.toString());
        if (rightsBytes == null || rightsBytes.length == 0) {
            return false;
        }
        String invokedByHash = SHA256Hasher.getHash(JSONParser.getJSON(identity));
        String rightsHash = Hex.encodeHexString(rightsBytes);
        return invokedByHash.equals(rightsHash);
    }

    /**
     * Returns true if the peer belongs to the specified organization.
     *
     * @param stub  Chaincode interface
     * @param mspId MSP ID to test
     * @return True if the peer and is from the same organization
     */
    private boolean isPeerFromFromOrganization(ChaincodeStub stub, String mspId) {
        String peerMspId = stub.getMspId();
        return mspId.equals(peerMspId);
    }
}
