# SocialAuthentication

Integarting Google Sign-in in Android Application:

Configuring App for Google Sign-in :

1. Click the link https://developers.google.com/identity/sign-in/android/start-integrating 
2. You will find "COnfigure a project" Action click on it.
3. In the prompt select "Create a new project" and give your project name then click Next
4. A prompt appears for collecting name which will be displayed for user while connecting , give a name and click Next.
5. Another prompt for the platform you wish to integrate, choose Android and click Create.
6. You will be asked to enter the package name give for your android and SHA 1 Signing Certificate.
6.1 Paste the Package name from the Manifest File
6.2 To get the SHA-1 Follow the steps,
6.2.1 In Android Studio, Right corner you will see a tab name gradle,click on it 
6.2.2 Expand the GoogleSignin under that you will see tasks and find Android folder in it.
6.2.3 Double click "SigningReport" , Build will execute and you will find the SHA-1 key.
7. Copy the Key and paste in the "SHA 1 Signing Certificate" field
8. Click create,now your project is configured for google Authentication



Configuring App for FaceBook Sign-in :

1. Log into your FaceBook Account.
2. Now click the link https://developers.facebook.com/
3. In Dashboard, click MyApps. In "Create a New App ID" Dialog  enter the display name and Email of owner , now the account has been created on clicking create App ID and redirected.
4. In Dashboard, click Docs ,under business tools select "FaceBook Login" and Choose Android 
5. Choose App or create New App, now we need to configure App
5.1 Add the dependency File in gradle  " implementation 'com.facebook.android:facebook-login:5.15.3' "
6. Click the link " https://developers.facebook.com/docs/facebook-login/android " and follow with and continue with step 4 and 5 
7. To generate Key Hashes, you must give openssl path click the link https://code.google.com/archive/p/openssl-for-windows/downloads and download "openssl-0.9.8e X64 "
8. Extract the download file and make a copy in the drive you wish to.
9. Navigate to bin folder and thats your openssl path , now we need execute cmd in terminal
10. In Cmd prompt  navigate to bin folder of java and do the following,
10.1 Enter cmd as "keytool -exportcert -alias androiddebugkey -keystore (Your keystore path)\debug.keystore | (Your OpenSSL path)\openssl sha1 -binary | (Your OpenSSL path)\openssl base64"
10.2 change the openssl and your keystore path in the above cmd line . Keystore is nothing but the ".android" folder under user level in c drive
10.3 on Executing the command asks for keystore password, default is "android"
10.4 you will be getting the "keyHash" , now go to the developer page and past the keyHash and click save
11. Enable signle SignIn and click save Thats it.
12. Now you can add the Button of FaceBook in your XML 
13. Follow the code for Further Actions.

