package it.android.j940549.fotovoltaico_material_design;

import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;

import it.android.j940549.fotovoltaico_material_design.Model.ConvertiData;
import it.android.j940549.fotovoltaico_material_design.SQLite.DBLayer;

public class RendimentiFragment extends Fragment {
    private static TableLayout tableLayoutProd;
    Spinner meseISpinner,meseFSpinner;
    Spinner annoISpinner,annoFSpinner;
    ArrayAdapter meseAdapter;
    ArrayAdapter annoAdapter;
    int f1prodOrigine=0;int f2prodOrigine=0;int f3prodOrigine=0;
    int f1prelOrigine=0;int f2prelOrigine=0;int f3prelOrigine=0;
    int f1immOrigine=0;int f2immOrigine=0;int f3immOrigine=0;

    int f1proddato=0;int f2proddato=0;int f3proddato=0;int proddatotot=0;
    int f1preldato=0;int f2preldato=0;int f3preldato=0;int preldatotot=0;
    int f1immdato=0;int f2immdato=0;int f3immdato=0;int immdatotot=0;
    int f1autodato=0;int f2autodato=0;int f3autodato=0;int autdatotot=0;
    int f1consdato=0;int f2consdato=0;int f3consdato=0;int consdatotot=0;
    float  f1prod_immdato=0;float f2prod_immdato=0;float f3prod_immdato=0;float prod_immdatotot=0;
    float f1prod_autdato=0;float f2prod_autdato=0;float f3prod_autdato=0;float prod_autdatotot=0;
    float f1cons_preldato=0;float f2cons_preldato=0;float f3cons_preldato=0;float cons_preldatotot=0;
    float f1cons_autdato=0;float f2cons_autdato=0;float f3cons_autdato=0;float cons_autdatotot=0;
   // @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)


    public RendimentiFragment() {
        // Required empty public constructor
    }

