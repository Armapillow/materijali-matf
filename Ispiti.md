1. [Rel baze podataka](#baze)
2. [Istraživanje podataka](#istraživanje-podataka)
3. [Razvoj softvera](#razvoj-softvera)
4. [Veštačka inteligencija](#veštačka-inteligencija)
5. [Grafika](#grafika)

# Baze

## sep1 2022

rel algebra i racun
  Napisati upit koji izdvaja imena predmeta koji su polozeni sa ocenom 10 od
  strane studenata koji nisu iz Beograda

```sql
  ((ispit where ocena=10)[idpredmeta, indeks]
  join
  (dosije where mestorodjenja<>Beograd)[indeks])
  join
  (predmet)[naziv]
```

procenti uspesnosti po rokovima studenata koji su trenutno aktivni i cije
mestorodjenja sadrzi nisku Beograd. Indeks, naziv roka, procenat uspesnosti
polaganja ispita (broj polozenih/broj prijavljenih), prosek...


TABELA TIMOVI

Par studenata koji su polozili programiranje2 sa ocenom barem 8 (1688 i 1700??
su id tih predmeta) pravi se tabela timovi sa atributima:

```sql
create table timovi
(
  indeks1 integer not null,
  indeks2 integer not null,
  naziv varchar(10),  -- inicijalni studenata MS.AD
  ocena double        -- prosek ocene iz programiranja oba studenta
);
```

Dodati ogranicenja za primarne kljuceve i da ocena mora da bude zaokruzena na 2
decimale.

Popuniti tabelu tako da parovi budu iz istog mesta rodjenja i zadovoljavaju onaj
uslov gore. Radio sam sa dva with

```sql
insert into timovi(indeks1, indeks2, naziv, ocena)
with stud1 as
(
  select d.indeks, d.ime, d.prezime, d.mestorodjenja, i.ocena
  from da.dosije d
  join da.ispit i
  on d.indeks=i.indeks
  where ocena>=8 and status='o' and idpredmeta in (1688, 1700)
),
  stud2 as
(
  select d.indeks, d.ime, d.prezime, d.mestorodjenja, i.ocena
  from da.dosije d
  join da.ispit i
  on d.indeks=i.indeks
  where ocena>=8 and status='o' and idpredmeta in (1688, 1700)
)
select stud1.indeks, stud2.indeks, ...,
      dec((stud1.ocena+stud2.ocena*1.0)/2.0, 5, 2)
from stud1, stud2
where stud1.indeks<stud2.indeks and stud1.indeks<>stud2.indeks
and stud1.mestorodjenja=stud2.mestorodjenja
```

ovo je oko 20 ili 21 K novih redova

obrisati sve parove studenata koji nisu sa istog smera ili čija godina upisa
nije ista!

```sql
delete from timovi
where (indeks1, indeks2) in
(
  select d1.indeks, d2.indeks
  from da.dosije d1, da.dosije d2
  where d1.indeks<d2.indeks and d1.indeks<>d2.indeks
  and (d1.idprograma<>d2.idprograma or (year(d1.datupisa)<>year(d2.datupisa)))
)
-- ovo obrise oko 19 407
```

5.

a) Definicija relacije, torke, atributa. Navesti primer sa svakog od njih. Sta
je zatvorenje relacionog sistema?

b) Navesti minimalni skup operatora relacione algebre. Opisati prosirenje i
navesti primer

c) Sta je ogranicenje integriteta, navesti i primer. Kako glasi zlatno pravilo
(za ogr. integr.)?

d) Sta je pogled, a sta vrednost pogleda?

6.

a) SQL funcija koja lista najkasniji datum polaganja nekog ispita za nekog
studenta, koji se prosedjuju kroz argumente. Koristiti konstrukciju bez BEGIN i
END

b) studente i predmete koji su polozili, ali u listu i urediti u rastucem
poretku. (Sa listgg)

c) Napraviti okidac, koji kada student prijavi ispit 11 put salje poruku 'Jedan
ispit se moze najvise prijviti 10 puta'.

