package it.android.j940549.fotovoltaico_material_design.SQLite;

/**
 * Created by J940549 on 22/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import it.android.j940549.fotovoltaico_material_design.Model.ConvertiData;
import it.android.j940549.fotovoltaico_material_design.ReportFragment;

/**
 * Created by J940549 on 22/04/2017.
 */

public class DBLayer {

    private static final String DATABASE_NAME = "fotovoltaico_material_design.db";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private  static Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS impianti (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " nome_impianto TEXT, data INTEGER UNIQUE, dimensione INTEGER , incentivo INTEGER );");

                db.execSQL("CREATE TABLE IF NOT EXISTS tableProduzione (" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " nome_impianto TEXT, data INTEGER UNIQUE, f1 INTEGER , f2 INTEGER , f3 INTEGER );");
            db.execSQL("CREATE TABLE IF NOT EXISTS tableImmissioni(" +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " nome_impianto TEXT, data INTEGER UNIQUE, f1 INTEGER , f2 INTEGER , f3 INTEGER );");

                db.execSQL("CREATE TABLE IF NOT EXISTS tablePrelievi (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " nome_impianto TEXT, data INTEGER UNIQUE, f1 INTEGER , f2 INTEGER , f3 INTEGER );");

                db.execSQL("CREATE TABLE IF NOT EXISTS tableBollette (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " nome_impianto TEXT, anno INTEGER, bimestre INTEGER, importo REAL);");

