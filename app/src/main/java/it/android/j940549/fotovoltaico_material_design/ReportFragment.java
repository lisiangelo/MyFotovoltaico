package it.android.j940549.fotovoltaico_material_design;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import it.android.j940549.fotovoltaico_material_design.SQLite.DBLayer;

import static it.android.j940549.fotovoltaico_material_design.HomeActivityNav.numberFormat;

public class ReportFragment extends Fragment {

    public static int f1ProdUltima;public static int f2ProdUltima;public static int f3ProdUltima;
    public static int f1PrelUltima;public static int f2PrelUltima;public static int f3PrelUltima;
    public static int f1ImmUltima;public static int f2ImmUltima;public static int f3ImmUltima;
    public static long dataUltima;
    int f1prodTot=0;int f2prodTot=0;int f3prodTot=0;int prodTot=0;
    int f1prelTot=0;int f2prelTot=0;int f3prelTot=0;int prelTot=0;
    int f1immTot=0;int f2immTot=0;int f3immTot=0;int immTot=0;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();

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
        View view=inflater.inflate(R.layout.fragment_report, parent, false);
        View view1=view.findViewById(R.id.datiProduzione);
        View view2=view.findViewById(R.id.letturePrelievi);
        View view3=view.findViewById(R.id.lettureImmissione);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiaDatiLetture(v);
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiaDatiLetture(v);
            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiaDatiLetture(v);
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

        caricaDati(view);

    }


    public void vaiaDatiLetture(View v){
        Intent vaidatiletture=new Intent(getActivity(), DatiLettureActivity.class);
        startActivity(vaidatiletture);

    }


    public void caricaDati(View view) {

        DBLayer dbLayer = null;

        Cursor datiProd = null;
        Cursor datiImm = null;
        Cursor datiPrel = null;

        try {
            dbLayer = new DBLayer(getContext());
            dbLayer.open();
            datiProd = dbLayer.getProduzioneTot(HomeActivityNav.NOMEIMPIANTO);
            datiImm = dbLayer.getImmissioniTot(HomeActivityNav.NOMEIMPIANTO);
            datiPrel = dbLayer.getPrelieviTot(HomeActivityNav.NOMEIMPIANTO);

            if (datiImm.getCount() > 0) {
                datiImm.moveToLast();
                dataUltima=datiImm.getInt(2);
                f1immTot = datiImm.getInt(3);
                f2immTot = datiImm.getInt(4);
                f3immTot = datiImm.getInt(5);
                Log.i("immissionitot", "..." + f1immTot + "--" + f2immTot + "--" + f3immTot);
                f1ImmUltima=f1immTot;f2ImmUltima=f2immTot;f3ImmUltima=f3immTot;
                immTot = f1immTot + f2immTot + f3immTot;
                TextView datoim = (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.datoImmissioni);

                datoim.setText("KW " + numberFormat.format(immTot) );

            }
            if (datiProd.getCount() > 0) {
                datiProd.moveToLast();
                f1prodTot = datiProd.getInt(3);
                f2prodTot = datiProd.getInt(4);
                f3prodTot = datiProd.getInt(5);
                Log.i("produzione Tot", "..." + f1prodTot + "--" + f2prodTot + "--" + f3prodTot);
                f1ProdUltima=f1prodTot;f2ProdUltima=f2prodTot;f3ProdUltima=f3prodTot;
                prodTot = f1prodTot + f2prodTot + f3prodTot;
                TextView datopd = (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.datoProduzione);
                TextView datoinc = (TextView) view.findViewById(R.id.datoIncentivo);
                TextView datoco2 = (TextView) view.findViewById(R.id.datoEmissioni);

                datopd.setText("KW " + numberFormat.format(prodTot));


                datoinc.setText("Incentivi per "+ HomeActivityNav.moneyFormat.format(prodTot*HomeActivityNav.INCENTIVO));
                datoco2.setText("CO2 risparmiata " + HomeActivityNav.numberFormat.format(prodTot*0.531)+" Kg");

            }
            if (datiPrel.getCount() > 0) {
                datiPrel.moveToLast();
                f1prelTot = datiPrel.getInt(3);
                f2prelTot = datiPrel.getInt(4);
                f3prelTot = datiPrel.getInt(5);

                Log.i("prelieiv Iniziali", "..." + f1prelTot + "--" + f2prelTot + "--" + f3prelTot);
                prelTot = f1prelTot + f2prelTot + f3prelTot;
                f1PrelUltima=f1prelTot;f2PrelUltima=f2prelTot;f3PrelUltima=f3prelTot;
                TextView datopl = (TextView) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.datoPrelievo);
                datopl.setText("KW " + numberFormat.format(prelTot) );
            }
            int autocTot = prodTot - immTot;
           // TextView datoaut = (TextView) view.findViewById(it.android.j940549.fotovoltaico.R.id.datoAutoconsumo);
            //datoaut.setText("" + autocTot + "KW");
      float prod_immdatotot=0;
            try {
                prod_immdatotot = ((float )immTot / (float )prodTot) * 100;
            }catch (Exception e){

            }
            float prod_autdatotot =0;

          try {
                prod_autdatotot = ((float)autocTot / (float)prodTot) * 100;
            }catch (Exception e){

            }

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

            PieDataSet dataset = new PieDataSet(Yvals, "PRODUZIONE " + numberFormat.format(prodTot));
            dataset.setValueFormatter(new PercentFormatter());
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#85be41"));
            colors.add(Color.parseColor("#d9e021"));
            dataset.setColors(colors);

            PieData data = new PieData(Xvals, dataset);
            data.setValueTextSize(10);//Formatter(new PercentFormatter());

            PieChart chart = (PieChart) view.findViewById(R.id.piechart1);
            chart.setData(data);
            chart.setDescription("");
            //chart.setMaxAngle(180f);
            //chart.setRotationAngle(180f);
           // chart.setPadding(0,0,0,0);
            //chart.setBackground(getResources().getDrawable(R.drawable.sfodo_rendimenti1));
            chart.animateX(3000);



        } catch (SQLException ex) {
            Toast.makeText(getContext(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
        dbLayer.close();

    }


}


