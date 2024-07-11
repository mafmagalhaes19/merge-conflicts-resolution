import subprocess
import os

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

if __name__ == "__main__":
    # Example usage: replace 'example.java' with the path to your Java file
    manual_testing_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-crawler/dataset-manual-testing'))
    file_path_1 = os.path.join(manual_testing_folder, 'Api2Impl.java')
    file_path_2 = os.path.join(manual_testing_folder, 'Api2Result.java')
    run_google_java_format(file_path_1)
    run_google_java_format(file_path_2)
