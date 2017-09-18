# EssentialLayoutWhitelist
Fill up the status bar yay.
Now with navigation bar removal too.

# Important Note
* Once you open this app, Essential's official whitelist will be **disabled**.
* Any future [Essential Resouces](https://play.google.com/store/apps/details?id=com.essential.resources) updates regarding app whitelisting will not be effective until you reset the settings.

# Reset Settings
Uninstall the app will not restore your settings.

If you want to reset the settings manually via ADB: 
1. Uninstall this app
2. Use following command:
```bash
adb shell settings delete global ESSENTIAL_LAYOUT_WHITELIST
adb shell settings delete global policy_control
```

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
  3. Open cmd or terminal and [goto](https://en.wikipedia.org/wiki/Cd_(command)) the directory where you just extracted the tools to
  4. Enter following command:
```bash
./adb shell pm grant in.tsdo.elw android.permission.WRITE_SECURE_SETTINGS
```
  5. On your phone, press "Check Again".
  6. Profit.

# Default configuration
The _"Restore to default"_ feature may not represent Essential's official whitelist.

Default system apps:
```c
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

// added since 1.3:
com.android.phone,

// added since 1.4:
com.android.systemui,
```

Default user apps:
```c
com.google.android.music,
com.google.android.play.games,
com.google.android.apps.docs,
com.google.android.apps.magazines,
com.google.android.videos,

// added since 1.4:
com.teslacoilsw.launcher,
```

If you want to use the official whitelist, see the [Reset Settings](#reset-settings) section.
