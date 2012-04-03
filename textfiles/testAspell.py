f = open('SHEFFIELDDAT.643')

total = 0
correct = 0

for line in f:
  first = line.split('\t')[0].strip()
  second = line.split('\t')[1].strip()

  print first, second, first == second

  if first == second:
    correct += 1

  total += 1

print float(correct)/total
