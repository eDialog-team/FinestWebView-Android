package com.thefinestartist.finestwebview;

import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class JavaScriptInterfaceBlob {
    private Context context;
    private NotificationManager nm;
    public JavaScriptInterfaceBlob(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void getBase64FromBlobData(String base64Data, String fileName, String type) throws IOException {
        convertBase64StringToPdfAndStoreIt(base64Data, fileName, type);
    }
   
    private void convertBase64StringToPdfAndStoreIt(String base64PDf, String fileName, String type) throws IOException {
        final int notificationId = 1;
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        final File dwldsPath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
        byte[] fileAsBytes = Base64.decode(base64PDf.replaceFirst("^data:" + type + ";base64,", ""), 0);
        FileOutputStream os;
        os = new FileOutputStream(dwldsPath, false);
        os.write(fileAsBytes);
        os.flush();

        if(dwldsPath.exists()) {
            Toast.makeText(context, "Ficher Télécharger!", Toast.LENGTH_SHORT).show();

            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(dwldsPath), type.trim());
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Le ficher " + type + "n'a pas peu être ouvert\n(Application de lecture non-disponible)", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
