package it.android.j940549.fotovoltaico_material_design.fragmentAnalisi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import it.android.j940549.fotovoltaico_material_design.HomeActivityNav;
import it.android.j940549.fotovoltaico_material_design.Model.Dato_Tot_Tabella;
import it.android.j940549.fotovoltaico_material_design.R;
import it.android.j940549.fotovoltaico_material_design.SQLite.DBLayer;

/**
 * Created by J940549 on 02/07/2017.
 */

public class AsyncConsumiTot extends AsyncTask<String, Void, LineData> {

    FragmentConsumiTot container;
    ArrayList <Dato_Tot_Tabella> datianno_prec = new ArrayList();
    ArrayList <Dato_Tot_Tabella> datianno_succ = new ArrayList();

    ArrayList <Dato_Tot_Tabella> datianno_precAutocon = new ArrayList();
    ArrayList <Dato_Tot_Tabella> datianno_succAutocon = new ArrayList();

    ArrayList <Dato_Tot_Tabella> datianno_precProd = new ArrayList();
    ArrayList <Dato_Tot_Tabella> datianno_succProd = new ArrayList();

    ArrayList <Dato_Tot_Tabella> datianno_precImm = new ArrayList();
    ArrayList <Dato_Tot_Tabella> datianno_succImm = new ArrayList();

    ArrayList <Dato_Tot_Tabella> datianno_precPrel = new ArrayList();
    ArrayList <Dato_Tot_Tabella> datianno_succPrel = new ArrayList();

    public AsyncConsumiTot(FragmentConsumiTot container) {
        this.container = container;

    }

    @Override
    protected LineData doInBackground(String... params) {
    String anno1=params[0];
    String anno2=params[1];
        Log.i("datiauto anno", anno1);
        ArrayList datiConsTot = new ArrayList();
        datiConsTot = getDatiConsumi(anno1,container.getContext(),datianno_prec, datianno_precAutocon,datianno_precProd,datianno_precImm, datianno_precPrel);
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i <= datiConsTot.size() - 1; i++) {
            Log.i("datoauto", "" + datiConsTot.get(i));
            entries.add(new Entry((int) datiConsTot.get(i), i ));
        }

        ArrayList datiConsTot2 = new ArrayList();

//            String anno2 = getArguments().getString("ANNO2");

        Log.i("datiauto anno2", anno2);
        datiConsTot2 = getDatiConsumi(anno2, container.getContext(),datianno_succ, datianno_succAutocon,datianno_succProd,datianno_succImm, datianno_succPrel);

        //datiproduzione2=getArguments().getStringArrayList("DATIPRODUZIONE2");

        ArrayList<Entry> entries2 = new ArrayList<>();
        for (int i = 0; i <= datiConsTot2.size() - 1; i++) {
            Log.i("datoautod2", "" + datiConsTot2.get(i));
            entries2.add(new Entry((int) datiConsTot2.get(i), i ));
        }

        LineDataSet dataset = new LineDataSet(entries, " ANNO " + anno1);
        LineDataSet dataset2 = new LineDataSet(entries2, "ANNO " + anno2);

