#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Approve and commit chaincode

#See output from the start.sh (install-chaincode.sh) script to get ID
export PACKAGE_ID=DatasetContract:ae28e1817bb901af2474325c79e544887be1bd831042237ec12e5462f9b68c83
export VERSION=1.0
export SEQUENCE=1

export CHANNEL_NAME=eckochannel
export ORG_NAME=eckochain.example.com
export ORDERER_ORG_NAME=orderers.eckochain.example.com
export ORDERER_NAME=orderer0.$ORDERER_ORG_NAME
export VOLUME_PATH=/eckostorage/fabric_data
export FABRIC_BIN_PATH=/home/ubuntu/bin
cd .. && export FABRIC_CFG_PATH=$PWD && cd $ORG_NAME
export ORDERER_CA_TLS_FILE=$VOLUME_PATH/crypto-config/msp.$ORDERER_ORG_NAME/tlscacerts/tlsca.pem

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

$FABRIC_BIN_PATH/peer lifecycle chaincode approveformyorg -o $ORDERER_NAME:7050 --tls --cafile $ORDERER_CA_TLS_FILE --channelID $CHANNEL_NAME --name DatasetContract --version $VERSION --package-id $PACKAGE_ID --sequence $SEQUENCE --peerAddresses $PEER_NAME:7051 --tlsRootCertFiles $PEER_TLS_ROOTCERT --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth
$FABRIC_BIN_PATH/peer lifecycle chaincode commit -o $ORDERER_NAME:7050 --tls --cafile $ORDERER_CA_TLS_FILE --channelID $CHANNEL_NAME --name DatasetContract --version $VERSION --sequence $SEQUENCE --peerAddresses $PEER_NAME:7051 --tlsRootCertFiles $PEER_TLS_ROOTCERT --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth
