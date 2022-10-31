# Monitoring and auditing strategy
This document references the proposed strategy to accomodate the performance, audit and monitoring requirements of the system. The base requirements for this are captured under 
#50 
All the monitoring and auditing needs are handled by composing them into logs and storing/retreiving them appropriately. Once the logs are persisted, another micro-service exposes the information 
required as APIs.

The overall architecture for capturing the logs is as follows:
![Log architectue](./.assets/monitoring-architecture.jpg)
NOTE: Diagram to change the queue
## Architecture
- All the microservices send the audit/log/monitoring information as messages to `ms-logger-queue` via the methods exposed in `Core` library.
- The `Log service` is a microservice that listens to events/messages on `ms-logger-queue` and logs into appropriate location.
- The records are either stored in AppInsights or into Log DB based on their differentiation. 
- Log DB is hosted as Azure Storage tables with different tables handling different types of logs. However, it is under a single Storage account. Thus, its considered single entity on cloud.
- App Insights is configured within Log service
- All the API requirements for monitoring or notifications are exposed via `Reporting service`



## Logs differentiation
Based on the kind of information logged and their relevance, the logs are divided as follows:

### Diagnostic logs 
These are logs used by the developers of the micro-services to figure out any issues related to the development and also any additional information that needs to be logged 
for development and debugging usage.

There are two ways in which the logs can be captured.
1. Using the Core() methods . (eg. `Core.getLogger().info("xxxx")`)
2. Using the regular `console.x` methods. (eg. `console.log("xxx")`)