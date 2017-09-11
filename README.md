# EssentialLayoutWhitelist
Fill up the status bar yay.

# Permission
This app need WRITE_SECURE_SETTINGS permission.

WRITE_SECURE_SETTINGS permission can only be granted via ADB or ROOT.

If your device is rooted, you will be prompted to use root method.
If the root method failed, please consider using ADB.

To grant permission using ADB:

  0. Install the app, plug your phone to your computer, open the app.
  1. If you don't have ADB already, get it using following links:
[Windows](https://dl.google.com/android/repository/platform-tools-latest-windows.zip)
[MacOS](https://dl.google.com/android/repository/platform-tools-latest-darwin.zip)
[Linux](https://dl.google.com/android/repository/platform-tools-latest-linux.zip)
  2. Extract platform tools.
  3. Open cmd or terminal and goto the directory where you just extracted the tools to
  4. Enter following command:
```bash
./adb shell pm grant in.tsdo.essentialtools android.permission.WRITE_SECURE_SETTINGS
```
  5. On your phone, press "Check Again".
  6. Profit.

# Restore to default
The default configuration may not represent Essential's official whitelist.

Here's the list of whitelisted system apps in this app:
```
com.android.egg,
com.google.android.calculator,
com.google.android.calendar,
com.essential.klik,
com.android.chrome,
com.google.android.deskclock,
com.google.android.contacts,
com.google.android.gm,
com.google.android.googlequicksearchbox,
com.android.vending,
com.android.launcher3,
com.google.android.apps.maps,
com.google.android.apps.messaging,
com.google.android.dialer,
com.google.android.apps.photos,
com.google.android.youtube,
com.android.settings,
com.android.phone
```


If you want to use the official whitelist, use following command:

```bash
./adb shell settings delete global ESSENTIAL_LAYOUT_WHITELIST
```



# Uninstall
Uninstall the app will not restore your settings.

If you want to restore the settings manually via ADB, use following command:
```bash
./adb shell settings delete global ESSENTIAL_LAYOUT_WHITELIST
```
