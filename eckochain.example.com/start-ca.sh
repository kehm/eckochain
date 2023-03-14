#!/bin/bash
#SPDF-License-Identifier: Apache-2.0
#Start TLS CA and CA

echo "Create directories for TLS CA..."
./create-tlsca-directories.sh

echo "Start Fabric peer organization TLS CA..."
docker-compose -f docker-compose-tlsca.yaml up -d
sleep 5s

echo "Register and enroll ca, peers and client with peer organization TLS CA..."
./init-tlsca.sh
sleep 5s

echo "Stop TLS CA container..."
./stop-tlsca.sh

echo "Copy TLS certificates..."
./copy-certificates.sh

echo "Start Fabric peer organization CA..."
docker-compose -f docker-compose-ca.yaml up -d
sleep 5s

echo "Register and enroll peers and client with CA..."
./init-ca.sh
sleep 5s
