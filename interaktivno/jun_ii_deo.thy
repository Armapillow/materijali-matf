theory jun1_rok
  imports Main
begin

fun presek :: "nat list \<Rightarrow> nat list \<Rightarrow> nat list" where
  "presek [] ys = []"
| "presek xs [] = []"
| "presek (x # xs) (y # ys) =
      ( if x = y then x # (presek xs ys)
        else if x < y
        then presek xs (y # ys)
        else presek (x#xs) ys)"

value "presek [1,2,3,5,7,9] [3,5, 7, 11, 13]"
value "presek [1, 2, 3, 4, 5] [2::nat, 4, 6]"

lemma presek_sort:
  assumes "sorted xs" "sorted ys"
  shows "set (presek xs ys) = set xs \<inter> set ys"
  using assms
  apply (induction xs ys rule: presek.induct)
    apply auto
  done

lemma presek_svi:
  assumes "sorted xs" "sorted ys"
  shows "sorted (presek xs ys)"
  using assms
  apply (induction xs ys rule: presek.induct)
    apply (auto simp add: presek_sort)
  done




(* 2 *)
primrec duzina :: "nat list \<Rightarrow> nat" where
  "duzina [] = 0"
| "duzina (x # xs) = 1 + duzina xs"

value "duzina [1,2,3]"

lemma duzina_length:
  shows "duzina ls = length ls"
  by (induction ls) auto

primrec k_stepen :: "nat \<Rightarrow> nat list \<Rightarrow> nat" where
  "k_stepen k [] = 0"
| "k_stepen k (x # xs) = x^k + (k_stepen k xs)"

value "k_stepen 2 [1, 2, 3, 4, 5]"

value "sum_list (map (\<lambda> x::nat. x^2) [1, 2, 3, 4, 5])"
definition k_stepen' :: "nat \<Rightarrow> nat list \<Rightarrow> nat" where
  "k_stepen' k xs = sum_list (map (\<lambda> x. x^k) xs)"
value "k_stepen' 2 [1, 2, 3, 4, 5]"

lemma k_stepen:
  shows "k_stepen k xs = sum_list (map (\<lambda> x. x^k) xs)"
  by (induction xs) auto

term "(/)"
value "(100 div 5::nat)"
value "101 mod 10::nat"

(* 
fun cifre :: "nat \<Rightarrow> nat list" where
  "cifre x = (if x \<le> 9 then [x] else x mod 10 # cifre (x div 10))"

fun cifre :: "nat \<Rightarrow> nat list" where
  "cifre n = (n mod 10) # (cifre (n div 10))"

primrec cifre' :: "nat \<Rightarrow> nat list" where
  "cifre' 0 = []"
| "cifre' n = (n mod 10) # (cifre (n div 10))"
 *)

fun cifre :: "nat \<Rightarrow> nat list" where
  "cifre 0 = []"
| "cifre n = (n mod 10) # (cifre (n div 10))"

value "cifre 1234"
value "cifre 1"

value "length (cifre 12345)"

definition armstrongov :: "nat \<Rightarrow> bool" where
  "armstrongov num \<longleftrightarrow> (let cif = cifre num; n = duzina cif
    in num = k_stepen n cif)"

function armstrongovi :: "nat \<Rightarrow> nat \<Rightarrow> nat list" where
  "armstrongovi a b = (
        if a > b then []
        else if (armstrongov a) then a # (armstrongovi (a+1) b)
        else (armstrongovi (a+1) b))"
 by pat_completeness auto
termination by (relation "measure (\<lambda> (a, b). b + 1 - a)")
auto

value "armstrongovi 1 1000"


value "filter armstrongov [1..<1000]"

lemma "armstrongovi a b = filter armstrongov [a..<Suc b]"
  apply (induction a b rule: armstrongovi.induct)
by (metis Suc_eq_plus1 armstrongovi.elims filter.simps(1) filter.simps(2) not_less_eq upt_rec) 


definition par1 :: "(nat \<times> nat) list" where
"par1 = [(1,10), (2,20), (3,30)]"

term "fst (1::nat, 8)"

primrec izdvoji :: "(nat \<times> nat) list \<Rightarrow> (nat list) \<times> (nat list)" where
  "izdvoji [] = ([], [])"
| "izdvoji (x # xs) = (let (a, b) = x; (al, bl) = izdvoji xs in (a # al, b # bl))"

value "izdvoji par1"
value "(map fst par1, map snd par1)"

lemma "izdvoji par = (map fst par, map snd par)"
  by (induction par) auto

fun sastavi :: "nat list \<Rightarrow> nat list \<Rightarrow> (nat \<times> nat) list" where
  "sastavi [] [] = []"
| "sastavi xs [] = []"
| "sastavi [] ys = []"
| "sastavi (x # xs) (y # ys) = (x, y) # sastavi xs ys"


value "sastavi [1::nat, 2, 3] [10::nat, 20, 30]"
value "sastavi [1::nat, 2, 3] [10::nat, 20, 30, 40, 50]"

value "zip [1::nat, 2, 3] [10, 20, 30::nat]"
value "zip [1::nat, 2, 3] [10::nat, 20, 30, 40, 50]"

lemma "sastavi xs ys = zip xs ys"
  apply (induction xs ys rule: sastavi.induct)
     apply auto
  done

lemma proba: "sastavi xs ys = zip xs ys"
  by (induction xs ys rule: sastavi.induct) auto





value "cifre 12345"

primrec vrednost' :: "nat list \<Rightarrow> nat \<Rightarrow> nat \<Rightarrow> nat" where
  "vrednost' [] s m = s"
| "vrednost' (x # xs) s m = vrednost' xs (s + m*x) (10*m)"


value "vrednost' [5, 4, 3, 2, 1] 0 1"

definition vrednost :: "nat list \<Rightarrow> nat" where
  "vrednost ls = vrednost' ls 0 1"

value "vrednost (cifre 8581)"

lemma "vrednost (cifre x) = x"
  unfolding vrednost_def
  sorry






(* 5 *)
datatype Izraz = Const nat
  | Plus Izraz Izraz
  | Minus Izraz Izraz
  | Puta Izraz Izraz

term "Plus (Const 5) (Const 6)"
term "Const 7"

fun vrednostIz :: "Izraz \<Rightarrow> nat" where
  "vrednostIz (Const n) = n"
| "vrednostIz (Plus i1 i2) = vrednostIz i1 + vrednostIz i2"
| "vrednostIz (Minus i1 i2) = vrednostIz i1 - vrednostIz i2"
| "vrednostIz (Puta i1 i2) = vrednostIz i1 * vrednostIz i2"

value "vrednostIz (Plus (Const 6) (Const 9))"

definition x1 :: "Izraz" where
  "x1 = Plus (Const 3) (Const 5)"

value "vrednostIz x1"

datatype Operacija = OpPlus
  | OpMinus
  | OpPuta
  | OpPush nat

type_synonym Stek = "nat list"

fun izvrsiOp :: "Operacija \<Rightarrow> Stek \<Rightarrow> Stek" where
  "izvrsiOp (OpPush n) st = (n # st)"
| "izvrsiOp OpMinus (x # y # xs) = (x-y) # xs"
| "izvrsiOp OpPuta (x # y # xs) = (x*y) # xs"
| "izvrsiOp OpPlus (x # y # xs) = (x+y) # xs"

type_synonym Program = "Operacija list"

primrec prevedi :: "Izraz \<Rightarrow> Program" where
  "prevedi (Const n) = [OpPush n]"
| "prevedi (Plus i1 i2) = OpPlus # (prevedi i1) @ (prevedi i2)"
| "prevedi (Minus i1 i2) = OpMinus # (prevedi i1) @ (prevedi i2)"
| "prevedi (Puta i1 i2) = OpPuta # (prevedi i1) @ (prevedi i2)"

value "prevedi x1"

primrec izvrsiProgram :: "Program \<Rightarrow> Stek \<Rightarrow> Stek" where
  "izvrsiProgram [] st = st"
| "izvrsiProgram (op # os) st = izvrsiOp op (izvrsiProgram os st)"

value "izvrsiProgram (prevedi x1) []"


definition racunar :: "Izraz \<Rightarrow> Stek" where
  "racunar iz = izvrsiProgram (prevedi iz) []"

value "hd (racunar x1)"
term "hd (racunar x1)"
term "vrednostIz"

definition x0 :: "Izraz" where
  "x0 = Puta (Const 0) (Const 1)"

value "vrednostIz x0"

lemma [simp]: "izvrsiProgram (p1 @ p2) s = izvrsiProgram p1 (izvrsiProgram p2 s)"
  by (induction p1) auto

lemma [simp]: "izvrsiProgram (prevedi i) s = vrednostIz i # s"
  by (induction i arbitrary: s) auto


lemma "hd (racunar iz) = vrednostIz iz"
  unfolding racunar_def
  apply (induction iz rule: vrednostIz.induct)
     apply auto
  done

end
