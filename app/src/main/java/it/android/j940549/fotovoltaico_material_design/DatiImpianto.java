package it.android.j940549.fotovoltaico_material_design;

import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import it.android.j940549.fotovoltaico_material_design.Model.ConvertiData;
import it.android.j940549.fotovoltaico_material_design.SQLite.DBLayerComuni;

public class DatiImpianto extends AppCompatActivity {
    public String NOMEIMPIANTO = "mio";
    public String DIMENSIONE = "";
    public float INCENTIVO = (float) 0.41;
    public int ANNOATTIVAZIONE = 2012;
    Spinner meseAttSpinner, annoAttSpinner, regioneSpinner, provinciaSpinner, comuneSpinner;
    ArrayAdapter meseAdapter, annoAdapter, regioneAdapter, provinciaAdapter, comuneAdapter;
    ArrayList<String> regioni=new ArrayList<String>();
    ArrayList<String> province=new ArrayList<String>();
    ArrayList<String> comuni=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dati_impianto);
        String[] mesi = {"Gen", "Feb", "Mar", "Apr", "Mag", "Giu",
                "Lug", "Ago", "Set", "Ott", "Nov", "Dic"};
        Calendar c = Calendar.getInstance();
        int correnteAnno = c.get(Calendar.YEAR);
        ArrayList anni = new ArrayList();
        for (int i = 2000; i <= correnteAnno; i++) {
            anni.add("" + i);
        }
        province.add("scegli prima la Regione");
        comuni.add("poi scegli la Provincia ");
        meseAttSpinner = (Spinner) findViewById(R.id.txtmeseAttiv);
        meseAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mesi);
        meseAttSpinner.setAdapter(meseAdapter);
        annoAttSpinner = (Spinner) findViewById(R.id.txtannoAttiv);
        annoAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, anni);
        annoAttSpinner.setAdapter(annoAdapter);
        setupSpinner(meseAttSpinner, annoAttSpinner, meseAdapter, annoAdapter);
        setUpAdpterRegione();
        regioneSpinner = (Spinner) findViewById(R.id.txtRegione);
        regioneAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, regioni);
        regioneSpinner.setAdapter(regioneAdapter);
        regioneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String id_nomeregione = regioneSpinner.getSelectedItem().toString();
                if (!id_nomeregione.contains("scegli")) {
                    String id_nome []= id_nomeregione.split("-");
                    String idregione=id_nome[0].trim();
                    setUpAdpterProvince(idregione);
                    provinciaSpinner.setAdapter(provinciaAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        provinciaSpinner = (Spinner) findViewById(R.id.txtProvincia);
        provinciaAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, province);
        provinciaSpinner.setAdapter(provinciaAdapter);
        provinciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nome_siglaprovincia = provinciaSpinner.getSelectedItem().toString();
                if (!nome_siglaprovincia.contains("scegli")) {
                    String nome_sg []= nome_siglaprovincia.split("-");
                    String sgprov=nome_sg[1].trim();
                    setUpAdpterComuni(sgprov);
                    comuneSpinner.setAdapter(comuneAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        comuneSpinner = (Spinner) findViewById(R.id.txtComune);
        comuneAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, comuni);
        comuneSpinner.setAdapter(comuneAdapter);

    }

    private void setupSpinner(Spinner annoSpinner, Spinner meseSpinner,
                              ArrayAdapter annoAdapter, ArrayAdapter meseAdapter) {


        Calendar c = Calendar.getInstance();
        int mese = c.get(Calendar.MONTH);
        int anno = c.get(Calendar.YEAR);
        String meseIF = "";
        String annoIF = "";

        if (mese == 0) {
            meseIF = "Dic";
            annoIF = "" + (anno - 1);
        } else {
            meseIF = new ConvertiData().getMese_int_to_Str(mese - 1);
            annoIF = "" + anno;
        }
        int meseIFpos = meseAdapter.getPosition(meseIF);
        meseSpinner.setSelection(meseIFpos);
        Log.i("mesi", meseIFpos + "..." + annoIF);
        int annoIFpos = annoAdapter.getPosition(annoIF);
        annoSpinner.setSelection(annoIFpos);

    }

    private void setUpAdpterRegione() {


        DBLayerComuni dbLayer = null;

        Cursor datiProv = null;
        Cursor datiCom = null;
        Cursor datiReg = null;

        try {
            dbLayer = new DBLayerComuni(this);
            dbLayer.open();
            datiCom = dbLayer.getComuni();
            if (datiCom.getCount() == 0) {
                dbLayer.caricaDati();
            }
            datiReg = dbLayer.getRegioni();

            if (datiReg.getCount() > 0) {
                datiReg.moveToFirst();
                regioni.clear();
                regioni.add("scegli una Regione");
                do {
                    regioni.add("" + datiReg.getInt(0) + " - " + datiReg.getString(2));
                } while (datiReg.moveToNext());

            }

        } catch (SQLException ex) {
            Toast.makeText(this, "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
        dbLayer.close();

    }


    private void setUpAdpterProvince(String idReg) {


        DBLayerComuni dbLayer = null;

        Cursor datiProv = null;
        Cursor datiCom = null;
        //Cursor datiReg = null;

        try {
            dbLayer = new DBLayerComuni(this);
            dbLayer.open();
            datiCom = dbLayer.getComuni();
            if (datiCom.getCount() == 0) {
                dbLayer.caricaDati();
            }
            datiProv = dbLayer.getprovincia(idReg);

            if (datiProv.getCount() > 0) {
                datiProv.moveToFirst();
                province.clear();
                province.add("scegli una Provincia");
                do {
                    province.add("" + datiProv.getString(2) + " - " + datiProv.getString(3));
                } while (datiProv.moveToNext());

            }

        } catch (SQLException ex) {
            Toast.makeText(this, "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
        dbLayer.close();

    }

    private void setUpAdpterComuni(String sgProv) {


        DBLayerComuni dbLayer = null;

        //Cursor datiProv = null;
        Cursor datiCom = null;
        Cursor datiCom_pr = null;

        try {
            dbLayer = new DBLayerComuni(this);
            dbLayer.open();
            datiCom = dbLayer.getComuni();
            if (datiCom.getCount() == 0) {
                dbLayer.caricaDati();
            }
            datiCom_pr = dbLayer.getComuni_provincia(sgProv);

            if (datiCom_pr.getCount() > 0) {
                datiCom_pr.moveToFirst();
                comuni.clear();
                comuni.add("scegli un Comune");
                do {
                    comuni.add(datiCom_pr.getString(2) );
                } while (datiCom_pr.moveToNext());

            }

        } catch (SQLException ex) {
            Toast.makeText(this, "" + ex.toString(), Toast.LENGTH_SHORT).show();
        }
        dbLayer.close();

    }

}
