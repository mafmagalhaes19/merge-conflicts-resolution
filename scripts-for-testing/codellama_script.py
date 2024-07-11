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
    response = requests.post(url, json=payload)
    responses = []
    for line in response.iter_lines():
        if line:
            try:
                data = json.loads(line)
                responses.append(data.get('response'))
            except json.JSONDecodeError as e:
                print(f"Error decoding JSON: {e}")
    return ' '.join(responses)

final_filename = 'conflicts-crawler/final_result.txt'

outputs_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'outputs'))
file_path = os.path.join(outputs_folder, 'conflict_urls.json')

# with open('conflict_urls.json', 'r') as file:
with open(file_path, 'r') as file:
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

            first_file = f"conflicts-crawler/dataset/{project_name}_{commit_key}_{first_x}_{filename}"
            second_file = f"conflicts-crawler/dataset/{project_name}_{commit_key}_{second_x}_{filename}"

            # Sol1 - 2 prompts diferentes - mandar os ficheiros e depois da explicação, pedir a resolução final
            # Sol2 - Dar exemplos (atenção ao número de caractéres) e pedir para ele replicar o comportamento

            prompt = f"Provide your answers only in Java code. I will give you two files that contain merge conflicts (File 1 and File 2). Please provide me a resolution of those conflicts using Java and give me full code for the final file with no conflicts:\n\nFile 1 ({first_file}):\n{open(first_file).read()}\n\nFile 2 ({second_file}):\n{open(second_file).read()}"
            # prompt = f" Provide your answers only in Java code. I will give you two files that contain merge conflicts (File 1 and File 2).
            #     Please provide me a resolution of those conflicts using Java and give me full code for the final
            #     file with no conflicts:\n\nFile 1 ({first_file}):\n{open(first_file).read()}\n\nFile 2 ({second_file})
            #     :\n{open(second_file).read()}"
            # prompt = f" Can you identify the merge conflicts between these two files:\n\nFile 1 ({first_file}):\n{open(first_file).read()}\n\nFile 2 ({second_file}):\n{open(second_file).read()}"
            result = compare_files(prompt)

            with open(final_filename, 'a') as final_file:
                final_file.write(f"Result for {project_name}_{commit_key}_{filename}:\n{result}\n\n")

print(f"Final result saved to {final_filename}")
