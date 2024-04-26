# TP Info805 - Compilation
###### Andrien MONTMAYEUR - Emilien BOITOUZET

Le but de ce TP était de générer du code assembleur à partir d'un arbre abstrait, puis d'exécuter le code généré.

### Utilisation
Pour exécuter le code, commencez par exécuter la commande `./gradlew build`. Cela génére les fichiers CUP et JFLEX. Ensuite, il faut exécuter le fichier main.java pour commencer à écrire le code à compiler. Pour finir le snippet de code, tapez un point et appuyez sur la touche `Entrée`. Cela générera l'arbre abstrait correspondant au code entré.

En voici un exemple : 

```
let prixHt = 200;
let prixTtc = prixHt * 119 / 100 .
```
donne l'arbre
```
        prixHt
    LET
        200
PV
            prixHt
        LET
            200
    PV
            prixTtc
        LET
                    prixHt
                *
                    119
            /
                100
```
Pour générer le code assembleur correspondant, il faut appuyer sur les touches `Ctrl + D`. On obtient alors le code suivant
```
DATA SEGMENT
	prixHt DD
	prixTtc DD
DATA ENDS
CODE SEGMENT
	mov eax, 200
	mov prixHt, eax
	mov eax, 200
	mov prixHt, eax
	mov eax, prixHt
	push eax
	mov eax, 119
	pop ebx
	mul eax, ebx
	push eax
	mov eax, 100
	pop ebx
	div ebx, eax
	mov eax, ebx
	mov prixTtc, eax
CODE ENDS
```
Il ne reste plus qu'à récupérer le code généré et le coller dans le fichier `pgcd.asm`.
On tape la commande `java -jar vm-0.9.jar pgcd.asm` et le fichier assembleur est exécuté.
Pour aller plus loin, nous avons rajouté le traitement d'autres opérateurs tels que le while, le if then else, le moins unaire, les différents opérateurs de comparaison et les opérateurs arithmétiques basiques.

### Exemples d'utilisation

```
let prixHt = 200;
let prixTtc = prixHt * 119 / 100;
output prixTtc .
```
Ce bout de code calcule le prix toutes taxes comprises d'un prix de base de 200 et l'affiche.

```
let a = input;
let b = input;
while (0 < b)
do (let aux=(a mod b); let a=b; let b=aux);
output a .
```
Ce bout de code permet de calculer le PGCD de deux nombres.

```
let a = input;
let b = input;
if (a > b)
then (output a)
else (output b)
endif .
```
Ici, on peut déterminer le plus grand de deux nombres.