from word_tools import *
import string
import math

def most_common(input_word, dictionary):
  candidates = []
  max_value = 0
  max_word = ""

  for real_word in dictionary:
    if edit_distance(input_word, real_word) <= 1:
      candidates.append(real_word)

  for candidate in candidates:
    prior = dictionary[candidate]

    if prior > max_value:
      max_value = prior
      max_word = candidate

  return max_word

def noisy_channel_norvig(word, dictionary):
  candidates = known([word], dictionary) or known(edits1(word), dictionary) or edits2(word, dictionary) or [word]
  return max(candidates, key=dictionary.get)

def edits1(input_word):
  splits = [(input_word[:i], input_word[i:]) for i in range(len(input_word) + 1)]
  deletes = [a + b[1:] for a,b in splits if b]
  transposes = [a + b[1] + b[0] + b[2:] for a, b in splits if len(b) > 1]
  replaces = [a + c + b[1:] for a, b in splits for c in string.lowercase]
  inserts = [a + c + b for a,b in splits for c in string.lowercase]

  return set(deletes + transposes + replaces + inserts)
  
def edits2(word, d):
  return set(e2 for e1 in edits1(word) for e2 in edits1(e1) if e2 in d)

def known(words, d):
  return set(w for w in words if w in d)

def noisy_channel_with_ngrams(word, context, dictionary, ngrams):
  candidates = known([word], dictionary) or known(edits1(word), dictionary) or edits2(word, dictionary) or [word]

  # If there's only one correction return it
  if len(candidates) == 1:
    return candidates.pop()
  # Otherwise rank based on frequency of correction and n-gram probability
  max_prob = 0.0
  max_word = ""
  for c in candidates:
    ngram_prob = 0.0
    try:
      if context[0] == "":
        ngram_prob = ngrams[c][context[1]]
      elif context[1] == "":
        ngram_prob = ngrams[context[0]][c]
      else:
        ngram_prob = max(ngrams[context[0]][c], ngrams[c][context[1]])

      if ngram_prob == 0.0:
        ngram_prob = 1/float(len(ngrams))
    except KeyError:
      ngram_prob = 1/float(len(ngrams))
    
    prob = dictionary[c] * ngram_prob
    if prob > max_prob:
      max_prob = prob
      max_word = c

  return max_word

  
