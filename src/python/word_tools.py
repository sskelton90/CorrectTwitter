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

def strip_punctuation(word):
  return word.translate(string.maketrans("",""), string.punctuation)

# Use NLTK tokenisation method
def tokenise(text):
  text = strip_html(text)
  tokens = [word.lower() for word in wordpunct_tokenize(text)]

  for token in tokens:
    try:
      token.decode("ascii")
    except UnicodeDecodeError:
      continue
    if len(token) == 1 and token in string.punctuation:
      tokens.remove(token)
    else:
      tokens.remove(token)

  return tokens

# Calculate edit distance using dynamic programming method
# Method taken from http://en.wikipedia.org/wiki/Levenshtein_distance
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

# Helper method to load error-correction file to test accuracy
# Used for the Sheffield corpus
def load_corrections_from_file(filename):
  corrections = {}
  input_file = open(filename)

  for line in input_file:
    error_mapping = line.split()
    corrections[error_mapping[0].lower()] = error_mapping[1].lower()

  return corrections

# Count the number of Tweets in a Tweet file
def number_of_tweets(filename):
  with open(filename) as f:
    for i, l in enumerate(f):
      pass
  return i + 1

# Check if there's a url present
# From http://stackoverflow.com/questions/161738/what-is-the-best-regular-expression-to-check-if-a-string-is-a-valid-url
def is_url(word):
  return re.search("(^http(s{0,1})://)?[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*", word) != None

# Check for timestamps
# They are contain 14 digits
def is_timestamp(word):
  return re.search("[0-9]{14}", word) != None

# Check for usernames
# They are of the form @Username
def is_at_username(word):
  return re.search("@[a-zA-Z0-9_]+", word) != None or re.search("user[0-9]+", word) != None

# Check for hashtags
# They are of the form #Hashtag
def is_hashtag(word):
  return re.search("#[a-zA-Z0-9_]+", word) != None

# Check for extra source information
# of the form rel=
def has_no_follow(word):
  return re.search("rel=\"[a-zA-Z0-9_]+", word) != None or re.search("relnofollow", word) != None

# Check for links related to source of Tweet
# Of the form href= or <a>
def has_link(word):
  return re.search("href=\"", word) != None or re.search("[a-zA-Z0-9_]+</a", word) != None

# Check for ReTweet
# Of the form RT
def is_retweet(word):
  return re.match("RT", word) != None

# Helper function to remove all punctuation words
def is_punctuation(word):
  for l in word:
    if l not in string.punctuation:
      return False
  
  return True

# Look for emoticons
# Extended from http://stackoverflow.com/questions/5862490/how-to-match-emoticons-with-regular-expressions
def is_emoticon(word):
  return re.search("(\-\.\/\(\)=:;]+)|((?::|;|=|x|X)(?:-)?(?:\)|\(|D|P|O))", word) != None

# If a Tweet has features specific to Tweets then return true so it won't be corrected
def has_tweet_specific_features(token):
  return is_url(token) or is_at_username(token) or is_hashtag(token) or is_timestamp(token) or has_no_follow(token) or has_link(token) or is_punctuation(token) or is_emoticon(token) or is_retweet(token)

# Generate character-level bigrams of size 2 
def generate_bigrams(word):
  bigrams = []
  for i in range(len(word) - 1):
    bigrams.append(word[i:i+2])

  return bigrams

# Calculate character-level n-gram similarity
def ngram_similarity(word1, word2):
  bigrams1 = generate_bigrams(word1)
  bigrams2 = generate_bigrams(word2)

  intersection = [b for b in bigrams1 if b in bigrams2]
  union = list(set(bigrams1) | set(bigrams2))

  return float(len(intersection)) / (len(union))

# Similar to above but enforce order
def ngram_similarity_order(word1, word2, m):
  count = 0

  if len(word2) > len(word1):
    temp = word2
    word2 = word1
    word1 = temp

  if len(word1) == 0 or len(word2) == 0:
    return 0
    
  if word1[0] == word2[0]:
    count += 1
  if word1[-1] == word2[-1]:
    count += 1

  bigrams1 = generate_bigrams(word1)
  bigrams2 = generate_bigrams(word2)

  for i in range(1, len(bigrams1) - 1):
    for j in range(m):
      # Basically only check m bigrams either side for a match
      if i+j < len(bigrams2) and bigrams1[i] == bigrams2[i+j]:
        count += 1
        break
      if i-j < len(bigrams2) and bigrams1[i] == bigrams2[i-j]:
        count += 1
        break

  return float(count) / (len(list(set(bigrams1) | set(bigrams2))))
