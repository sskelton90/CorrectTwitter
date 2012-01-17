from nltk.tokenize import *
import re
import string

def load_words(filepath):
  input_file = open(filepath)
  words = []

  for line in input_file:
    words.append(tokenise(line))

  return words
      
def strip_html(text):
  return re.sub('<[^<]+?>', '', text)

def tokenise(text):
  text = strip_html(text)
  tokens = [word.lower() for word in wordpunct_tokenize(text)]

  for token in tokens:
    if len(token) == 1 and token in string.punctuation:
      tokens.remove(token)
    else:
      tokens.remove(token)

  return tokens

def edit_distance(first_word, second_word):
  length1 = len(first_word)
  length2 = len(second_word)
  distance_matrix = [[0]*(length2 + 1) for x in range(length1 + 1)]

  for i in range(length1 + 1):
    distance_matrix[i][0] = i

  for j in range(length2 + 1):
    distance_matrix[0][j] = j
 
  for i in range(1, length1 + 1):
    for j in range(1, length2 + 1):
      sub_cost = {True: 0, False: 1}[first_word[i - 1] == second_word[j - 1]]

      deletion_cost = distance_matrix[i - 1][j] + 1
      insertion_cost = distance_matrix[i][j - 1] + 1
      substitution_cost = distance_matrix[i - 1][j - 1] + sub_cost

      distance_matrix[i][j] = min([deletion_cost, insertion_cost, substitution_cost])

  return distance_matrix[length1][length2]

def load_corrections_from_file(filename):
  corrections = {}
  input_file = open(filename)

  for line in input_file:
    error_mapping = line.split()
    corrections[error_mapping[0].lower()] = error_mapping[1].lower()

  return corrections
