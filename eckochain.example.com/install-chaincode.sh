#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Install chaincode

export CHANNEL_NAME=eckochannel
export ORG_NAME=eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data
export FABRIC_BIN_PATH=/home/ubuntu/bin
cd .. && export FABRIC_CFG_PATH=$PWD && cd $ORG_NAME
export CA_TLS_FILE=$VOLUME_PATH/crypto-config/msp.$ORG_NAME/tlscacerts/tlsca.pem

export PEER_NAME=peer0.$ORG_NAME
export PEER_TLS_ROOTCERT=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_ADDRESS=$PEER_NAME:7051
export CORE_PEER_LOCALMSPID="$ORG_NAME-MSP"
export CORE_PEER_MSPCONFIGPATH=$VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/admin/msp

export CORE_PEER_TLS_ENABLED=true
export CORE_PEER_TLS_CLIENTAUTHREQUIRED=true
export CORE_PEER_TLS_ROOTCERT_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_TLS_CLIENTCERT_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_TLS_CLIENTKEY_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-priv.key

$FABRIC_BIN_PATH/peer lifecycle chaincode package DatasetContract.tar.gz --path ../chaincode/DatasetContract --lang java --label DatasetContract
$FABRIC_BIN_PATH/peer lifecycle chaincode install DatasetContract.tar.gz --peerAddresses $PEER_NAME:7051 --tlsRootCertFiles $PEER_TLS_ROOTCERT --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth
$FABRIC_BIN_PATH/peer lifecycle chaincode queryinstalled --peerAddresses $PEER_NAME:7051 --tlsRootCertFiles $PEER_TLS_ROOTCERT 

sudo rm -fr DatasetContract.tar.gz
