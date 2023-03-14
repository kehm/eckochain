# ECKO Blockchain
## A Hyperledger Fabric Blockchain

ECKO is a distributed blockchain data consortium for ecological resurvey datasets.  
This repository holds Hyperledger Fabric configuration files, chaincode and scripts for bringing up a blockchain network.  

To bring up a new network, first modify the environment variables in the scripts (.sh) and the .yaml files:
1. Rename (or copy) the peer and orderer organization directories to align with your organization (i.e. replace example.com).
2. Replace all occurrences of example.com with your organisation's domain.
3. Replace the secrets (filter on 'secret') with your own secrets.
4. Modify the connection profile in the client-config directory to match the changes you make to the other configuration files. This is the file you provide to any client application that connects to your nodes.

The CA and TLS CA can be replaced by other CAs, if your organization already has one. If you are using the Fabric CA, make sure to update the csr section of fabric-ca-server-config.yaml and fabric-tlsca-server-config.yaml with your organization's details (and set the bootstrap identity secret).  

Each organization directory has a `start.sh` script. The script in the peer organization directory starts just the peer organization, while the script in the orderer organization directory starts both organizations.  
If you are joining peers to an existing network that already has an ordering service, you can ignore the orderer organization and start only a peer organization.  
The majority of organisations in the ECKOchain network must approve your organisation to join the network before you are allowed to join.  
The start script will install the Dataset Contract chaincode on the peer, but will not commit the chaincode definition. To commit the chaincode definition you must run the `commit-chaincode.sh` script.  

NOTE: All scripts are created for guidance only and they might need modifications to work for your setup. An understanding of how Hyperledger Fabric works is required.

This project is created by the University of Bergen (UiB), Norway, and is available under the Apache License, Version 2.0 (Apache-2.0).

Read more about ECKO: <https://ecko.uib.no>
