import os
import json
import requests
from bs4 import BeautifulSoup
from urllib.parse import urlparse

content_dict = {}

# Create a dataset folder if it doesn't exist
if not os.path.exists('dataset'):
    os.makedirs('dataset')

with open('conflict_urls.json', 'r') as file:
    conflict_urls = json.load(file)

for project_name, commits in conflict_urls.items():
    for commit_key, urls in commits.items():
        for url in urls:
            response = requests.get(url)
            if response.status_code == 200:
                soup = BeautifulSoup(response.text, 'html.parser')
                html_content = response.text
                # Extract x from the URL
                url_parts = urlparse(url)
                raw_index = url_parts.path.split('/').index('raw')
                x = url_parts.path.split('/')[raw_index + 1]
                # Generate filename
                filename = f"dataset/{project_name}_{commit_key}_{x}_{url.split('/')[-1]}.java"
                with open(filename, 'w', encoding='utf-8') as html_file:
                    html_file.write(html_content)
                print(f"HTML content saved to {filename}")
            elif response.status_code == 404:
                print(f"Ignoring URL {url} (404 Not Found)")
            else:
                print(f"Failed to fetch URL {url} (Status Code: {response.status_code})")

print("All HTML content saved to files in the dataset folder")
