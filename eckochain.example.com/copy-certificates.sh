#!/bin/bash
#Create and populate crypto directories

export ORG_NAME=eckochain.example.com
export VOLUME_PATH=/eckostorage/fabric_data

#Create directory structure
mkdir -p $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/ca
mkdir -p $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/tls
mkdir -p $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/msp/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/admin/tls
mkdir -p $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/admin/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/orderer0.$ORG_NAME/enca/tls
mkdir -p $VOLUME_PATH/crypto-config/orderer0.$ORG_NAME/enca/msp/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/orderer1.$ORG_NAME/enca/tls
mkdir -p $VOLUME_PATH/crypto-config/orderer1.$ORG_NAME/enca/msp/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/orderer2.$ORG_NAME/enca/tls
mkdir -p $VOLUME_PATH/crypto-config/orderer2.$ORG_NAME/enca/msp/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/peer0.$ORG_NAME/enca/tls
mkdir -p $VOLUME_PATH/crypto-config/peer0.$ORG_NAME/enca/msp/tlscacerts
mkdir -p $VOLUME_PATH/crypto-config/peer1.$ORG_NAME/enca/tls
mkdir -p $VOLUME_PATH/crypto-config/peer1.$ORG_NAME/enca/msp/tlscacerts

#Copy TLS certificates
cp -L $VOLUME_PATH/crypto-config/ca.$ORG_NAME/tlsca/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/tls/ca.$ORG_NAME-tlscert.crt
cp -L $VOLUME_PATH/crypto-config/ca.$ORG_NAME/tlsca/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/admin/tls/ca.$ORG_NAME-tlscert.crt
cp -L $VOLUME_PATH/crypto-config/orderer0.$ORG_NAME/tlsca/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/orderer0.$ORG_NAME/enca/tls/orderer0.$ORG_NAME-tlscert.crt
cp -L $VOLUME_PATH/crypto-config/orderer1.$ORG_NAME/tlsca/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/orderer1.$ORG_NAME/enca/tls/orderer1.$ORG_NAME-tlscert.crt
cp -L $VOLUME_PATH/crypto-config/orderer2.$ORG_NAME/tlsca/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/orderer2.$ORG_NAME/enca/tls/orderer2.$ORG_NAME-tlscert.crt
cp -L $VOLUME_PATH/crypto-config/peer0.$ORG_NAME/tlsca/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/peer0.$ORG_NAME/enca/tls/peer0.$ORG_NAME-tlscert.crt
cp -L $VOLUME_PATH/crypto-config/peer1.$ORG_NAME/tlsca/msp/signcerts/cert.pem $VOLUME_PATH/crypto-config/peer1.$ORG_NAME/enca/tls/peer1.$ORG_NAME-tlscert.crt

#Copy keys
cp -L $VOLUME_PATH/crypto-config/ca.$ORG_NAME/tlsca/msp/keystore/*_sk $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/tls/ca.$ORG_NAME-priv.key
cp -L $VOLUME_PATH/crypto-config/ca.$ORG_NAME/tlsca/msp/keystore/*_sk $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/admin/tls/ca.$ORG_NAME-priv.key
cp -L $VOLUME_PATH/crypto-config/orderer0.$ORG_NAME/tlsca/msp/keystore/*_sk $VOLUME_PATH/crypto-config/orderer0.$ORG_NAME/enca/tls/orderer0.$ORG_NAME-priv.key
cp -L $VOLUME_PATH/crypto-config/orderer1.$ORG_NAME/tlsca/msp/keystore/*_sk $VOLUME_PATH/crypto-config/orderer1.$ORG_NAME/enca/tls/orderer1.$ORG_NAME-priv.key
cp -L $VOLUME_PATH/crypto-config/orderer2.$ORG_NAME/tlsca/msp/keystore/*_sk $VOLUME_PATH/crypto-config/orderer2.$ORG_NAME/enca/tls/orderer2.$ORG_NAME-priv.key
cp -L $VOLUME_PATH/crypto-config/peer0.$ORG_NAME/tlsca/msp/keystore/*_sk $VOLUME_PATH/crypto-config/peer0.$ORG_NAME/enca/tls/peer0.$ORG_NAME-priv.key
cp -L $VOLUME_PATH/crypto-config/peer1.$ORG_NAME/tlsca/msp/keystore/*_sk $VOLUME_PATH/crypto-config/peer1.$ORG_NAME/enca/tls/peer1.$ORG_NAME-priv.key

#Copy TLS root certificates
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/admin/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/ca.$ORG_NAME/enca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/orderer0.$ORG_NAME/enca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/orderer1.$ORG_NAME/enca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/orderer2.$ORG_NAME/enca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/peer0.$ORG_NAME/enca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
cp -L $VOLUME_PATH/crypto-config/tlsca.$ORG_NAME/ca-cert.pem $VOLUME_PATH/crypto-config/peer1.$ORG_NAME/enca/msp/tlscacerts/tlsca.$ORG_NAME-cert.crt
