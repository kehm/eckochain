#!/bin/bash
#SPDF-License-Identifier: Apache-2.0
#Start peers and install chaincode

echo "Start Fabric Peers..."
docker-compose -f docker-compose-peers.yaml up -d
sleep 5s

echo "Join peers to channel..."
./init-peers.sh
sleep 5s

echo "Install chaincode..."
./install-chaincode.sh