    public static RendimentiFragment newInstance() {
        RendimentiFragment fragment = new RendimentiFragment();
        /*Bundle args = new Bundle();
        args.putString("alunno", alunno);
        args.putString("annosc", annosc);
        args.putString("quadrimestre", quadrimestre);
        fragment.setArguments(args);*/
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view=inflater.inflate(R.layout.fragment_rendimenti, parent, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String [] mesi={"Gen","Feb","Mar","Apr","Mag","Giu",
                "Lug","Ago","Set","Ott","Nov","Dic"};
        meseISpinner= (Spinner) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.meseIniz);
        meseAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,mesi);
        meseISpinner.setAdapter(meseAdapter);
        meseFSpinner= (Spinner) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.mese);
        meseAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,mesi);
        meseFSpinner.setAdapter(meseAdapter);

        annoISpinner= (Spinner) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.annoIniz);
        annoAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, HomeActivityNav.ANNI);
        annoISpinner.setAdapter(annoAdapter);
        annoFSpinner= (Spinner) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.anno);
        annoAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item, HomeActivityNav.ANNI);
        annoFSpinner.setAdapter(annoAdapter);
        setupSpinner(annoISpinner,annoFSpinner,meseISpinner,meseFSpinner,annoAdapter,meseAdapter);
        reportDati(view);
        annoISpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                reportDati(v.getRootView());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        annoFSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                reportDati(v.getRootView());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        meseISpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                reportDati(v.getRootView());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        meseFSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                reportDati(v.getRootView());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setupSpinner(Spinner annoISpinner,Spinner annoFSpinner,Spinner meseISpinner,Spinner meseFSpinner,
                 ArrayAdapter annoAdapter, ArrayAdapter meseAdapter){
        Calendar c= Calendar.getInstance();
        int mese=c.get(Calendar.MONTH);
        int anno=c.get(Calendar.YEAR);
        String meseIF="";
        String annoIF="";

        if(mese==0){
            meseIF="Dic";
            annoIF=""+(anno-1);
        }else {
            meseIF=new ConvertiData().getMese_int_to_Str(mese-1);
            annoIF=""+anno;
        }
        int meseIFpos=meseAdapter.getPosition(meseIF);
        meseISpinner.setSelection(meseIFpos);
        Log.i("mesi",meseIFpos+"..."+annoIF);
        meseFSpinner.setSelection(meseIFpos);
        int annoIFpos=annoAdapter.getPosition(annoIF);
        annoISpinner.setSelection(annoIFpos);
        annoFSpinner.setSelection(annoIFpos);

    }


    public void reportDati (View v) {


        String meseRifeI = (String) meseISpinner.getSelectedItem();
        String annoRifeI = (String) annoISpinner.getSelectedItem();

        String meseRifeF = (String) meseFSpinner.getSelectedItem();
        String annoRifeF = (String) annoFSpinner.getSelectedItem();
       // Toast.makeText(getContext(), ""+meseRifeI+"..."+meseRifeF, Toast.LENGTH_SHORT).show();
        DBLayer dbLayer = null;

        Cursor datiProd = null;
        Cursor datiImm = null;
        Cursor datiPrel = null;

        try {
            dbLayer = new DBLayer(getActivity());
            dbLayer.open();
            datiProd = dbLayer.getProduzione(meseRifeI, annoRifeI,meseRifeF, annoRifeF, HomeActivityNav.NOMEIMPIANTO);
            datiImm = dbLayer.getImmissioni(meseRifeI, annoRifeI,meseRifeF, annoRifeF, HomeActivityNav.NOMEIMPIANTO);
            datiPrel = dbLayer.getPrelievi(meseRifeI, annoRifeI,meseRifeF, annoRifeF, HomeActivityNav.NOMEIMPIANTO);

            if (datiImm.getCount() > 0) {
                datiImm.moveToPosition(0);
                f1immOrigine = datiImm.getInt(3);
                f2immOrigine = datiImm.getInt(4);
                f3immOrigine = datiImm.getInt(5);
                Log.i("immissioniIniziali", "..." + f1immOrigine + "--" + f2immOrigine + "--" + f3immOrigine);
                caricaDatiImmis(datiImm);

            }
            if (datiProd.getCount() > 0) {

                datiProd.moveToPosition(0);
                f1prodOrigine = datiProd.getInt(3);
                f2prodOrigine = datiProd.getInt(4);
                f3prodOrigine = datiProd.getInt(5);
                Log.i("produzione Iniziali", "..." + f1prodOrigine + "--" + f2prodOrigine + "--" + f3prodOrigine);

                caricaDatiProd(datiProd);

            }
            if (datiPrel.getCount() > 0) {

                datiPrel.moveToPosition(0);
                f1prelOrigine = datiPrel.getInt(3);
                f2prelOrigine = datiPrel.getInt(4);
                f3prelOrigine = datiPrel.getInt(5);
                Log.i("prelieiv Iniziali", "..." + f1prelOrigine + "--" + f2prelOrigine + "--" + f3prelOrigine);

                caricaDatiPrel(datiPrel);
            }

        } catch (SQLException ex) {
            Toast.makeText(getActivity(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
        dbLayer.close();

        scriviDati(v);
    }


    private void caricaDatiProd(Cursor datiProd) {
        int f1prodF=0;
        int f2prodF=0;
        int f3prodF=0;

        datiProd.moveToPosition(0);
        do {
            f1prodF=datiProd.getInt(3);
            f2prodF=datiProd.getInt(4);
            f3prodF=datiProd.getInt(5);
            Log.i("prodFin", "..."+f1prodF+"--"+f2prodF+"--"+f3prodF);

        }while (datiProd.moveToNext());
        f1proddato=f1prodF-f1prodOrigine;
        f2proddato=f2prodF-f2prodOrigine;
        f3proddato=f3prodF-f3prodOrigine;
    }
    private void caricaDatiPrel(Cursor datiPrel) {
        int f1prelF=0;
        int f2prelF=0;
        int f3prelF=0;

        datiPrel.moveToPosition(0);
        do {
            f1prelF=datiPrel.getInt(3);
            f2prelF=datiPrel.getInt(4);
            f3prelF=datiPrel.getInt(5);
            Log.i("prelFin", "..."+f1prelF+"--"+f2prelF+"--"+f3prelF);

        }while (datiPrel.moveToNext());

        f1preldato=f1prelF-f1prelOrigine;
        f2preldato=f2prelF-f2prelOrigine;
        f3preldato=f3prelF-f3prelOrigine;
    }
    private void caricaDatiImmis(Cursor datiImm) {
        int f1immF=0;
        int f2immF=0;
        int f3immF=0;

        datiImm.moveToPosition(0);
        do {
            f1immF=datiImm.getInt(3);
            f2immF=datiImm.getInt(4);
            f3immF=datiImm.getInt(5);
            Log.i("immFin", "..."+f1immF+"--"+f2immF+"--"+f3immF);

        }while (datiImm.moveToNext());

        f1immdato=f1immF-f1immOrigine;
        f2immdato=f2immF-f2immOrigine;
        f3immdato=f3immF-f3immOrigine;
    }


    private void scriviDati(View view){

        TextView f1pd= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf1prod);f1pd.setText(HomeActivityNav.numberFormat.format(f1proddato));
        TextView f2pd= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf2prod);f2pd.setText(HomeActivityNav.numberFormat.format(f2proddato));
        TextView f3pd= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf3prod);f3pd.setText(HomeActivityNav.numberFormat.format(f3proddato));
        proddatotot=f1proddato+f2proddato+f3proddato;
        TextView totpd= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.reptotprod);totpd.setText("Kw "+HomeActivityNav.numberFormat.format(proddatotot));
        TextView incf1= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.incf1prod);incf1.setText(HomeActivityNav.numberFormat.format(f1proddato*0.41));
        TextView incf2= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.incf2prod);incf2.setText(HomeActivityNav.numberFormat.format(f2proddato*0.41));
        TextView incf3= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.incf3prod);incf3.setText(HomeActivityNav.numberFormat.format(f3proddato*0.41));
        TextView incTot= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.inctotprod);incTot.setText(HomeActivityNav.moneyFormat.format(proddatotot*0.41));
        TextView co2f1= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.co2f1);co2f1.setText(HomeActivityNav.numberFormat.format(f1proddato*0.531));
        TextView co2f2= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.co2f2);co2f2.setText(HomeActivityNav.numberFormat.format(f2proddato*0.531));
        TextView co2f3= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.co2f3);co2f3.setText(HomeActivityNav.numberFormat.format(f3proddato*0.531));
        TextView co2Tot= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.co2tot);co2Tot.setText("Kg "+(HomeActivityNav.numberFormat.format(proddatotot*0.531)));


        TextView f1im= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf1imm);f1im.setText(HomeActivityNav.numberFormat.format(f1immdato));
        TextView f2im= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf2imm);f2im.setText(HomeActivityNav.numberFormat.format(f2immdato));
        TextView f3im= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf3imm);f3im.setText(HomeActivityNav.numberFormat.format(f3immdato));
        immdatotot=f1immdato+f2immdato+f3immdato;
        TextView totim= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.reptotimm);totim.setText("kw "+HomeActivityNav.numberFormat.format(immdatotot));

        TextView f1pl= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf1prel);f1pl.setText(HomeActivityNav.numberFormat.format(f1preldato));
        TextView f2pl= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf2prel);f2pl.setText(HomeActivityNav.numberFormat.format(f2preldato));
        TextView f3pl= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf3prel);f3pl.setText(HomeActivityNav.numberFormat.format(f3preldato));
        preldatotot=f1preldato+f2preldato+f3preldato;
        TextView totpl= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.reptotprel);totpl.setText("kw "+HomeActivityNav.numberFormat.format(preldatotot));

        f1autodato=f1proddato-f1immdato;
        f2autodato=f2proddato-f2immdato;
        f3autodato=f3proddato-f3immdato;
        autdatotot=f1autodato+f2autodato+f3autodato;

        TextView f1at= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf1autoc);f1at.setText(HomeActivityNav.numberFormat.format(f1autodato));
        TextView f2at= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf2autoc);f2at.setText(HomeActivityNav.numberFormat.format(f2autodato));
        TextView f3at= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf3autoc);f3at.setText(HomeActivityNav.numberFormat.format(f3autodato));
        TextView totat= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.reptotautoc);totat.setText("kw "+HomeActivityNav.numberFormat.format(autdatotot));



        f1consdato=f1autodato+f1preldato;
        f2consdato=f2autodato+f2preldato;
        f3consdato=f2autodato+f2preldato;
        consdatotot=f1consdato+f2consdato+f3consdato;

        TextView f1cs= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf1cons);f1cs.setText(HomeActivityNav.numberFormat.format(f1consdato));
        TextView f2cs= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf2cons);f2cs.setText(HomeActivityNav.numberFormat.format(f2consdato));
        TextView f3cs= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.repf3cons);f3cs.setText(HomeActivityNav.numberFormat.format(f3consdato));
        TextView totcs= (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.reptotcons);totcs.setText("kw "+HomeActivityNav.numberFormat.format(consdatotot));

