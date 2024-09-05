import subprocess
import os
import re


# Function to extract the code from the LLM's response
def extract_code(response_text):
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



# Function to run google-java-format
def run_google_java_format(java_file):

    google_java_format_jar = 'helpers/google-java-format-1.22.0-all-deps.jar'

    # Check if the java file exists
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

# Function to save the extracted code to a Java file
def save_code_to_file(code, file_path):
    with open(file_path, 'w') as file:
        file.write(code)

if __name__ == "__main__":
    # Example LLM response, replace this with your actual response
    llm_response = """
    Here is the resolution:
    ```java
    public class Example {
        public static void main(String[] args) {
            System.out.println("Hello World");
        }
    }
    ```
    """
    
    # Extract code from LLM's response
    extracted_code = extract_code(llm_response)
    
    if extracted_code:
        # Define the path for the extracted Java file
        manual_testing_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-crawler/dataset-manual-testing'))
        file_path_1 = os.path.join(manual_testing_folder, 'Api2Impl.java')
        file_path_2 = os.path.join(manual_testing_folder, 'Api2Result.java')
        
        # Save the extracted code to the first file (as an example)
        save_code_to_file(extracted_code, file_path_1)
        
        # Run google-java-format on the saved file
        run_google_java_format(file_path_1)
        run_google_java_format(file_path_2)





# import subprocess
# import os

# def run_google_java_format(java_file):

#     google_java_format_jar = 'helpers/google-java-format-1.22.0-all-deps.jar'

#     # Check if the java file exists
#     if not os.path.isfile(java_file):
#         print(f"File {java_file} does not exist.")
#         return

#     # Run the google-java-format command
#     try:
#         result = subprocess.run(
#             ['java', '-jar', google_java_format_jar, '-i', java_file],
#             check=True,
#             stdout=subprocess.PIPE,
#             stderr=subprocess.PIPE
#         )
#         print(f"Formatted {java_file} successfully.")
#     except subprocess.CalledProcessError as e:
#         print(f"Error formatting {java_file}: {e.stderr.decode()}")

# if __name__ == "__main__":
#     # Example usage: replace 'example.java' with the path to your Java file
#     manual_testing_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-crawler/dataset-manual-testing'))
#     file_path_1 = os.path.join(manual_testing_folder, 'Api2Impl.java')
#     file_path_2 = os.path.join(manual_testing_folder, 'Api2Result.java')
#     run_google_java_format(file_path_1)
#     run_google_java_format(file_path_2)
