package it.android.j940549.fotovoltaico_material_design.fragmentAnalisi;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

import it.android.j940549.fotovoltaico_material_design.Model.ConvertiData;
import it.android.j940549.fotovoltaico_material_design.Model.Dato_Tot_Tabella;
import it.android.j940549.fotovoltaico_material_design.R;

/**
 * Created by J940549 on 24/05/2017.
 */

public class FragmentImmissioni extends Fragment {
    Spinner anno1;
    Spinner anno2;
    LineChart chart;
    LineData data;
    TableLayout tableLayoutImm_prec;
    TableLayout tableLayoutImm_succ;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = null;

        //String annorife= (String) annoSpinner.getSelectedItem().toString();
        rootView = inflater.inflate(R.layout.fragment_analisi_dati, container, false);
        TextView titolo = (TextView) rootView.findViewById(R.id.titoloAnalisi);
        titolo.setText("Analisi e confronto Immissioni");

        String anno1 = getArguments().getString("ANNO1");
        String anno2 = getArguments().getString("ANNO2");

        chart = (LineChart) rootView.findViewById(R.id.linechart);
        chart.setDescription("# dati Immissioni");
        chart.setBackground(getResources().getDrawable(R.drawable.sfondo_tabella));

        tableLayoutImm_prec = (TableLayout) rootView.findViewById(R.id.table_anno_precedente);
        tableLayoutImm_prec.setStretchAllColumns(true);
        tableLayoutImm_prec.bringToFront();
        //tableLayoutImm_prec.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));
        //tableLayoutProd_prec.setLayoutParams(new TableLayout.LayoutParams(width/2,height));


        tableLayoutImm_succ = (TableLayout) rootView.findViewById(R.id.table_anno_successivo);
        tableLayoutImm_succ.setStretchAllColumns(true);
        tableLayoutImm_succ.bringToFront();
        //tableLayoutImm_succ.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));


        new AsyncImmissioni(this).execute(anno1,anno2);

        setRetainInstance(true);


        return rootView;

    }

    public void populateResult(LineData data, ArrayList<Dato_Tot_Tabella> datianno_prec, ArrayList<Dato_Tot_Tabella> datianno_succ) {
        this.data = data;
        chart.setData(data);
        chart.animateX(3000);

        for (int i=0; i<=datianno_prec.size()-2; i++) {
            String data1=datianno_prec.get(i+1).getData_();
            int valore1=Integer.parseInt(datianno_prec.get(i+1).getTot_());
            int valore0=Integer.parseInt(datianno_prec.get(i).getTot_());
            int valore=valore1-valore0;
            Log.i("valori",""+valore1+".."+valore0+".."+valore);
            TableRow rowdato = new TableRow(getContext());
            rowdato.setGravity(Gravity.CENTER);
            TextView data_ = new TextView(getContext());
            data_.setText(new ConvertiData().long_data(Integer.parseInt(data1)));
            TextView kwTot = new TextView(getContext());
            kwTot.setText(""+valore);//kwTot.setTextSize(25);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                kwTot.setBackground(getResources().getDrawable(R.color.ocra));
            }
            kwTot.setGravity(Gravity.CENTER);
            // kwTot.setWidth(150);

            rowdato.addView(data_);
            rowdato.addView(kwTot);

            tableLayoutImm_prec.addView(rowdato);
        }
        for (int i=0; i<=datianno_succ.size()-2; i++) {
            String data1=datianno_succ.get(i+1).getData_();
            int valore1=Integer.parseInt(datianno_succ.get(i+1).getTot_());
            int valore0=Integer.parseInt(datianno_succ.get(i).getTot_());
            int valore=valore1-valore0;
            Log.i("valori",""+valore1+".."+valore0+".."+valore);

            TableRow rowdatos = new TableRow(getContext());
            rowdatos.setGravity(Gravity.CENTER);
            TextView datas = new TextView(getContext());
            datas.setText(new ConvertiData().long_data(Integer.parseInt(data1)));
             TextView kwTots = new TextView(getContext());
            kwTots.setText(""+valore);//kwTot.setTextSize(25);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                kwTots.setBackground(getResources().getDrawable(R.color.orange));
            }
            kwTots.setGravity(Gravity.CENTER);
            // kwTots.setWidth(150);

            rowdatos.addView(datas);
            rowdatos.addView(kwTots);

            tableLayoutImm_succ.addView(rowdatos);

        }

    }
}