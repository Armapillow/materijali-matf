import pysam
import random

fasta_path = "/home/boza/Desktop/Homo_sapiens_assembly38.fasta"
fasta = pysam.Fastafile(fasta_path)
#print(fasta.references)

region = "HLA-DRB1*15:01:01:01"
print(fasta.fetch(region))
print(fasta.lengths)

#print(fasta.fetch(region, 0, 100))

n      = fasta.nreferences
region = fasta.references[random.randrange(0, n)]
print(fasta.fetch(region, 0, 100))

#for seq_name in fasta.references:
    #print(seq_name)

#print(fasta.get_reference_length('chr5'))
#print(fasta.fetch('chr17', 43044295, 43125370))
