Name:    Safe To Fly

What:    Shows weather conditions to ensure a safe drone flight.

Author:  Pete Storli
Created: Feb 14, 2020
Email:   pstorli@gmail.com
Phone:   971-888-2534
Address: 18529 Chemawa Lne NE, Silverton, OR 97381

Design:  https://sites.google.com/site/storlidesignsllc/home/safetofly
Resume:  https://sites.google.com/site/pstorli/resume

DarkSky: 7386af409fd3f453040de57912e0cccb

Issues Resolved in version 1000: 
  0000 Created initial project architecture diagram
  0000 Created project and classes from  architecture diagram in AndroidStudio
  0000 Added LiveData bindings between SafeToFlyFragment and SafeToFlyViewModel
  0000 Added snackbar to highlight fact that pressing on plane in actionbar causes data to refresh.
  0000 Added View and Data binding to intercat with ui widgets in the safe_to_fly_fragment.xml
       https://developer.android.com/topic/libraries/view-binding
  0000 Finished layout and colored icons for each category status
  0000 Added code to determine gps location.
   
Known Issues:
  0000 Have app display map of current GPS location as the background.
  0000 Add an AboutFragment and have it use new Google Screen Navigation system.
  0000 Added android:screenOrientation="portrait" until landscape screen is created and tested.