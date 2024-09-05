import os
import difflib

def read_file(file_path):
    with open(file_path, 'r') as file:
        return file.readlines()

def save_diff_to_file(file1_path, file2_path, output_path):
    file1_lines = read_file(file1_path)
    file2_lines = read_file(file2_path)

    diff = difflib.unified_diff(
        file1_lines,
        file2_lines,
        fromfile='file1.java',
        tofile='file2.java',
        lineterm=''
    )
    
    with open(output_path, 'w') as output_file:
        for line in diff:
            output_file.write(line + '\n')


manual_testing_folder = os.path.abspath(os.path.join(os.path.dirname(__file__), 'conflicts-crawler/dataset-manual-testing'))
file_path_1 = os.path.join(manual_testing_folder, 'Api2Impl.java')
file_path_2 = os.path.join(manual_testing_folder, 'Api2Result.java')
result = os.path.join(manual_testing_folder, 'result.txt')

save_diff_to_file(file_path_1, file_path_2, result)

