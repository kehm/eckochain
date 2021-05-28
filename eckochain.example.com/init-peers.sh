#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Join peers to channel

export CHANNEL_NAME=eckochannel
export ORG_NAME=eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data
export ORDERER_NAME=orderer0.$ORG_NAME
export FABRIC_BIN_PATH=/home/ubuntu/bin
cd .. && export FABRIC_CFG_PATH=$PWD && cd $ORG_NAME

export CA_TLS_FILE=$VOLUME_PATH/crypto-config/msp.$ORG_NAME/tlscacerts/tlsca.pem
export CORE_PEER_LOCALMSPID="$ORG_NAME-MSP"
export CORE_PEER_MSPCONFIGPATH=$VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/admin/msp
export CORE_PEER_TLS_ENABLED=true
export CORE_PEER_TLS_CLIENTAUTHREQUIRED=true

export PEER_NAME=peer0.$ORG_NAME
export CORE_PEER_ADDRESS=$PEER_NAME:7051
export CORE_PEER_TLS_ROOTCERT_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
export CORE_PEER_TLS_CLIENTCERT_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_TLS_CLIENTKEY_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-priv.key
$FABRIC_BIN_PATH/peer channel fetch config $VOLUME_PATH/channel-artifacts/config_block.pb -o $ORDERER_NAME:7050 -c $CHANNEL_NAME --tls --cafile $CA_TLS_FILE --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth
$FABRIC_BIN_PATH/peer channel join -b $VOLUME_PATH/channel-artifacts/config_block.pb --tls --cafile $CA_TLS_FILE --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth

export PEER_NAME=peer1.$ORG_NAME
export CORE_PEER_ADDRESS=$PEER_NAME:8051
export CORE_PEER_TLS_ROOTCERT_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_TLS_CLIENTCERT_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-tlscert.crt
export CORE_PEER_TLS_CLIENTKEY_FILE=$VOLUME_PATH/crypto-config/$PEER_NAME/enca/tls/$PEER_NAME-priv.key
$FABRIC_BIN_PATH/peer channel join -b $VOLUME_PATH/channel-artifacts/config_block.pb --tls --cafile $CA_TLS_FILE --certfile $CORE_PEER_TLS_CLIENTCERT_FILE --keyfile $CORE_PEER_TLS_CLIENTKEY_FILE --clientauth
