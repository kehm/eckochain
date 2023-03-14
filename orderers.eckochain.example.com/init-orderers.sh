#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Join orderers to channel

export CHANNEL_NAME=eckochannel
export ORG_NAME=orderers.eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data
export FABRIC_BIN_PATH=/home/ubuntu/bin
cd .. && export FABRIC_CFG_PATH=$PWD && cd $ORG_NAME
export CA_TLS_FILE=$VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
export ADMIN_TLS_SIGN_CERT=$VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/tls/ca.$ORG_NAME-tlscert.crt
export ADMIN_TLS_PRIVATE_KEY=$VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/tls/ca.$ORG_NAME-priv.key

export ORDERER_ADMIN_LISTENADDRESS=orderer0.$ORG_NAME:7443
$FABRIC_BIN_PATH/osnadmin channel join --channelID $CHANNEL_NAME --config-block $VOLUME_PATH/channel-artifacts/genesis_block.pb -o $ORDERER_ADMIN_LISTENADDRESS --ca-file $CA_TLS_FILE --client-cert $ADMIN_TLS_SIGN_CERT --client-key $ADMIN_TLS_PRIVATE_KEY

export ORDERER_ADMIN_LISTENADDRESS=orderer1.$ORG_NAME:8443
$FABRIC_BIN_PATH/osnadmin channel join --channelID $CHANNEL_NAME --config-block $VOLUME_PATH/channel-artifacts/genesis_block.pb -o $ORDERER_ADMIN_LISTENADDRESS --ca-file $CA_TLS_FILE --client-cert $ADMIN_TLS_SIGN_CERT --client-key $ADMIN_TLS_PRIVATE_KEY

export ORDERER_ADMIN_LISTENADDRESS=orderer2.$ORG_NAME:9443
$FABRIC_BIN_PATH/osnadmin channel join --channelID $CHANNEL_NAME --config-block $VOLUME_PATH/channel-artifacts/genesis_block.pb -o $ORDERER_ADMIN_LISTENADDRESS --ca-file $CA_TLS_FILE --client-cert $ADMIN_TLS_SIGN_CERT --client-key $ADMIN_TLS_PRIVATE_KEY