/*    int f1prod_imm = 0;
    int f2prod_imm = 0;
    int f3prod_imm = 0;
    int totprod_imm = 0;
    */try {
            f1prod_immdato = ((float)f1immdato / (float  )f1proddato) * 100;
            f2prod_immdato= ((float )f2immdato / (float  )f2proddato) * 100;
            f3prod_immdato = ((float )f3immdato / (float )f3proddato) * 100;
            prod_immdatotot = ((float )immdatotot / (float )proddatotot) * 100;
        }catch (Exception e){

        }

        /*



        TextView f1pd_im = (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf1imm_prod);
        f1pd_im.setText((String.format("%.02f", f1prod_immdato ))+ "%");
        TextView f2pd_im = (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf2imm_prod);
        f2pd_im.setText((String.format("%.02f", f2prod_immdato ))+ "%");
        TextView f3pd_im = (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf3imm_prod);
        f3pd_im.setText((String.format("%.02f", f3prod_immdato)) + "%");
        TextView totpd_im = (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.reptotimm_prod);
        totpd_im.setText((String.format("%.02f", prod_immdatotot ))+ "%");
*/
/*    int f1prod_aut = 0;
    int f2prod_aut = 0;
    int f3prod_aut = 0;
    int totprod_aut = 0;
  */  try {
            f1prod_autdato = ((float)f1autodato / (float)f1proddato) * 100;
            f2prod_autdato = ((float)f2autodato / (float)f2proddato) * 100;
            f3prod_autdato = ((float)f3autodato / (float)f3proddato) * 100;
            prod_autdatotot = ((float)autdatotot / (float)proddatotot) * 100;
        }catch (Exception e){

        }
