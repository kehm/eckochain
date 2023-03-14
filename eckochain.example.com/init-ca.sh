#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Register and enroll peer and client with Fabric CA

export ORG_NAME=eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data
export CLIENT_NAME=ecko.example.com
export CA_PORT=7054
export CA_NAME=ca.$ORG_NAME
export PEER_0_NAME=peer0.$ORG_NAME
export FABRIC_BIN_PATH=/home/ubuntu/bin

export FABRIC_CA_CLIENT_HOME=$VOLUME_PATH/crypto-config/$CA_NAME/enca/admin
export FABRIC_CA_CLIENT_MSPDIR=msp
export FABRIC_CA_CLIENT_TLS_CERTFILES=tlscacerts/tlsca.$ORG_NAME-cert.crt
export FABRIC_CA_CLIENT_TLS_CLIENT_CERTFILE=tls/$CA_NAME-tlscert.crt
export FABRIC_CA_CLIENT_TLS_CLIENT_KEYFILE=tls/$CA_NAME-priv.key
$FABRIC_BIN_PATH/fabric-ca-client enroll -d -u https://encaadmin:secret@$CA_NAME:$CA_PORT --tls.certfiles $FABRIC_CA_CLIENT_TLS_CERTFILES --mspdir $FABRIC_CA_CLIENT_MSPDIR
$FABRIC_BIN_PATH/fabric-ca-client register -d --id.name $PEER_0_NAME --id.secret secret --id.type peer -u https://$CA_NAME:$CA_PORT
$FABRIC_BIN_PATH/fabric-ca-client register -d --id.name $CLIENT_NAME --id.secret secret --id.type client -u https://$CA_NAME:$CA_PORT

export FABRIC_CA_CLIENT_HOME=$VOLUME_PATH/crypto-config/$PEER_0_NAME/enca
export FABRIC_CA_CLIENT_TLS_CLIENT_CERTFILE=tls/$PEER_0_NAME-tlscert.crt
export FABRIC_CA_CLIENT_TLS_CLIENT_KEYFILE=tls/$PEER_0_NAME-priv.key
$FABRIC_BIN_PATH/fabric-ca-client enroll -d -u https://$PEER_0_NAME:secret@$CA_NAME:$CA_PORT --tls.certfiles msp/$FABRIC_CA_CLIENT_TLS_CERTFILES --tls.client.certfile $FABRIC_CA_CLIENT_TLS_CLIENT_CERTFILE --tls.client.keyfile $FABRIC_CA_CLIENT_TLS_CLIENT_KEYFILE

#Copy admin certs
mkdir -p $VOLUME_PATH/crypto-config/$CA_NAME/enca/admin/msp/admincerts
mkdir -p $VOLUME_PATH/crypto-config/$PEER_0_NAME/enca/msp/admincerts
cp $VOLUME_PATH/crypto-config/$CA_NAME/enca/admin/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/$CA_NAME/enca/admin/msp/admincerts/$ORG_NAME-admin-cert.pem
cp $VOLUME_PATH/crypto-config/$CA_NAME/enca/admin/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/$PEER_0_NAME/enca/msp/admincerts/$ORG_NAME-admin-cert.pem

#Create organization msp folder
mkdir -p $VOLUME_PATH/crypto-config/msp.$ORG_NAME/admincerts
mkdir -p $VOLUME_PATH/crypto-config/msp.$ORG_NAME/cacerts
mkdir -p $VOLUME_PATH/crypto-config/msp.$ORG_NAME/tlscacerts
cp $VOLUME_PATH/crypto-config/$CA_NAME/enca/admin/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/msp.$ORG_NAME/admincerts/$ORG_NAME-admin-cert.pem
cp $VOLUME_PATH/crypto-config/$PEER_0_NAME/enca/msp/cacerts/*.pem $VOLUME_PATH/crypto-config/msp.$ORG_NAME/cacerts/ca.pem
cp $VOLUME_PATH/crypto-config/$PEER_0_NAME/enca/msp/$FABRIC_CA_CLIENT_TLS_CERTFILES $VOLUME_PATH/crypto-config/msp.$ORG_NAME/tlscacerts/tlsca.pem
cp ./config.yaml $VOLUME_PATH/crypto-config/msp.$ORG_NAME/config.yaml
