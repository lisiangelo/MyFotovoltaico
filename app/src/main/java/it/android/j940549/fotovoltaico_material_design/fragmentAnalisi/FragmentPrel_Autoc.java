package it.android.j940549.fotovoltaico_material_design.fragmentAnalisi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import it.android.j940549.fotovoltaico_material_design.HomeActivityNav;
import it.android.j940549.fotovoltaico_material_design.R;
import it.android.j940549.fotovoltaico_material_design.SQLite.DBLayer;

/**
 * Created by J940549 on 24/05/2017.
 */

public class FragmentPrel_Autoc extends Fragment {
    Spinner anno1;
    Spinner anno2;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = null;

        //String annorife= (String) annoSpinner.getSelectedItem().toString();
        rootView = inflater.inflate(R.layout.fragment_analisi_dati_piechart, container, false);
        TextView titolo = (TextView) rootView.findViewById(R.id.titoloAnalisiPie);
        titolo.setText("Analisi e confronto composizione Consumi");
        ArrayList datiAutoc_Immissioni = new ArrayList();
        //datiproduzione=getArguments().getStringArrayList("DATIPRODUZIONE");
        String anno1 = getArguments().getString("ANNO1");
        String anno2 = getArguments().getString("ANNO2");

        Log.i("datiauto anno", anno1);

        //dati anno 1
        int totConsumi=0;
        int totproduzione=0;
        int totImmissioni=0;
        ArrayList datiproduzione=new ArrayList();
        datiproduzione=getDatiProduzione(anno1,getContext());
        Log.i("nr datiprod",""+datiproduzione.size());
        for(int i=1;i<=datiproduzione.size()-1;i++) {
            int in=(int) datiproduzione.get(i-1);
            int fin= (int) datiproduzione.get(i);
            int dato=fin-in;
            totproduzione=totproduzione+dato;
            Log.i("totprdo..cr",""+totproduzione);
        }
        ArrayList datiimmissione=new ArrayList();
        datiimmissione=getDatiImmissioni(anno1,getContext());
        for(int i=1;i<=datiimmissione.size()-1;i++) {
            int in=(int) datiimmissione.get(i-1);
            int fin= (int) datiimmissione.get(i);
            int dato=fin-in;
            totImmissioni=totImmissioni+dato;
            Log.i("totpim..cr",""+totImmissioni);
        }

        ArrayList datiprelievi=getDatiPrelievi(anno1,getContext());
        int totprelievi=0;
        int totautoconsumo=0;
        ArrayList<Entry> entries = new ArrayList<>();
        for(int i=1;i<=datiprelievi.size()-1;i++) {
            int in= 0;
            //if(i!=0){
            in=(int) datiprelievi.get(i-1);
            //}
            int fin= (int) datiprelievi.get(i);
            int dato=fin-in;
            totprelievi=totprelievi+dato;
            Log.i("totpel..cr",""+totprelievi);

            entries.add(new Entry((int) dato, i-1));
        }

        totautoconsumo=totproduzione-totImmissioni;
        totConsumi=totprelievi+totautoconsumo;
        Log.i("totprdo",""+totproduzione);
        Log.i("totpimmm",""+totImmissioni);
        Log.i("totprdo",""+totprelievi);
        Log.i("totcons",""+totConsumi);
        float perc_prel_consumi=((float)totprelievi/(float)totConsumi)*100;
        float perc_aut_consumi=((float)totautoconsumo/(float)totConsumi)*100;
        Log.i("float consumi",""+perc_aut_consumi);
        float Ydata[]={perc_prel_consumi,perc_aut_consumi};
        String Xdata[]={"Prelievi","Autoconsumo"};
        ArrayList<Entry> Yvals = new ArrayList<>();
        for(int i=0;i<=Ydata.length-1;i++) {

            Yvals.add(new Entry((int) Ydata[i], i));
        }
        ArrayList<String> Xvals = new ArrayList<>();
        for(int i=0;i<=Xdata.length-1;i++) {

            Xvals.add(Xdata[i]);
        }

