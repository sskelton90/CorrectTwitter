import nltk
from nltk.collocations import *
from word_tools import *

bigram_measures = nltk.collocations.BigramAssocMeasures()
n_grams = []

def train_model(files):
  words = []
  for f in files:
    if f == "abc":
      text = nltk.corpus.abc.words()
    elif f == "genesis":
      text = nltk.corpus.genesis.words()
    elif f == "gutenberg":
      text = nltk.corpus.gutenberg.words()
    elif f == "inaugural":
      text = nltk.corpus.inaugural.words()
    elif f == "udhr":
      text = nltk.corpus.udhr.words()
    elif f == "state_union":
      text = nltk.corpus.state_union.words()
    else:
      text = open(f)

    for line in text:
      words.extend([strip_punctuation(word).lower() for word in line.split()])
  # Use NLTK's built-in collocation finder to find all n-grams of size >= 40  
  finder = BigramCollocationFinder.from_words(words)
  finder.apply_freq_filter(40)

  finder.apply_word_filter(lambda w: w.isdigit() or w == "")
  scored = finder.score_ngrams(bigram_measures.raw_freq)
  sort = sorted(finder.above_score(bigram_measures.raw_freq, 1.0 / len(nltk.bigrams(words))))

  output = file("n_gram_output.txt", 'w')

  for s in sort:
    output.write(s[0] + " " + s[1] + " " + str(finder.score_ngram(bigram_measures.raw_freq, s[0], s[1])) + "\n")

train_model(["../../textfiles/apw200201", "../../textfiles/apw200202", "../../textfiles/apw200203", "../../textfiles/apw200204", "../../textfiles/apw200205", "../../textfiles/apw200206", "../../textfiles/MyCorpus", "abc"])