7.

a) Opisati problem zavisnosti od nepotvrdjenih podataka

b) Objasniti dvofazni commit

c) Navesti jednu prednost i jednu manu tabela organizovanih po kolonama umesto
redova.

d) Navesti Amstrongove aksiome i gde se one primenjuju.

8.

Dato B+ drvo
Navesti uslove koje vaze za unutrasnje cvorove i listove.

unosenje i brisanje 

# Istraživanje podataka

## Septembar 1 2022

1. Podela atributa po operacijama. Gde pripada ime destinacije. Kako bi vektorski predstavili destinacije Kairo, Zanzibar, Maldive, Istanbul.
2. Kosinusna sličnost, definicija, objasniti šta je šta. Gde se koristi najčešće.
3. Kako se i šta predstavlja sa graficima sa paralelnim... Koje su prednosti, za šta su pogodni.
4. Objasniti Kandanov?? algoritam za konstrukciju drveta odlučivanja
5. Roc kriva. Kako se koristi, šta predstavlja. Nacrtati primer.
6. Objasniti kako funkcioniše i gde se najčešće koristi alogritam K disekcija??
7. Algoritam percentila, objasniti, nacrtati???
8. Objasniti kako rade algoritmi zasnovani na .../gustinama
9. Šta su anomalije/elementi van granice. Kako se određuju i prikazuju na boxplot-u
10. Definisati Lift, za šta se koristi, navesti opseg i objasniti šta znači.

# Razvoj softvera

## jun1 2022 

- Skicirati diagram za posetilac
- Neke vrste spregnutosti (logička ili ...)
- Optimizacija
- Objasni neki princip agilni - isporučivanje


##  jun2 2022

1. Koncept polimorfizma. Koje vrste postoje i razlike među njima. [228]
2. Logicka kohezija u kontekstu razvoja softvera. [95]
3. Sta je dekompozicija prema promenljivosti? Što je značajna? [82]
4. Objasniti princip dizajna softvera Stvaralac, kojoj grupi pripada, na sta se odnosi i sta sugeriše. [165]
5. Elementi razvojnog ciklusa po metodologiji Skram. [??]
6. Koje uloge ima testiranje jedinice koda. Zbog čega je sve važno? [214, 215]
7. Objasniti namenu obrazaca ponašanja. Navesti bar 3. [138]
8. Zašto privremene promenljive mogu da predstavljaju problem. [249]
9. Objasniti pravilo debagovanja "Navesti sistem na grešku". [275]
10. Najvažnije metrike praćenja softvera. Šta opisuju. [357]


```
[
  # Magacin 1; slično za magacin 2 i 3, samo drugačiji brojevi kod products
  {
    "id":1,
    "products": [
      "Hleb:-5",
      "Mleko:10",
      "Crvena paprika:40",
      "Brasno:15",
      "Zelena parika:20",
    ]
  }, ...
]
```

Klasa Warehouse, koja ima id magacina, listu (vektor) proizvoda, jedan proizvod je `Qpair<QString, int>`, ime i količina.
Metoda za biranje random proizvoda koji ima nedovoljnu količinu (u minusu), taj metod vraća `Qpair<QString, int>`. Takođe metod koji menja količinu proizvodu sa datim imenom, metod koji oduzima datu količinu proizvodu sa prosleđenim imenom. Za svaki magacin se pokreće jedna nit. U jednoj niti bira se random magacin koji nije tekući.



# Veštačka inteligencija

## Jun1 2022

1. Šta dodati DFS algoritmu da postane informisovana pretraga
2. Ako imamo rastojanje od grada do grada, da li nam više koristi informisana ili neinformisana pretraga. Zašto? (Kako može da ti pomogne informisana pretraga za rastojanje od gradova)
3. Šta je gradijent?
4. Koji je uslov da bi se zaustavio algoritam _prvo najbolji_?
5. Kad se izračuvana funkcija evaluacije u algoritmu minimaks:  
    * U svim čvorovima
    * Samo na krajnjim čvorovima
    * samo u najboljim čvorovima
