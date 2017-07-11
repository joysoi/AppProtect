# AppProtect
App that protects user selected apps with a fake pop up "Application not responding"

Getting all user installed apps and system apps that are preloaded.
The Broadcast receiver registered for a PACKAGE_ADDED or PACKAGE_REMOVED, adding or deleting entries accordingly.
If a user locks an application, a service is started. The service purpose is to find a match between the foreground application and a locked app by the user, when a match is found a screen is shown, fooling the person that the app is not responding. User not knowing the the long press trick on the Title of the dialog will press OK which causes the foreground app to finish(by starting Home launcher in the background)


->    Intent startMain = new Intent(Intent.ACTION_MAIN);
      startMain.addCategory(Intent.CATEGORY_HOME);
      startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      getContext().startActivity(startMain);


The activity has excludeFromRecents attribute set to true which allowes me to hide any fingerprints on the app not responding dialog, and not allowing the user to manually kill the app the service is able to find the top foreground app through GET_TASKS  permission or PACKAGE_USAGE_STATS for devices with api 21 and up the service is kept alive by a broadcast receiver which pings it on every 20 seconds. 

### Screenshots:
<img width="20%" src="https://github.com/joysoi/AppProtect/blob/master/art/Screenshot_2016-12-11-21-19-19%20(1).png" />
<img width="20%" src="https://github.com/joysoi/AppProtect/blob/master/art/Screenshot_2016-12-11-21-19-31.png" />
<img width="20%" src="https://github.com/joysoi/AppProtect/blob/master/art/Screenshot_2016-12-11-21-19-50%20(1).png" />
