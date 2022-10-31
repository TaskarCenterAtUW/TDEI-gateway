# System architecture
This document explains the current architectue of the TDEI system and the components along with their interactions.

## Architecture 

![System architecture](./.assets/architecture-diagram-v1.1.jpg)


## Connectivity
Throughout the system there are three ways of communicating between the components
1. Through HTTP interface : This is done for the systems where the requestor waits for the response. Eg. Auth-service. Any service communicating to this may need a response. All the internal HTTP interfaces are secured through internal VPN specific to the Resource Group.
2. Topics: These are for further processing of information. Any service that needs to communicate asynchronously adapts to this strategy. 
3. Database/ Cloud interface : Any database connection or Cloud system connection is done through this.


## Components 

### Client system 
- The client system is reference to all the downstream applications and web-apps that make use of the TDEI system. These are connected to TDEI system via REST APIs exposed.
- The REST APIs are documented via Swagger for publicly available APIs and also internal Swaggers for any internal APIs
- The REST APIs are access-controlled based on the PATH and the METHOD.
- There are two ways of access control : API Key based and Access Token based.

### Gateway
- Gateway acts as the primary interface for all the REST APIs
- Gateway connects to Auth-service via VPN + HTTP connection.
- Gateway performs a minimal check of valid AccessToken or API Key via Auth-service for the API calls.
- Gateway processes all the Authentication requests + User Generation (token generation, role change, adding new user) via Auth-service.
- Gateway code  is currently hosted as Azure DevOps repository with a pipeline to generate container and deploy as App Service.(refer to README.md of gateway repo for this)
- All the upload requests are pre-validated and sent to `File-Service` for processing and response is relayed back to the client.
- All the GET requests are sent to `Data service` for processing and response is relayed back to the client.
- All the requests for getting the status of a request/upload or a job are sent to `Logger service`
- The gateway also hosts the Swagger documentation that is exposed to the public.

### Auth service 
- Responsible for authentication of users, user management
- Exposes internal HTTP APIs for authenticating a user and also validating a particular user against a permission. (eg. whether a specific userId is permitted to upload gtfs-flex)
- For all the internal purposes, it connects to Keycloak server via Keycloak SDK + VPN.
- Though there is swagger documentation of the APIs for this, these are not exposed to the public and are for internal use only
- The micro service code is currently hosted in Azure DevOps repository with a pipeline to generate container and deploy as App Service. Refer to README.md of auth-n-z repo 
- The internal HTTP APIs are accessible for all the micro-services within the system but not publicly available.

### Keycloak
- Responsible for user management of the system
- An Opensource implementation of Keycloak along with certain customization is hosted as App Service in Azure cloud. 
- Keycloak version picked is 19.0.2
- It does not expose API to any other system other than Auth-service 
- It connects to TDEI Users Database for all the persistence.
- Currently the code for this is hosted offline and is deployed manually.

### TDEI Users
- Responsible for hosting the user information
- Hosted as Postgresql instance in Azure cloud
- Data encryption for password is done at column level
- Data encryption at rest is done volume level by Azure 
- Has open connection only with the Keycloak hosted in the system.

### File Service
- Responsible for handling the file upload loads for the system.
- Has methodology to verify and upload the files per role.
- Verifies the permission of the user and then uploads the files appropriately to blob storage.
- Once the upload is done, creates a unique jobId and sends the following information to `ms-data-upload-topic`
    - jobId
    - file path for the files uploaded
    - user-access-token/credentials
    - type of file
    - any additional information based on the file type and request type.
- Also writes to `ms-logger-topic` on the actions performed. (audit logs.)
- This is not yet built but is planned to be hosted as a container with Azure App service.

### Blob Storage 
- Contains all the files uploaded to the system.
- Read/write permissions to this are to be configured as per the need for the micro-service.
- Currently hosted with ZRS (Zone redundant storage). Refer to [Azure Redundancy](../general/azure-redundancy.md)


