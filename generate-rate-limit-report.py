import json
import sys
import os

# Validate command-line arguments
if len(sys.argv) != 2:
    print("Usage: python generate_rate_limits_md.py <env>")
    sys.exit(1)

env = sys.argv[1]  # Get environment from command-line argument
input_file = f"config/settings/{env}/endpoints.json"  # Construct file path

# Check if file exists
if not os.path.exists(input_file):
    print(f"Error: JSON file '{input_file}' not found.")
    sys.exit(1)

# Read the JSON data from the file
try:
    with open(input_file, "r") as file:
        data = json.load(file)
        endpoints = data.get("endpoints", [])  # Extract "endpoints" list
except Exception as e:
    print(f"Error reading JSON file: {e}")
    sys.exit(1)

# Prepare Markdown content
md_content = f"# KrakenD Rate Limit Configuration ({env.upper()} Environment)\n\n"

# Separate rate-limiting strategies
endpoint_limit = []
user_limit = []

for entry in endpoints:  # Loop through "endpoints" list
    http_method = entry.get("http_method", "N/A")
    endpoint = entry.get("endpoint", "N/A")
    config = entry.get("extra_config", {}).get("qos/ratelimit/router", {})
    max_rate = config.get("max_rate", "N/A")
    client_max_rate = config.get("client_max_rate", "N/A")
    every = config.get("every", "N/A")
    strategy = config.get("strategy", "N/A")

    if "client_max_rate" in config:
        user_limit.append((http_method, endpoint, client_max_rate, every, strategy))
    else:
        endpoint_limit.append((http_method, endpoint, max_rate, every))

# Function to generate markdown table
def generate_table(title, headers, data):
    if not data:
        return ""
    table = f"## {title}\n\n"
    table += f"| {' | '.join(headers)} |\n"
    table += f"| {' | '.join(['-' * len(h) for h in headers])} |\n"
    for row in data:
        table += f"| {' | '.join(str(cell) for cell in row)} |\n"
    table += "\n"
    return table

# Append tables to markdown content
md_content += generate_table(
    "Endpoint Rate Limiting",
    ["HTTP Method", "Endpoint", "Max Rate", "Every"],
    endpoint_limit
)
md_content += generate_table(
    "User Rate Limiting",
    ["HTTP Method", "Endpoint", "Client Max Rate", "Every", "Strategy"],
    user_limit
)

# Write to Markdown file
output_file = f"krakend_rate_limits_{env}.md"
try:
    with open(output_file, "w") as md_file:
        md_file.write(md_content)
    print(f"Markdown file generated successfully: {output_file}")
except Exception as e:
    print(f"Error writing Markdown file: {e}")