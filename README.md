## MobileNetworksTracker
Application collects information about mobile network signal strengths and type (GSM UMTS LTE), displays this information to user in human-readable format and reports data to server. 

## What is this?
The app is a part of solid service which is intended to provide real network coverage information to subscribers giving ability to compare coverage of different mobile operators.

## Functionality
- Displays network coverage level & data: Signal strengths, Network type, Location area, Cell ID, Network country
- Displays measurement results on map in realtime
- Logs all measurements to database
- Tracking in background with notification to user
- Ability to explore and manage logged tracks on device 
- Exports logged tracks to webserver
- Gets coordinates boths from GPS and Wifi so indoor positioning and measurements is quite correct

## Screenshots
 <body>
  <table cellspacing="0" class="layout">
   <tr> 
    <td><img src="https://github.com/droidcrib/MobileNetworksTracker/blob/master/data.png?raw=true" alt=""></td>
    <td ><img src="https://github.com/droidcrib/MobileNetworksTracker/blob/master/list.png?raw=true" alt=""></td>
    <td ><img src="https://github.com/droidcrib/MobileNetworksTracker/blob/master/list_context_menu.png?raw=true" alt=""></td>
   </tr>
      <tr> 
    <td ><img src="https://raw.githubusercontent.com/droidcrib/MobileNetworksTracker/master/trace_status.png" alt=""></td>
    <td ><img src="https://github.com/droidcrib/MobileNetworksTracker/blob/master/track_log_1.png?raw=true" alt=""></td>
    <td ><img src="https://github.com/droidcrib/MobileNetworksTracker/blob/master/track_log_3.png?raw=true" alt=""></td>
   </tr>
  </table>



## Supported devices
Supports every device with an SDK level of at least 15 (Android 4.1 and higher).

## Updates & Bugfix
To be done till release V 1.0
- add settings
- update icons and add description fields for LAC, CI, country
- remove test buttons from IU
- customize series list items
- add legend to map view
- get updates from PhoneStateListenel via Interface
- lockscreen tutrial to be added

## About
My name is Andrey Bulanov and I am a software developer with an interest in mobile development (Android platform currently).
