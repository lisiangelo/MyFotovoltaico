package it.android.j940549.fotovoltaico_material_design.Backup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import it.android.j940549.fotovoltaico_material_design.BuildConfig;
import it.android.j940549.fotovoltaico_material_design.HomeActivityNav;


/**
 * Created by J940549 on 24/06/2017.
 */

public class BackUp {
    private Context context;
    private String mailUser;

    private File backupDB;


    public BackUp( Context context, String mymail){
        this.context=context;
        mailUser= mymail;

    }

    public boolean exportDb(){

        boolean execute=false;
        try {
            File sd = Environment.getExternalStorageDirectory();

            File data = Environment.getDataDirectory();
            Log.i("SD", sd.toString());
            Log.i("data", data.toString());


            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

                //  DBLayer dbLayer=new DBLayer(context);
                // dbLayer.getDB().execSQL();
                String currentDBPath = context.getDatabasePath("fotovoltaico_material_design.db").getAbsolutePath();
                Log.i("currenteDBpathexport", currentDBPath);
                String backupDBPath = "fotovoltaico_material_design.db";

                File currentDB = new File(currentDBPath);//(data, currentDBPath);
                backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(context, "Backup Successfull!", Toast.LENGTH_SHORT).show();
                    execute=true;

                }

                //invia backup alla mail dell'utente
                if(inviaMail(backupDB)){
                    Toast.makeText(context, "invio il Backup alla tua mail!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Backup NON INVIATO alla tua mail!", Toast.LENGTH_SHORT).show();

                }
            }else{
                Log.i("data sd canwrite", ""+sd.canWrite());
                execute=false;
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("errore", e.toString());
            execute=false;
        }
        return execute;
    }

    public  boolean importDB(String backupDBPath) {
        boolean execute=false;
        try {
            File sd = Environment.getExternalStorageDirectory();

            File data = Environment.getDataDirectory();
            Log.i("directory SD", sd.toString());
            Log.i("directory data", data.toString());

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {


                String currentDBPath = context.getDatabasePath("fotovoltaico_material_design.db").getAbsolutePath();
                Log.i("currentDBpathimport", backupDBPath);

               // String backupDBPath = "password.db";
                File currentDB = new File( currentDBPath);//(data, currentDBPath);
                File backupDB = new File( sd, backupDBPath);


                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                Toast.makeText(context, "Import Successful!", Toast.LENGTH_SHORT).show();
                execute=true;
            }

        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            Log.i("currentDBimport", e.toString());

            execute=false;
        }
        return execute;
    }

    public  boolean inviaMail(File fileBackup) {
        boolean execute=false;
        Log.i("mail to", fileBackup.getAbsolutePath());
        //controllaPermessi();
        try {
            String backupDBPath = "fotovoltaico_material_design.db";

            File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), backupDBPath);
            //Uri patch= Uri.fromFile(filelocation);
            Uri patch = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",filelocation);
            Intent emailintent= new Intent((Intent.ACTION_SEND));
            // impostiamo il tipo di mail
            emailintent.setType("vnd.android.cursor.dir/email");
            //String to[]={utente.getMail();};
            String to[]={mailUser};
            emailintent.putExtra(Intent.EXTRA_EMAIL, to);
            // allegato
            emailintent.putExtra(Intent.EXTRA_STREAM,patch);
            //oggetto della mail
            emailintent.putExtra(Intent.EXTRA_SUBJECT,"backup MyFotovoltaico");


            context.startActivity(Intent.createChooser(emailintent,"invia mail...."));
            Log.i("mail try", fileBackup.getAbsolutePath());

            execute=true;
            Log.i("mail try", String.valueOf(execute));

        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            execute=false;
        }
        return execute;
    }

}
