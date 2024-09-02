import requests
from bs4 import BeautifulSoup
import json

url = 'https://merge-nature.netlify.app/'
response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

tr_elements = soup.find_all('tr')

data_list = []

# Change the number of projects you want to analyze
n = 4

for tr in tr_elements[3:(n+1)]: 
    td_elements = tr.find_all('td')
    if len(td_elements) >= 3:  
        name = td_elements[0].text.strip()
        url = td_elements[2].text.strip()
        developers = td_elements[3].text.strip()
        commits = td_elements[4].text.strip()
        merges = td_elements[5].text.strip()
        href = f"https://merge-nature.netlify.app{td_elements[0].find('a')['href']}"
        data = {
            'name': name,
            'href': href,
            'url': url,
            'developers': developers,
            'commits': commits,
            'merges': merges
        }

        href_response = requests.get(href)
        href_soup = BeautifulSoup(href_response.text, 'html.parser')

        sha_td_elements = href_soup.find_all('td', {'data-title': 'SHA'})

        filenames = []
        commit_data = {}
        for td_element in sha_td_elements:
            a_tag = td_element.find('a')
            if a_tag:
                href_sha = a_tag.get('href')
                full_href_sha = 'https://merge-nature.netlify.app' + href_sha
                href_sha_response = requests.get(full_href_sha)
                href_sha_soup = BeautifulSoup(href_sha_response.text, 'html.parser')
                filenames_elements = href_sha_soup.find_all('td', {'data-title': 'FileName'})
                filenames = [filename_element.text.strip() for filename_element in filenames_elements]

                commit_url = f"{url}/commit/{td_element.text.strip()}"

                commit_response = requests.get(commit_url)
                commit_soup = BeautifulSoup(commit_response.text, 'html.parser')
                commit_desc = commit_soup.find('div', class_='commit-desc')
                if commit_desc:
                    conflicts = commit_desc.text.strip().replace('Conflicts:', '').strip().split('\n')
                    conflicts = [conflict for conflict in conflicts if any(filename in conflict for filename in filenames)]
                else:
                    conflicts = []
                parent_block = commit_soup.find('span', class_='sha-block ml-0')
                parent_one = ""
                parent_two = ""
                if parent_block:
                    parent_links = parent_block.find_all('a', class_='sha')
                    if len(parent_links) == 2:
                        parent_one = parent_links[0].text.strip()
                        parent_two = parent_links[1].text.strip()
                commit_data[f"commit_{td_element.text.strip()}"] = {
                    'sha': td_element.text.strip(),
                    'files_in_conflict': filenames,
                    'conflicts': conflicts,
                    'parent_one': parent_one,
                    'parent_two': parent_two
                }
        data.update(commit_data)
        data_list.append(data)


data = {'data': data_list}

output_file = "conflicts-solver-dataset/conflicts-crawler/outputs/projects_and_conflicts.json"

with open(output_file, 'w') as f:
    json.dump(data, f, indent=4)

print(f"Data has been written to {output_file}")