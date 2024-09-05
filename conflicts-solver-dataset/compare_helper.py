import os
import re

# Function to extract the code from the LLM's response (ignoring explanations)
def extract_code(response_text):
    # Using regex to find the code block wrapped in triple backticks
    code_blocks = re.findall(r"```(.*?)```", response_text, re.DOTALL)
    
    # If no triple backticks are found, try to extract from "Here is the resolution" onward
    if not code_blocks:
        match = re.search(r"Here is the resolution:(.*)", response_text, re.DOTALL)
        if match:
            return match.group(1).strip()
        else:
            return response_text  # Fallback to the entire response if nothing is found

    # If multiple code blocks exist, concatenate them
    return "\n".join(code_blocks).strip()

# Function to compare two code files (generated and original)
def compare_files(generated_file, original_file):
    with open(generated_file, 'r') as gen_file:
        generated_code = gen_file.read().strip()

    with open(original_file, 'r') as orig_file:
        original_code = orig_file.read().strip()

    if generated_code == original_code:
        print(f"Comparison successful: The files {generated_file} and {original_file} are identical.")
    else:
        print(f"Comparison failed: The files {generated_file} and {original_file} differ.")
        # Optionally, you can display the differences or store them in a file

# Example usage
response_text = """
The conflict is between two different implementations of the `Api2Impl` class. ...
Here is the resolution:
package net.threescale.api.v2;

import net.threescale.api.LogFactory; import net.threescale.api.cache.ApiCache; import net.threescale.api.cache.NullCacheImpl;

// Rest of the code...

"""

# Extract the code
extracted_code = extract_code(response_text)

# Save extracted code to a file
generated_file = 'generated_result.txt'
with open(generated_file, 'w') as gen_file:
    gen_file.write(extracted_code)

# Path to the original file
original_file = 'original_result.txt'

# Compare the generated code with the original code
compare_files(generated_file, original_file)
