# WalkApp
The app is used to measure distance while walking and at the same time measures the number of steps the users makes.
When the app is installed and the user presses "Start" button for the first time, the user is asked to enter her/his weight which is consequently used to calculate the amount of calories burnt during the walk. After that, the user has to press "Start" again and the measurement begins. This only happens the very first time since the user's weight is stored in SharedPreferences.
At this point a background service is started which fetches data from GPS and the text of the button changes to "Stop".
The background service writes the GPS coordinates every ten metres line by line in a .txt file locally.
The first line of every measurement is a UUID which is used to parse the data later when getting a specific measurement.
When the "Stop" button is clicked the background service is shut down and the calculated data of distance, steps and calories together with the date, starting point and stopping point coordinates are written in a SQLite database.
When "History" button is clicked, data from every measurement is displeyed in a ScrollView in table layout.
When a specific row in a table is clicked, a map is shown whith the walking route and start and stop markers. There is also a "Share" button on the map which can be used to share the data on Facebook. 
The "Delete History" button is used to delete the table from the database together with the .txt file which contains the
coordinates. 
Because the measurement is running in a background service, the screen of the device can be turned of while using the app.
The "Modify" button is used to modify the user's weight if necessary since this is used to calculate the calories.
When "Get Weather" button is pressed the user is asked to enter her/his location. This is used to fetch the weather forecast for the location specified for the next 15 hours in 3 hour intervals. To get the weather data the app uses "openweathermap" API.
The app was successfully tested on a "Samsung S5 neo" device running Android Lollipop. 

