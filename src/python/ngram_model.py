import nltk
from nltk.collocations import *
from word_tools import *

bigram_measures = nltk.collocations.BigramAssocMeasures()
trigram_measures = nltk.collocations.TrigramAssocMeasures()

n_grams = []

def train_model(files):
  words = []
  for f in files:
    text = open(f)
    for line in text:
      words.extend([strip_punctuation(word).lower() for word in line.split()])
    
  finder = BigramCollocationFinder.from_words(words)
  finder.apply_freq_filter(40)

  finder.apply_word_filter(lambda w: w.isdigit() or w == "")
  scored = finder.score_ngrams(bigram_measures.raw_freq)
  sort = sorted(finder.above_score(bigram_measures.raw_freq, 1.0 / len(nltk.bigrams(words))))

  output = file("n_gram_output.txt", 'w')

  for s in sort:
    output.write(s[0] + " " + s[1] + " " + str(finder.score_ngram(bigram_measures.raw_freq, s[0], s[1])) + "\n")

train_model(["../../textfiles/apw200201", "../../textfiles/apw200202", "../../textfiles/apw200203", "../../textfiles/apw200204"])
