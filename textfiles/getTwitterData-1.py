import gzip
import sys

input_file = gzip.open('/group/project/statmt13/twitter-data/twitterStream-20091110-20100201-v0.1.1.gz')
output_file = open('Tweets.txt', 'w')

lower = int(sys.argv[1])
upper = int(sys.argv[2])

def write_to_file():
    for i, line in enumerate(input_file):
	if (i > upper):
	    return
	elif (i >= lower and i <= upper):
	    output_file.write(line)

write_to_file()

input_file.close()
output_file.close()
