import pysam

# a) Ucitati _______ FASTA file koristeci PYSAM python biblioteku.

path = "/home/boza/Desktop/Homo_sapiens_assembly38.fasta"
fasta_file = pysam.Fastafile(path)

# b) Odstampati regione svih kontiga u FASTI. Koji je njihov broj?

for i in fasta_file.references:
    print(i)
print("a)  ", fasta_file.nreferences)

# c) Koja je duzina hromozoma 17?

chr17 = fasta_file.fetch('chr17')
print("c)  Duzina hromozoma 17 je ", len(chr17))

# d) Dohvatiti region 43044295:43125370 sa hromozoma 17 i odrediti procenat G i C
#    baza.

seq_begin = 43044295
seq_end   = 43125370

dna_seq = fasta_file.fetch('chr17', seq_begin, seq_end)
n       = len(dna_seq)
count   = { 'G' : 0, 'C' : 0}

for i in dna_seq:
    if i == 'G' or i == 'C':
        count[i] += 1

pro = (count['G'] + count['C']) / n
print("d)  Procenat G i C baza je ", round(pro * 100, 3))

# e) Dohvatiti region 50100:50200 sa hromozoma 1 i odrediti koliko kojih baza je
#    prisutno. Koliko A,T,C,G?

seq_begin, seq_end = 50100, 50200
dna_seq = fasta_file.fetch('chr1', seq_begin, seq_end)

bases = {'A' : 0, 'T' : 0, 'G' : 0, 'C' : 0}
for i in dna_seq:
    bases[i] += 1

print("e)  A - ", bases['A'])
print("    T - ", bases['T'])
print("    G - ", bases['G'])
print("    C - ", bases['C'])
