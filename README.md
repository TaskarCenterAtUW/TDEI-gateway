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

docker build --build-arg ENV=dev -t tdei-api-gateway:v1 .

## Building for Staging

docker build --build-arg ENV=stage -t tdei-api-gateway:v1 .

## Building for Production

docker build --build-arg ENV=prod -t tdei-api-gateway:v1 .

## Deploying

docker run -p 8080:8080 tdei-api-gateway:v1 -d -c krakend.json


## Generate Krakend Config from flexibile configuration

Below command run on terminal will generate `krakend.json` file output.
```
FC_ENABLE=1 \
FC_SETTINGS="config/settings/dev" \
FC_PARTIALS="config/partials" \
FC_TEMPLATES="config/templates" \
FC_OUT="krakend.json" \
krakend check -t -c "krakend.tmpl"

```

## Debugging the configuration and running the KrakenD locally for testing

The following command runs the Docker image in watch mode. It dynamically reads the flexible configuration from the `config` folder and generates the `krakend.json` file at runtime. Any modifications made to the configuration files within the `config` folder will automatically trigger a rebuild of `krakend.json` and refresh the application with the updated settings.

```
$docker run -p 8181:8080 --rm -it -v "$PWD:/etc/krakend" \
    -e "FC_ENABLE=1" \
    -e "FC_OUT=krakend.json" \
    -e "FC_PARTIALS=/etc/krakend/config/partials" \
    -e "FC_SETTINGS=/etc/krakend/config/settings/dev" \
    -e "FC_TEMPLATES=/etc/krakend/config/templates" \
    devopsfaith/krakend:2.4.3-watch run -c krakend.tmpl
```

## Rate Limiting in KrakenD API Gateway

Each API endpoint in the KrakenD gateway is configured with a specific **rate limit**. These configurations can be found in:

`config/settings/{env}/endpoints.json`

Each endpoint is assigned a rate limit based on **practical usage scenarios** of the end user. More details on KrakenD rate limiting can be found [here](https://www.krakend.io/docs/v2.4/endpoints/rate-limit/).

The **TDEI** gateway uses a mix of **endpoint rate-limiting** and **user rate-limiting** strategies for better performance.

### **Rate limit Strategy**

| Rate Limiting Type      | Applied On  | Limit Scope | Status Code on Exceeding Limit |
|------------------------|------------|------------|------------------------------|
| **User Rate Limiting**  | Individual Clients (IP-based) | Per user (e.g., 20 requests/user per 60s) | `429 Too Many Requests` |
| **Global Endpoint Limiting** | Entire API Endpoint | Total requests across all users | `503 Service Unavailable` |


### User-Based Rate Limiting (Per Client/IP)
Example endpoint configuration

```json
"extra_config": {
    "qos/ratelimit/router": {
        "client_max_rate": 20,
        "every": "60s",
        "strategy": "ip"
    }
}
```
### How It Works?
- Each **client (identified by IP)** can make **20 requests every 60 seconds**.
- If a client exceeds this limit, **KrakenD rejects further requests** with:
  - **`429 Too Many Requests`**


### Global Endpoint Rate Limiting (All Users Combined)
Example endpoint configuration

```json
"extra_config": {
    "qos/ratelimit/router": {
        "max_rate": 20,
        "every": "60s"
    }
}
```
### How It Works?
- All users **combined** can make a total of **20 requests every 60 seconds**.
- If the total requests exceed this limit, **KrakenD rejects connections** with:
  - **`503 Service Unavailable`**



## CLI to test endpoint Rate limit 

We can use any HTTP cli client or write bash script to test the rate limit of any endpoint. 

We are using `hey` one of the HTTP client to test the endpoint rate limit. Official documentation for cli arguments can be found [here](https://github.com/rakyll/hey) , installation instructions can be found [here](https://github.com/rakyll/hey?tab=readme-ov-file#installation)

```
hey -z 60s -q 5 -c 1 -H "x-api-key: xxx-xxx-xxx-xxx" "http://localhost:8181/{endpoint-to-test}"
```
 
- Replace `xxx-xxx-xxx-xxx` with actual api-key if testing TDEI GET calls. if want to test [PUT|POST] please compose the header with `authorization` header and make the request. 
- Replace `{endpoint-to-test}` with api endpoints to test. 

#### Explanation:

| Option  | Description  |
|---------|-------------|
| `hey`   | Load testing tool to simulate requests. |
| `-z 60s` | Runs the test for **60 seconds** (duration-based test). |
| `-q 5`   | Sets the request rate to **5 requests per second** (queries per second). |
| `-c 1`   | Runs **1 concurrent request** at a time (single user simulation). |
| `-H "x-api-key: xxx-xxx-xxx-xxx"` | Adds a **custom header** (`x-api-key`) for authentication. |
| `"http://localhost:8181/{endpoint-to-test}"` | The target **Krakend API endpoint** being tested. |

#### Purpose:
- Simulates **5 requests per second** for **60 seconds** to test Krakend’s **rate-limiting configuration**.  
- Ensures the **configured rate limits are correctly enforced** by Krakend.  
- Helps analyze how many requests succeed (`200 OK`) and how many are rejected (`429 Too Many Requests`).  

#### Expected Behavior:
- If the configured rate limit allows only **X requests per second**, any excess requests should return a **429 Too Many Requests** response.  
- The results should reflect the correct enforcement of Krakend’s rate-limiting policy.


## Generate Rate Limiting Report

Report for each environement can be generated by running below python command , which will generate report by name `krakend_rate_limit_{env}.md`.

```
python3 generate-rate-limit-report.py env 
```

- `env` is environment argument, valid arguments are dev, prod, stage. 