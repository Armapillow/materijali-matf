theory jul_2_deo
  imports Main
begin


(* 1 *)                          
(* Napisati funkciju razlika koja od dva skupa (predstavljena listama prirodnih brojeva) 
   formira njihovu razliku. Zahteva se da su obe liste sortirane, mogu da imaju 
   ponavljanja. 
   Rezultat treba da bude sortirana lista prirodnih brojeva bez ponavljanja. *)

fun razlika :: "nat list \<Rightarrow> nat list \<Rightarrow> nat list" where
  "razlika [] ys = []"
| "razlika xs [] = xs"
| "razlika (x # xs) (y # ys) = (
    if x = y then
       razlika xs (y # ys)
    else if x < y then
      x # razlika xs (y # ys)
    else
       razlika (x # xs)  ys
)"

value "razlika [1,3,3,5,7,9] [3,5]"
value "razlika [0, 0] [0]"

value "razlika [1, 0] [0]"

value "set [1, 3, 3, 5, 7, 9::nat] - set [3, 5]"

(* dokazati da se rezultujuca lista dobro slaze sa ugradjenim funkcijama, da se u 
   njoj nalaze tacno oni elementi koji treba da se nalaze *)


lemma [simp]:
  assumes "sorted xs" "sorted ys"
  shows "set (razlika xs ys) = set xs - set ys"
  using assms
  apply (induction xs ys rule: razlika.induct)
    apply auto
  done


(* dokazati da je rezultujuca lista sortirana *)
lemma [simp]:
  assumes "sorted xs" "sorted ys"
  shows "sorted (razlika xs ys)"
  using assms
  apply (induction xs ys rule: razlika.induct)
    apply auto
  done




(* 2 *)
(* definisati rekurzivnu funkciju koja određuje broj pojavljivanja datog elementa u listi *)
primrec prebroj :: "nat list \<Rightarrow> nat \<Rightarrow> nat" where
  "prebroj [] n = 0"
| "prebroj (x # xs) n = (if x = n then 1 + prebroj xs n else prebroj xs n)"


value "prebroj [1, 3, 2, 5, 8, 1, 5, 2, 5] 5"
value "prebroj [5,2,3,1,10] 3" (* 1 *)
value "prebroj [5,2,3,1,10,3] 3" (* 2 *)

(* izraziti broj pojavljivanja pomocu biblioteckih funkcija i dokazati ekvivalentnost sa 
   prethodnom definicijom *)
value "length (filter (\<lambda> x. x = 3) [5,2,3,1,10,3::nat])"
lemma [simp]: "prebroj xs n = length (filter (\<lambda> x. x = n) xs)"
  by (induction xs) auto

(* napisati funkciju izbaci koja izbacuje sva pojavljivanja elementa iz liste. 
   Lista moze imati ponavljanje u sebi *)
primrec izbaci :: "nat list \<Rightarrow> nat \<Rightarrow> nat list" where
  "izbaci [] n = []"
| "izbaci (x # xs) n = (if x = n then izbaci xs n else x # izbaci xs n)"

value "izbaci [1, 3, 2, 5, 8, 1, 5, 2, 5] 5"
value "izbaci [2,3,1,5,3,6] 3"

(* dokazati da je suma elemenata pocetne liste za n*k veca od sume elemenata 
   rezultujuce liste, gde je n broj pojavljivanja elementa k *)
value "sum_list [2::nat, 3, 1, 5, 3, 6]"
value "((prebroj [2, 3, 1::nat, 5, 3, 6] 3)*3) + sum_list (izbaci [2,3,1,5,3,6] 3)"
lemma "sum_list xs = (prebroj xs n)*n + sum_list (izbaci xs n)"
  by (induction xs) auto

(* napisati funkciju koja proverava da li se element nalazi u listi *)
primrec provera :: "nat list \<Rightarrow> nat \<Rightarrow> bool" where
  "provera [] n = False"
| "provera (x # xs) n = (if x = n then True else provera xs n)"

value "provera [2,3,1,5,3,6] 3"

(* pokazati da se funkcije nalazi i broj_pojavljivanja dobro slazu 
   (kroz naredne dve leme): *)

(* pokazati da se element nalazi u listi akko mu je broj pojavljivanja pozitivan *)
lemma nalazi_se: "provera xs n \<longleftrightarrow> (prebroj xs n) > 0"
  by (induction xs) auto

(* pokazati, koriscenjem samo prethodne leme, da se element ne nalazi u listi 
   akko mu je broj pojavljivanja jednak nula *)

lemma ne_nalazi_se:
  shows "provera xs n = False \<longleftrightarrow> (prebroj xs n) = 0"
  using nalazi_se
  by auto
  (* by (induction xs) (auto simp add: nalazi_se) *)


(* -------------------------------------------- *)
(* definisati primitivno rekurzivnu funkciju koja odredjuje faktorijel broja *)

primrec fact :: "nat \<Rightarrow> nat" where
  "fact 0 = 1"
| "fact (Suc n) = (Suc n) * fact n"

value "fact 5"

(* definisati proizvod elemenata liste preko funkcije fold *)
term "fold"
value "fold (*) [1..5] 1"
definition proiz :: "nat list \<Rightarrow> nat" where
  "proiz xs = fold (*) xs 1"

value "proiz [1, 2, 3, 4, 5]"

(* dokazati da je faktorijel jednak proizvodu elemenata liste [1, 2, ..., n] *)
lemma [simp]: "fact n = proiz [1..<Suc n]"
  unfolding proiz_def
  by (induction n) auto

(* definisati funkciju koja racuna dupli faktorijal broja: 
   n*(n-2)*(n-4)*...  *)
primrec dupli_fact :: "nat \<Rightarrow> nat" where
  "dupli_fact 0 = 1"
| "dupli_fact (Suc n) = undefined"

fun dupli :: "nat \<Rightarrow> nat" where
  "dupli 0 = 1"
| "dupli (Suc 0) = 1"
| "dupli (Suc (Suc n)) = (Suc (Suc n)) * dupli n"

value "dupli 7"
value "dupli 6"

(* napisati funkciju koja od broja n konstruise listu duplih faktorijala za sve brojeve od 1 do n *)
value "map (\<lambda> x. dupli x) [1..<Suc 7]"

definition svi_dupli :: "nat \<Rightarrow> nat list" where
  "svi_dupli n = map (\<lambda> x. dupli x) [1..<Suc n]"

value "svi_dupli 7"


(* 
   izraziti proizvod neparnih brojeva od 1 do n preko biblioteckih funkcija, i 
   pokazati da je za neparne brojeve vrednost funkcije double_fact jednaka proizvodu
   neparnih brojeva od 1 do n
*)
term "(div)"
value "filter (\<lambda> x. ((mod) x 2) \<noteq> 0) [1..5]"
value "sum_list (filter (\<lambda> x. (mod) x 2 \<noteq> 0) [1..5])"

value "fold (*) (filter odd [1..5]) 1"
value "filter odd [1..5]"

lemma [simp]: 
  assumes "odd n"
  shows "dupli n = fold (*) (filter odd [1..<n+1]) 1"
  using assms
  apply (induction n rule: dupli.induct)
    apply auto
  done



(* izraziti proizvod parnih brojeva od 1 do n preko biblioteckih funkcija, i 
   pokazati da je za parne brojeve vrednost funkcije double_fact jednaka proizvodu
   parnih brojeva od 1 do n *)

value "filter even [1..10]"
value "fold (*) (filter even [1..10]) 1"
lemma [simp]:
  assumes "even n"
  shows "fold (*) (filter even [1..<Suc n]) 1 = dupli n"
  using assms
  apply (induction n rule: dupli.induct)
    apply auto
  done


(*-----------------------------------------------------------------------------*)
(* 4 *)
(* Definisati strukturu Drvo:
   Drvo je ili prazno (što obeležavamo sa Null) ili sadrži vrednost (prirodan broj)
   i levo i desno poddrvo (što obeležavamo sa Cvor l v d) *)
datatype drvo = Null
  | Cvor drvo nat drvo

(* Definisati funkciju ubaci za ubacivanje elementa u sortirano pretrazivacko drvo, 
   bez ponavljanja *)
primrec ubaci :: "drvo \<Rightarrow> nat \<Rightarrow> drvo" where
  "ubaci Null n = Cvor Null n Null"
| "ubaci (Cvor ld v dd) n = (
    if (n < v) then
        Cvor (ubaci ld n) v dd
    else
        Cvor ld v (ubaci dd n)
)"