        PieDataSet dataset = new PieDataSet(Yvals, "CONSUMI "+totConsumi);
        dataset.setValueFormatter(new PercentFormatter());
        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.parseColor("#d9e021"));
        //colors.add(Color.parseColor("#85be41"));
        //colors.add(Color.CYAN);
        dataset.setColors(colors);

        PieData data = new PieData(Xvals, dataset);
        data.setValueTextSize(15);//Formatter(new PercentFormatter());

        PieChart chart = (PieChart) rootView.findViewById(R.id.piechart1);
        chart.setData(data);
        chart.setDescription("# "+anno1);chart.setCenterText(anno1);
        chart.setBackground(getResources().getDrawable(R.drawable.bordo_ocra));
        chart.animateX(3000);


        //dati anno2
        int totConsumi2=0;
        int totproduzione2=0;
        int totImmissioni2=0;
        ArrayList datiproduzione2=new ArrayList();
        datiproduzione2=getDatiProduzione(anno2,getContext());
        Log.i("nr datiprod",""+datiproduzione2.size());
        for(int i=1;i<=datiproduzione2.size()-1;i++) {
            int in=(int) datiproduzione2.get(i-1);
            int fin= (int) datiproduzione2.get(i);
            int dato=fin-in;
            totproduzione2=totproduzione2+dato;
            Log.i("totprdo..cr",""+totproduzione2);
        }
        ArrayList datiimmissione2=new ArrayList();
        datiimmissione2=getDatiImmissioni(anno2,getContext());
        for(int i=1;i<=datiimmissione2.size()-1;i++) {
            int in=(int) datiimmissione2.get(i-1);
            int fin= (int) datiimmissione2.get(i);
            int dato=fin-in;
            totImmissioni2=totImmissioni2+dato;
            Log.i("totpim..cr",""+totImmissioni2);
        }

        ArrayList datiprelievi2=getDatiPrelievi(anno2,getContext());
        int totprelievi2=0;
        int totautoconsumo2=0;
        ArrayList<Entry> entries2 = new ArrayList<>();
        for(int i=1;i<=datiprelievi2.size()-1;i++) {
            int in= 0;
            //if(i!=0){
            in=(int) datiprelievi2.get(i-1);
            //}
            int fin= (int) datiprelievi2.get(i);
            int dato=fin-in;
            totprelievi2=totprelievi2+dato;
            Log.i("totpel..cr",""+totprelievi2);

            entries2.add(new Entry((int) dato, i-1));
        }

        totautoconsumo2=totproduzione2-totImmissioni2;
        totConsumi2=totprelievi2+totautoconsumo2;
        Log.i("totprdo",""+totproduzione2);
        Log.i("totpimmm",""+totImmissioni2);
        Log.i("totprdo",""+totprelievi2);
        Log.i("totcons",""+totConsumi2);
        float perc_prel_consumi2=((float)totprelievi2/(float)totConsumi2)*100;
        float perc_aut_consumi2=((float)totautoconsumo2/(float)totConsumi2)*100;
        Log.i("float consumi",""+perc_aut_consumi2);
        float Ydata2[]={perc_prel_consumi2,perc_aut_consumi2};
        String Xdata2[]={"Prelievi","Autoconsumo"};
        ArrayList<Entry> Yvals2 = new ArrayList<>();
        for(int i=0;i<=Ydata2.length-1;i++) {

            Yvals2.add(new Entry((int) Ydata2[i], i));
        }
        ArrayList<String> Xvals2 = new ArrayList<>();
        for(int i=0;i<=Xdata2.length-1;i++) {

            Xvals2.add(Xdata2[i]);
        }

        PieDataSet dataset2 = new PieDataSet(Yvals2, "CONSUMI "+totConsumi2);
        dataset2.setValueFormatter(new PercentFormatter());
        dataset2.setColors(colors);

        PieData data2 = new PieData(Xvals2, dataset2);
        data2.setValueTextSize(15);//Formatter(new PercentFormatter());

        PieChart chart2 = (PieChart) rootView.findViewById(R.id.piechart2);
        chart2.setData(data2);
        chart2.setDescription("# "+anno2);chart2.setCenterText(anno2);
        chart2.setBackground(getResources().getDrawable(R.drawable.bordo_ocra));
        chart2.animateX(3000);

        return rootView;
    }

    public ArrayList getDatiConsumi(String annorife,Context context) {
        ArrayList datiConsumi=new ArrayList();
        ArrayList datiPrelievi=getDatiPrelievi(annorife,context);
        ArrayList datiAutoconsumo=getDatiAutoconsumo(annorife,context);
        for (int i = 1; i <= datiPrelievi.size() - 1; i++) {
            int in = (int) datiPrelievi.get(i - 1);
            int fin = (int) datiPrelievi.get(i);
            int datop = fin - in;
            int datoaut = (int) datiAutoconsumo.get(i-1);
            int datoconds=datop+datoaut ;
            datiConsumi.add(datoconds);
            Log.i("totconsusmo..cr", ""+i +".."+ datoconds);
        }
        return datiConsumi;
    }
    public ArrayList getDatiAutoconsumo(String annorife, Context context) {
        ArrayList datiautoconsumo = new ArrayList();

        ArrayList datiProduzione=getDatiProduzione(annorife,context);
        ArrayList datiImmissione=getDatiImmissioni(annorife,context);
        for (int i = 1; i <= datiProduzione.size() - 1; i++) {
            int in = (int) datiProduzione.get(i - 1);
            int fin = (int) datiProduzione.get(i);
            int datop = fin - in;
            int inim = (int) datiImmissione.get(i - 1);
            int finim = (int) datiImmissione.get(i);
            int datoim = finim - inim;
            int datoaut=datop-datoim;
            datiautoconsumo.add(datoaut);
            Log.i("totAutoconusmo..cr", ""+i +".."+ datoaut);
        }
        return datiautoconsumo;
    }
    public static ArrayList getDatiImmissioni(String annorife, Context context) {
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
                    f1prodF = datiProd.getInt(3);
                    f2prodF = datiProd.getInt(4);
                    f3prodF = datiProd.getInt(5);
                    Log.i("prodFin", "..." + f1prodF + "--" + f2prodF + "--" + f3prodF);
                    proddatotot = f1prodF + f2prodF + f3prodF;
                    datiImmissioni.add(proddatotot);
                } while (datiProd.moveToNext());
            }
            datiProd.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbLayer.close();


        return datiImmissioni;
    }
    public static ArrayList getDatiProduzione(String annorife, Context context) {
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
                    f1prodF = datiProd.getInt(3);
                    f2prodF = datiProd.getInt(4);
                    f3prodF = datiProd.getInt(5);
                    Log.i("prodFin", "..." + f1prodF + "--" + f2prodF + "--" + f3prodF);
                    proddatotot = f1prodF + f2prodF + f3prodF;
                    datiProduzione.add(proddatotot);
                } while (datiProd.moveToNext());
            }
            datiProd.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbLayer.close();


        return datiProduzione;
    }
    public static ArrayList getDatiPrelievi(String annorife, Context context) {
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
                    f1prodF = datiProd.getInt(3);
                    f2prodF = datiProd.getInt(4);
                    f3prodF = datiProd.getInt(5);
                    Log.i("prodFin", "..." + f1prodF + "--" + f2prodF + "--" + f3prodF);
                    proddatotot = f1prodF + f2prodF + f3prodF;
                    datiprelievi.add(proddatotot);
                } while (datiProd.moveToNext());
            }
            datiProd.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbLayer.close();


        return datiprelievi;
    }

}

