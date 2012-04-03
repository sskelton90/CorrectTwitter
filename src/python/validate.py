import sys
from word_tools import *
import re

file1 = open(sys.argv[1])
file2 = open(sys.argv[2])

correct = 0
size = 0.0

f1 = []
f2 = []

for line in file1:
  f1.append(line)

for line in file2:
  f2.append(line)

# Compare every word in the corrected version to the hand-corrected version
# Return percentage accuracy alongside any errors
for i in range(len(f1)):
  s1 = f1[i].split()
  s2 = f2[i].split()

  size += float(len(s1))

  for j in range(min(len(s1), len(s2))):
    if s1[j].lower() == s2[j].lower():
      correct += 1
    else:
      print s1[j].lower(), s2[j].lower()
    
print correct/size
