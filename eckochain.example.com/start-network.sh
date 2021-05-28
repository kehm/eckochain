#!/bin/bash
#SPDF-License-Identifier: Apache-2.0
#Start Hyperledger Fabric blockchain network

export CHANNEL_NAME=eckochannel
export VOLUME_PATH=/eckostorage/fabric_data
export FABRIC_BIN_PATH=/home/ubuntu/bin

echo "Start Fabric TLS CA..."
docker-compose -f docker-compose-tlsca.yaml up -d
sleep 5s

echo "Register and enroll ca, peers, orderers and client with TLS CA..."
./init-tlsca.sh
sleep 5s

echo "Copy TLS certificates..."
./copy-certificates.sh

echo "Start Fabric CA..."
docker-compose -f docker-compose-ca.yaml up -d
sleep 5s

echo "Register and enroll peers, orderers and client with CA..."
./init-ca.sh
sleep 5s

echo "Create genesis block..."
./create-block.sh

echo "Start Fabric Orderers..."
docker-compose -f docker-compose-orderers.yaml up -d
sleep 5s

echo "Join orderers to channel..."
./init-orderers.sh
sleep 5s

echo "Start Fabric Peers..."
docker-compose -f docker-compose-peers.yaml up -d
sleep 5s

echo "Join peers to channel..."
./init-peers.sh
sleep 5s

echo "Install chaincode..."
./init-chaincode.sh

echo "Done"
