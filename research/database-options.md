# Database and visualization options for audit and event logging

Author: Naresh

## Problem statement
The current TDEI system needs a robust, scalable mechanism for data storage (structured and unstructured). There are multiple logging requirements as discussed in #50 which will need to be addressed.
After seggregating them as per the needs, we need to figure out the options for hosting the data in cloud. The research will have to evaluate the following parameters

- Consistency of the data
- Flexibility to serve for multiple evolving storage structures
- Ease of querying and flexibility to add dynamic filters
- Availability for integration with visualization tools
- Cost for both setup and operation

## Research findings

For the above problems, we looked at all the database options that Azure offers along with other popular options of database.

## Databases 

The pricing is calculated with the following requirement:
Data to be stored is 8GB
RUs are about 8 Million per month.
Redundancy is limited to LRU

| | Cosmos DB | Azure Table Storage | PostgreSQL | SQL | MySQL | MongoDB |
|-|-|-|-|-|-|-|
| SQL/NoSQL | NoSQL | NoSQL | Hybrid | SQL | SQL | NoSQL |
| Connectivity and Access | Connector API is available and is same for CosmsoDB and Azure Table Storage | The entities are exposed as oData API | Either connect directly via pgsql or create our own API from the database | Either connect directly via pgsql or create our own API from the database| Either connect directly via pgsql or create our own API from the database| MongoDB Query API is available |
| GraphQL Support | | | | | |
| Structure updates for data stored | Yes | Yes | Possible with a migration script | Possible with a migration script | Possible with a migration script |
| Migration to other data store | can be moved to any document based db. Has provisioning to move to Azure Table storage | Can be easily migrated. Has interoperability to cosmosdb via azure cloud | Export the .sql file and import in other location. Does not operate well with nosql db | Export the .sql file and import in other location. Does not operate well with nosql db| Export the .sql file and import in other location. Does not operate well with nosql db| Can be exported as a .json file and re-imported|
| Pricing per month | $25.36 | $0.65 | $50.56 | $125.22 | $124.83 | (about) $20 (serverless) |
| Throughput | Can take upto 10M operations per second | Limited to 20,000 operations per second | <> | <> | <> | 100k operations per second|

Apart from the above, there needs to be research on the visualization tools that will be used. We will also talk about the visualization tools.

## Visualization

For this comparision, we have taken up about 5 visualization tools into consideration
- [Tableau](https://www.tableau.com/)
- PowerBI
- [Metabase](https://www.metabase.com/)
- [Domo](https://www.domo.com)
- [Qlik Sense](https://www.qlik.com/us/products/qlik-sense)


### Connectivity

| | Cosmos DB | Azure Table Storage | PostgreSQL | SQL | MySQL | MongoDB |
|-|-|-|-|-|-|-|
| Tableau [Connectors](https://help.tableau.com/current/pro/desktop/en-us/exampleconnections_overview.htm) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:|
| PowerBI [Connectors](https://learn.microsoft.com/en-us/power-query/connectors/) | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |  :white_check_mark:|
| Domo [Connectors](https://www.domo.com/appstore/apps?q=azure)| :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| Metabase [Data sources](https://www.metabase.com/data_sources/)| :x:  | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| Qlik Sense [Data sources](https://www.qlik.com/us/products/qlik-sense/data-sources) | :x: | :white_check_mark: | :white_check_mark: | :white_check_mark: | :white_check_mark:|:white_check_mark:|

### Pricing

| | Tableau | PowerBI | Domo | Metabase | Qlik Sense |
|-|-|-|-|-|-|
| Link | [NA]() | [Pricing](https://powerbi.microsoft.com/en-us/pricing/) | [NA](https://www.domo.com/pricing) | [Pricing](https://www.metabase.com/pricing) | |
| Approximate | - | $20 per user/month | | $85 for 5 users per month | $30/user/month |

### Other alternatives
- [Microstrategy](https://www.microstrategy.com/en)
- Google Data studio
- Visual.ly
- Building own dashboard with d3 or any other charting library.



## Conclusion