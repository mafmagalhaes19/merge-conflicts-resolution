import os
import json
import requests
from bs4 import BeautifulSoup
from urllib.parse import urlparse

content_dict = {}

# Create a dataset folder if it doesn't exist
if not os.path.exists('conflicts-solver-dataset/conflicts-crawler/dataset-files-in-conflict'):
    os.makedirs('conflicts-solver-dataset/conflicts-crawler/dataset-files-in-conflict')

outputs_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'outputs'))
file_path = os.path.join(outputs_folder, '2_conflict_urls.json')

# with open('conflict_urls.json', 'r') as file:
with open(file_path, 'r') as file:
    conflict_urls = json.load(file)

for project_name, commits in conflict_urls.items():
    for commit_key, urls in commits.items():
        for url in urls:
            response = requests.get(url)
            if response.status_code == 200:
                soup = BeautifulSoup(response.text, 'html.parser')
                html_content = response.text
                url_parts = urlparse(url)
                raw_index = url_parts.path.split('/').index('raw')
                commit_sha = url_parts.path.split('/')[raw_index + 1]  # Extract commit SHA from URL
                
                # Determine if the current URL is the main commit or parent commit
                is_main_commit = commit_key.split('_')[-1] == commit_sha
                
                if is_main_commit:
                    # Store the main commit URL as {project_name}_{commit_key}_{x}_result
                    filename = f"conflicts-solver-dataset/conflicts-crawler/dataset-files-in-conflict/{project_name}_{commit_key}_result_{url.split('/')[-1]}"
                else:
                    # Store the parent commit URL with the original naming convention
                    filename = f"conflicts-solver-dataset/conflicts-crawler/dataset-files-in-conflict/{project_name}_{commit_key}_{commit_sha}_{url.split('/')[-1]}"
                
                with open(filename, 'w', encoding='utf-8') as html_file:
                    html_file.write(html_content)
                
                print(f"File content saved to {filename}")
            elif response.status_code == 404:
                print(f"Ignoring URL {url} (404 Not Found)")
            else:
                print(f"Failed to fetch URL {url} (Status Code: {response.status_code})")

print("All conflicting files saved in the dataset folder")