definition primer :: "drvo" where
  "primer = Cvor (Cvor Null 4 Null) 5 (Cvor Null 8 Null)"

value "primer"
value "ubaci primer 3"
value "ubaci primer 6"
value "ubaci primer 7"

(* Napisati funkciju koja odredjuje skup svih elemenata u drvetu *)
primrec skup :: "drvo \<Rightarrow> nat set" where
  "skup Null = {}"
| "skup (Cvor ld v dd) = {v} \<union> (skup ld) \<union> (skup dd)"

value "skup primer"

(* Dokazati da se ubacivanjem elementa u drvo, skup elemenata prosiruje ubacenim elementom. *)
lemma [simp]: "skup (ubaci tree x) = {x} \<union> (skup tree)"
  by (induction tree) auto




(* ----------------------------------------------------------- *)

datatype prirodni = Nula | Sledbenik prirodni

term "Sledbenik (Sledbenik Nula)"

primrec prevedi :: "prirodni \<Rightarrow> nat" where
  "prevedi Nula = 0"
| "prevedi (Sledbenik n) = 1 + prevedi n"

definition x4 :: "prirodni" where
  "x4 = Sledbenik (Sledbenik (Sledbenik (Sledbenik Nula)))"

definition x3 :: "prirodni" where
  "x3 = Sledbenik (Sledbenik (Sledbenik Nula))"

value "prevedi x4"
value "prevedi x3"

definition saberi' :: "prirodni \<Rightarrow> prirodni \<Rightarrow> nat" where
  "saberi' a b = (prevedi a) + (prevedi b)"

primrec saberi :: "prirodni \<Rightarrow> prirodni \<Rightarrow> prirodni" where
  "saberi Nula n = n"
| "saberi (Sledbenik m) n = Sledbenik (saberi m n)"

value "saberi x3 x4"
value "prevedi (saberi x3 x4)"

lemma saberi_prevedi:
  shows "prevedi (saberi x1 x2) = prevedi x1 + prevedi x2"
  by (induction x1) auto


primrec mnozi :: "prirodni \<Rightarrow> prirodni \<Rightarrow> prirodni" where
  "mnozi Nula m = Nula"
| "mnozi (Sledbenik n) m = saberi (mnozi n m) (m)"

value "mnzoi x3 x3"
value "prevedi (mnozi x3 x3)"
value "prevedi (mnozi x4 x4)"

lemma "prevedi (mnozi x1 x2) = prevedi x1 * prevedi x2"
  by (induction x1 arbitrary: x2) (auto simp add: saberi_prevedi)


end