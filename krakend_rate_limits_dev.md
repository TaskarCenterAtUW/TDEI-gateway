# KrakenD Rate Limit Configuration (DEV Environment)

## Endpoint Rate Limiting

| HTTP Method | Endpoint | Max Rate | Every |
| ----------- | -------- | -------- | ----- |
| POST | /api/v1/refresh-token | 100 | 10s |
| POST | /api/v1/login | 100 | 10s |
| POST | /api/v1/authenticate | 100 | 10s |
| POST | /api/v1/regenerate-api-key | 100 | 10s |
| GET | /api/v1/jobs | 100 | 10s |
| POST | /api/v1/osw/union | 100 | 10s |
| POST | /api/v1/osw/dataset-bbox | 100 | 10s |
| POST | /api/v1/osw/dataset-tag-road | 100 | 10s |
| POST | /api/v1/osw/spatial-join | 100 | 10s |
| POST | /api/v1/osw/dataset-inclination/{tdei_dataset_id} | 100 | 10s |
| GET | /api/v1/project-groups | 100 | 10s |
| DELETE | /api/v1/dataset/{tdei_dataset_id} | 100 | 10s |
| GET | /api/v1/datasets | 100 | 10s |
| GET | /api/v1/osw/{tdei_dataset_id} | 100 | 10s |
| GET | /api/v1/osw/versions | 100 | 10s |
| POST | /api/v1/osw/publish/{tdei_dataset_id} | 100 | 10s |
| POST | /api/v1/osw/confidence/{tdei_dataset_id} | 100 | 10s |
| POST | /api/v1/osw/quality-metric/ixn/{tdei_dataset_id} | 100 | 10s |
| POST | /api/v1/osw/quality-metric/tag/{tdei_dataset_id} | 100 | 10s |
| GET | /api/v1/job/download/{job_id} | 100 | 10s |
| GET | /api/v1/gtfs-flex/{tdei_dataset_id} | 100 | 10s |
| GET | /api/v1/gtfs-flex/versions | 100 | 10s |
| POST | /api/v1/gtfs-flex/publish/{tdei_dataset_id} | 100 | 10s |
| GET | /api/v1/gtfs-pathways/{tdei_dataset_id} | 100 | 10s |
| GET | /api/v1/gtfs-pathways/versions | 100 | 10s |
| POST | /api/v1/gtfs-pathways/publish/{tdei_dataset_id} | 100 | 10s |
| GET | /api/v1/services | 10 | 10s |
| GET | /api/v1/api | 10 | 10s |
| GET | /api/v1/system-metrics | 10 | 10s |
| GET | /api/v1/data-metrics | 10 | 10s |
| GET | /api/v1/service-metrics/{tdei_project_group_id} | 10 | 10s |
| POST | /api/v1/recover-password | 10 | 10s |
| POST | /api/v1/verify-email | 100 | 10s |

## User Rate Limiting

| HTTP Method | Endpoint | Client Max Rate | Every | Strategy |
| ----------- | -------- | --------------- | ----- | -------- |
| PUT | /api/v1/metadata/{tdei_dataset_id} | 10 | 10s | ip |
| POST | /api/v1/dataset/clone/{tdei_dataset_id}/{tdei_project_group_id}/{tdei_service_id} | 10 | 10s | ip |
| POST | /api/v1/osw/upload/{tdei_project_group_id}/{tdei_service_id} | 10 | 10s | ip |
| POST | /api/v1/osw/validate | 10 | 10s | ip |
| POST | /api/v1/osw/convert | 10 | 10s | ip |
| POST | /api/v1/gtfs-flex/upload/{tdei_project_group_id}/{tdei_service_id} | 10 | 10s | ip |
| POST | /api/v1/gtfs-flex/validate | 10 | 10s | ip |
| POST | /api/v1/gtfs-pathways/upload/{tdei_project_group_id}/{tdei_service_id} | 10 | 10s | ip |
| POST | /api/v1/gtfs-pathways/validate | 10 | 10s | ip |

