import os
import json
import requests
from urllib.parse import urlparse

# Function to send a request to the Codellama API
def compare_files(prompt):
    url = 'http://localhost:11434/api/generate'
    payload = {
        "model": "codellama",
        "prompt": prompt
    }
    #print("Payload:", payload)
    response = requests.post(url, json=payload)
    print("Response Content:", response.content)
    return response.json()

final_filename = 'final_result.txt'

with open('conflict_urls.json', 'r') as file:
    conflict_urls = json.load(file)

for project_name, commits in conflict_urls.items():
    for commit_key, urls in commits.items():
        url_pairs = zip(urls[::2], urls[1::2])
        for first_url, second_url in url_pairs:
            first_url_parts = urlparse(first_url)
            first_raw_index = first_url_parts.path.split('/').index('raw')
            first_x = first_url_parts.path.split('/')[first_raw_index + 1]

            second_url_parts = urlparse(second_url)
            second_raw_index = second_url_parts.path.split('/').index('raw')
            second_x = second_url_parts.path.split('/')[second_raw_index + 1]

            filename = first_url.split('/')[-1]

            first_file = f"dataset/{project_name}_{commit_key}_{first_x}_{filename}.java"
            second_file = f"dataset/{project_name}_{commit_key}_{second_x}_{filename}.java"

            prompt = f"Identify the conflicts in these files and solve them:\n\nFile 1 ({first_file}):\n{open(first_file).read()}\n\nFile 2 ({second_file}):\n{open(second_file).read()}"
            result = compare_files(prompt)

            with open(final_filename, 'a') as final_file:
                final_file.write(f"Result for {project_name}_{commit_key}_{filename}:\n{result}\n\n")

print(f"Final result saved to {final_filename}")