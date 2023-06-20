# Daniele_Ziaco_Android_Currency_Converter
Daniele Ziaco (Matricola 317310)
Un app per convertire valute fatta in android studio per l'esame di programmazione di sistemi mobili

# Struttura

I file .java sono presenti nel percorso [app/src/main/java/com/exam/mycurrency](app/src/main/java/com/exam/mycurrency)
I file di layout xml sono presenti nel percorso [app/src/main/res/layout](app/src/main/res/layout)
Tutte le immagini custom sono presenti nel percorso [app/src/main/res/drawable](app/src/main/res/drawable)
Ignorare la cartella (ScreensForReadMe)

Le due classi SavedCurrencies.java e SavedList.java definiscono rispettivamente le singole coppie di valute salvabili e la lista di coppie di valute salvate, SavedList estende RecyclerView.Adapter (e implementa i relativi metodi) per poter definire una "cella" personalizzata per recycleViews e poter mostrare le coppie di valute salvabili (l'xml per la cella è saved_currrencies_item.xml)


- MainActivity è il controller per la vista activity_main.xml
- HistoryActivity è il controller per la vista activity_history.xml
- Favorites è il controller per la vista favorites_activity.xml

L'api utilizzata per ricavare valute e tassi di cambio è freecurrencyapi.com

# ScreenShots
Schermata principale dove effettuare le conversioni fra valute, cliccando la stella a destra è possibile salvare fra i preferiti una coppia di valute, in caso quella coppia di valute dovesse essere già presente verrà mostrato un toast:

![](https://github.com/ErZicky/Daniele_Ziaco_Android_Currency_Converter/blob/master/ScreensForReadMe/screen1.jpeg)

toast per coppia duplicata:

![](https://github.com/ErZicky/Daniele_Ziaco_Android_Currency_Converter/blob/master/ScreensForReadMe/screen4.jpeg)

Schermata dove è possibile vedere il tasso di cambio fra due valute negli ultimi 7 giorni:

![](https://github.com/ErZicky/Daniele_Ziaco_Android_Currency_Converter/blob/master/ScreensForReadMe/screen2.jpeg)

Schermata dove è possibile visualizzare le coppie di valute salvate fra i preferiti, cliccando su una di esse si verrà riportati alla schermata principale e verranno pre selezionate le due valute in questione:

![](https://github.com/ErZicky/Daniele_Ziaco_Android_Currency_Converter/blob/master/ScreensForReadMe/screen3.jpeg)


