import subprocess
import os
import re
import difflib


def extract_code_from_file(file_path):
    with open(file_path, 'r') as file:
        response_text = file.read()
    
    # Using regex to find the code block wrapped in triple backticks
    code_blocks = re.findall(r"```(.*?)```", response_text, re.DOTALL)
    
    # If no triple backticks are found, try to extract from "Here is the resolution" onward
    if not code_blocks:
        match = re.search(r"Here is the resolution:(.*)", response_text, re.DOTALL)
        if match:
            return match.group(1).strip()
        else:
            # Fallback to the entire response if nothing is found
            return response_text  

    # If multiple code blocks exist, concatenate them
    return "\n".join(code_blocks).strip()



def run_google_java_format(java_file):
    google_java_format_jar = 'helpers/google-java-format-1.22.0-all-deps.jar'

    if not os.path.isfile(java_file):
        print(f"File {java_file} does not exist.")
        return

    # Run the google-java-format command
    try:
        result = subprocess.run(
            ['java', '-jar', google_java_format_jar, '-i', java_file],
            check=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE
        )
        print(f"Formatted {java_file} successfully.")
    except subprocess.CalledProcessError as e:
        print(f"Error formatting {java_file}: {e.stderr.decode()}")



def save_code_to_file(code, file_path):
    with open(file_path, 'w') as file:
        file.write(code)



def read_file(file_path):
    with open(file_path, 'r') as file:
        return file.readlines()



def save_diff_to_file(file1_path, file2_path, output_path):
    file1_lines = read_file(file1_path)
    file2_lines = read_file(file2_path)

    diff = difflib.unified_diff(
        file1_lines,
        file2_lines,
        fromfile=file1_path,
        tofile=file2_path,
        lineterm=''
    )
    
    with open(output_path, 'w') as output_file:
        for line in diff:
            output_file.write(line + '\n')


if __name__ == "__main__":

    # Folder paths
    llm_resolution_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'llm-conflicts-resolution'))
    dataset_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-crawler/dataset-files-in-conflict'))
    testing_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'testing-files'))


    project_name = "3scale_ws_api_for_java" 
    commit_key = "commit_332b6ed4e324199565e235cd915368f9bc1b43d2"
    filename = "Api2Impl.java"
    
    llm_result_file_path = os.path.join(llm_resolution_folder, f"{project_name}_{commit_key}_{filename}")

    main_commit_file_path = os.path.join(dataset_folder, f"{project_name}_{commit_key}_result_{filename}")

    extracted_code = extract_code_from_file(llm_result_file_path)

    # Check if the main commit file exists
    if not os.path.exists(main_commit_file_path):
        print(f"Main commit file not found: {main_commit_file_path}")
    else:
        llm_result_formatted_path = os.path.join(testing_folder, f"{project_name}_{commit_key}_{filename}_LLMFormatted.java")
        main_commit_formatted_path = os.path.join(testing_folder, f"{project_name}_{commit_key}_{filename}_MainCommitFormatted.java")

        # Save the extracted LLM code to a new file for formatting
        save_code_to_file(extracted_code, llm_result_formatted_path)

        # Copy the main commit file to the manual testing folder
        os.system(f'cp {main_commit_file_path} {main_commit_formatted_path}')

        # First, format both files using google-java-format
        run_google_java_format(llm_result_formatted_path)
        run_google_java_format(main_commit_formatted_path)

        # After formatting, compare the formatted files and save the diff
        diff_output_file = os.path.join(testing_folder, f"{project_name}_{commit_key}_{filename}_diff_result.txt")
        save_diff_to_file(llm_result_formatted_path, main_commit_formatted_path, diff_output_file)

        print(f"Diff saved to {diff_output_file}")
