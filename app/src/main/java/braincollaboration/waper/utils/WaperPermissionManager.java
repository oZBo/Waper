package braincollaboration.waper.utils;


import android.Manifest;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import braincollaboration.waper.WaperApp;

public class WaperPermissionManager {

    private static WaperPermissionManager instance;

    private WaperPermissionManager() {
    }

    public static WaperPermissionManager getInstance() {
        if(instance == null){
            instance = new WaperPermissionManager();
        }
        return instance;
    }

    public void checkMandatoryPermissions(PermissionListener listener){
        new TedPermission(WaperApp.getCurrentActivity())
                .setPermissionListener(listener)
                .setDeniedMessage("If u reject those permissions u cant use app")
                .setRationaleMessage("To use this app please allow next permissions")
                .setGotoSettingButtonText("Settings")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO)
                .setRationaleConfirmText("Confirm")
                .check();
    }

    public void checkSpecifiedPermissions(PermissionListener listener, String... permissions){
        new TedPermission(WaperApp.getCurrentActivity())
                .setPermissionListener(listener)
                .setDeniedMessage("If you reject permission,you can not use DiPocket\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setRationaleMessage("Stub")
                .setGotoSettingButtonText("Stub")
                .setPermissions(permissions)
                .setRationaleConfirmText("Confirm")
                .check();
    }

}
