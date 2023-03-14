#!/bin/bash
#SPDF-License-Identifier: Apache-2.0
#Destroy orderer organization containers and certificates

export ORG_NAME=orderers.eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data

docker stop orderer0.$ORG_NAME orderer1.$ORG_NAME orderer2.$ORG_NAME ca.$ORG_NAME tlsca.$ORG_NAME
docker rm orderer0.$ORG_NAME orderer1.$ORG_NAME orderer2.$ORG_NAME ca.$ORG_NAME tlsca.$ORG_NAME
yes | docker volume prune

sudo rm -fr $VOLUME_PATH/channel-artifacts
sudo rm -fr $VOLUME_PATH/production/orderer0.$ORG_NAME $VOLUME_PATH/crypto-config/orderer0.$ORG_NAME
sudo rm -fr $VOLUME_PATH/production/orderer1.$ORG_NAME $VOLUME_PATH/crypto-config/orderer1.$ORG_NAME
sudo rm -fr $VOLUME_PATH/production/orderer2.$ORG_NAME $VOLUME_PATH/crypto-config/orderer2.$ORG_NAME
sudo rm -fr $VOLUME_PATH/production/ca.$ORG_NAME $VOLUME_PATH/crypto-config/ca.$ORG_NAME
sudo rm -fr $VOLUME_PATH/production/tlsca.$ORG_NAME $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME
sudo rm -fr $VOLUME_PATH/production/tlsca.$ORG_NAME $VOLUME_PATH/crypto-config/msp.$ORG_NAME
