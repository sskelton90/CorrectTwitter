from word_tools import *

class Dictionary:

  dictionary_words = {}

  def learn_from_text(self, text):
    f = open(text)

    for line in f:
      self.learn_words(tokenise(line))
    
  def learn_words(self, words):
    for word in words:
      if word in self.dictionary_words:
        self.dictionary_words[word] = self.dictionary_words[word] + 1
      else:
        self.dictionary_words[word] = 1

  def save_dictionary(self):
    output = file('dictionary.txt', 'w')
    for key in self.dictionary_words.keys():
      output.write(key + "\t" + str(self.dictionary_words[key]) + "\n")

    output.close()

  def load_dictionary(self, filename):
    input_file = open(filename)
    for line in input_file:
      split_line = line.split('\t')
      self.dictionary_words[split_line[0]] = int(split_line[1])

    input_file.close()

  def in_dictionary(self, word):
    return word in self.dictionary_words
