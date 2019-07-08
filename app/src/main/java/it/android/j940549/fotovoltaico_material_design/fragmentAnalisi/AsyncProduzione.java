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

public class AsyncProduzione extends AsyncTask<String, Void, LineData> {

    FragmentProduzioni container;
    ArrayList <Dato_Tot_Tabella> datianno_prec = new ArrayList();
    ArrayList <Dato_Tot_Tabella> datianno_succ = new ArrayList();

    public AsyncProduzione (FragmentProduzioni container) {
        this.container = container;

    }

    @Override
    protected LineData doInBackground(String... params) {
    String anno1=params[0];
    String anno2=params[1];
        Log.i("datiprod anno", anno1);
        ArrayList datiproduzione = new ArrayList();
        datiproduzione = getDatiProduzione(anno1, container.getContext(),datianno_prec);
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 1; i <= datiproduzione.size() - 1; i++) {
            int in = (int) datiproduzione.get(i - 1);
            int fin = (int) datiproduzione.get(i);
            int dato = fin - in;
            Log.i("datoprod", "" + dato);
            entries.add(new Entry((int) dato, i - 1));
        }

        ArrayList datiproduzione2 = new ArrayList();

//            String anno2 = getArguments().getString("ANNO2");

        Log.i("datiprod anno2", anno2);
        datiproduzione2 = getDatiProduzione(anno2, container.getContext(),datianno_succ);

        //datiproduzione2=getArguments().getStringArrayList("DATIPRODUZIONE2");

        ArrayList<Entry> entries2 = new ArrayList<>();
        for (int i = 1; i <= datiproduzione2.size() - 1; i++) {
            int in2 = (int) datiproduzione2.get(i - 1);
            int fin2 = (int) datiproduzione2.get(i);
            int dato2 = fin2 - in2;
            Log.i("datoprod2", "" + dato2);
            entries2.add(new Entry((int) dato2, i - 1));
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

    public ArrayList getDatiProduzione(String annorife, Context context, ArrayList datianno_) {
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


        return datiProduzione;
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
            container.populateResult(data, datianno_prec,datianno_succ);
            //container.hideProgressBar();
            this.container = null;
        }
    }

}
