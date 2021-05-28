#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Install, approve and commit chaincode

export CHANNEL_NAME=eckochannel
export ORG_NAME=eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data
export FABRIC_BIN_PATH=/home/ubuntu/bin
export COLLECTIONS_CONFIG_PATH=./collections-config.json
cd .. && export FABRIC_CFG_PATH=$PWD && cd $ORG_NAME
export CA_TLS_FILE=$VOLUME_PATH/crypto-config/msp.$ORG_NAME/tlscacerts/tlsca.pem

#See output from chaincode install or run chaincode queryinstalled to get ID
export DATASET_CONTRACT_ID=eb0291ce39b91ca9a40106133533b07e9c9421744e41e0f9249230e0bc475ede

export ORDERER_NAME=orderer0.$ORG_NAME

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
$FABRIC_BIN_PATH/peer lifecycle chaincode approveformyorg -o $ORDERER_NAME:7050 --channelID $CHANNEL_NAME --name DatasetContract --version 1.0 --package-id DatasetContract:$DATASET_CONTRACT_ID --sequence 1 --collections-config $COLLECTIONS_CONFIG_PATH --tls --cafile $CA_TLS_FILE --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth
$FABRIC_BIN_PATH/peer lifecycle chaincode commit -o $ORDERER_NAME:7050 --channelID $CHANNEL_NAME --name DatasetContract --version 1.0 --sequence 1 --collections-config $COLLECTIONS_CONFIG_PATH --tls --cafile $CA_TLS_FILE --peerAddresses $PEER_NAME:7051 --tlsRootCertFiles $PEER_TLS_ROOTCERT --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth

export PEER_NAME=peer1.$ORG_NAME
export PEER_TLS_ROOTCERT=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_ADDRESS=$PEER_NAME:8051
export CORE_PEER_MSPCONFIGPATH=$VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/admin/msp

export CORE_PEER_TLS_ROOTCERT_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_TLS_CLIENTCERT_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_TLS_CLIENTKEY_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-priv.key
$FABRIC_BIN_PATH/peer lifecycle chaincode install DatasetContract.tar.gz --peerAddresses $PEER_NAME:8051 --tlsRootCertFiles $PEER_TLS_ROOTCERT --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth
$FABRIC_BIN_PATH/peer lifecycle chaincode querycommitted -o $ORDERER_NAME:7050 --channelID $CHANNEL_NAME --tls --cafile $CA_TLS_FILE --peerAddresses $PEER_NAME:8051 --tlsRootCertFiles $PEER_TLS_ROOTCERT
$FABRIC_BIN_PATH/peer chaincode list --installed

sudo rm -fr DatasetContract.tar.gz
