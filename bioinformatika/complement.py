# Problem: https://rosalind.info/problems/revc/

dna_string = "AAAACCCGGT"

rev_string = dna_string[::-1]

rev = ""
for s in rev_string:
    if s == 'A':
        rev += 'T'
    elif s == 'T':
        rev += 'A'
    elif s == 'C':
        rev += 'G'
    else:
        rev += 'C'

print(rev)
