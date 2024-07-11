# from mlx_lm import load, generate

# model, tokenizer = load("mlx-community/Meta-Llama-3-8B-Instruct-4bit")

# sentence = "Write a short story about the last woman on earth"

# response = generate(model, tokenizer, prompt=sentence, verbose=True, max_tokens=3000)


from mlx_lm import load, generate

# Load the model and tokenizer
model, tokenizer = load("mlx-community/Meta-Llama-3-8B-Instruct-4bit")

# Define your role/content messages
messages = [
    {"role": "system", "content": "You are a pirate chatbot who always responds in pirate speak!"},
    {"role": "user", "content": "Who are you?"}
]

# Convert messages to a formatted prompt
formatted_prompt = ""
for message in messages:
    formatted_prompt += f"{message['role']}: {message['content']}\n"


# Generate the response
response = generate(model, tokenizer, prompt=formatted_prompt, verbose=True, max_tokens=512)

# Extract and print the response
print(response["generated_text"][len(formatted_prompt):])
