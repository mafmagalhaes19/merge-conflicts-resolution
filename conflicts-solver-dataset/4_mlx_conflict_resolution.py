import os
import json
from urllib.parse import urlparse
from mlx_lm import load, generate

# Load the model and tokenizer
#model, tokenizer = load("mlx-community/Meta-Llama-3-8B-Instruct-4bit")
model, tokenizer = load("mlx-community/CodeLlama-13b-Instruct-hf-4bit-MLX")

# Function to generate a response using the mlx_lm model
def generate_response(prompt):
    response = generate(model, tokenizer, prompt=prompt, verbose=True, max_tokens=3000)
    return response 

final_filename = 'conflicts-crawler/final_result.txt'

outputs_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-crawler/outputs'))
manual_testing_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-crawler/dataset-manual-testing'))
file_path = os.path.join(outputs_folder, 'conflict_urls.json')

# Paths to example files
example_files = [
    ("3scale_ws_api_for_java_commit_c3b5f5d43f5699e2e4d698bd32a74dad9ac1d477_78eceb4_HttpSenderImpl.java",
     "3scale_ws_api_for_java_commit_c3b5f5d43f5699e2e4d698bd32a74dad9ac1d477_85d75a2_HttpSenderImpl.java",
     "HttpSenderImpl.java"),
]

# Read the example files
examples = []
for conflict_a, conflict_b, resolution in example_files:
    with open(os.path.join(manual_testing_folder, conflict_a), 'r') as file:
        conflict_a_content = file.read()
    with open(os.path.join(manual_testing_folder, conflict_b), 'r') as file:
        conflict_b_content = file.read()
    with open(os.path.join(manual_testing_folder, resolution), 'r') as file:
        resolution_content = file.read()
    examples.append((conflict_a_content, conflict_b_content, resolution_content))

# Construct the role/content prompt with examples
example_prompt = ""
for i, (conflict_a, conflict_b, resolution) in enumerate(examples, start=1):
    example_prompt += (
        f"role: user\ncontent: I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java.\n\n"
        #f"role: user\ncontent: I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java. Please answer with just the code and no comments about your resolution.\n\n"
        f"role: user\ncontent: File 1:\n{conflict_a}\n\n"
        f"role: user\ncontent: File 2:\n{conflict_b}\n\n"
        f"role: assistant\ncontent: Resolution:\n{resolution}\n\n"
    )

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

            first_file_path = f"conflicts-crawler/dataset/{project_name}_{commit_key}_{first_x}_{filename}"
            second_file_path = f"conflicts-crawler/dataset/{project_name}_{commit_key}_{second_x}_{filename}"

            with open(first_file_path, 'r') as first_file:
                first_file_content = first_file.read()
            with open(second_file_path, 'r') as second_file:
                second_file_content = second_file.read()

            # Construct the prompt with the role/content format
            prompt = (
                f"{example_prompt}"
                f"Now, solve the following conflict:\n"
                f"role: user\ncontent: File 1:\n{first_file_content}\n\n"
                f"role: user\ncontent: File 2:\n{second_file_content}\n\n"
                f"role: assistant\ncontent: Resolution:\n"
            )

            result = generate_response(prompt)

            with open(final_filename, 'a') as final_file:
                final_file.write(f"Result for {project_name}_{commit_key}_{filename}:\n{result}\n\n")

print(f"Final result saved to {final_filename}")
