# Introduction
The TDEI API Gateway is the primary entry point to the TDEI system. It plays a pivotal role in authenticating users and applications, ensuring secure access. Additionally, it facilitates custom analytics, enabling data-driven decision-making within the TDEI ecosystem. This repository contains the essential code and configurations for the TDEI API Gateway.

## System Requirement
Linux Latest LTS ( >= Ubuntu 22.04)

golang 1.20.6

krakenD 2.4.3

Docker


## Building the Docker Image

Building the docker image runs the Dockerfile, which builds the configuration 'krakend.json' file by running the krakend.tmpl file with environment specific defined values. 

## Building for Development

docker build --build-arg ENV=dev -t tdei-api-krakend:v1 .

## Building for Staging

docker build --build-arg ENV=stage -t tdei-api-krakend:v1 .

## Building for Production

docker build --build-arg ENV=prod -t tdei-api-krakend:v1 .

## Deploying

docker run -p 8080:8080 tdei-api-krakend:v1 run -d -c krakend.json

