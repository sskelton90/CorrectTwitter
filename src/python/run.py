import sys
import time
from word_tools import *
from dictionary import *
from correction_strategies import *
from collections import defaultdict
import nltk.corpus
import os

start = time.time()

# Stop the timer and print running time
def finish():
  print time.time() - start, "seconds"
  sys.exit("Finishing")

# Method for creating a nested dictionary
def nested_dict():
  return defaultdict(float)

# Load the n-grams file
# File is of the form:
# Word1 Word2 Occurrences
def load_ngrams(filepath):
  textfile = open(filepath)
  ngrams = defaultdict(nested_dict)
  last_word = ""
  current_set = {}
  for line in textfile:
    split_line = line.split()

    if last_word != split_line[0]:
      ngrams[last_word] = current_set
      last_word = split_line[0]
      current_set = {}
      current_set[split_line[1]] = float(split_line[2])
    else:
      current_set[split_line[1]] = float(split_line[2])

  ngrams[last_word] = current_set

  return ngrams


# Entry point for the run script
# Creates a dictionary
d = Dictionary()
strategies = {"mostcommon": most_common, "noisynorvig": noisy_channel_norvig, "noisyngrams": noisy_channel_with_ngrams, "charngrams": char_level_ngrams, "voting": voting}
n_grams = {}

if len(sys.argv) == 1:
  print "Please supply a function. The choices are train, accuracy or tweets"
  finish()

if sys.argv[1] == "train":
  print "Training the system!"

  if len(sys.argv) == 2:
    print "Please enter some training texts.\nUsage: python run.py train [files]"
    finish()
  for i in range(2, len(sys.argv)):
    # If a built-in nltk corpus use that
    # else load the text file and learn
    print "Processing: ", i-1, " of ", len(sys.argv) - 2
    if sys.argv[i] == "abc":
      d.learn_words(nltk.corpus.abc.words())
    elif sys.argv[i] == "genesis":
      d.learn_words(nltk.corpus.genesis.words())
    elif sys.argv[i] == "gutenberg":
      d.learn_words(nltk.corpus.gutenberg.words())
    elif sys.argv[i] == "inaugural":
      d.learn_words(nltk.corpus.inaugural.words())
    elif sys.argv[i] == "udhr":
      d.learn_words(nltk.corpus.udhr.words())
    elif sys.argv[i] == "state_union":
      d.learn_words(nltk.corpus.state_union.words())
    elif sys.argv[i] == "nps":
      d.learn_words(nltk.corpus.nps_chat.words())
    else:
      d.learn_from_text(sys.argv[i])

  # Remove possible noise by thresholding dictionary
  # to at least 40 occurrences
  d.cull_words(40)

  # Save dictionary to file
  d.save_dictionary()

elif sys.argv[1] == "accuracy":
  # Test against an error to correction mapping text file such as
  # Sheffield test set
  if len(sys.argv) < 5:
    print "Please provide a dictionary, correction strategy, and a test file.\nUsage: python run.py accuracy <dictionary> <correction strategy> <test file>"
    finish()

  d.load_dictionary(sys.argv[2])

  if sys.argv[3] not in strategies:
    print "Correction strategy not recognised. Available strategies are: ", strategies
    finish()
    
  strategy = strategies[sys.argv[3]]

  error_mapping = load_corrections_from_file(sys.argv[4])

  if sys.argv[3] == "noisyngrams" or sys.argv[3] == "voting":
    n_grams = load_ngrams(sys.argv[5])

  correct_count = 0.0
  incorrect_count = 0.0

  for correct_word in error_mapping:
    error = error_mapping[correct_word]
    if error not in d.dictionary_words:
      if (sys.argv[3] == "noisyngrams") or (sys.argv[3] == "voting"):
        nearest_word = strategy(error.lower(), ["",""], d.dictionary_words, n_grams) 
      else:
        nearest_word = strategy(error.lower(), d.dictionary_words)
      
      if nearest_word == correct_word:
        correct_count += 1.0
      else:
        incorrect_count += 1.0
        print "The system corrected ", error, " to ", nearest_word, " but the correct answer was ", correct_word
  
  print "Total accuracy:", (correct_count/(correct_count + incorrect_count))
  finish()

elif sys.argv[1] == "tweets":
  # Usage: run.py tweets dictionary_file correction_strategy tweets_file (n-grams if method needs n-grams)
  d.load_dictionary(sys.argv[2])

  if sys.argv[3] not in strategies:
    print "Correction strategy not recognised. Available strategies are: ", strategies
    finish()
  
  if sys.argv[3] == "noisyngrams" or sys.argv[3] == "voting":
    n_grams = load_ngrams(sys.argv[5])

  strategy = strategies[sys.argv[3]]

  total = number_of_tweets(sys.argv[4])

  print "Correcting", total, "tweets"

  tweets = open(sys.argv[4])
  output = file("output.txt", 'w')

  count = 0

  # A dictionary of common contractions that were found using error analysis
  common_contractions = {"r":"are", "u":"you", "aint":"ain't", "ur":"your", "n":"and", "cud":"could", "pls":"please", "plz":"please", "2":"to", "4":"for", "ya":"your", "y":"why", "thnx":"thanks", "thx":"thanks", "tho":"though", "sumthin":"something", "sumthing":"something", "bday":"birthday", "hw":"how", "lyf":"life", "luv":"love", "lovin":"loving", "nite":"night", "goodnite":"goodnight", "2morrow":"tomorrow", "2moro":"tomorrow", "da":"the", "thru":"through", "dnt":"don't", "msg":"message", "msgs":"messages", "nxt":"next", "wk":"week", "isn":"isn't", "theres":"there's", "bac":"back", "itll":"it'll", "ppl":"people"}

  for tweet in tweets:
    corrected_tweet = ""
    tweet_length = len(tweet.split())
    corrections_count = 1.0
    corrections = []
    words = tweet.split()
    count += 1

    for i in range(len(words)):
      word = words[i]
      context = []
      
      # Find the context of the error (one word either side)
      if i == 0:
        context = ["",strip_punctuation(words[1]).lower()]
      elif i == len(words) - 1:
        context = [strip_punctuation(words[i - 1]).lower(), ""]
      else:
        context = [strip_punctuation(words[i - 1]).lower(), strip_punctuation(words[i + 1]).lower()]

      # If we are correcting over 1/3 then it's probably non-English
      if corrections_count > (tweet_length/3):
        corrected_tweet = "Non-English language detected!"
        break
      # If the error is a common contraction simply perform dictionary lookup and add
      # to corrected Tweet
      if word.lower() in common_contractions:
        corrected_tweet += common_contractions[word.lower()] + " "
        corrections.append(word.lower())
      elif has_tweet_specific_features(word) or strip_punctuation(word.lower()) in d.dictionary_words:
        # if it has Tweet specific features or the word is in the dictionary then it's
        # not an error so add it to the corrected Tweet
        corrected_tweet += word + " "
      else:
        # Must be an error so use the strategy selected and correct it
        word_stripped = strip_punctuation(word)
        if (sys.argv[3] == "noisyngrams") or (sys.argv[3] == "voting"):
          corrected_tweet += strategy(word_stripped.lower(), context, d.dictionary_words, n_grams) + " "
        else:
          corrected_tweet += strategy(word_stripped.lower(), d.dictionary_words) + " "
        corrections_count += 1.0
        corrections.append(word.lower())

    print "Corrected: ", count, "of", total

    output.write(corrected_tweet + "\n") 

  print "Completed succesfully!"