        dataset.setColor(container.getResources().getColor(R.color.ocra));
        dataset2.setColor(container.getResources().getColor(R.color.red));
        dataset.setLineWidth(2);
        dataset2.setLineWidth(2);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataset); // add the datasets
        dataSets.add(dataset2);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Gennaio");
        labels.add("Febbraio");
        labels.add("Marzo");
        labels.add("Aprile");
        labels.add("Maggio");
        labels.add("Giugno");
        labels.add("Luglio");
        labels.add("Agosto");
        labels.add("Settembre");
        labels.add("Ottobre");
        labels.add("Novembre");
        labels.add("Dicembre");




        LineData data = new LineData(labels, dataSets);

        return data;
    }


    public ArrayList getDatiConsumi(String annorife,Context context, ArrayList<Dato_Tot_Tabella> datianno_,
                                    ArrayList <Dato_Tot_Tabella>  datianno_autocon,ArrayList <Dato_Tot_Tabella>  datianno_prod,
                                    ArrayList <Dato_Tot_Tabella> datianno_Imm,ArrayList <Dato_Tot_Tabella> datianno_Prel) {
        ArrayList datiConsumi=new ArrayList();
        ArrayList datiPrelGraf_datiAnnoTab=getDatiPrelievi(annorife,context,datianno_Prel);
        ArrayList datiPrelievi= (ArrayList) datiPrelGraf_datiAnnoTab.get(0);
        ArrayList datiannoPrel=(ArrayList) datiPrelGraf_datiAnnoTab.get(1);

        ArrayList datiAutocGraf_datiAnnoTab=getDatiAutoconsumo(annorife,context,datianno_autocon,datianno_prod,datianno_Imm);
        ArrayList datiAutoconsumo= (ArrayList) datiAutocGraf_datiAnnoTab.get(0);
        ArrayList datiannoAutoc=(ArrayList) datiAutocGraf_datiAnnoTab.get(1);




        for (int i = 1; i <= datiPrelievi.size() - 1; i++) {
            int in = (int) datiPrelievi.get(i - 1);
            int fin = (int) datiPrelievi.get(i);
            int datop = fin - in;
            int datoaut = (int) datiAutoconsumo.get(i-1);
            int datoconds=datop+datoaut ;
            datiConsumi.add(datoconds);
            Log.i("totconsusmo..cr", ""+i +".."+ datoconds);}

        for(int i=0;i<=datiannoPrel.size()-1;i++){
            Dato_Tot_Tabella datoTabellaPrelievi= (Dato_Tot_Tabella) datiannoPrel.get(i);
            Dato_Tot_Tabella datoTabellaAutoc= (Dato_Tot_Tabella) datiannoAutoc.get(i);
            String data=datianno_Prel.get(i).getData_();
            int dato=Integer.parseInt(datoTabellaPrelievi.getTot_())+Integer.parseInt(datoTabellaAutoc.getTot_());
            Dato_Tot_Tabella datox=new Dato_Tot_Tabella();
            datox.setData_(data);
            datox.setTot_(""+dato);
            datianno_.add(datox);
        }
        return datiConsumi;
    }
    public ArrayList getDatiAutoconsumo(String annorife, Context context,ArrayList <Dato_Tot_Tabella>  datianno_autocon,ArrayList <Dato_Tot_Tabella>  datianno_prod,
                                        ArrayList <Dato_Tot_Tabella> datianno_Imm) {
        ArrayList datiautoconsumo = new ArrayList();

        ArrayList datiProdGraf_datiAnnoTab=getDatiProduzione(annorife,context,datianno_prod);
        ArrayList datiProduzione= (ArrayList) datiProdGraf_datiAnnoTab.get(0);
        ArrayList datiannoProd=(ArrayList) datiProdGraf_datiAnnoTab.get(1);
        ArrayList produzioni=new ArrayList();

        ArrayList datiImmisGraf_datiAnnoTab=getDatiImmissioni(annorife,context,datianno_Imm);
        ArrayList datiImmissione= (ArrayList) datiImmisGraf_datiAnnoTab.get(0);
        ArrayList datiannoImmis= (ArrayList) datiImmisGraf_datiAnnoTab.get(1);
        ArrayList immissioni=new ArrayList();
        for (int i = 1; i <= datiProduzione.size() - 1; i++) {
            int in = (int) datiProduzione.get(i - 1);
            int fin = (int) datiProduzione.get(i);
            int datop = fin - in;
            produzioni.add(datop);}

        for (int i = 1; i <= datiImmissione.size() - 1; i++) {

            int inim = (int) datiImmissione.get(i - 1);
            int finim = (int) datiImmissione.get(i);
            int datoim = finim - inim;
            immissioni.add(datoim);}

        for(int i=0;i<=produzioni.size()-1;i++) {
            Log.i("totAutoconusmo..cr", "" + i + ".." + (int) produzioni.get(i) + "...." + (int) immissioni.get(i));
            int datoaut = (int) produzioni.get(i) - (int) immissioni.get(i);
            datiautoconsumo.add(datoaut);
            Log.i("totAutoconusmo..cr", "" + i + ".." + datoaut);
        }
        for(int i=0;i<=datiannoProd.size()-1;i++){
            Dato_Tot_Tabella datoProdTab= (Dato_Tot_Tabella) datiannoProd.get(i);
            Dato_Tot_Tabella datoImmis= (Dato_Tot_Tabella) datiannoImmis.get(i);
            String data=datoProdTab.getData_();
            int dato=Integer.parseInt(datoProdTab.getTot_())-Integer.parseInt(datoImmis.getTot_());
            Dato_Tot_Tabella datox=new Dato_Tot_Tabella();
            datox.setData_(data);
            datox.setTot_(""+dato);
            datianno_autocon.add(datox);


        }
        ArrayList datiAutocGraf_datiAnnoTab= new ArrayList();
        datiAutocGraf_datiAnnoTab.add(datiautoconsumo);
        datiAutocGraf_datiAnnoTab.add(datianno_autocon);
        return datiAutocGraf_datiAnnoTab;
    }


    public static ArrayList getDatiImmissioni(String annorife, Context context,ArrayList <Dato_Tot_Tabella> datianno_) {
        //datiProduzione.removeAll(datiProduzione);
        ArrayList datiImmissioni = new ArrayList();
        int proddatotot = 0;

        DBLayer dbLayer = null;

        Cursor datiProd = null;

        try {
            dbLayer = new DBLayer(context);
            dbLayer.open();
            datiProd = dbLayer.getImmissioneAnnua(HomeActivityNav.NOMEIMPIANTO, annorife);
            Log.i("righe dei datiProd", "" + datiProd.getCount());

            if (datiProd.getCount() > 0) {
                int f1prodF = 0;
                int f2prodF = 0;
                int f3prodF = 0;

                datiProd.moveToPosition(0);
                do {
                    Dato_Tot_Tabella dato =new Dato_Tot_Tabella();
                    dato.setData_(datiProd.getString(2));
                    f1prodF = datiProd.getInt(3);
                    f2prodF = datiProd.getInt(4);
                    f3prodF = datiProd.getInt(5);
                    Log.i("prodFin", "..." + f1prodF + "--" + f2prodF + "--" + f3prodF);
                    proddatotot = f1prodF + f2prodF + f3prodF;
                    dato.setTot_(""+proddatotot);
                    datiImmissioni.add(proddatotot);
                    datianno_.add(dato);
                } while (datiProd.moveToNext());
            }
            datiProd.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbLayer.close();

        ArrayList datiImmGraf_datiAnnoTab= new ArrayList();
        datiImmGraf_datiAnnoTab.add(datiImmissioni);
        datiImmGraf_datiAnnoTab.add(datianno_);
        return datiImmGraf_datiAnnoTab;
    }
    public static ArrayList getDatiProduzione(String annorife, Context context,ArrayList <Dato_Tot_Tabella> datianno_) {
        //datiProduzione.removeAll(datiProduzione);
        ArrayList datiProduzione = new ArrayList();
        int proddatotot = 0;

        DBLayer dbLayer = null;

        Cursor datiProd = null;

        try {
            dbLayer = new DBLayer(context);
            dbLayer.open();
            datiProd = dbLayer.getProduzioneAnnua(HomeActivityNav.NOMEIMPIANTO, annorife);
            Log.i("righe dei datiProd", "" + datiProd.getCount());

            if (datiProd.getCount() > 0) {
                int f1prodF = 0;
                int f2prodF = 0;
                int f3prodF = 0;

                datiProd.moveToPosition(0);
                do {
                    Dato_Tot_Tabella dato =new Dato_Tot_Tabella();
                    dato.setData_(datiProd.getString(2));
                    f1prodF = datiProd.getInt(3);
                    f2prodF = datiProd.getInt(4);
                    f3prodF = datiProd.getInt(5);
                    Log.i("prodFin", "..." + f1prodF + "--" + f2prodF + "--" + f3prodF);
                    proddatotot = f1prodF + f2prodF + f3prodF;
                    dato.setTot_(""+proddatotot);
                    datiProduzione.add(proddatotot);
                    datianno_.add(dato);
                } while (datiProd.moveToNext());
            }
            datiProd.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbLayer.close();

        ArrayList datiProdGraf_datiAnnoTab= new ArrayList();
        datiProdGraf_datiAnnoTab.add(datiProduzione);
        datiProdGraf_datiAnnoTab.add(datianno_);
        return datiProdGraf_datiAnnoTab;
    }
    public static ArrayList getDatiPrelievi(String annorife, Context context, ArrayList<Dato_Tot_Tabella>datianno_) {
        //datiProduzione.removeAll(datiProduzione);
        ArrayList datiprelievi= new ArrayList();
        int proddatotot = 0;

        DBLayer dbLayer = null;

        Cursor datiProd = null;

        try {
            dbLayer = new DBLayer(context);
            dbLayer.open();
            datiProd = dbLayer.getPrelieviAnnua(HomeActivityNav.NOMEIMPIANTO, annorife);
            Log.i("righe dei datiProd", "" + datiProd.getCount());

            if (datiProd.getCount() > 0) {
                int f1prodF = 0;
                int f2prodF = 0;
                int f3prodF = 0;

                datiProd.moveToPosition(0);
                do {
                    Dato_Tot_Tabella dato =new Dato_Tot_Tabella();
                    dato.setData_(datiProd.getString(2));
                    f1prodF = datiProd.getInt(3);
                    f2prodF = datiProd.getInt(4);
                    f3prodF = datiProd.getInt(5);
                    Log.i("prodFin", "..." + f1prodF + "--" + f2prodF + "--" + f3prodF);
                    proddatotot = f1prodF + f2prodF + f3prodF;
                    dato.setTot_(""+proddatotot);
                    datiprelievi.add(proddatotot);
                    datianno_.add(dato);
                } while (datiProd.moveToNext());
            }
            datiProd.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbLayer.close();

        ArrayList datiPrelGraf_datiAnnoTab= new ArrayList();
        datiPrelGraf_datiAnnoTab.add(datiprelievi);
        datiPrelGraf_datiAnnoTab.add(datianno_);
        return datiPrelGraf_datiAnnoTab;
    }




    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(LineData data) {
        super.onPostExecute(data);
        // The activity can be null if it is thrown out by Android while task is running!
        if(container!=null && container.getActivity()!=null) {
            container.populateResult(data,datianno_prec,datianno_succ);
            //container.hideProgressBar();
            this.container = null;
        }
    }

}
