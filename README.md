# TDEI API Gateway
The TDEI API Gateway is the primary entry point to the TDEI system. It plays a pivotal role in authenticating users and applications, ensuring secure access. Additionally, it facilitates custom analytics, enabling data-driven decision-making within the TDEI ecosystem. This repository contains the essential code and configurations for the TDEI API Gateway.

## System Requirement
Linux Latest LTS ( >= Ubuntu 22.04)
golang 1.20.6
krakenD 2.4.3
Docker

## Building the Docker Image
Building the docker image runs the Dockerfile, which builds the configuration 'krakend.json' file by running the krakend.tmpl file with environment specific defined values. 

## Building & deploying for local development

docker build --build-arg ENV=dev -t tdei-api-gateway:v1 .

### Deploying

docker run -p 8080:8080 tdei-api-gateway:v1 -d -c krakend.json

## Deployment to environments
Deployment to the environments is managed through GitHub Workflows. The workflow automates the build and deployment process based on the target environment.

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

## Endpoint Settings
Any new endpoints that need to be exposed externally should be added to the `endpoints.json` file located at `settings/{env}`.

## Plugin

The TDEI Gateway is customized using a plugin, with the code located in the plugins folder. The main entry point for the plugin is the main.go file.

All incoming requests to the TDEI Gateway are processed through the plugin, where authentication is enforced using either an API key or an access token. If the authentication fails due to an invalid or expired token, the request is rejected at the gateway level. If the validation succeeds, the request is forwarded to the backend service.

### Plugin Environment Variables

Plugin-specific environment variables are configured in the service.json file located under the settings/{env} folder. These variables are passed to the plugin at build time.
```
 "extra_config": {
        "plugin/http-server": {
            "name": [
                "tdei-api-gateway"
            ],
            "tdei-api-gateway": {
                "api_key_header": {name of the api key header},
                "auth_server": "{tdei auth server url}",
                "pass-through-urls": "{pass thorugh url suffix which does not require authentication by gateway}",
                "tdei-api-documentation-url": "{api documentation url}",
                "tdei-api-specification-url": "{api specification url}"
            }
        },
 }
```
