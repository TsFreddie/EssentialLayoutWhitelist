package in.tsdo.elw;

import android.Manifest;
import android.app.DialogFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {
    public static final String DEFAULT_SYSTEM_WHITELIST = "com.android.egg,com.google.android.calculator,com.google.android.calendar,com.essential.klik,com.android.chrome,com.google.android.deskclock,com.google.android.contacts,com.google.android.gm,com.google.android.googlequicksearchbox,com.android.vending,com.android.launcher3,com.google.android.apps.maps,com.google.android.apps.messaging,com.google.android.dialer,com.google.android.apps.photos,com.google.android.youtube,com.android.settings,com.android.phone,com.android.systemui,";
    public static String DEFAULT_WHITELIST = DEFAULT_SYSTEM_WHITELIST + "com.google.android.music,com.google.android.play.games,com.google.android.apps.docs,com.google.android.apps.magazines,com.google.android.videos,com.teslacoilsw.launcher,";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView instructionText = (TextView)findViewById(R.id.instructionText);
        instructionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", "adb shell pm grant in.tsdo.elw android.permission.WRITE_SECURE_SETTINGS");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, R.string.copied_adb, Toast.LENGTH_SHORT).show();
            }
        });

        Button checkButton = (Button)findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    Toast.makeText(MainActivity.this, R.string.permission_failed, Toast.LENGTH_SHORT).show();
                };
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    protected boolean checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS);
        if (permissionCheck < 0) {
            if (RootUtils.isDeviceRooted()) {
                DialogFragment rootDialog = new PermissionDialogFragment();
                rootDialog.show(getFragmentManager(), "root_grant");
            }
        }

        if (permissionCheck >= 0) {
            Intent intent = new Intent(this, AppPickerActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    protected boolean rootGrantPermission() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("pm grant " + getPackageName() + " android.permission.WRITE_SECURE_SETTINGS \n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            p.waitFor();
        } catch (java.io.IOException e) {
            Toast.makeText(this, "Permission command failed", Toast.LENGTH_SHORT).show();
        } catch (java.lang.InterruptedException e) {
            Toast.makeText(this, "Permission command Interrupted", Toast.LENGTH_SHORT).show();
        }
        int newCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS);
        if (newCheck > 0) return true;
        return false;
    }
 }
