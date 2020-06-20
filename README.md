# AppProtect
App that protects user selected apps with a fake pop up "Application not responding"
<br><br>
Overview:<br>
Getting all user installed apps and system apps that are preloaded.
The Broadcast receiver registered for a PACKAGE_ADDED or PACKAGE_REMOVED, adding or deleting entries accordingly.
If a user locks an application, a service is started. The service purpose is to find a match between the foreground application and a locked app by the user, when a match is found a screen is shown, fooling the person that the app is not responding. User not knowing the the long press trick on the Title of the dialog will press OK which causes the foreground app to finish(by starting Home launcher in the background).
<br>
<br>
Screens:
<br>
<p align="left">
<img src="https://github.com/joysoi/AppProtect/blob/master/art/Screenshot_2016-12-11-21-19-19%20(1).png"
height="470" width="240"/>
<img src="https://github.com/joysoi/AppProtect/blob/master/art/Screenshot_2016-12-11-21-19-31.png" 
height="470" width="240"/>
<img src="https://github.com/joysoi/AppProtect/blob/master/art/Screenshot_2016-12-11-21-19-50%20(1).png" 
height="470" width="240"/>
</p>
