#!/bin/bash
#SPDF-License-Identifier: Apache-2.0
#Start both the orderer and peer organization

export PEER_ORG_NAME=eckochain.example.com

echo "Create directories for TLS CA..."
./create-tlsca-directories.sh

echo "Start Fabric orderer organization TLS CA..."
docker-compose -f docker-compose-tlsca.yaml up -d
sleep 5s

echo "Register and enroll ca and orderers with orderer organization TLS CA..."
./init-tlsca.sh
sleep 5s

echo "Stop TLS CA container..."
./stop-tlsca.sh

echo "Copy TLS certificates..."
./copy-certificates.sh

echo "Start Fabric orderer organization CA..."
docker-compose -f docker-compose-ca.yaml up -d
sleep 5s

echo "Register and enroll orderers with orderer organization CA..."
./init-ca.sh
sleep 5s

echo "Initialize peer organization..."
(cd ../$PEER_ORG_NAME; ./start-ca.sh)

echo "Create genesis block..."
./create-block.sh

echo "Start Fabric Orderers..."
docker-compose -f docker-compose-orderers.yaml up -d
sleep 5s

echo "Join orderers to channel..."
./init-orderers.sh
sleep 5s

echo "Start peers from peer organization..."
(cd ../$PEER_ORG_NAME; ./start-peers.sh)

echo "Done"