                db.execSQL("CREATE TABLE IF NOT EXISTS tableBonifici (" +
                        " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " nome_impianto TEXT,anno INTEGER , nr_acconto INTEGER, data INTEGER, importo REAL);");


            }catch (SQLException ex){
                Toast.makeText(ourContext, ""+ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS impianti;");
            db.execSQL("DROP TABLE IF EXISTS tableProduzione;");
            db.execSQL("DROP TABLE IF EXISTS tableImmissioni;");
            db.execSQL("DROP TABLE IF EXISTS tablePrelievi;");
            db.execSQL("DROP TABLE IF EXISTS tableBollette;");
            db.execSQL("DROP TABLE IF EXISTS tableBonifici;");
            onCreate(db);
        }
    }

    public DBLayer(Context c){
        this.ourContext = c;
    }

    public DBLayer open() throws SQLException {
        this.ourHelper = new DbHelper(ourContext);
        this.ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        this.ourHelper.close();
    }







    public Cursor getProduzione(String mese_I, String anno_I, String mese_F, String anno_F, String impianto ){
     impianto="\""+impianto+"\"";
        long date[]=getDateI_F(mese_I,anno_I,mese_F,anno_F);
        long dataI=date[0];
        long dataF=date[1];
            String query= "SELECT * FROM `tableProduzione` WHERE nome_impianto= "+impianto+" and data between "+dataI+" and "+dataF+"";
            Log.i("query getProd...",query);
            Cursor c= ourDatabase.rawQuery(query,null);
            return c;
    }
    public Cursor getProduzioneTot(String impianto ){
        impianto="\""+impianto+"\"";
        String query= "SELECT * FROM `tableProduzione` WHERE nome_impianto= "+impianto+" order by data";
        Log.i("query getProd...",query);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }

    public Cursor getProduzioneAnnua(String impianto, String anno ){
        int annorife=Integer.parseInt(anno);
        long dataI= new ConvertiData().data_long("01","Dic",""+(annorife-1));
        long dataF= new ConvertiData().data_long("01","Gen",""+(annorife+1));
        impianto="\""+impianto+"\"";

        String query= "SELECT * FROM `tableProduzione` WHERE nome_impianto= "+impianto+" and data between "+dataI+" and "+dataF+" order by data";
        Log.i("query getProdxanno...",query);
        Log.i("query getProdxanno...",""+annorife);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }

    public Cursor getImmissioni(String mese_I, String anno_I, String mese_F, String anno_F, String impianto ){
        impianto="\""+impianto+"\"";
        long date[]=getDateI_F(mese_I,anno_I,mese_F,anno_F);
        long dataI=date[0];
        long dataF=date[1];
        String query= "SELECT * FROM `tableImmissioni` WHERE nome_impianto= "+impianto+" and data between "+dataI+" and "+dataF+"";

        Log.i("query getImm...",query);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }
    public Cursor getImmissioniTot(String impianto ){
        impianto="\""+impianto+"\"";
        String query= "SELECT * FROM `tableImmissioni` WHERE nome_impianto= "+impianto+" order by data";
        Log.i("query getImm...",query);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }

    public Cursor getImmissioneAnnua(String impianto, String anno ){
        int annorife=Integer.parseInt(anno);
        long dataI= new ConvertiData().data_long("01","Dic",""+(annorife-1));
        long dataF= new ConvertiData().data_long("01","Gen",""+(annorife+1));
        impianto="\""+impianto+"\"";

        String query= "SELECT * FROM `tableImmissioni` WHERE nome_impianto= "+impianto+" and data between "+dataI+" and "+dataF+" order by data";
        Log.i("query getImmixanno...",query);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }

    public Cursor getPrelievi(String mese_I, String anno_I, String mese_F, String anno_F, String impianto ){
        impianto="\""+impianto+"\"";
        long date[]=getDateI_F(mese_I,anno_I,mese_F,anno_F);
        long dataI=date[0];
        long dataF=date[1];
        String query= "SELECT * FROM `tablePrelievi` WHERE nome_impianto= "+impianto+" and data between "+dataI+" and "+dataF+"";
        Log.i("query getPreliev...",query);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }
    public Cursor getPrelieviTot(String impianto ){
        impianto="\""+impianto+"\"";
        String query= "SELECT * FROM `tablePrelievi` WHERE nome_impianto= "+impianto+" order by data";
        Log.i("query getPreliev...",query);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }
    public Cursor getPrelieviAnnua(String impianto, String anno ){
        int annorife=Integer.parseInt(anno);
        long dataI= new ConvertiData().data_long("01","Dic",""+(annorife-1));
        long dataF= new ConvertiData().data_long("01","Gen",""+(annorife+1));
        impianto="\""+impianto+"\"";

        String query= "SELECT * FROM `tablePrelievi` WHERE nome_impianto= "+impianto+" and data between "+dataI+" and "+dataF+" order by data";
        Log.i("query getPrelxanno...",query);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }

    public boolean newLettProduzione(String impianto,String mese, String anno, String f1,
                                       String f2,String f3){
        impianto="\""+impianto+"\"";
        long data= getDateInsert(mese,anno);
        if(data< ReportFragment.dataUltima){
            Toast.makeText(ourContext, " Attento data precedente all'ultimo inserimento", Toast.LENGTH_SHORT).show();
        }

        int f1_int=Integer.parseInt(f1);
        int f2_int=Integer.parseInt(f2);
        int f3_int=Integer.parseInt(f3);

        String Query="insert into tableProduzione (_id,nome_impianto,data,f1, f2 , f3)" +
                "values (null,"+impianto+","+data+","+f1_int+","+f2_int+","+f3_int+");";
        Log.i("query",Query);
        boolean c=false;
        try{
                    ourDatabase.execSQL(Query);
        c=true;
        }catch (Exception e){
            c=false;
        }

        return c;
    }

    public boolean newLettImmissioni(String impianto,String mese, String anno, String f1,
                                     String f2,String f3){
        impianto="\""+impianto+"\"";
        long data= getDateInsert(mese,anno);
        int f1_int=Integer.parseInt(f1);
        int f2_int=Integer.parseInt(f2);
        int f3_int=Integer.parseInt(f3);

        String Query="insert into tableImmissioni (_id,nome_impianto,data,f1, f2 , f3)" +
                "values (null,"+impianto+","+data+","+f1_int+","+f2_int+","+f3_int+");";
        Log.i("query",Query);
        boolean c=false;
        try{
            ourDatabase.execSQL(Query);
            c=true;
        }catch (Exception e){
            c=false;
        }

        return c;
    }
    public boolean newLettPrelievi(String impianto,String mese, String anno, String f1,
                                     String f2,String f3){
        impianto="\""+impianto+"\"";
        long data= getDateInsert(mese,anno);
        int f1_int=Integer.parseInt(f1);
        int f2_int=Integer.parseInt(f2);
        int f3_int=Integer.parseInt(f3);

        String Query="insert into tablePrelievi (_id,nome_impianto,data,f1, f2 , f3)" +
                "values (null,"+impianto+","+data+","+f1_int+","+f2_int+","+f3_int+");";
        Log.i("query",Query);
        boolean c=false;
        try{
            ourDatabase.execSQL(Query);
            c=true;
        }catch (Exception e){
            c=false;
        }

        return c;
    }


    public int modificaLettProd(String id,String impianto, String f1, String f2,
                                        String f3){
        int f1_int=Integer.parseInt(f1);
        int f2_int=Integer.parseInt(f2);
        int f3_int=Integer.parseInt(f3);
        ContentValues contentValues= new ContentValues();
        contentValues.put("nome_impianto",impianto);
        contentValues.put("f1",f1_int);
        contentValues.put("f2",f2_int);
        contentValues.put("f3",f3_int);
        int c=-1;

        try{
            c=ourDatabase.update("tableProduzione",contentValues,"_id="+"\""+id+"\"",null);

        }catch (Exception e){

        }

        return c;
    }

    public int modificaLettImmis(String id,String impianto, String f1, String f2,
                                String f3){
        int f1_int=Integer.parseInt(f1);
        int f2_int=Integer.parseInt(f2);
        int f3_int=Integer.parseInt(f3);
        ContentValues contentValues= new ContentValues();
        contentValues.put("nome_impianto",impianto);
        contentValues.put("f1",f1_int);
        contentValues.put("f2",f2_int);
        contentValues.put("f3",f3_int);
        int c=-1;

        try{
            c=ourDatabase.update("tableImmissioni",contentValues,"_id="+"\""+id+"\"",null);

        }catch (Exception e){

        }

        return c;
    }

    public int modificaLettPrel(String id,String impianto, String f1, String f2,
                                String f3){
        int f1_int=Integer.parseInt(f1);
        int f2_int=Integer.parseInt(f2);
        int f3_int=Integer.parseInt(f3);
        ContentValues contentValues= new ContentValues();
        contentValues.put("nome_impianto",impianto);
        contentValues.put("f1",f1_int);
        contentValues.put("f2",f2_int);
        contentValues.put("f3",f3_int);
        int c=-1;

        try{
            c=ourDatabase.update("tablePrelievi",contentValues,"_id="+"\""+id+"\"",null);

        }catch (Exception e){

        }

        return c;
    }

    public int deleteDatoProd(int id){

    int res=ourDatabase.delete("tableProduzione", "_id="+id,null);
    //db.delete(DATABASE_TABLE, KEY_ROWID + "=" + row, null);
    return res;
}
    public int deleteDatoImmis(int id){

        int res=ourDatabase.delete("tableImmissioni", "_id="+id,null);
        //db.delete(DATABASE_TABLE, KEY_ROWID + "=" + row, null);
        return res;
    }
    public int deleteDatoPrel(int id){

        int res=ourDatabase.delete("tablePrelievi", "_id="+id,null);
        //db.delete(DATABASE_TABLE, KEY_ROWID + "=" + row, null);
        return res;
    }

    public boolean insertBolletta(String impianto, String anno, String bimestre, String importo){

        String annoRif= "\""+anno+"\"";
        String impianto_="\""+impianto+"\"";
        int intbimestre=new ConvertiData().intBimestre(bimestre);
        float importoFt= Float.parseFloat(importo);System.out.println(importoFt);
        float iva=(importoFt/1.10f);
        float costo=importoFt-iva;

        String Query="insert into tableBollette  (_id,nome_impianto,anno,bimestre, importo)" +
                "values (null,"+impianto_+","+annoRif+","+intbimestre+","+importoFt+");";
        Log.i("query",Query);
        boolean c=false;
        try{
            ourDatabase.execSQL(Query);
            c=true;
        }catch (Exception e){
            c=false;
        }

        return c;

    }

    public Cursor getBollette(String impianto,String anno){
            impianto="\""+impianto+"\"";
            anno="\""+anno+"\"";
            String query= "SELECT * FROM `tableBollette` WHERE nome_impianto= "+impianto+" and anno="+anno+" order by bimestre";
            Log.i("query getBollette...",query);
            Cursor c= ourDatabase.rawQuery(query,null);
            return c;
        }
    public int deleteDatoBollette(int id){

        int res=ourDatabase.delete("tableBollette", "_id="+id,null);
        //db.delete(DATABASE_TABLE, KEY_ROWID + "=" + row, null);
        return res;
    }

    public boolean insertBonifico(String impianto, String anno, String periodo,String dataBonifico, String importo){

        String annoRif= "\""+anno+"\"";
        String impianto_="\""+impianto+"\"";
        int nrBonifico= new ConvertiData().intPeriodo(periodo);
        String data[]=dataBonifico.split("/");
        long databon=new ConvertiData().data_long(data[0].trim(),data[1].trim(),data[2].trim());
        float importobon= Float.parseFloat(importo);System.out.println(importobon);

        String Query="insert into tableBonifici  (_id,nome_impianto,anno,nr_acconto, data, importo)" +
                "values (null,"+impianto_+","+annoRif+","+nrBonifico+","+databon+","+importobon+");";
        Log.i("query",Query);
        boolean c=false;
        try{
            ourDatabase.execSQL(Query);
            c=true;
        }catch (Exception e){
            c=false;
        }

        return c;

    }

    public Cursor getBonifici(String impianto,String anno){
        impianto="\""+impianto+"\"";
        anno="\""+anno+"\"";
        String query= "SELECT * FROM `tableBonifici` WHERE nome_impianto= "+impianto+" and anno="+anno+" order by data";
        Log.i("query getBonifici...",query);
        Cursor c= ourDatabase.rawQuery(query,null);
        return c;
    }
    public int deleteDatoBonifico(int id){

        int res=ourDatabase.delete("tableBonifici", "_id="+id,null);
        //db.delete(DATABASE_TABLE, KEY_ROWID + "=" + row, null);
        return res;
    }



    public SQLiteDatabase getDB(){
    return this.ourDatabase;

}
private long[] getDateI_F(String mese_I,String anno_I, String mese_F,String anno_F ){
    Log.i("date",mese_I+".."+mese_F);
    long dataI=0;long dataF=0;

    int  annoI=Integer.parseInt((String) anno_I);
    int  annoF=Integer.parseInt((String) anno_F);

    String mmI=(String) mese_I;
    String mmF=(String) mese_F;
    String aaI="";
    String aaF="";
    String ggI="";
    String ggF="";

    switch(mmI){
        case "Gen": ggI="31";mmI="11";aaI=""+(annoI-1); break;
        case "Feb": ggI="31";mmI="0"; aaI=""+annoI;break;
        case "Mar": ggI="28";mmI="01"; aaI=""+annoI;break;
        case "Apr": ggI="31";mmI="02"; aaI=""+annoI;break;
        case "Mag": ggI="30";mmI="03"; aaI=""+annoI;break;
        case "Giu": ggI="31";mmI="04"; aaI=""+annoI;break;
        case "Lug":ggI="30";mmI="05"; aaI=""+annoI;break;
        case "Ago": ggI="31";mmI="06"; aaI=""+annoI;break;
        case "Set": ggI="31";mmI="07"; aaI=""+annoI;break;
        case "Ott": ggI="30";mmI="08"; aaI=""+annoI;break;
        case "Nov": ggI="31";mmI="09"; aaI=""+annoI;break;
        case "Dic": ggI="30";mmI="10"; aaI=""+annoI;break;
    }
    switch(mmF){
        case "Gen": ggF="01";mmF="01";aaF=""+(annoF); break;
        case "Feb": ggF="01";mmF="02"; aaF=""+annoF;break;
        case "Mar": ggF="01";mmF="03"; aaF=""+annoF;break;
        case "Apr": ggF="01";mmF="04"; aaF=""+annoF;break;
        case "Mag": ggF="01";mmF="05"; aaF=""+annoF;break;
        case "Giu": ggF="01";mmF="06"; aaF=""+annoF;break;
        case "Lug":ggF="01";mmF="07"; aaF=""+annoF;break;
        case "Ago": ggF="01";mmF="08"; aaF=""+annoF;break;
        case "Set": ggF="01";mmF="09"; aaF=""+annoF;break;
        case "Ott": ggF="01";mmF="10"; aaF=""+annoF;break;
        case "Nov": ggF="01";mmF="11"; aaF=""+annoF;break;
        case "Dic": ggF="01";mmF="0"; aaF=""+(annoF+1);break;
    }

    dataI=new ConvertiData().data_long(ggI, mmI, aaI);
    dataF=new ConvertiData().data_long(ggF, mmF, aaF);
long [] dato={dataI,dataF};
 Log.i("dataI-- dataF", ""+dato[0]+"--"+dato[1]);
    return dato;
}
    private long getDateInsert(String mese_F,String anno_F ){
        long dataInsert;


        String gg="";

        switch(mese_F){
            case "Gen": gg="31"; break;
            case "Feb": gg="28";break;
            case "Mar": gg="31"; break;
            case "Apr": gg="30"; break;
            case "Mag": gg="31"; break;
            case "Giu": gg="30"; break;
            case "Lug":gg="31"; break;
            case "Ago": gg="31";break;
            case "Set": gg="30";break;
            case "Ott": gg="31";break;
            case "Nov": gg="30";break;
            case "Dic": gg="31";break;
        }
        dataInsert=new ConvertiData().data_long(gg, mese_F, anno_F);

        return dataInsert;
    }
}