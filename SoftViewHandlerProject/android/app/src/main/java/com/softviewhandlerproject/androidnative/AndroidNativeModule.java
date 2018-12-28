package com.softviewhandlerproject.androidnative;

import java.util.HashMap;
import java.util.Map;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import android.app.Activity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class AndroidNativeModule extends ReactContextBaseJavaModule {

  @Override
  public String getName() {
    /**
     * return the string name of the NativeModule which represents this class in JavaScript
     * In JS access this module through React.NativeModules.AndroidNative
     */
    return "AndroidNative";
  }

  /* constructor */
  public AndroidNativeModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }
  
  @Override
  public Map<String, Object> getConstants() {
    Map<String, Object> constants = new HashMap<>();

    constants.put("has_soft_keys", hasSoftKeys());

    return constants;
  }

  private boolean hasSoftKeys() {
    boolean hasSoftwareKeys;

    Activity activity = getCurrentActivity();

    if (activity == null) {
        return true;
    }

    WindowManager window = getCurrentActivity().getWindowManager();

    if(window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
        Display d = getCurrentActivity().getWindowManager().getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        hasSoftwareKeys =  (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    } else {
        // boolean hasMenuKey = ViewConfiguration.get(mContext).hasPermanentMenuKey();
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        hasSoftwareKeys = !hasHomeKey && !hasBackKey;
    }
    return hasSoftwareKeys;
  }
  
}
