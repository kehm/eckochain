#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Create directories for TLS CA

export ORG_NAME=orderers.eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data
export TLS_CA_NAME=tlsca.$ORG_NAME
export CA_NAME=ca.$ORG_NAME
export ORDERER_0_NAME=orderer0.$ORG_NAME
export ORDERER_1_NAME=orderer1.$ORG_NAME
export ORDERER_2_NAME=orderer2.$ORG_NAME

mkdir -p $VOLUME_PATH/crypto-config/$TLS_CA_NAME/admin/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/$CA_NAME/tlsca/admin/tlscacerts

mkdir -p $VOLUME_PATH/crypto-config/$CA_NAME/tlsca/msp/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/$ORDERER_0_NAME/tlsca/msp/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/$ORDERER_1_NAME/tlsca/msp/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/$ORDERER_2_NAME/tlsca/msp/tlscacerts

#Copy CA config
cp ./fabric-tlsca-server-config.yaml $VOLUME_PATH/crypto-config/$TLS_CA_NAME/fabric-ca-server-config.yaml
