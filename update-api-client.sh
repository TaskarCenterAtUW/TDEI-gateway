#!/bin/sh

result=`git diff --name-only HEAD HEAD~1`

# Check if atleast one file is modified in src/main folder
if [[ $result == *"src/main"* ]]; then
    echo "Changes present in src/main folder.."
    curl -L \
        -X POST \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer $1"\
        -H "X-GitHub-Api-Version: 2022-11-28" \
        https://api.github.com/repos/TaskarCenterAtUW/TDEI-api-client/actions/workflows/autogenerate_api_client/dispatches \
        -d '{"ref":"main"}'
else
    echo "No changes in src/main folder"
fi