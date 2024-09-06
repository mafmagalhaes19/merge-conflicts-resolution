import os
import json
from urllib.parse import urlparse
from mlx_lm import load, generate

# Load the model and tokenizer
model, tokenizer = load("mlx-community/CodeLlama-13b-Instruct-hf-4bit-MLX")

# Function to generate a response using the mlx_lm model
def generate_response(prompt):
    response = generate(model, tokenizer, prompt=prompt, verbose=True, max_tokens=3000)
    return response 

# Create a folder for the results if it doesn't exist
results_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'llm-results'))
os.makedirs(results_folder, exist_ok=True)

solved_examples_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'dataset-custom-conflicts/solved-examples'))

# Paths to example files
example_files = [
    ("BankAccount1.java",
     "BankAccount2.java",
     "BankAccount0.java"),
    ("ShoppingCart1.java",
     "ShoppingCart2.java",
     "ShoppingCart0.java"),
    ("Student1.java",
     "Student2.java",
     "Student0.java"),
]

# Read the example files
examples = []
for conflict_a, conflict_b, resolution in example_files:
    with open(os.path.join(solved_examples_folder, conflict_a), 'r') as file:
        conflict_a_content = file.read()
    with open(os.path.join(solved_examples_folder, conflict_b), 'r') as file:
        conflict_b_content = file.read()
    with open(os.path.join(solved_examples_folder, resolution), 'r') as file:
        resolution_content = file.read()
    examples.append((conflict_a_content, conflict_b_content, resolution_content))

# Construct the role/content prompt with examples
example_prompt = ""
for i, (file1, file2, solved_file) in enumerate(examples, start=1):
    example_prompt += (
        f"role: user\ncontent: I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java.\n\n"
        f"role: user\ncontent: File 1:\n{file1}\n\n"
        f"role: user\ncontent: File 2:\n{file2}\n\n"
        f"role: assistant\ncontent: Resolution:\n{solved_file}\n\n"
    )

# Paths to conflict folders
conflict_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'dataset-custom-conflicts/conflicts-examples'))

# Load conflicts from 'conflicts.json'
conflicts_json_path = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts.json'))
with open(conflicts_json_path, 'r') as conflicts_file:
    conflicts_data = json.load(conflicts_file)

# Process each conflict category
for category, conflict_names in conflicts_data.items():
    for conflict_name in conflict_names:
        # Construct the file names for the conflicts (A and B)
        conflict_a_filename = f"{conflict_name}A.java"
        conflict_b_filename = f"{conflict_name}B.java"
        
        # Full paths to conflict files
        conflict_a_path = os.path.join(conflict_folder, conflict_a_filename)
        conflict_b_path = os.path.join(conflict_folder, conflict_b_filename)
        
        # Check if both conflict files exist
        if os.path.exists(conflict_a_path) and os.path.exists(conflict_b_path):
            with open(conflict_a_path, 'r') as file_a:
                conflict_a_content = file_a.read()
            with open(conflict_b_path, 'r') as file_b:
                conflict_b_content = file_b.read()

            # Construct the prompt with the examples and the new conflict
            conflict_prompt = (
                f"I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java.\n"
                f"role: user\ncontent: File 1:\n{conflict_a_content}\n\n"
                f"role: user\ncontent: File 2:\n{conflict_b_content}\n\n"
                f"role: assistant\ncontent: Resolution:\n"
            )
            
            # Combine the example prompt with the conflict prompt
            prompt = example_prompt + conflict_prompt

            # Get the result from the LLM
            result = generate_response(prompt)
            
            # Save the result in a file inside 'llm-results' folder
            result_filename = os.path.join(results_folder, f"{conflict_name}_resolution.txt")
            with open(result_filename, 'w') as result_file:
                result_file.write(f"Result for {conflict_name}:\n{result}\n\n")

            print(f"Result for {conflict_name} saved to {result_filename}")
        else:
            print(f"Conflict files for {conflict_name} not found.")
