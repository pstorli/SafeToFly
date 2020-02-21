Name:    Safe To Fly\\

What:    Shows weather conditions to ensure a safe drone flight.\\

Author:  Pete Storli\
Created: Feb 14, 2020\
Email:   pstorli@gmail.com\
Phone:   971-888-2534\
Address: 18529 Chemawa Lne NE, Silverton, OR 97381\\

Design:  https://sites.google.com/site/storlidesignsllc/home/safetofly\
Resume:  https://sites.google.com/site/pstorli/resume\\

Issues Resolved in version 1000:\

  0000 Created initial project architecture diagram\
  0000 Created project and classes from  architecture diagram in AndroidStudio\
  0000 Added LiveData bindings between SafeToFlyFragment and SafeToFlyViewModel\
  0000 Added snackbar to highlight fact that pressing on plane in actionbar causes data to refresh.\
  0000 Added View and Data binding to intercat with ui widgets in the safe_to_fly_fragment.xml\
       https://developer.android.com/topic/libraries/view-binding\
  0000 Finished layout and colored icons for each category status\
  0000 Added code to determine gps location.\
  0000 Added DarkSky key to local properties using BuildConfig, for safety.\
  0000 Determined City, State and Zip from lat/lon using android.location.Geocoder\
  0000 Added support for landscape mode\
       added new safe_to_fly_fragment.xml to layout-land and new dimens.xml in values-land\
  0000 Changed to request location permissions\
  0000 Added showMessage method to main activity, in addition to showSnackBar\
  0000 Added call to showMessage to explain that user needs to press plane to refresh data.\
   
Known Issues:\

  0000 Check for correctness Cloud Ceiling formula: (temp - dew point) / 4.4 = feet above sea level\
  0000 Have app display map of current GPS location as the background.\
  0000 Add an AboutFragment and have it use new Google Screen Navigation system.\
  0000 Added android:screenOrientation="portrait" until landscape screen is created and tested.\
  0000 In SafeToFlyViewModel.update, instead of picking item 0, go through list and find correct time for dailyForcast.\
  0000 In SafeToFlyViewModel.update, we are using Date with no time zone. Fix to use LocalDateTime\
  0000 Remove jankapotamus.darkskyandroidwrapper and add in my own RDS and retrofit classes to get dark sky data.\
  0000 Dark Sky docs state that they return wind gusts for current conditions, but not there in jankapotamus.darkskyandroidwrapper\
  0000 jankapotamus.darkskyandroidwrapper caused several warnings to appear\
  0000 MainActivity.onRequestPermissionsResult getting expecting-member-declaration error?\
  0000 Need to add junit and expresso tests\