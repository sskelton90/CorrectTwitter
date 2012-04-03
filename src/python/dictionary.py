from word_tools import *
import codecs

class Dictionary:

  dictionary_words = {}

  def learn_from_text(self, text):
    f = open(text)

    # Tokenise (in word_tools file) and learn words
    for line in f:
      self.learn_words(tokenise(line))

  # Count occurrences of words
  def learn_words(self, words):
    for i in range(len(words)):
      if words[i].lower() in self.dictionary_words:
         self.dictionary_words[words[i].lower()] = self.dictionary_words[words[i].lower()] + 1
      else:
         self.dictionary_words[words[i].lower()] = 1

  # Save dictionary as key (word) value (occurrences) pairs into text file
  def save_dictionary(self):
    output = file('dictionary.txt', 'w')
    for key in self.dictionary_words.keys():
      try:
        output.write(key + "\t" + str(self.dictionary_words[key]) + "\n")
      except UnicodeEncodeError:
        continue

    output.close()

  # Load dictionary from word/value key pair text file
  def load_dictionary(self, filename):
    input_file = open(filename)
    for line in input_file:
      split_line = line.split('\t')
      self.dictionary_words[split_line[0]] = int(split_line[1])

    input_file.close()

  def in_dictionary(self, word):
    return word in self.dictionary_words

  # Remove words that fall under a certain threshold
  def cull_words(self, thresh):
    for k, v in list(self.dictionary_words.items()):
      if v < thresh:
        del self.dictionary_words[k]
