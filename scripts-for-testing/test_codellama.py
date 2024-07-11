import requests
import json


def get_codellama_response(prompt):
    url = 'http://localhost:11434/api/generate'
    data = {
        "model": "codellama",
        "prompt": prompt
    }
    response = requests.post(url, json=data)
    responses = []
    for line in response.iter_lines():
        if line:
            try:
                data = json.loads(line)
                responses.append(data.get('response', ''))
            except json.JSONDecodeError as e:
                print(f"Error decoding JSON: {e}")
    return ' '.join(responses)

# Example usage
prompt = "Write me a function that outputs the fibonacci sequence in Java"
response = get_codellama_response(prompt)
print(response)
