package it.android.j940549.fotovoltaico_material_design;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import it.android.j940549.fotovoltaico_material_design.Backup.BackUp;
import it.android.j940549.fotovoltaico_material_design.fragmentAnalisi.AnalisiDatiFragment;

public class HomeActivityNav extends AppCompatActivity{

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView navigationView ;
    private BottomNavigationView bottomNavigationView ;
    private Switch notifSwitch;
    private SharedPreferences mSharedPreferences;
    public static String NOMEIMPIANTO = "mio";
    public static String DIMENSIONE = "";
    public static float INCENTIVO = (float) 0.41;
    public static int ANNOATTIVAZIONE= 2012;
    public static String MYMAIL= "lisi.angelo@gdf.it";
    private static final int CREA_BACKUP = 1;
    private static final int CARICA_BACKUP= 2;
    private boolean permesso=true;
    private static final int READ_REQUEST_CODE=42;
    private String backupPath;

    public static ArrayList<String> ANNI=new ArrayList<>();
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    public static NumberFormat numberFormat;
    public static NumberFormat moneyFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);
        numberFormat =
                NumberFormat.getNumberInstance(new Locale("it", "IT"));
        numberFormat.setMaximumFractionDigits(2);
        moneyFormat =
                NumberFormat.getCurrencyInstance(new Locale("it", "IT"));

        Calendar c=Calendar.getInstance();
        int correnteAnno=c.get(Calendar.YEAR);
        ANNI.clear();
        for (int i=HomeActivityNav.ANNOATTIVAZIONE; i<=correnteAnno;i++){
            ANNI.add(""+i);
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectDrawerItem(menuItem, menuItem.getItemId());
                return false;
            }
        });

        bottomNavigationView =findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                selectBottomItem(menuItem, menuItem.getItemId());
                return false;
            }
        });

        Fragment fragment=ReportFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        //imposto il titolo dell'action bar
        setTitle(bottomNavigationView.getMenu().getItem(0).getTitle());

        mSharedPreferences = getSharedPreferences("MyFotovoltaicoPreference", Context.MODE_PRIVATE);

        // To set whether switch is on/off use:
        navigationView.getMenu().findItem(R.id.checkOnNotification)
                .setActionView(new Switch(this));
        notifSwitch = (Switch)
                navigationView.getMenu().findItem(R.id.checkOnNotification).getActionView();

        //        aSwitch.setChecked(false);

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {

                if (ischecked) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean("notifSwitch",true);
                    editor.commit();

                    attivaNotifiche();
                    Log.i("switch", "on" );
                    Toast.makeText(getBaseContext(),"Notifiche abilitate",Toast.LENGTH_LONG).show();
                    //  startService(i);
                } else {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean("notifSwitch",false);
                    editor.commit();

                    disattivaNotifiche();
                    Log.i("switch", "off" );
                    Toast.makeText(getBaseContext(),"Notifiche disabilitate",Toast.LENGTH_LONG).show();

                }

            }
        });

        leggiPreferenze();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.btnaggiungi_in_app_bar, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.add_new_lettura:
                vaiaNewLettura();
                break;
            default:
                break;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void leggiPreferenze(){
        boolean siNotifiche= mSharedPreferences.getBoolean("notifSwitch", false);

        if(!siNotifiche){
            Toast.makeText(this, "ATTIVA LE NOTIFICHE ", Toast.LENGTH_SHORT).show();
            notifSwitch.setChecked(false);

        }else {
            notifSwitch.setChecked(true);


        }

    }

    private void attivaNotifiche(){

        Intent intent = new Intent(this, AllarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingalarmIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // se esiste prima lo cancello
        alarmManager.cancel(pendingalarmIntent);

        // Set the alarm to start and repet evert 2 hours
        Calendar calendar_ora_allarm= Calendar.getInstance();
        calendar_ora_allarm.set(Calendar.MONTH,calendar_ora_allarm.get(Calendar.MONTH)+1);
        calendar_ora_allarm.set(Calendar.DAY_OF_MONTH,1);
        calendar_ora_allarm.set(Calendar.HOUR_OF_DAY,11);
        calendar_ora_allarm.set(Calendar.MINUTE,05);

        Calendar calendarOra_adesso= Calendar.getInstance();

        long diff=calendar_ora_allarm.getTimeInMillis()-calendarOra_adesso.getTimeInMillis();
        Log.i("notifica ","dopo "+calendar_ora_allarm.get(Calendar.DAY_OF_MONTH));
        Log.i("notifica ","dopo "+calendar_ora_allarm.get(Calendar.MONTH));
        Log.i("notifica ","dopo "+diff);

        if(diff<0){
            // sommo ai millisecondi di una settimana il tempo (negativo) in millisecondi passati dall'ultimo lunedi
            diff=(2592000+diff/1000)*1000;
            Log.i("notifica ","passata "+diff);

        }
        Log.i("notifica ","ora "+calendar_ora_allarm.getTimeInMillis());

        long repeatInterval = 31 * AlarmManager.INTERVAL_DAY;

        long triggerTime = SystemClock.elapsedRealtime()+diff;

        // If the Toggle is turned on, set the repeating alarm with
        // a 2 hours interval.

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime, repeatInterval,
                    pendingalarmIntent);
        }


    }

    private void disattivaNotifiche(){

        Intent intent = new Intent(this, AllarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(alarmIntent);

    }



    public void vaiaNewLettura(){
        Intent vaiNewLettura=new Intent(this, NewLetturaActivity.class);

        startActivity(vaiNewLettura);
        //finish();

    }

    public void selectDrawerItem(MenuItem menuItem,int menuItem_id) {

      //  boolean esito=false;
        switch(menuItem.getItemId()){
            case R.id.optionSettaggi:{
                Intent vaiaDatiImpianto=new Intent(this, DatiImpianto.class);
                startActivity(vaiaDatiImpianto);
                //finish();
                break;
            }

            case R.id.optionExport:{
                exportDB(this);
                break;
            }
            case R.id.optionImport:{
                importDB(this);
                break;
            }

            case R.id.optionEsci:{
                finish();
                break;
            }

        }

       menuItem.setChecked(true);

    //imposto il titolo dellìaction bar
    //setTitle(menuItem.getTitle());
    //chiudo il navigationdrawer
        mDrawer.closeDrawers();
        //return esito;

    }



    public void selectBottomItem(MenuItem item,int menuItem_id) {

        Fragment fragment=null;


        switch (menuItem_id) {


            case R.id.bottom_home:
                fragment = ReportFragment.newInstance();

                break;


            case R.id.bottom_rendimenti:

                fragment = RendimentiFragment.newInstance();
                break;

            case R.id.bottom_analisi:
                Calendar c=Calendar.getInstance();
                String anno1=""+(c.get(Calendar.YEAR)-1);
                String anno2=""+c.get(Calendar.YEAR);

                fragment = AnalisiDatiFragment.newInstance(anno1,anno2);
                break;

            case R.id.bottom_bilancio:

                fragment = BilancioFragment.newInstance();
                break;


            default:
                fragment = ReportFragment.newInstance();
                bottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
                break;
        }

        //inserisci il fragment rimpiazzando i frgment esitente
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        //evidenzio  l'item che è stato selezionato nel Navigationview
        item.setChecked(true);

        //imposto il titolo dellìaction bar
        setTitle(item.getTitle());
        //chiudo il navigationdrawer
        //mDrawer.closeDrawers();

    }


    public  void importDB(Context context) {
        backupPath="";
        controllaPermessi();




        if(permesso) {
            prendiPath();

        }
    }


    public  void exportDB(Context context) {
        controllaPermessi();
        if(permesso) {
            BackUp backUp = new BackUp(context, MYMAIL);
            //backUp.decryptDB();
            boolean fatto = backUp.exportDb();
            if (fatto) {
                Toast.makeText(context, "Backup Successful!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Backup non riuscito", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void controllaPermessi(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CREA_BACKUP);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    CARICA_BACKUP);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CREA_BACKUP: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "permesso accordato\n riprova", Toast.LENGTH_SHORT).show();
                    permesso=true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(this, "permesso negato", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permesso=false;
                }
                break;
            }
            case CARICA_BACKUP: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "permesso accordato \n riprova", Toast.LENGTH_SHORT).show();
                    permesso=true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(this, "permesso negato", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    permesso=false;
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void prendiPath(){
        Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){
        if(requestCode==READ_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            Uri uri=null;
            if(resultData!=null){
                uri=resultData.getData();

                Cursor cursor=getContentResolver().query(uri,null,null,null,null,null);

                try{
                    if(cursor!=null&&cursor.moveToFirst()){
                        String display_name= cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.i("backupath_display_name", display_name);
                        backupPath=display_name;//"fotovoltaico_material_design.db";
                    }
                }catch (Exception e){

                }finally {
                    cursor.close();
                }

                Log.i("backupath", backupPath );
                if(backupPath!=null||!backupPath.equals("")) {
                    BackUp backUp = new BackUp(this, MYMAIL);

                    boolean fatto = backUp.importDB(backupPath);

                    if (fatto) {

                        Toast.makeText(this, "Backup Successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Backup non riuscito", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "percorso Backup errato", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

}
