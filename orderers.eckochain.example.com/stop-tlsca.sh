#!/bin/bash
#SPDF-License-Identifier: Apache-2.0
#Stop TLS CA container

export ORG_NAME=orderers.eckochain.example.com
export TLS_CA_NAME=tlsca.$ORG_NAME

docker stop $TLS_CA_NAME
