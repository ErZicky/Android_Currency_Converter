# Android_Currency_Converter
A simple app made with Android Studio and Java for the mobile programming exam, where you can convert between currencies and save them for quicker selection

# Structure

the .java files are at this path: [app/src/main/java/com/exam/mycurrency](app/src/main/java/com/exam/mycurrency)
The xml files are at: [app/src/main/res/layout](app/src/main/res/layout)
All the custom images are at: [app/src/main/res/drawable](app/src/main/res/drawable)
Ignore the "ScreensForReadMe" folder

The two classes SavedCurrencies.java and SavedList.java respectively define the single saveable currency pairs and the list of saved currency pairs, SavedList extends RecyclerView.Adapter (and implements the related methods) to be able to define a custom "cell" for recycleViews and be able to show the currency pairs that can be saved (the xml for the cell is saved_currrencies_item.xml)


- MainActivity is the controller for the activity_main.xml view
- HistoryActivity is the controller for the activity_history.xml view
- Favorites is the controller for the favorites_activity.xml view

The api used to get currencies and exchange rates is freecurrencyapi.com

# ScreenShots
Main screen where to make conversions between currencies, by clicking on the star on the right it is possible to save a currency pair among your favourites, if that currency pair is already present a toast will be shown:

![](https://github.com/ErZicky/Daniele_Ziaco_Android_Currency_Converter/blob/master/ScreensForReadMe/screen1.jpeg)

toast for duplicate pair:

![](https://github.com/ErZicky/Daniele_Ziaco_Android_Currency_Converter/blob/master/ScreensForReadMe/screen4.jpeg)

Screen where you can see the exchange rate between two currencies in the last 7 days:

![](https://github.com/ErZicky/Daniele_Ziaco_Android_Currency_Converter/blob/master/ScreensForReadMe/screen2.jpeg)

Screen where it is possible to view the currency pairs saved in your favourites, by clicking on one of them you will be taken back to the main screen and the two currencies in question will be pre-selected:

![](https://github.com/ErZicky/Daniele_Ziaco_Android_Currency_Converter/blob/master/ScreensForReadMe/screen3.jpeg)


