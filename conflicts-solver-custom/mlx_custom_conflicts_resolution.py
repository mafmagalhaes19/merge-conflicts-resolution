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

final_filename = 'results/TESTE_final_simple_merge_result.txt'
final_filename_comments = 'results/final_comments_result.txt'
final_filename_reverse = 'results/final_reverse_result.txt'

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
        #f"role: user\ncontent: I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java. Please answer with just the code and no comments about your resolution.\n\n"
        f"role: user\ncontent: File 1:\n{file1}\n\n"
        f"role: user\ncontent: File 2:\n{file2}\n\n"
        f"role: assistant\ncontent: Resolution:\n{solved_file}\n\n"
    )

conflict_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'dataset-custom-conflicts'))
conflict_folder_comments = os.path.abspath(os.path.join(os.path.dirname(__file__), 'dataset-custom-conflicts/comments'))
conflict_folder_reverse = os.path.abspath(os.path.join(os.path.dirname(__file__), 'dataset-custom-conflicts/reverse-order'))

conflict_files = sorted(os.listdir(conflict_folder))
conflict_files_comments = sorted(os.listdir(conflict_folder_comments))
conflict_files_reverse = sorted(os.listdir(conflict_folder_reverse))

# Filter out files ending with '0.java'
filtered_conflict_files = [f for f in conflict_files if not f.endswith('0.java')]
#filtered_conflict_files = [f for f in conflict_files_comments if not f.endswith('0.java')]
#filtered_conflict_files = [f for f in conflict_files_reverse if not f.endswith('0.java')]


# Iterate over the filtered conflict files in pairs
for i in range(0, len(filtered_conflict_files), 2):
    file1_path = os.path.join(conflict_folder, filtered_conflict_files[i])
    file2_path = os.path.join(conflict_folder, filtered_conflict_files[i + 1])
    
    with open(file1_path, 'r') as file1:
        file1_content = file1.read()
    with open(file2_path, 'r') as file2:
        file2_content = file2.read()

    # Construct the prompt with the role/content format
    conflict_prompt = (
        #f"Now, solve the following conflict:\n"
        #f"Now, solve the following conflict using the same strategy as the previous examples and give just code not an explanation:\n"
        f"I will give you two Java files that have a merge conflict and I want you to give me a resolution file with the solution of the conflict between them using Java.\n"
        f"role: user\ncontent: File 1:\n{file1_content}\n\n"
        f"role: user\ncontent: File 2:\n{file2_content}\n\n"
        f"role: assistant\ncontent: Resolution:\n"
    )

    # Combine the example prompt with the conflict prompt
    prompt = example_prompt + conflict_prompt

    result = generate_response(prompt)
    #result = generate(model, tokenizer, prompt, verbose=True)

    with open(final_filename, 'a') as final_file:
        base_name = os.path.splitext(filtered_conflict_files[i])[0]
        final_file.write(f"Result for {base_name}:\n{result}\n\n")

print(f"Final result saved to {final_filename}")
#print(f"Final result saved to {final_filename_comments}")
#print(f"Final result saved to {final_filename_reverse}")