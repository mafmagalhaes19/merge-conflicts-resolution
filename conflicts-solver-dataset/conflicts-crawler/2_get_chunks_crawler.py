import json
import os
import re
import requests

outputs_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'outputs'))
file_path = os.path.join(outputs_folder, '1_projects_and_conflicts.json')

def count_methods_and_tokens(java_code):
    # Regex pattern to match Java methods
    method_pattern = r'((public|protected|private|static|\s)*)\s+[\w\<\>\[\]]+\s+\w+\s*\([^\)]*\)\s*\{'
    
    # Finding all matches for method patterns
    methods = re.findall(method_pattern, java_code)
    
    # Counting tokens
    tokens = re.findall(r'\S+', java_code) 
    
    return len(methods), len(tokens)


with open(file_path, 'r') as file:
    data = json.load(file)

conflict_urls = {}
file_metrics = {}

for project in data['data']:
    project_name = project['name']
    for commit_key, commit_data in project.items():
        if commit_key.startswith('commit_'):
            commit_sha = commit_data['sha']
            for conflict in commit_data['conflicts']:
                file_path = conflict.strip()
                if file_path.endswith('.java'):
                    for parent in ['parent_one', 'parent_two']:
                        url = f"{project['url']}/raw/{commit_data[parent]}/{file_path}"
                        if project_name not in conflict_urls:
                            conflict_urls[project_name] = {}
                        if commit_key not in conflict_urls[project_name]:
                            conflict_urls[project_name][commit_key] = []

                        # Download the Java file
                        response = requests.get(url)
                        if response.status_code == 200:
                            java_code = response.text
                            num_methods, num_tokens = count_methods_and_tokens(java_code)
                            
                            # Store metrics
                            if project_name not in file_metrics:
                                file_metrics[project_name] = {}
                            if commit_key not in file_metrics[project_name]:
                                file_metrics[project_name][commit_key] = []
                            file_metrics[project_name][commit_key].append({
                                'file_path': file_path,
                                'num_methods': num_methods,
                                'num_tokens': num_tokens
                            })
                        
                        conflict_urls[project_name][commit_key].append(url)

# Save the URLs and metrics to JSON files
with open('conflicts-solver-dataset/conflicts-crawler/outputs/2_conflict_urls.json', 'w') as file:
    json.dump(conflict_urls, file, indent=4)

with open('conflicts-solver-dataset/conflicts-crawler/outputs/2_file_metrics.json', 'w') as file:
    json.dump(file_metrics, file, indent=4)

print("Conflict URLs and file metrics extracted and stored.")

