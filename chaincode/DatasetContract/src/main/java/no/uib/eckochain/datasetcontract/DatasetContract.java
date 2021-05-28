/* SPDX-License-Identifier: Apache-2.0 */
package main.java.no.uib.eckochain.datasetcontract;

import main.java.no.uib.eckochain.datasetcontract.model.*;
import main.java.no.uib.eckochain.datasetcontract.util.JSONParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import main.java.no.uib.eckochain.datasetcontract.util.SHA256Hasher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.contract.ClientIdentity;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ResponseUtils;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.json.JSONArray;

/**
 * Class to handle DatasetContract chaincode invocations
 */
public class DatasetContract extends ChaincodeBase {

    private static final Log LOG = LogFactory.getLog(DatasetContract.class);

    @Override
    public Response init(ChaincodeStub stub) {
        try {
            LOG.info("Initializing DatasetContract chaincode...");
            String function = stub.getFunction();
            if (!function.equals("init")) {
                LOG.error("Function not implemented");
                return ResponseUtils.newErrorResponse("Function not implemented");
            }
            List<String> args = stub.getParameters();
            if (!args.isEmpty()) {
                LOG.error("Expecting 0 arguments");
                return ResponseUtils.newErrorResponse("Expecting 0 arguments");
            }
            LOG.error("Successfully initialized chaincode");
            return ResponseUtils.newSuccessResponse("Initialize success");
        } catch (Throwable ex) {
            LOG.error(ex);
            return ResponseUtils.newErrorResponse(ex);
        }
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            LOG.info("Invoking DatasetContract chaincode...");
            switch (stub.getFunction()) {
                case "putDatasetFile":
                    if (stub.getParameters().size() != 1) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Putting dataset in private data...");
                        return putDatasetFile(stub, stub.getParameters());
                    }
                case "putDatasetKey":
                    if (stub.getParameters().size() != 1) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Putting key in private data...");
                        return putDatasetKey(stub, stub.getParameters());
                    }
                case "removeDataset":
                    if (stub.getParameters().size() != 1) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Removing dataset file and setting metadata status removed...");
                        return removeDataset(stub, stub.getParameters());
                    }
                case "createMetadata":
                    if (stub.getParameters().size() != 1) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Creating new metadata for dataset...");
                        return createMetadata(stub, stub.getParameters());
                    }
                case "getDatasetFile":
                    if (stub.getParameters().size() != 1) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Getting dataset file...");
                        return getDatasetFile(stub, stub.getParameters());
                    }
                case "getDatasetKey":
                    if (stub.getParameters().size() != 1) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Getting dataset key...");
                        return getDatasetKey(stub, stub.getParameters());
                    }
                case "createContract":
                    if (stub.getParameters().size() < 1 || stub.getParameters().size() > 2) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Creating new contract...");
                        return createContract(stub, stub.getParameters());
                    }
                case "resolveContract":
                    if (stub.getParameters().size() != 2) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Resolving contract...");
                        return resolveContract(stub, stub.getParameters());
                    }
                case "queryPrivateData":
                    if (stub.getParameters().size() != 1) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Querying private data...");
                        return queryPrivateData(stub, stub.getParameters());
                    }
                case "query":
                    if (stub.getParameters().size() != 1) {
                        LOG.error("Invalid number of arguments");
                        return ResponseUtils.newErrorResponse("Invalid number of arguments");
                    } else {
                        LOG.info("Querying state database...");
                        return query(stub, stub.getParameters());
                    }
            }
            return ResponseUtils.newErrorResponse("Chaincode function does not exist");
        } catch (SecurityException ex) {
            LOG.error(ex);
            return ResponseUtils.newErrorResponse(ex);
        } catch (JsonProcessingException ex) {
            LOG.error("Could not parse JSON", ex);
            return ResponseUtils.newErrorResponse("Could not parse JSON");
        } catch (CertificateException | IOException ex) {
            LOG.error("Could not get client identity", ex);
            return ResponseUtils.newErrorResponse("Could not get client identity");
        }
    }

    /**
     * Put dataset file in private data (automatically creates a contract for the data owner)
     * Transient: invokedBy, file
     *
     * @param stub Chaincode interface
     * @param args Dataset ID
     * @return Response
     * @throws JsonProcessingException If cannot parse JSON
     * @throws CertificateException    If cannot get client identity
     * @throws IOException             If cannot get client identity
     */
    private Response putDatasetFile(ChaincodeStub stub, List<String> args) throws JsonProcessingException, CertificateException, IOException {
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey("invokedBy") || !transientData.containsKey("file")) {
            LOG.info("Required transient data is missing");
            return ResponseUtils.newErrorResponse("Required transient data is missing");
        }
        String datasetId = args.get(0);
        Identity invoker = new Identity(new String(transientData.get("invokedBy"), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!invoker.getMspId().equals(stub.getMspId())) {
            LOG.info("Client organization does not match peer organization");
            return ResponseUtils.newErrorResponse("Client organization does not match peer organization");
        }
        byte[] fileBytes = stub.getPrivateData("file_collection_" + stub.getMspId().replaceAll("\\.", "_"), "file:" + datasetId);
        if (fileBytes != null && fileBytes.length > 0) {
            LOG.info("A dataset with the supplied ID already exists in the private data collection");
            return ResponseUtils.newErrorResponse("A contract with the supplied ID already exists in the private data collection");
        }
        String state = stub.getStringState(datasetId);
        if (state != null && !state.isEmpty()) {
            LOG.info("A dataset with the supplied ID already exists in the state database");
            return ResponseUtils.newErrorResponse("A dataset with the supplied ID already exists in the state database");
        }
        String contractId = SHA256Hasher.getHash(invoker.getId() + datasetId);
        ContractDetails contractDetails = new ContractDetails(datasetId);
        contractDetails.setCreatedAt(stub.getTxTimestamp().toString());
        contractDetails.setCreatedBy(invoker.getId());
        stub.putPrivateData("file_collection_" + stub.getMspId().replaceAll("\\.", "_"), "file:" + datasetId, transientData.get("file"));
        stub.putPrivateData("_implicit_org_" + stub.getMspId(), "rights:" + datasetId, JSONParser.getJSON(invoker));
        stub.putPrivateData("_implicit_org_" + stub.getMspId(), "contract:" + contractId, JSONParser.getJSON(contractDetails));
        LOG.info("Successfully put dataset in private data");
        return ResponseUtils.newSuccessResponse("Invoke success", datasetId.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Put dataset key in private data
     * Transient: invokedBy, key
     *
     * @param stub Chaincode interface
     * @param args Dataset ID
     * @return Response
     * @throws JsonProcessingException If cannot parse JSON
     * @throws CertificateException    If cannot get client identity
     * @throws IOException             If cannot get client identity
     */
    private Response putDatasetKey(ChaincodeStub stub, List<String> args) throws JsonProcessingException, CertificateException, IOException {
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey("invokedBy") || !transientData.containsKey("key")) {
            LOG.info("Required transient data is missing");
            return ResponseUtils.newErrorResponse("Required transient data is missing");
        }
        String datasetId = args.get(0);
        Identity invoker = new Identity(new String(transientData.get("invokedBy"), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!invoker.getMspId().equals(stub.getMspId())) {
            LOG.info("Client organization does not match peer organization");
            return ResponseUtils.newErrorResponse("Client organization does not match peer organization");
        }
        byte[] fileBytes = stub.getPrivateData("file_collection_" + stub.getMspId().replaceAll("\\.", "_"), "file:" + datasetId);
        if (fileBytes == null || fileBytes.length == 0) {
            LOG.info("Could not find dataset");
            return ResponseUtils.newErrorResponse("Could not find dataset");
        }
        byte[] keyBytes = stub.getPrivateData("_implicit_org_" + stub.getMspId(), "key:" + datasetId);
        if (keyBytes != null && keyBytes.length != 0) {
            LOG.info("A key already exists in private data");
            return ResponseUtils.newErrorResponse("A key already exists in private data");
        }
        stub.putPrivateData("_implicit_org_" + stub.getMspId(), "key:" + datasetId, transientData.get("key"));
        LOG.info("Successfully put key in private data");
        return ResponseUtils.newSuccessResponse("Invoke success", datasetId.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Set metadata status removed and remove file and key
     * Transient: invokedBy
     *
     * @param stub Chaincode interface
     * @param args Dataset ID
     * @return Response
     * @throws JsonProcessingException If cannot parse JSON
     * @throws CertificateException    If cannot get client identity
     * @throws IOException             If cannot get client identity
     */
    private Response removeDataset(ChaincodeStub stub, List<String> args) throws JsonProcessingException, CertificateException, IOException {
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey("invokedBy")) {
            LOG.info("Required transient data is missing");
            return ResponseUtils.newErrorResponse("Required transient data is missing");
        }
        String datasetId = args.get(0);
        String state = stub.getStringState(datasetId);
        if (state == null || state.isEmpty()) {
            LOG.info("Could not find metadata for the dataset");
            return ResponseUtils.newErrorResponse("Could not find metadata for the dataset");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        Identity invoker = new Identity(new String(transientData.get("invokedBy"), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        if (!metadata.getInstitutionId().equals(invoker.getMspId())) {
            LOG.info("Peer organization does not match owner organization");
            return ResponseUtils.newErrorResponse("Peer organization does not match owner organization");
        }
        if (stub.getMspId().equals(invoker.getMspId())) {
            stub.delPrivateData("file_collection_" + stub.getMspId().replaceAll("\\.", "_"), "file:" + datasetId);
            stub.delPrivateData("_implicit_org_" + stub.getMspId(), "key:" + datasetId);
        }
        String timestamp = stub.getTxTimestamp().toString();
        metadata.setStatus(DatasetStatus.REMOVED);
        metadata.setFileInfo(null);
        metadata.setModified(timestamp);
        metadata.getEvents().add(new Event(EventType.REMOVE_FILE, timestamp));
        stub.putStringState(metadata.getDatasetId(), JSONParser.getJSON(metadata));
        return ResponseUtils.newSuccessResponse("Invoke success");
    }

    /**
     * Create metadata for a dataset and accept data owner contract (overwrites existing metadata if it exists)
     * Transient: invokedBy
     *
     * @param stub Chaincode interface
     * @param args A JSON object with fields corresponding to Metadata, Policy and FileInfo objects
     * @return Response
     * @throws JsonProcessingException If cannot parse JSON
     * @throws CertificateException    If cannot get client identity
     * @throws IOException             If cannot get client identity
     */
    private Response createMetadata(ChaincodeStub stub, List<String> args) throws JsonProcessingException, CertificateException, IOException {
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey("invokedBy")) {
            LOG.info("Required transient data is missing");
            return ResponseUtils.newErrorResponse("Required transient data is missing");
        }
        Identity invoker = new Identity(new String(transientData.get("invokedBy"), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        Metadata metadata = JSONParser.getMetadata(args.get(0));
        Policy policy = JSONParser.getPolicy(args.get(0));
        FileInfo fileInfo = JSONParser.getFileInfo(args.get(0));
        String timestamp = stub.getTxTimestamp().toString();
        metadata.setStatus(DatasetStatus.ACTIVE);
        metadata.setCreatedAt(timestamp);
        metadata.setModified(timestamp);
        metadata.setInstitutionId(invoker.getMspId());
        policy.setCreatedAt(timestamp);
        metadata.setPolicy(policy);
        byte[] fileHash = stub.getPrivateDataHash("file_collection_" + metadata.getInstitutionId().replaceAll("\\.", "_"), "file:" + metadata.getDatasetId());
        if (fileHash == null || fileHash.length == 0) {
            LOG.info("Could not find a dataset with matching ID");
            return ResponseUtils.newErrorResponse("Could not find a dataset with matching ID");
        }
        byte[] keyHash = stub.getPrivateDataHash("_implicit_org_" + metadata.getInstitutionId(), "key:" + metadata.getDatasetId());
        if (keyHash == null || keyHash.length == 0) {
            LOG.info("Could not find a key with matching ID");
            return ResponseUtils.newErrorResponse("Could not find a key with matching ID");
        }
        String state = stub.getStringState(metadata.getDatasetId());
        if (state != null && !state.isEmpty()) {
            LOG.info("Metadata for this dataset already exist. Replacing existing metadata...");
            Metadata existingMetadata = JSONParser.getMetadata(state);
            metadata.setFileInfo(existingMetadata.getFileInfo());
            metadata.setEvents(existingMetadata.getEvents());
            metadata.getEvents().add(new Event(EventType.CHANGE_METADATA, timestamp));
            if (!metadata.getPolicy().equals(existingMetadata.getPolicy())) {
                metadata.getEvents().add(new Event(EventType.CHANGE_POLICY, timestamp));
            }
        } else {
            fileInfo.setFileHash(fileHash);
            metadata.setFileInfo(fileInfo);
        }
        String contractId = SHA256Hasher.getHash(invoker.getId() + metadata.getDatasetId());
        byte[] contractHash = stub.getPrivateDataHash("_implicit_org_" + metadata.getInstitutionId(), "contract:" + contractId);
        if (contractHash == null || contractHash.length == 0) {
            LOG.info("Could not find contract details for dataset owner");
            return ResponseUtils.newErrorResponse("Could not find contract details for dataset owner");
        }
        String hashString = Base64.getEncoder().encodeToString(contractHash);
        Event event = new Event(EventType.ACCEPT_CONTRACT, timestamp);
        Contract contract = new Contract(hashString, ContractStatus.ACCEPTED, timestamp);
        contract.getEvents().add(event);
        metadata.getContracts().put(hashString, contract);
        String metadataJSON = JSONParser.getJSON(metadata);
        stub.putStringState(metadata.getDatasetId(), metadataJSON);
        LOG.info("Successfully created metadata for the dataset");
        return ResponseUtils.newSuccessResponse("Invoke success");
    }

    /**
     * Get dataset file if a valid contract is found
     *
     * @param stub Chaincode interface
     * @param args Dataset ID
     * @return Response
     * @throws JsonProcessingException If cannot parse JSON
     * @throws CertificateException    If cannot get client identity
     * @throws IOException             If cannot get client identity
     */
    private Response getDatasetFile(ChaincodeStub stub, List<String> args) throws JsonProcessingException, CertificateException, IOException {
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey("invokedBy")) {
            LOG.info("Required transient data is missing");
            return ResponseUtils.newErrorResponse("Required transient data is missing");
        }
        Identity invoker = new Identity(new String(transientData.get("invokedBy"), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        String state = stub.getStringState(args.get(0));
        if (state == null || state.isEmpty()) {
            LOG.info("Could not find metadata for the dataset");
            return ResponseUtils.newErrorResponse("Could not find metadata for the dataset");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        byte[] fileHash = stub.getPrivateDataHash("file_collection_" + metadata.getInstitutionId().replaceAll("\\.", "_"), "file:" + metadata.getDatasetId());
        byte[] fileBytes = stub.getPrivateData("file_collection_" + metadata.getInstitutionId().replaceAll("\\.", "_"), "file:" + metadata.getDatasetId());
        if (fileBytes == null || fileHash == null || fileBytes.length == 0 || fileHash.length == 0) {
            LOG.error("Could not find dataset file");
            return ResponseUtils.newErrorResponse("Could not find dataset file");
        }
        if (!Arrays.equals(fileHash, metadata.getFileInfo().getFileHash())) {
            LOG.error("File hash does not match registered hash");
            return ResponseUtils.newErrorResponse("File hash does not match registered hash");
        }
        String contractId = SHA256Hasher.getHash(invoker.getId() + metadata.getDatasetId());
        byte[] contractHash = stub.getPrivateDataHash("_implicit_org_" + metadata.getInstitutionId(), "contract:" + contractId);
        if (contractHash == null || contractHash.length == 0) {
            LOG.info("Could not find contract for user");
            return ResponseUtils.newErrorResponse("Could not find contract for user");
        }
        Contract contract = metadata.getContracts().get(Base64.getEncoder().encodeToString(contractHash));
        if (contract == null || !contract.getStatus().equals(ContractStatus.ACCEPTED)) {
            LOG.info("Could not find an accepted contract");
            return ResponseUtils.newErrorResponse("Could not find an accepted contract");
        }
        Event event = new Event(EventType.GET_FILE, stub.getTxTimestamp().toString());
        contract.getEvents().add(event);
        metadata.getEvents().add(event);
        stub.putStringState(metadata.getDatasetId(), JSONParser.getJSON(metadata));
        LOG.info("Successfully retrieved dataset file");
        return ResponseUtils.newSuccessResponse("Invoke success", fileBytes);
    }

    /**
     * Get dataset key (peer msp must equal dataset owner msp)
     *
     * @param stub Chaincode interface
     * @param args Dataset ID
     * @return Response
     * @throws JsonProcessingException If cannot parse JSON
     * @throws CertificateException    If cannot get client identity
     * @throws IOException             If cannot get client identity
     */
    private Response getDatasetKey(ChaincodeStub stub, List<String> args) throws JsonProcessingException, CertificateException, IOException {
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey("invokedBy")) {
            LOG.info("Required transient data is missing");
            return ResponseUtils.newErrorResponse("Required transient data is missing");
        }
        Identity invoker = new Identity(new String(transientData.get("invokedBy"), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        String state = stub.getStringState(args.get(0));
        if (state == null || state.isEmpty()) {
            LOG.info("Could not find metadata for the requested dataset");
            return ResponseUtils.newErrorResponse("Could not find metadata for the requested dataset");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        if (!stub.getMspId().equals(metadata.getInstitutionId())) {
            LOG.info("Peer organization does not match owner organization");
            return ResponseUtils.newErrorResponse("Peer organization does not match owner organization");
        }
        byte[] keyBytes = stub.getPrivateData("_implicit_org_" + stub.getMspId(), "key:" + metadata.getDatasetId());
        if (keyBytes == null || keyBytes.length == 0) {
            LOG.error("Could not find dataset key");
            return ResponseUtils.newErrorResponse("Could not find dataset key");
        }
        String contractId = SHA256Hasher.getHash(invoker.getId() + metadata.getDatasetId());
        byte[] contractHash = stub.getPrivateDataHash("_implicit_org_" + metadata.getInstitutionId(), "contract:" + contractId);
        if (contractHash == null || contractHash.length == 0) {
            LOG.info("Could not find contract for user");
            return ResponseUtils.newErrorResponse("Could not find contract for user");
        }
        Contract contract = metadata.getContracts().get(Base64.getEncoder().encodeToString(contractHash));
        if (contract == null || !contract.getStatus().equals(ContractStatus.ACCEPTED)) {
            LOG.info("Could not find an accepted contract");
            return ResponseUtils.newErrorResponse("Could not find an accepted contract");
        }
        LOG.info("Successfully retrieved dataset key");
        return ResponseUtils.newSuccessResponse("Invoke success", keyBytes);
    }

    /**
     * Create a new contract for the dataset and place it in implicit collection of the invoker organization and the owner organization
     * Transient: invokedBy
     *
     * @param stub Chaincode interface
     * @param args datasetId, proposal
     * @return Response
     * @throws JsonProcessingException If cannot parse JSON
     * @throws CertificateException    If cannot get client identity
     * @throws IOException             If cannot get client identity
     */
    private Response createContract(ChaincodeStub stub, List<String> args) throws JsonProcessingException, CertificateException, IOException {
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey("invokedBy")) {
            LOG.info("Required transient data is missing");
            return ResponseUtils.newErrorResponse("Required transient data is missing");
        }
        Identity invoker = new Identity(new String(transientData.get("invokedBy"), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        String state = stub.getStringState(args.get(0));
        if (state == null || state.isEmpty()) {
            LOG.info("Could not find metadata for dataset");
            return ResponseUtils.newErrorResponse("Could not find metadata for dataset");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        if (!stub.getMspId().equals(invoker.getMspId()) || !stub.getMspId().equals(metadata.getInstitutionId())) {
            LOG.info("Peer organization does not match client or owner organization");
            return ResponseUtils.newErrorResponse("Peer organization does not match client or owner organization");
        }
        String contractId = SHA256Hasher.getHash(invoker.getId() + metadata.getDatasetId());
        Policy policy = metadata.getPolicy();
        ContractDetails contractDetails;
        if (policy.getTerms() != null) {
            try {
                contractDetails = new ContractDetails(args.get(0), args.get(1));
            } catch (Exception e) {
                LOG.info("Contract proposal cannot be empty when requesting a restricted dataset");
                return ResponseUtils.newErrorResponse("Contract proposal cannot be empty when requesting a restricted dataset");
            }
        } else {
            contractDetails = new ContractDetails(args.get(0));
        }
        contractDetails.setPolicy(policy);
        contractDetails.setCreatedAt(stub.getTxTimestamp().toString());
        contractDetails.setCreatedBy(invoker.getId());
        stub.putPrivateData("_implicit_org_" + stub.getMspId(), "contract:" + contractId, JSONParser.getJSON(contractDetails));
        LOG.info("Successfully created contract");
        return ResponseUtils.newSuccessResponse("Invoke success");
    }

    /**
     * Place the contract's private data hash in the metadata object on the blockchain, accompanied by the contract status
     * Transient: invokedBy, contractId
     *
     * @param stub Chaincode interface
     * @param args datasetId, boolean approve/reject
     * @return Response
     * @throws JsonProcessingException If cannot parse JSON
     * @throws CertificateException    If cannot get client identity
     * @throws IOException             If cannot get client identity
     */
    private Response resolveContract(ChaincodeStub stub, List<String> args) throws JsonProcessingException, CertificateException, IOException {
        Map<String, byte[]> transientData = stub.getTransient();
        if (transientData.isEmpty() || !transientData.containsKey("invokedBy") || !transientData.containsKey("contractId")) {
            LOG.info("Required transient data is missing");
            return ResponseUtils.newErrorResponse("Required transient data is missing");
        }
        Identity invoker = new Identity(new String(transientData.get("invokedBy"), StandardCharsets.UTF_8), new ClientIdentity(stub).getMSPID());
        String state = stub.getStringState(args.get(0));
        if (state == null || state.isEmpty()) {
            LOG.info("Could not find metadata for dataset");
            return ResponseUtils.newErrorResponse("Could not find metadata for dataset");
        }
        Metadata metadata = JSONParser.getMetadata(state);
        String contractId = new String(transientData.get("contractId"), StandardCharsets.UTF_8);
        byte[] contractHash = stub.getPrivateDataHash("_implicit_org_" + metadata.getInstitutionId(), "contract:" + contractId);
        if (contractHash == null || contractHash.length == 0) {
            LOG.info("Could not find contract details");
            return ResponseUtils.newErrorResponse("Could not find contract details");
        }
        if (metadata.getPolicy().getTerms() != null && !invoker.getMspId().equals(metadata.getInstitutionId())) {
            LOG.info("The client msp does not match the dataset owner's msp");
            return ResponseUtils.newErrorResponse("The client msp does not match the dataset owner's msp");
        }
        String hashString = Base64.getEncoder().encodeToString(contractHash);
        if (metadata.getContracts().get(hashString) != null) {
            LOG.info("The contract has already been resolved");
            return ResponseUtils.newErrorResponse("The contract has already been resolved");
        }
        ContractStatus contractStatus = ContractStatus.REJECTED;
        EventType eventType = EventType.REJECT_CONTRACT;
        if (Boolean.parseBoolean(args.get(1))) {
            contractStatus = ContractStatus.ACCEPTED;
            eventType = EventType.ACCEPT_CONTRACT;
        }
        String timestamp = stub.getTxTimestamp().toString();
        Event event = new Event(eventType, timestamp);
        Contract contract = new Contract(hashString, contractStatus, timestamp);
        contract.getEvents().add(event);
        metadata.getContracts().put(hashString, contract);
        metadata.getEvents().add(event);
        stub.putStringState(metadata.getDatasetId(), JSONParser.getJSON(metadata));
        LOG.info("Successfully resolved contract");
        return ResponseUtils.newSuccessResponse("Invoke success");
    }

    /**
     * Query private data
     *
     * @param stub Chaincode interface
     * @param args CouchDB query string
     * @return Response and payload (byte array of string)
     * @throws CertificateException If cannot get client identity
     * @throws IOException          If cannot get client identity
     */
    private Response queryPrivateData(ChaincodeStub stub, List<String> args) throws CertificateException, IOException {
        String clientMspId = new ClientIdentity(stub).getMSPID();
        if (!clientMspId.equals(stub.getMspId())) {
            LOG.info("Client organization does not match peer organization");
            return ResponseUtils.newErrorResponse("Client organization does not match peer organization");
        }
        JSONArray jsonArray = new JSONArray();
        QueryResultsIterator<KeyValue> rows = stub.getPrivateDataQueryResult("_implicit_org_" + clientMspId, args.get(0));
        while (rows.iterator().hasNext()) {
            jsonArray.put(rows.iterator().next().getStringValue());
        }
        LOG.info("Successfully invoked query");
        return ResponseUtils.newSuccessResponse("Invoke success", jsonArray.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Query state database
     *
     * @param stub Chaincode interface
     * @param args CouchDB query string
     * @return Response and payload (byte array of string array)
     */
    private Response query(ChaincodeStub stub, List<String> args) {
        JSONArray jsonArray = new JSONArray();
        QueryResultsIterator<KeyValue> rows = stub.getQueryResult(args.get(0));
        while (rows.iterator().hasNext()) {
            jsonArray.put(rows.iterator().next().getStringValue());
        }
        LOG.info("Successfully invoked query");
        return ResponseUtils.newSuccessResponse("Invoke success", jsonArray.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * DatasetContract main method
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        LOG.info("DatasetContract main method called");
        new DatasetContract().start(args);
    }
}
