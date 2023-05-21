import pysam

# a) Ucitati FASTA ________ koristeci PYSAM biblioteku i 
#    izdvojiti sekvencu na __ chr od ___:___
#    pozicije u promenljivu ‘sekvenca’.

fasta_path = "/home/boza/Desktop/Homo_sapiens_assembly38.fasta"
fasta = pysam.Fastafile(fasta_path)

#print(len(fasta.references))
seq = 'chr20'
seq_length = len(fasta.fetch(seq))
#print(seq_length)
seq_begin = 88888
seq_end   = 88970

dna_sequence = fasta.fetch(seq, seq_begin, seq_end)
print("a)  ", dna_sequence, end="\n\n")
#print(len(dna_sequence))

# b) Napisati funkciju kamerizuj(sekvenca, k) koja za argumente ima DNK sekvencu
#    i duzinu k-mera, a vraca listu svih jedinstvenih k-mera prisutnih u sekvenci
#    i pozvati je na izdvojenoj sekvenci. Koliko je jedinstvenih k-mera duzine 4?

def kamerizuj(sequence, k):
    n = len(sequence)
    kmers = []
    for i in range(0, n):
        if (i + k < n) and (sequence[i: i+k] not in kmers):
            kmers.append(sequence[i: i+k])

    return kmers, len(kmers)

kmers, kmers_length = kamerizuj(dna_sequence, 4)
print("b)  ", kmers)
print("    ", kmers_length, end="\n\n")


# c) Napisati funkciju debruijnizuj(read, k) koja za DNK sekvencu i duzinu
#    k-mera, za svaki jedinstveni k-mer u sekvenci vraca listu njegovog
#    levog k-1-mera i njegovog desnog k-1-mera u paru.
#    Npr: print(debruijnizuj(“ACGCGTCG", 3))
#    [(‘AC', 'CG'), ('CG', 'GC'), ('GC', 'CG'),
#     ('CG', 'GT'), ('GT', 'TC'), ('TC', 'CG')])

def debruijnizuj(read, k):
    n = len(read)
    kmers = []
    for i in range(n - k + 1):
        kmers.append((read[i: i+k-1], read[i+1: i+k]))

    return kmers

print("c)  ", debruijnizuj("ACGCGTCG", 3), end="\n\n")

# d) Koju vrednost vraca len(debruijnizuj(sekvenca, 3))?

v = len(debruijnizuj(dna_sequence, 3))
print("d)  ", v)