/*        TextView f1pd_au= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf1aut_prod);
        f1pd_au.setText((String.format("%.02f",f1prod_autdato))+"%");
        TextView f2pd_au= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf2aut_prod);
        f2pd_au.setText((String.format("%.02f",f2prod_autdato))+"%");
        TextView f3pd_au= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf3aut_prod);
        f3pd_au.setText((String.format("%.02f",f3prod_autdato))+"%");
        TextView totpd_au= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.reptotaut_prod);
        totpd_au.setText((String.format("%.02f",prod_autdatotot))+"%");
*/
/*    int f1cons_pre = 0;
    int f2cons_pre = 0;
    int f3cons_pre = 0;
    int totcons_pre = 0;
   */

        float Ydata[] = {prod_immdatotot, prod_autdatotot};
        String Xdata[] = {"Immissioni", "Autoconsumo"};
        ArrayList<Entry> Yvals = new ArrayList<>();
        for (int i = 0; i <= Ydata.length - 1; i++) {

            Yvals.add(new Entry((int) Ydata[i], i));
        }
        ArrayList<String> Xvals = new ArrayList<>();
        for (int i = 0; i <= Xdata.length - 1; i++) {

            Xvals.add(Xdata[i]);
        }

        PieDataSet dataset = new PieDataSet(Yvals, "PRODUZIONE " + HomeActivityNav.numberFormat.format(proddatotot));
        dataset.setValueFormatter(new PercentFormatter());
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#85be41"));
        colors.add(Color.parseColor("#d9e021"));
        dataset.setColors(colors);

        PieData data = new PieData(Xvals, dataset);
        data.setValueTextSize(10);//Formatter(new PercentFormatter());

        PieChart chart = (PieChart) view.findViewById(R.id.piechart1);
        chart.setData(data);
        chart.setDescription("# Impiego produzione ");//chart.setCenterText("impiego produzione");
        //chart.setBackground(getResources().getDrawable(R.drawable.sfodo_rendimenti1));
        chart.animateX(3000);



try {
            f1cons_preldato = ((float)f1preldato / (float)f1consdato) * 100;
            f2cons_preldato = ((float)f2preldato / (float)f2consdato) * 100;
            f3cons_preldato = ((float)f3preldato / (float)f3consdato)* 100;
            cons_preldatotot = ((float)preldatotot /(float)consdatotot) * 100;
        }catch (Exception e){

        }
