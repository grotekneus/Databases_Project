## kort overzicht
De organisatie bestaat uit een aantal museums. Ieder museum kan 3 type items bevatten: games, consoles en shop items. Shop items is alles wat geen game of console is, T-shirts en etc. Er kunnen meerdere instanties van deze items aanwezig zijn in één museum, maar deze instantie kan maar in één museum tegelijk zijn. Dit geeft een één op veel relatie tussen museum en item.  
Ieder van deze items kan verkocht worden, en deze verkopen moeten bijgehouden worden. Een verkoop wordt dus gelinkt aan één van deze drie item types en aan één klant. Deze klant heeft ook de mogelijkheid om games uit te lenen. Leningen worden ook in hun eigen tabel bijgehouden. 
Verder heeft de klant ook de mogelijkheid om geld te doneren aan de organisatie in ruil voor voordelen. Deze donaties worden bijgehouden en gelinkt aan de juiste klant.
In de komende hoofdstukken wordt er per tabel besproken welke eigenschappen ze bevatten, waarom ze deze bevatten. Verder wordt er per tabel besproken welke relaties ze hebben met andere tabellen en waarom deze relaties nodig zijn.

## Museums 
Ieder museum heeft vanzelfsprekend een adres en een land waar het ligt. In deze tabel wordt ook bijgehouden hoeveel bezoekers het museum heeft gehad en hoeveel winst het museum heeft gemaakt. Dit aantal kan bijvoorbeeld om het jaar gereset worden. Ieder museum heeft ook zijn eigen ID wat gebruikt wordt om het inventaris bij te houden. Dit maakt het makkelijk om een overzicht te krijgen van alle items die gelinkt zijn aan dit museum. Bezoekers en winst dient meer voor analytische doeleinde. 

## Console
Van iedere console wordt de naam, jaar van productie en waarde bijgehouden. Verder krijgt iedere instantie van de console ook een consoleID. Er wordt bijgehouden welke console in welk museum ligt door iedere console een museumID te geven.  Aangezien een product maar op één plaats tegelijk kan zijn, maar deze plaats meerdere producten kan bevatten, bestaat er een nul of meer op één relatie tussen consoles en museums. 

## Game
Iedere game heeft een naam, genre, productiejaar, waarde en eigen ID. Het museumID wordt opnieuw gebruikt om een overzicht te creëren van welke games in welke museums aanwezig zijn. Verder bevat iedere game ook een consoleID om de game te linken aan één of meerdere consoles, aangezien er altijd een mogelijkheid is dat een game op meerdere consoles beschikbaar is.

## Genre
Het is handig als er snel en efficiënt op genres gefilterd kan worden. Hiervoor is er een genre tabel aangemaakt waarin ieder genre zijn eigen ID krijgt, dit ID wordt vervolgens in de game tabel gebruikt om de games met de juiste genres te linken. Aangezien één game meerdere genres kan bevatten, en een genre nul of meer games tegelijk kan bevatten, bestaat er een nul of meer op één of meer relatie tussen game en genre. 
## Customers
Voor iedere klant van de organisatie wordt de naam, het adres en email bijgehouden. Iedere klant krijgt ook zijn/haar eigen customerID. Het gaat hier vooral over klanten die spellen lenen/kopen en of donaties geven aan de vereniging. Als regelmatige museumbezoekers voordelen zouden krijgen zou dit ook bijgehouden kunnen worden, maar dit lijkt niet het geval en zou het schema dus enkel ingewikkelder maken. 
In dit kader kan een klant enkel een game uitlenen, een donatie doen of een aankoop verrichten. Dit zijn dus ook de drie gebieden waar het customerID voor gebruikt zal worden.

## Loans 
In de loan tabel wordt de uitleendatum, het ID van de uitgeleende game en het ID van de klant die het uitleent bijgehouden. Iedere instantie van een game die wordt uitgeleend krijgt dus zijn eigen rij in de tabel.
Het zou voor analytische doeleinde nuttig kunnen zijn om bij te houden welke games het meeste uitgeleend worden. Dit model gaat er dus vanuit dat een instantie van een lening niet direct verwijderd wordt. Dit is de reden dat 1 lening aan nul of meer leningen verbonden kan zijn. Dit is ook de reden dat iedere lening een “returned” boolean heeft. 
In dit model wordt ervanuit gegaan dat enkel games uitgeleend kunnen worden. Indien consoles ook uitgeleend kunnen worden kan er simpelweg een veel op één relatie tussen loans en console gemaakt worden. 

## Donations
Klanten die doneren kunnen bepaalde voordelen krijgen. Hiervoor is het natuurlijk nodig dat wordt bijgehouden welke klant de donatie heeft gedaan en hoeveel er gedoneerd werd. Klanten die on de maand doneren kunnen nog extra voordelen krijgen, dus de datum van donaties is ook een belangrijke eigenschap. 

## Shop item
Shop items zijn alle items die niet in de museums tentoongesteld worden maar wel verkocht worden, zoals pluchies, T-shirts,… Om bij te houden of het om een kledingstuk of een tas of.. gaat wordt een itemType bijgehouden in de vorm van een enum. Ieder item krijgt, net zoals de games en consoles, een prijs, naam en itemID. 
Shop items worden niet tentoongesteld in de museums maar moeten nog altijd ergens worden opgeslaan. In deze structuur wordt verondersteld dat deze opslagplaats één van de museums is. Indien dit niet het geval is kan een extra tabel worden aangemaakt genaamd warehouse. Deze tabel krijgt zijn eigen locatie en warehouseID kolommen. Daarna wordt het museumID gewoon gewijzigd door warehouse ID. 

## Purchases
Purchases is een tabel waarin elke aankoop wordt bijgehouden. Iedere aankoop wordt door een klant verricht en het is voor juridische redenen handig als het bedrijf weet welke klant welke aankoop heeft verricht. Voor deze reden bevat iedere aankoop een custemorID. 
Er kunnen games, consoles of shop items gekocht worden, ieder heeft zijn eigen type ID. De enum ItemType wordt gebruikt om aan te geven wat voor soort item nu verkocht wordt. Eens dit vast staat kan het item terug gevonden worden met behulp van het itemID.   
Iedere aankoop krijgt ook zijn eigen ID, aangezien deze meestal bij aankoop worden meegegeven.
Een aankoop kan 0 of 1 game, console of shop item bevatten, en een game, console, of shop item kan aan 0 of 1 aankopen verbonden zijn. Dit is de reden dat alle relaties tussen aankopen en andere tabellen nul of meer op nul of meer relaties zijn.  

## Eventuele uitbreiding
Een eventuele uitbreiding zou zijn om ook een tabel met werknemers toe te voegen en deze te linken aan museums. Aan deze tabel kan ook een functie tabel verbonden worden die de verschillende functies bijhoud. 


