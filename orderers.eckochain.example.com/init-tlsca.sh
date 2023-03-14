#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Create TLS certificates for CA and orderers

export ORG_NAME=orderers.eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data
export TLS_CA_PORT=10054
export TLS_CA_NAME=tlsca.$ORG_NAME
export CA_NAME=ca.$ORG_NAME
export CA_ADMIN_NAME=encaadmin
export ORDERER_0_NAME=orderer0.$ORG_NAME
export ORDERER_1_NAME=orderer1.$ORG_NAME
export ORDERER_2_NAME=orderer2.$ORG_NAME
export FABRIC_BIN_PATH=/home/ubuntu/bin

# Copy TLS root cert
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/$TLS_CA_NAME/admin/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/$CA_NAME/tlsca/admin/tlscacerts/tlsca.$ORG_NAME-cert.crt

cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/$CA_NAME/tlsca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/$ORDERER_0_NAME/tlsca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/$ORDERER_1_NAME/tlsca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/$ORDERER_2_NAME/tlsca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt

# Enroll boostrap admin identity
export FABRIC_CA_CLIENT_HOME=$VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/admin
export FABRIC_CA_CLIENT_MSPDIR=msp
export FABRIC_CA_CLIENT_TLS_CERTFILES=tlscacerts/tlsca.$ORG_NAME-cert.crt
$FABRIC_BIN_PATH/fabric-ca-client enroll -d -u https://tlscaadmin:secret@$TLS_CA_NAME:$TLS_CA_PORT --enrollment.profile tls --tls.certfiles $FABRIC_CA_CLIENT_TLS_CERTFILES --mspdir $FABRIC_CA_CLIENT_MSPDIR --csr.hosts "$TLS_CA_NAME"
 
# Register nodes
$FABRIC_BIN_PATH/fabric-ca-client register -d --id.name $CA_ADMIN_NAME --id.secret secret -u https://$TLS_CA_NAME:$TLS_CA_PORT --tls.certfiles $FABRIC_CA_CLIENT_TLS_CERTFILES --mspdir $FABRIC_CA_CLIENT_MSPDIR
$FABRIC_BIN_PATH/fabric-ca-client register -d --id.name $ORDERER_0_NAME --id.secret secret -u https://$TLS_CA_NAME:$TLS_CA_PORT --tls.certfiles $FABRIC_CA_CLIENT_TLS_CERTFILES --mspdir $FABRIC_CA_CLIENT_MSPDIR
$FABRIC_BIN_PATH/fabric-ca-client register -d --id.name $ORDERER_1_NAME --id.secret secret -u https://$TLS_CA_NAME:$TLS_CA_PORT --tls.certfiles $FABRIC_CA_CLIENT_TLS_CERTFILES --mspdir $FABRIC_CA_CLIENT_MSPDIR
$FABRIC_BIN_PATH/fabric-ca-client register -d --id.name $ORDERER_2_NAME --id.secret secret -u https://$TLS_CA_NAME:$TLS_CA_PORT --tls.certfiles $FABRIC_CA_CLIENT_TLS_CERTFILES --mspdir $FABRIC_CA_CLIENT_MSPDIR

# Enroll nodes
export FABRIC_CA_CLIENT_TLS_CERTFILES=msp/tlscacerts/tlsca.$ORG_NAME-cert.crt

export FABRIC_CA_CLIENT_HOME=$VOLUME_PATH/crypto-config/$CA_NAME/tlsca
$FABRIC_BIN_PATH/fabric-ca-client enroll -d -u https://$CA_ADMIN_NAME:secret@$TLS_CA_NAME:$TLS_CA_PORT --enrollment.profile tls --csr.hosts "*.$ORG_NAME"

export FABRIC_CA_CLIENT_HOME=$VOLUME_PATH/crypto-config/$ORDERER_0_NAME/tlsca
$FABRIC_BIN_PATH/fabric-ca-client enroll -d -u https://$ORDERER_0_NAME:secret@$TLS_CA_NAME:$TLS_CA_PORT --enrollment.profile tls --csr.hosts "$ORDERER_0_NAME"

export FABRIC_CA_CLIENT_HOME=$VOLUME_PATH/crypto-config/$ORDERER_1_NAME/tlsca
$FABRIC_BIN_PATH/fabric-ca-client enroll -d -u https://$ORDERER_1_NAME:secret@$TLS_CA_NAME:$TLS_CA_PORT --enrollment.profile tls --csr.hosts "$ORDERER_1_NAME"

export FABRIC_CA_CLIENT_HOME=$VOLUME_PATH/crypto-config/$ORDERER_2_NAME/tlsca
$FABRIC_BIN_PATH/fabric-ca-client enroll -d -u https://$ORDERER_2_NAME:secret@$TLS_CA_NAME:$TLS_CA_PORT --enrollment.profile tls --csr.hosts "$ORDERER_2_NAME"
