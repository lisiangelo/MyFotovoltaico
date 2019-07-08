package it.android.j940549.fotovoltaico_material_design;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import it.android.j940549.fotovoltaico_material_design.Model.ConvertiData;
import it.android.j940549.fotovoltaico_material_design.SQLite.DBLayer;

public class NewLetturaActivity extends AppCompatActivity {
    Spinner meseSpinner;
    Spinner annoSpinner;
    ArrayAdapter meseAdapter;
    ArrayAdapter annoAdapter;
    EditText f1_prod, f2_prod, f3_prod, f1_imm, f2_imm, f3_imm, f1_prel, f2_prel, f3_prel;
    Spinner mese, anno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it.android.j940549.fotovoltaico_material_design.R.layout.activity_new_lettura);
        String[] mesi = {"Gen", "Feb", "Mar", "Apr", "Mag", "Giu",
                "Lug", "Ago", "Set", "Ott", "Nov", "Dic"};
        meseSpinner = (Spinner) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.mese);
        meseAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mesi);
        meseSpinner.setAdapter(meseAdapter);

        annoSpinner = (Spinner) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.anno);
        annoAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, HomeActivityNav.ANNI);
        annoSpinner.setAdapter(annoAdapter);
    setupSpinner();

        }

        public void setupSpinner(){
            Calendar c= Calendar.getInstance();
            int mese=c.get(Calendar.MONTH);
            int anno=c.get(Calendar.YEAR);
            String meseIF="";
            String annoIF="";

            if(mese==0){
                meseIF="Dicembre";
                annoIF=""+(anno-1);
            }else {
                meseIF=new ConvertiData().getMese_int_to_Str(mese-1);
                annoIF=""+anno;
            }
            int meseIFpos=meseAdapter.getPosition(meseIF);
            meseSpinner.setSelection(meseIFpos);
            int annoIFpos=annoAdapter.getPosition(annoIF);
            annoSpinner.setSelection(annoIFpos);

        }

    public void inserisciLetture(View v) {

        String meseInser = (String) meseSpinner.getSelectedItem();
        String annoInser = (String) annoSpinner.getSelectedItem();

        f1_prod = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f1prod);
        String f1prod = f1_prod.getText().toString();
        f2_prod = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f2prod);
        String f2prod = f2_prod.getText().toString();
        f3_prod = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f3prod);
        String f3prod = f3_prod.getText().toString();

        f1_imm = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f1immis);
        String f1imm = f1_imm.getText().toString();
        f2_imm = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f2immis);
        String f2imm = f2_imm.getText().toString();
        f3_imm = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f3immis);
        String f3imm = f3_imm.getText().toString();

        f1_prel = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f1prel);
        String f1prel = f1_prel.getText().toString();
        f2_prel = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f2prel);
        String f2prel = f2_prel.getText().toString();
        f3_prel = (EditText) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.f3prel);
        String f3prel = f3_prel.getText().toString();

        if (f1prod.equals("") || f2prod.equals("") || f3prod.equals("") ||
                f1prel.equals("") || f2prel.equals("") || f3prel.equals("") ||
                f1imm.equals("") || f2imm.equals("") || f3imm.equals("")) {
            Toast.makeText(this, "Dati incompleti", Toast.LENGTH_SHORT).show();
        } else {
           /* if (Integer.parseInt(f1prod) < ReportFragment.f1ProdUltima || Integer.parseInt(f2prod) < ReportFragment.f2ProdUltima || Integer.parseInt(f3prod) < ReportFragment.f3ProdUltima ||
                    Integer.parseInt(f1prel) < ReportFragment.f1PrelUltima || Integer.parseInt(f2prel) < ReportFragment.f2PrelUltima || Integer.parseInt(f3prel) < ReportFragment.f3PrelUltima ||
                    Integer.parseInt(f1imm) < ReportFragment.f1ImmUltima || Integer.parseInt(f2imm) < ReportFragment.f2ImmUltima || Integer.parseInt(f3imm) < ReportFragment.f3ImmUltima ) {
                Toast.makeText(this, "Controlla i Dati \n letture inferiori all'ultima inserita", Toast.LENGTH_SHORT).show();
            } else {
*/
            DBLayer dbLayer = null;
            boolean inserProd = false;
            boolean inserImm = false;
            boolean inserPrel = false;

            try {
                dbLayer = new DBLayer(this);
                dbLayer.open();
                inserProd = dbLayer.newLettProduzione(HomeActivityNav.NOMEIMPIANTO, meseInser, annoInser, f1prod, f2prod, f3prod);
                inserImm = dbLayer.newLettImmissioni(HomeActivityNav.NOMEIMPIANTO, meseInser, annoInser, f1imm, f2imm, f3imm);
                inserPrel = dbLayer.newLettPrelievi(HomeActivityNav.NOMEIMPIANTO, meseInser, annoInser, f1prel, f2prel, f3prel);

            } catch (SQLException ex) {
                Toast.makeText(this, "" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
            if (inserProd == true && inserImm == true && inserPrel == true) {
                Toast.makeText(this, "dati inseriti correttamente", Toast.LENGTH_SHORT).show();
                finish();
            }


            //}
        }
        Intent ritornaaReport=new Intent(this, HomeActivityNav.class);
        startActivity(ritornaaReport);
        finish();

    }
}
