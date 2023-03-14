#!/bin/bash
#SPDF-License-Identifier: Apache-2.0
#Start the peer organization

echo "Start TLS CA and CA..."
./start-ca.sh

echo "Start Fabric Peers..."
./start-peers.sh

echo "Done"
