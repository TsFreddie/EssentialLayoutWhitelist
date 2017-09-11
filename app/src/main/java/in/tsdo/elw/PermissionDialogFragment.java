package in.tsdo.elw;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class PermissionDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.root_grant)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!((MainActivity)getActivity()).rootGrantPermission()) {
                            Toast.makeText(getContext(), R.string.root_failed, Toast.LENGTH_SHORT).show();
                        };
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
