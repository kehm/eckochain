#!/bin/bash
#SPDX-License-Identifier: Apache-2.0
#Create genesis block

export CHANNEL_NAME=eckochannel
export VOLUME_PATH=/eckostorage/fabric_data
export FABRIC_BIN_PATH=/home/ubuntu/bin

mkdir -p $VOLUME_PATH/channel-artifacts && export FABRIC_CFG_PATH=$PWD
$FABRIC_BIN_PATH/configtxgen -profile InitialOrgsOrdererGenesis -outputBlock $VOLUME_PATH/channel-artifacts/genesis_block.pb -channelID $CHANNEL_NAME
