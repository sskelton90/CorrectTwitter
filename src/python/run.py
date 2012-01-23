import sys
from word_tools import *
from dictionary import *
from correction_strategies import *
from collections import defaultdict

def finish():
  sys.exit("Finishing")

def nested_dict():
  return defaultdict(float)

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


d = Dictionary()
strategies = {"mostcommon": most_common, "noisynorvig": noisy_channel_norvig, "noisyngrams": noisy_channel_with_ngrams}
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
    d.learn_from_text(sys.argv[i])

  d.save_dictionary()

elif sys.argv[1] == "accuracy":
  if len(sys.argv) != 5:
    print "Please provide a dictionary, correction strategy, and a test file.\nUsage: python run.py correct <dictionary> <correction strategy> <test file>"
    finish()

  d.load_dictionary(sys.argv[2])

  if sys.argv[3] not in strategies:
    print "Correction strategy not recognised. Available strategies are: ", strategies
    finish()
    
  strategy = strategies[sys.argv[3]]

  error_mapping = load_corrections_from_file(sys.argv[4])

  correct_count = 0.0
  incorrect_count = 0.0

  for correct_word in error_mapping:
    error = error_mapping[correct_word]
    if error not in d.dictionary_words:
      nearest_word = strategy(error, d.dictionary_words)
      if nearest_word == correct_word:
        correct_count += 1.0
      else:
        incorrect_count += 1.0
        print "The system corrected ", error, " to ", nearest_word, " but the correct answer was ", correct_word
  
  print "Total accuracy:", (correct_count/(correct_count + incorrect_count))
  finish()

elif sys.argv[1] == "tweets":
  d.load_dictionary(sys.argv[2])

  if sys.argv[3] not in strategies:
    print "Correction strategy not recognised. Available strategies are: ", strategies
    finish()
  
  if sys.argv[3] == "noisyngrams":
    n_grams = load_ngrams(sys.argv[5])

  strategy = strategies[sys.argv[3]]

  print "Correcting", number_of_tweets(sys.argv[4]), "tweets"

  tweets = open(sys.argv[4])
  output = file("output.txt", 'w')

  for tweet in tweets:
    corrected_tweet = ""
    tweet_length = len(tweet.split())
    corrections_count = 1.0
    corrections = []
    words = tweet.split()

    for i in range(len(words)):
      word = words[i]
      
      context = []
      if i == 0:
        context = [words[1]]
      elif i == len(words) - 1:
        context = [words[i - 1]]
      else:
        context = [words[i-1], words[i + 1]]

      if corrections_count > (tweet_length/3):
        corrected_tweet = "Non-English language detected!"
        break
      if has_tweet_specific_features(word) or strip_punctuation(word.lower()) in d.dictionary_words:
        corrected_tweet += word + " "
      else:
        word_stripped = strip_punctuation(word)
        if (sys.argv[3] == "noisyngrams"):
          corrected_tweet += strategy(word_stripped.lower(), context, d.dictionary_words, n_grams) + " "
        else:
          corrected_tweet += strategy(word_stripped.lower(), d.dictionary_words) + " "
        corrections_count += 1.0
        corrections.append(word.lower())

    output.write(tweet + "\n" + corrected_tweet + "\n")
    for correction in corrections:
      output.write(correction + "\n")
  
  print "Completed succesfully!"




