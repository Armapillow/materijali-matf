# Problem: https://rosalind.info/problems/dna/

#sequence = "ATGCTTCAGAAAGGTCTTACG"
sequence = "AGCTTTTCATTCTGACTGCAACGGGCAATATGTCTCTGTGTGGATTAAAAAAAGAGTGTCTGATAGCAGC"

count = {'A' : 0,
         'T' : 0,
         'G' : 0,
         'C' : 0}
n = len(sequence)
for i in range(n):
    count[sequence[i]] += 1

print(count['A'], count['C'], count['G'], count['T'])
