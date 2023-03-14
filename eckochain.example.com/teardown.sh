#!/bin/bash
#SPDF-License-Identifier: Apache-2.0
#Destroy peer organization containers and certificates

export ORG_NAME=eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data

docker stop peer0.$ORG_NAME couchdb0.$ORG_NAME ca.$ORG_NAME tlsca.$ORG_NAME
docker rm peer0.$ORG_NAME couchdb0.$ORG_NAME ca.$ORG_NAME tlsca.$ORG_NAME
yes | docker volume prune

sudo rm -fr $VOLUME_PATH/channel-artifacts
sudo rm -fr $VOLUME_PATH/production/peer0.$ORG_NAME $VOLUME_PATH/crypto-config/peer0.$ORG_NAME
sudo rm -fr $VOLUME_PATH/production/ca.$ORG_NAME $VOLUME_PATH/crypto-config/ca.$ORG_NAME
sudo rm -fr $VOLUME_PATH/production/tlsca.$ORG_NAME $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME
sudo rm -fr $VOLUME_PATH/production/tlsca.$ORG_NAME $VOLUME_PATH/crypto-config/msp.$ORG_NAME
sudo rm -fr $VOLUME_PATH/crypto-config/ecko.example.com