6. Date je tačka sa njenim koordinatama i njena nadmorska visina - smisli heuristiku gde može ovo da se iskoristi.
7. Šta su **$\top$**, **$\bot$** i 0 , 1 u logici?
8. U šta se, u svakoj interpretaciji logike prvog reda, preslikava predikatski simbol ???
9. Navesti teoremu o zameni za logiku prvog reda.
10. Šta je Cejtinova transformacija i zbog čega je pogodna.
11. Ako je $\neg A$ nije poreciva šta je $A$?  
Ako je $\neg A$ poreciva šta je $A$?
12. Da li valjanost/zadovoljivost/nezadovoljivost u log prvog reda odlučiva: Da/ne
13. Zašto se kod mašinskog učenja koristi verovatnoća, a ne logika?
14. Koji koeficijent najviše utiču, poređati, f = 2x1-10x2+3x3-x4
15. Šta znači kada je koeficijent determinisanosti $R^2=1$
16. Zašto/kada kažemo da su markovljevi procesi stohastički
17. Kad je nešto najveće...
18. Agent, primer sa semaforima, odredi agenta

## Jun2 2022

1. Ako prvi put stignemo do nekog čvora, da li je taj put ujedno i najkraći:<br>
   DFS: da/ne <br>
   BFS: da/ne <br>
   Dajkstra: da/ne <br>
2. Kada algoritam pohlepne pretrage vraća neuspeh?
3. Šta je stohastičko penjanje uzbrdo?
4. U pretrazi prvo najbolji šta se stavlja u otvorenu listu?
5. Kako osnovni algoritam alfa/beta vrši pretragu: dubina/širina
6. Ako su date dopustive heuristike h1, h2..., hn za algoritam A\*, kako se može dobiti nova dopustiva heuristika?
7. Cejtinovo kodiranje za formulu: $(p \wedge \neg q) \vee \neg p $?
8. Ako možemo samo da izračunamo zadovoljivost, kako da dokažemo da je formula tautologija A preko toga?
9. Da li je ispravna iskazana formula, ako je p iskazno slovo (da/ne): $p \vee p$; $p \Leftrightarrow p$; $p \equiv p$
10. Da li su ispravni izrazi u logici prvog reda (da/ne): $\forall x \forall y (x \Rightarrow p(x))$; $\forall x (p(x) \Rightarrow p(x))$; $\forall x \exists x\  p(x)$; $\forall x \forall p\ p(x)$
11. Zapiši sledeću rečenicu pomoću logike prvog reda: "Ne postoji zemlja koja se graniči sa Španijom, a nema izlaz na more".
12. Šta je najopštiji unifikator?
13.  Kategorički ili neprekidni: temperatura, rasa psa, reč u igri asocijacija, vreme ispitivanja zadovoljivosti iskazane formule.
14. Šta znači kada je hiperparametar $\lambda$ jednak 0?
15. Ako je ulazna slika veličine 6x6, filter 3x3, kolika je dimenzija rezultujuće slike?
16. Koja je uloga validacionog skupa?
17. Šta je Markovljevo svojstvo?
18. Kada se ažurira vrednost q(s,a) u q-učenju?
19. Kada je primenljiv osnovni algoritam q-učenja?

# Grafika

## April 2022

Pismeni:

1. Petlja renderovanja
2. Pipeline
3. Koordinate u kom su opsegu i kako se zove transformacija koja ih pretvara u screen coordinates
4. gl_Position
5. Rotacija u R3 oko z ose ugao 60° i neke tačke
6. Neunoformno skaliranje, primer slika, kako se popravlja
7. Face culling, koliko otprilike % manje računanja fragment sejdera ima, zašto
8. Koji vektori scene se koriste kod računanja difuznog??
9. Od čega se sastoji matrica perspektivne zapremine i da se nacrta kako izgleda perspektivna zapremina
10. Od čega se sastoji fongov model svetlosti
11. Kako se računa intenzitet spotlight osvetljenja
12. Kako se računa spekularno kod blin fongovog modela, izvesti (nacrtati?)
13. Šta je z-fighting i kako se popravlja
14. Dopuna koda, interpolacija proseka koordinata

