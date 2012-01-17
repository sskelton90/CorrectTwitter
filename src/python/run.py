import sys
from word_tools import *
from dictionary import *
from correction_strategies import *

def finish():
  sys.exit("Finishing")

d = Dictionary()
strategies = {"mostcommon": most_common, "noisynorvig": noisy_channel_norvig}

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
