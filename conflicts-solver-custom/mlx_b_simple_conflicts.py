import os
import json
from urllib.parse import urlparse
from mlx_lm import load, generate

# Load the model and tokenizer
model, tokenizer = load("mlx-community/Meta-Llama-3-8B-Instruct-4bit")

# Function to generate a response using the mlx_lm model
def generate_response(prompt):
    response = generate(model, tokenizer, prompt=prompt, verbose=True, max_tokens=3000)
    return response 

final_filename = 'results/final_simple_merge_result.txt'
final_filename_comments = 'results/final_comments_result.txt'
final_filename_reverse = 'results/final_reverse_result.txt'

solved_examples_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-solver-custom/dataset-simple-merges/solved-examples'))

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
for i, (conflict_a, conflict_b, resolution) in enumerate(examples, start=1):
    example_prompt += (
        f"role: user\ncontent: I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java.\n\n"
        #f"role: user\ncontent: I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java. Please answer with just the code and no comments about your resolution.\n\n"
        f"role: user\ncontent: File 1:\n{conflict_a}\n\n"
        f"role: user\ncontent: File 2:\n{conflict_b}\n\n"
        f"role: assistant\ncontent: Resolution:\n{resolution}\n\n"
    )

conflict_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-solver-custom/dataset-simple-merges'))
conflict_folder_comments = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-solver-custom/dataset-simple-merges/comments'))
conflict_folder_reverse = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-solver-custom/dataset-simple-merges/reverse-order'))

conflict_files = sorted(os.listdir(conflict_folder))
conflict_files_comments = sorted(os.listdir(conflict_folder_comments))
conflict_files_reverse = sorted(os.listdir(conflict_folder_reverse))

# Group files by base name and indexes
conflict_pairs = {}
for file in conflict_files:
    base_name = ''.join(filter(lambda x: not x.isdigit(), file)).replace('.java', '')
    index = ''.join(filter(str.isdigit, file))
    if base_name not in conflict_pairs:
        conflict_pairs[base_name] = {}
    conflict_pairs[base_name][index] = os.path.join(conflict_folder, file)
    #conflict_pairs[base_name][index] = os.path.join(conflict_files_comments, file)
    #conflict_pairs[base_name][index] = os.path.join(conflict_files_reverse, file)

# Iterate over grouped files and pair them
for base_name, files in conflict_pairs.items():
    sorted_indexes = sorted(files.keys(), key=int)
    for i in range(0, len(sorted_indexes) - 1, 2):
        file1_path = files[sorted_indexes[i]]
        file2_path = files[sorted_indexes[i + 1]]
        
        with open(file1_path, 'r') as file1:
            file1_content = file1.read()
        with open(file2_path, 'r') as file2:
            file2_content = file2.read()

        # Construct the prompt with the role/content format
        prompt = (
            f"{example_prompt}"
            f"Now, solve the following conflict:\n"
            f"role: user\ncontent: File 1:\n{file1_content}\n\n"
            f"role: user\ncontent: File 2:\n{file2_content}\n\n"
            f"role: assistant\ncontent: Resolution:\n"
        )

        result = generate_response(prompt)

        with open(final_filename, 'a') as final_file:
            final_file.write(f"Result for {base_name}_{sorted_indexes[i]}_{sorted_indexes[i + 1]}:\n{result}\n\n")

print(f"Final result saved to {final_filename}")
#print(f"Final result saved to {final_filename_comments}")
#print(f"Final result saved to {final_filename_reverse}")