Grafika teorijski ispit:

1. Renderovanje unapred i unazad, šta je, navedu jedan algoritam za svaki od njih, algoritam dubine
2. Midpoint krug, prva vrednost promenljive odlučivanja, (0, 4) je prva tačka koja je sledeća
3. Napisati proceduru za leftFill4 ili bountyFill4
4. Rotacija oko neke tačke, kao i inverzija te rotacije
5. Koen-sander.. složenost po nekom uslovu
6. Šta je rezultat i koja je složenost leftEdgeScan algoritma
7. Koje su paralelne projekcije i po čemu se razlikuju
8. Zašto se zadaju prednja i zadnja ravan odsecanja, njihove vrednosti kod paralelne i perspektivne zapremine pogleda
9. Zašto se koristi deljenje ivice?? Kad se jedan uzak i dugačak deli na dva
10. Šta se čuva u Wing edge strukturi za ivice i temena
11. Sortiranje objekata kod Rej trejsing i z bafera: da/ne
12. Slika, trougao B u  A matrica transformacije
13. Prvi korak u algoritmu dubine i kako bi to izgledalo (nacrtati sliku)
14. ...
15. Šta je gama korekcija
16. HSV over RGB, zašto
17. Fongovo i guoroovo spekularno, zašto se razlikuju
18.  Koja je razlika između rekurzivnog Rej trejsing i običnog
19. Nadsamplovanje, one tri i šta rade
20. Dati odnosi, izračunaj baricentričke koordinate


## sept 2

1. Sta je frejm bafer
2. Sta je bit dubine
3. Ako je b u jednacini y = ax + by + c parno, da li su u midpoint algoritmu za crtanje duzi neki izraz mnozi sa dva. Obrazloziti.
4. Ako je R poluprecinik, koliko se otprilike piksela boji u midpoint algoritmu za crtanje kruga.
5. Da li se algoritam sken linije koristi za: (a) vektorsku, (b) rastersku (c) obe  
   Da li se algoritam floodfill koristi za: (a) vektorsku, (b) rastersku (c) obe
6. Na koliko regiona deli Koen-Saderlandov algoritam kliping region. Sa koliko bita se predstavljaju. Obrazloziti.
7. Sejrus Beker nešto sa t=0.
8. Rotacija u ravni oko tačke (2, -1) za 30 stepeni i inverz. Ne treba mnoziti matrice.
9. Koji tip transformacije se može zadati sa 2x2 matricom u 2d?  
   Koji tip transformacije se može zadati sa 3x3 u 2d??
10. Proizvoljna u standardnu perspektivnu koje tačke su potrebne i gde se preslikavaju.
11. Mreže mnogostrukosti da/ne:  
  - sa granicom da li svaka ivica ima dva suseda
  - da li ima granicu jednako nula
  - da li teme ima više suseda
12. Sažimanje ivice 6,7 sa neke slike da se nacrta.
2. Kako se predstavljaju parametrizovane površi u 3D i sa koliko parametara?
3. Sta je primarno vido polje? Zašto nije dovoljno kod algoritama dubine?
4. Sta se radi kad se poklapaju poligoni u algoritmu sortiranja dubine?
5. Kako se za mreže poligona računa da li se nalaze ispred ili iza u odnosu na posmatrača.
6. Da li X, Y i Z mogu da se predstave preko x, y, z u CIE modelu. Da li moze obrnuto. Objasniti.
7. Koja struktura se koristi kod neravnomerno raspoređenih primitiva: (a) oktri (b) BSP (c)...
8. Navesti materijal iz stvarno života koji je difuzan/spekularan. Da li pozicija posmatrača utiče na difuzno/spekularno osvetljenje: da/ne.
9. Imamo neku teksturu pravougaonog oblika koja je parametrizovana sa u i v. Na koji način možemo da je preslikano na valjak sa visinom H i poluprečnikom R. Koje tehnike

