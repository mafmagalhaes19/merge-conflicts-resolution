import json
import os

outputs_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'outputs'))
file_path = os.path.join(outputs_folder, 'projects_and_conflicts.json')

# with open('outputs/projects_and_conflicts.json', 'r') as file:
with open(file_path, 'r') as file:
    data = json.load(file)

conflict_urls = {}

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
                        conflict_urls[project_name][commit_key].append(url)

with open('conflicts-crawler/outputs/conflict_urls.json', 'w') as file:
    json.dump(conflict_urls, file, indent=4)

print("Conflict URLs extracted and stored in conflict_urls.json")

