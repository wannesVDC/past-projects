# Project JavaScript

In dit practicum zullen we het spel [Zeeslag](https://nl.wikipedia.org/wiki/Zeeslag_(spel)) implementeren in Javascript.

# Praktische informatie

## Gedragscode

De practica worden gequoteerd, en het examenreglement is dan ook van toepassing.
Soms is er echter wat onduidelijkheid over wat toegestaan is en niet inzake samenwerking bij opdrachten zoals deze.

De oplossing en/of programmacode die ingediend wordt moet volledig het resultaat zijn van werk dat je zelf gepresteerd hebt.

Je mag je werk uiteraard bespreken met andere studenten, in de zin dat je praat over algemene oplossingsmethoden of algoritmen, maar de bespreking mag niet gaan over specifieke code of verslagtekst die je aan het schrijven bent, noch over specifieke resultaten die je wenst in te dienen.

Als je het met anderen over je practicum hebt, mag dit er dus *nooit* toe leiden, dat je op om het even welk moment in het bezit bent van een geheel of gedeeltelijke kopie van het opgeloste practicum of verslag van anderen, onafhankelijk van of die code of verslag nu op papier staat of in elektronische vorm beschikbaar is, en onafhankelijk van wie de code of het verslag geschreven heeft (mede-studenten, eventueel uit andere studiejaren, volledige buitenstaanders, internet-bronnen, e.d.).

Dit houdt tevens ook in dat er geen enkele geldige reden is om je code of verslag door te geven aan mede-studenten, noch om dit beschikbaar te stellen via publiek bereikbare directories of websites. De git-repository die we jullie ter beschikking stellen is standaard op privaat ingesteld.

Elke student is verantwoordelijk voor de code en het werk dat hij of zij indient.
Als tijdens de beoordeling van het practicum er twijfels zijn over het feit of het practicum zelf gemaakt is (bvb. gelijkaardige code, of oplossingen met andere practica), zal de student gevraagd worden hiervoor een verklaring te geven.
Indien dit de twijfels niet wegwerkt, zal er worden overgegaan tot het melden van een onregelmatigheid, zoals voorzien in het onderwijs- en examenreglement (zie http://www.kuleuven.be/onderwijs/oer/).

### Toelichting

Het is toegestaan om gebruik te maken van informatie en code (bijvoorbeeld hulpfuncties) op het internet om je practicum op te lossen, **indien je de bron vermeldt**.
Het is niet de bedoeling om een volledig opgelost spel te downloaden en dit in te dienen, zelfs met bronvermelding.

Indien je twijfelt over wat al dan niet toegestaan is, stel dan een vraag op het discussieforum!

Alle inzendingen worden automatisch met elkaar vergeleken met behulp van plagiaatsoftware voor code. Deze software geeft een score van gelijkaardigheid aan projecten. De projecten die het meest op elkaar lijken worden nadien manueel nagekeken.

## Deadline

De *deadline* voor dit practicum is **zondag 17 november 2019** om **23u59**.
Na deze datum kunnen jullie geen commits meer pushen naar jullie repository.

Indien je toch laattijdig moet indienen kunnen we je nog toegang geven tot de repository. Hierdoor verlies je wel punten.

## Verdediging

De verdedigingen van het practicum gaan door op 19 en 20 november. 
De praktische planning en lokaalverdeling volgt later op Toledo.

Op de verdediging krijgen jullie 1 uur tijd om enkele kleine uitbreidingen te maken op basis van jullie ingediende oplossing.
Hierbij mag internet gebruikt worden, *behalve* om te communiceren met medestudenten.
Nadien krijgen jullie tijd om de oplossing te tonen en uit te leggen aan een assistent.

De verdediging is een examenmoment, behandel dit ook zo.
**Zorg ervoor dat je op tijd aanwezig bent op je toegekende moment.**
Indien je om eender welke reden niet op tijd op de verdediging kan geraken, neem dan zo snel mogelijk contact op met het assistententeam zodat er gezocht kan worden naar een oplossing.
*Niet komen opdagen op je verdediging, zonder enige verwittiging, zal resulteren in een 0 op dit practicum*.
Indien je door ziekte je verdediging niet kan halen, bezorg je je ombudspersoon een doktersattest.
Via de ombudspersoon kan dan een inhaalverdediging vastgelegd worden.

## Opgave downloaden

Via GitHub Classroom hebben jullie een eigen private kopie gekregen van deze repository.

Deze repository kan je clonen met het commando:

``` bash
$ git clone https://www.github.com/url-naar-eigen-repo/project-javascript-zeeslag.git

```

De repository bevat reeds een basis-HTML bestand gekoppeld aan een leeg Javascript-bestand en een leeg CSS-bestand.
Vanuit deze bestanden kan je starten.
Het is echter ook toegestaan je eigen bestanden toe te voegen of de meegeleverde bestanden te verwijderen.

## Indienen

Het project wordt ingediend met behulp van *git*.

Zorg ervoor dat alle bestanden van je oplossing toegevoegd zijn aan je git repository en gepusht zijn naar de master branch voor de deadline.
**Alle repositories worden afgesloten na de deadline!**

Het is ook een goed idee om regelmatig gedurende het maken van je project je bestanden te committen en pushen met git.
Zo heb je altijd een back-up van je code.

``` bash

$ git add index.html
$ git add code.js
... <alle bestanden toevoegen met git add> ...
$ git commit -m "Oplossing practicum"
$ git push origin master
```

*Controleer* je inzending door je eigen repository opnieuw te clonen in een tijdelijke folder.

``` bash
$ cd /tmp
$ git clone https://www.github.com/url-naar-eigen-repo/project-javascript-zeeslag.git
$ cd project-javascript-zeeslag
$ firefox index.html

```

Indien alles correct is gepusht zouden bovenstaande commando's je oplossing moeten downloaden van GitHub
en vervolgens openen in Firefox.

Je kan de inhoud van de repository ook nakijken op de website van GitHub zelf.

**Kijk goed na of je alle bestanden correct hebt ingediend. Een foute inzending wordt gelijkgesteld aan laattijdige inzending!**

## Forum

Alle vragen over het practicum, *inclusief vragen aan medestudenten*, moeten gesteld worden via het **discussieforum** op Toledo.

Alle antwoorden van assistenten op het discussieforum worden beschouwd als **deel van de opgave** en kunnen bijgevolg aanvullingen of correcties bevatten. Zorg ervoor dat je deze posts leest!

Tip: open het forum en klik bovenaan op *subscribe* om een e-mail te krijgen wanneer nieuwe threads worden toegevoegd aan het forum.

## Evaluatiecriteria

In deze sectie beschrijven we kort enkele criteria die we gebruiken om een score toe te kennen aan het practicum.

### Functionaliteit

In de eerste plaats wordt er gekeken naar een correcte, foutloze werking van je programma.
Zorg ervoor dat alle functionaliteit die in de opgave gevraagd wordt aanwezig is ook in je programma.
Je zal enkel kunnen slagen voor dit practicum als je een werkend spel oplevert!

### Leesbaarheid code 

Zorg ervoor dat je code leesbaar is!

Enkele tips:

* Gebruik duidelijke, verklarende namen voor alle variabelen en functies
* Maak veel gebruik van functies om je code op te delen. Deel lange functies op in kleinere subfuncties die je probleem stap voor stap oplossen. Als je je code voldoende opsplitst in functies met heldere namen zal de code snel leesbaar worden.
* *Indenteer* je code! Vele editors hebben auto-formatting functionaliteit. Maak daar gebruik van! Er is niets vervelender dan code te lezen met foute of geen indentatie.
* Maak gebruik van commentaar, maar enkel voor de stukken code die slecht leesbaar zijn. Voeg deze commentaar dus pas toe nadat je alle bovenstaande stappen hebt uitgevoerd en nog steeds merkt dat het stuk code lastig te lezen is.
* Wees consequent met alle stijlkeuzes die je maakt. Indien je bijvoorbeeld beslist om een accolade telkens op een nieuwe regel te laten beginnen, doe dit dan voor het volledige document.

### Correcte inzending

Zorg ervoor dat je code correct en tijdig is ingediend via GitHub.

### Niet gequoteerd

We geven geen punten op

* Mooiheid van de user interface. Het kan leuk zijn om je spel mooier te maken, maar hier kan je geen punten mee winnen.
* Extra (ongevraagde) functionaliteit. Je mag eigen functies toevoegen, maar hier kan je geen punten mee winnen.

### Verdediging

Tijdens de verdediging zal je een uitbreiding moeten schrijven op je practicum. De criteria uit deze sectie zijn ook van toepassing op de code die je schrijft op de verdediging. Uiteraard wordt er rekening gehouden met het feit dat de nieuwe code in korte tijd geschreven is.

Daarnaast is het belangrijk dat je goed kan uitleggen wat je code doet, en waarom je bepaalde keuzes hebt gemaakt.
Wanneer we je vragen om een deel code uit te leggen is het niet voldoende om deze code gewoon voor te lezen.

# Opdracht


De opdracht van in practicum bestaat eruit Zeeslag te implementeren met behulp van HTML, CSS en Javascript.
In deze sectie beschrijven we eerst de algemene regels en werking van het spel.
Vervolgens beschrijven we de specifieke functionaliteit die we verwachten in jullie oplossing.


## Zeeslag

Om de regels van zeeslag te leren kennen kan je het spel online spelen op http://nl.battleship-game.org/.
We beschrijven de regels ook in deze sectie.

### Spelbord

Zeeslag wordt standaard gespeeld door twee spelers.
Elke speler heeft een eigen spelbord met afmeting 10x10.

De kolommen van het spelbord beschrijven we aan de hand van letters A tot J.
De rijen nummeren we van 1 tot 10.
Individuele vakjes kunnen we identificeren aan de hand van hun rijnummer en kolom (bijvoorbeeld vakje H3).

Beide spelers zien enkel hun eigen spelbord.
Het bord van de tegenspeler is verborgen.


### Opstelling

Aan het begin van het spel plaatsen beide spelers tien verschillende schepen op hun spelbord, op een positie naar keuze.
Een schip is telkens 1 vakje breed en kan 1 tot 6 vakjes lang zijn.
Schepen kunnen horizontaal of verticaal geplaatst worden.

Twee schepen mogen niet aan elkaar grenzen.

Wanneer beide spelers alle schepen geplaatst hebben start het spel.

### Verloop spel

Het doel van het spel is om de positie van de schepen van je tegenstander te raden.
Dit doe je door om de beurt een locatie te kiezen op het spelbord van de tegenspeler.
Indien er zich op die locatie een schip bevindt, is dat schip geraakt.
De getroffen speler geeft dan aan dat de gekozen locatie "raak" was.

Wanneer alle vakjes van een boot geraakt zijn, zinkt deze boot.
De getroffen speler geeft in dat geval aan dat de boot gezonken is.

De eerste speler die alle boten van de tegenspeler kan laten zinken wint het spel.

## Functionaliteit


In deze sectie beschrijven we de functionaliteit die we verwachten dat jullie implementeren voor dit practicum.

### Interface

* Voorzie een knop op een nieuw spel te starten.
* Toon een timer die aangeeft hoe lang het huidige spel reeds aan de gang is.
* Toon beide spelborden naast elkaar.
* De boten van speler A zijn altijd volledig zichtbaar.
* De boten van speler B (de computer) zijn verborgen, tenzij de boot volledig gezonken is.
* Elke "hit" en elke "miss" op het spelbord wordt visueel gemarkeerd. Deze markering blijft zichtbaar tot het einde van het spel.

### Bordopstelling

#### Start spel

* Bij het starten van een nieuw spel worden de boten van beide spelers willekeurig op het spelbord geplaatst.
* Elke boot heeft evenveel kans horizontaal als verticaal geplaatst te worden.
* Twee boten kunnen nooit aan elkaar grenzen.

#### Boten

De tien boten die op het spelbord geplaatst moeten worden hebben de volgende afmetingen:

* 2x1 (4 boten)
* 3x1 (3 boten)
* 4x1 (2 boten)
* 6x1 (1 boot)

### Verloop spel

Het spel wordt telkens gespeeld vanuit het perspectief van speler A. We spelen tegen de computer.

Bij de start van het spel is het de beurt aan speler A.

#### Beurt speler A

* Kies een vakje op het spelbord van speler B door erop te klikken.
* Het spel geeft nu visueel aan of deze keuze een hit of een miss was.
* Wanneer alle vakjes van een boot op het spelbord van speler B geraakt zijn, wordt deze boot zichtbaar.
* De beurt gaat naar speler B.

#### Beurt speler B

* De computer kiest een willekeurig vakje op het spelbord van speler A *dat nog niet eerder gekozen was*.
* Het spel geeft visueel aan of deze keuze een hit of een miss was.
* De beurt gaat naar speler A.

#### Einde spel

Een speler verliest wanneer al zijn boten gezonken zijn.


## Oplossingsstrategie

Om dit practicum op te lossen kan je het werk opsplitsen in verschillende stappen:

1. Maak eerst met behulp van HTML en CSS de volledige interface, op een specifiek moment in het spel.
Zorg ervoor dat je hierin alle grafische elementen (hit, miss, gezonken boot) al verwerkt.

2. Bedenk een representatie in JavaScript die de volledige state van het spel kan beschrijven. Denk bijvoorbeeld aan de oefenzitting, waarin we een tweedimensionale lijst gebruikten om een Sliding Puzzle voor te stellen. 

3. Schrijf een functie die de interne Javascript-representatie kan omzetten naar een HTML-representatie. Kijk bijvoorbeeld naar de functies *draw_puzzle* en *generate_puzzle_html* uit de oefenzitting.

4. Vervolgens kan je functies schrijven die gebruik maken van de interne voorstelling van je spel om het spel te spelen. Bij iedere aanpassing van de interne representatie kan je deze opnieuw omzetten naar HTML met behulp van je omzetfunctie. Je kan de functies telkens testen door deze uit te voeren vanuit de JavaScript console.

5. Maak gebruik van de onclick-attributen van HTML om ervoor te zorgen dat wanneer men op het spelbord klikt, de correcte JavaScript functies (geschreven in de vorige stap) uitgevoerd worden
