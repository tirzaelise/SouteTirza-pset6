- Headers mogen uitgebreider
- Duidelijke variable namen, waardoor comments overbodig zijn en dus ook goed niet gebruikt worden
- Regel 86 SavedActivity.java enter te veel ;o
- getSavedEvents in SavedActivity hoort eigenlijk bij de class die de database dingen regelt

Je kan deze ook toevoegen aan de database class door de adapter mee te geven en dan adapter.notifyDataSetChanged() aan te roepen

App zelf
- Share werkt niet in searchresults: closed de activity
- App is langzaam
- Als je uitlogt, gaat hij even naar het login scherm, maar dan gelijk door naar de search
- Het  is logischer als je na het inloggen naar de search page gaat ipv op de login pagina te blijven
- Hij geeft niet aan als je saved events leeg is, omdat het laden lang duurt weet je dus niet of hij leeg is, of dat hij nog bezig is
- Mooi design

Names: 4
Headers: 2
Comments: 4
Layout: 4
Formatting: 4
Flow: 3
Idiom: 4
Expressions: 4
Decomposition: 4
Modularization: 4