Praktični:

1. 120 frejma, koliko treba da bude vreme izvršavanja petlje renderovanja. Koje su tehnike kad ide prebrzo.

2. Kako se zove element računara za protočnu obradu

3. Šta su uniformne promenljive

4. Neka rotacija u r3

5. Z-fighting

6. Face culling za kocku koliko se % otprilike gubi, objasni

7. Halfway vektor kako se računa 

8. Spekularno slika i izvod

9. Dopuniti šejder tako da se pretvore kordinate iz svetskih u kordinate kamere, gl_position=...


## Jan2 2023

1. Petlja renderovanja, oznaciti neblokirajuci deo.
2. Sejderi se prevode tokom: izvrsavanja ili prevodjenja glavnog programa.
3. Dopuniti sejdere tako da boje budu zbir interpolacije koordinata aPos.
4. Sta su mipmape i cemu sluze.
5. Koja transformacija u R3 ne moze da se predstavi kao mat 3x3.
6. Koje su opcije kada su teksture van opsega.
7. Skicirate koordinatne sisteme i napisati transformacije tako da pocetne koordinate dodju do lokalnih.
8. Dopuniti vertex shader tako da koord aPos budu upisane u clip prostor.
9. Kako se racuna difuzno, slika.
10. U kom sejderu se implementira Fongov model?
11. Koji faktor u formuli slabljenja ima najveci uticaj na daleke fragmente,
zasto?
1. Z-konflikt i kako se resava.
2. Kojim redosledom se renderuju objekti kada ima providnih.
3. Na osnovu cega se racuna orjentacija u openGL-u.
4. Koje tipove svetlost modeluje Blin-Fongov model?


## April 2023


Teorija:

1. Razlika izmedju ray casting i rasterizacije.
2. Ako je data slika od 512x512 piksela, da li njena veličina zavisi od poluprečnika kruga r u vektorskoj i rasterskoj grafici.
3. Nešto sa signalom i uzorkovanjem
4. Kako/Zasto? se koristi uzorkovanje trougla umesto rasterizacije.
5. Skaliranje u prostoru preko homogenih koordinata, koef po osama za 4, 2 i 1/3 za tačku 2, -1, 3 i inverz toga.
6. Da se zaokruži da li je translacija/skaliranje: afino, indirektno, direktno, linearno.
7. Neka projekcija za tačku, potrebno je matricu napisati.
8. Kada je perspektivno zakrivljene veliko pri perspektivnom projektovanju, više odg su ponuđeni, tačno: kada su uglovi vidnog polja veliki i mali.
9. Bar dve prednosti std paralelne u odnosu na std perspektivnu projekciju.
10. Kada se menja z koordinata u baferu u z algoritmu?
11. Kada je poligon sa prednje/zadnje strane posmatrača?
12. Odrediti baricentricne koordinate za tačku P, dati su neki odnosi.
13. Kako se vrsi dvostepeno nanošenje tekstura??
14. Sta je funkcija raspodele dvosmerne refleksije (BRDF) i kakva je kod spekularnog i difuznog materijala?
15. Po čemu se razlikuju fongovo i blin fongov model?
16. Koji korol model koristi oduzimanje boja, kako se boje dobijaju u odnosu na belu boju?
17. Zašto sa RGB modelom ne možemo da prikažemo sve boje?
18. Nešto o strukturama. Koje imaju poravnate granične opsege sa koordinatnim osama i koje imaju ćelije jednake veličine??
19. Šta je topologija, a šta geometrija mreže?
20. Da li se kod ketmul-rom splajna računa tangenti vektor u svakoj tački: da/ne <br>
    Kako se računa tangente vektor u svakoj tački?