/*        TextView f1cs_pl= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf1prel_con);
        f1cs_pl.setText((String.format("%.02f",f1cons_preldato))+"%");
        TextView f2cs_pl= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf2prel_con);
        f2cs_pl.setText((String.format("%.02f",f2cons_preldato))+"%");
        TextView f3cs_pl= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf3prel_con);
        f3cs_pl.setText((String.format("%.02f",f3cons_preldato))+"%");
        TextView totcs_pl= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.reptotprel_con);
        totcs_pl.setText((String.format("%.02f",cons_preldatotot))+"%");
*/
/*    int f1cons_aut = 0;
    int f3cons_aut = 0;
    int f2cons_aut = 0;
    int totcons_aut = 0;

  */      try {
            f1cons_autdato = ((float)f1autodato / (float)f1consdato)* 100;
            f2cons_autdato = ((float)f2autodato / (float)f2consdato)* 100;
            f3cons_autdato = ((float)f3autodato / (float)f3consdato)* 100;
            cons_autdatotot= ((float)autdatotot / (float)consdatotot)* 100;
        }catch (Exception e){

        }
  /*      TextView f1cs_au= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf1aut_con);
        f1cs_au.setText((String.format("%.02f",f1cons_autdato))+"%");
        TextView f2cs_au= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf2aut_con);
        f2cs_au.setText((String.format("%.02f",f2cons_autdato))+"%");
        TextView f3cs_au= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.repf3aut_con);
        f3cs_au.setText((String.format("%.02f",f3cons_autdato))+"%");
        TextView totcs_au= (TextView) findViewById(it.android.j940549.fotovoltaico.R.id.reptotaut_con);
        totcs_au.setText((String.format("%.02f",cons_autdatotot))+"%");
*/
        float Ydatac[] = {cons_preldatotot, cons_autdatotot};
        String Xdatac[] = {"Prelieivi", "Autoconsumo"};
        ArrayList<Entry> Yvalsc = new ArrayList<>();
        for (int i = 0; i <= Ydatac.length - 1; i++) {

            Yvalsc.add(new Entry((int) Ydatac[i], i));
        }
        ArrayList<String> Xvalsc = new ArrayList<>();
        for (int i = 0; i <= Xdatac.length - 1; i++) {

            Xvalsc.add(Xdatac[i]);
        }

        PieDataSet datasetc = new PieDataSet(Yvalsc, "CONSUMI TOTALI " + HomeActivityNav.numberFormat.format(consdatotot));
        datasetc.setValueFormatter(new PercentFormatter());
        //datasetc.setValueTextSize(5);
        ArrayList<Integer> colorsc = new ArrayList<>();
        colorsc.add(Color.RED);
        colorsc.add(Color.parseColor("#d9e021"));
        datasetc.setColors(colorsc);

        PieData datac = new PieData(Xvalsc, datasetc);
        datac.setValueTextSize(10);//Formatter(new PercentFormatter());

        PieChart chartc = (PieChart) view.findViewById(R.id.piechart2);
        chartc.setData(datac);
        chartc.setDescription("# Composizione consumi");
        //chartc.setHoleColor(Color.BLACK);
        //chartc.setCenterText("composizione consumi");
        //chartc.setBackground(getResources().getDrawable(R.drawable.sfodo_rendimenti1));
        chartc.animateX(3000);

    }

    /*public void vaiaReport(View v){
        Intent vaiReport=new Intent(this, ReportFragment.class);
        startActivity(vaiReport);
    finish();

    }
/*    public void vaiaRendimenti(View v){
        Intent vaiRendimenti=new Intent(this, RendimentiActivity.class);
        startActivity(vaiRendimenti);


    }*/

   /* public void vaiaAnalisiDati(View v){
        Calendar c=Calendar.getInstance();
        String anno1=""+(c.get(Calendar.YEAR)-1);
        String anno2=""+c.get(Calendar.YEAR);
        Intent vaiaAnalisi=new Intent(this, AnalisiDatiActivitX.class);
        vaiaAnalisi.putExtra("anno1",anno1);
        vaiaAnalisi.putExtra("anno2",anno2);
        startActivity(vaiaAnalisi);
        finish();

    }
    public void vaiaDatiLetture(View v){
        String letturaXX=""+v.getId();
        Intent vaiaLettureTot =new Intent(this, DatiLettureActivity.class);
        vaiaLettureTot.putExtra("letturaXX",letturaXX);
        startActivity(vaiaLettureTot);
        finish();

    }
    public void vaiaSP(View v){
        Intent vaiaSP=new Intent(this, BilancioSPActivity.class);
        startActivity(vaiaSP);
        finish();

    }*/

}

