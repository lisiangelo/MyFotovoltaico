package it.android.j940549.fotovoltaico_material_design;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import it.android.j940549.fotovoltaico_material_design.Model.ConvertiData;
import it.android.j940549.fotovoltaico_material_design.SQLite.DBLayer;


public class DatiLettureActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(it.android.j940549.fotovoltaico_material_design.R.layout.activity_dati_letture);

//        Toolbar toolbar = (Toolbar) findViewById(it.android.j940549.fotovoltaico.R.id.toolbar);
//        setSupportActionBar(toolbar);
  //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(it.android.j940549.fotovoltaico_material_design.R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.icon_produzione);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_prelievi);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_immissione);

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static TableLayout tableLayoutProd;
        private static TableLayout tableLayoutPrel;
        private static TableLayout tableLayoutImmis;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(it.android.j940549.fotovoltaico_material_design.R.layout.fragment_dati_letture_prod, container, false);
                tableLayoutProd = (TableLayout) rootView.findViewById(R.id.lista_prod_precedenti);
                tableLayoutProd.setStretchAllColumns(true);
                tableLayoutProd.bringToFront();

                DBLayer dbLayer = null;

                Cursor datiProd = null;

                try {
                    dbLayer = new DBLayer(rootView.getContext());
                    dbLayer.open();
                    datiProd = dbLayer.getProduzioneTot(HomeActivityNav.NOMEIMPIANTO);
                    Log.i("righe dei datiProd", "" + datiProd.getCount());

                    if (datiProd.getCount() > 0) {
                        datiProd.moveToPosition(0);
                        do {
                            final TableRow rowprod = new TableRow(rootView.getContext());//(TableRow) rootView.findViewById(R.id.row_prod_dati);
                            //rowprod.setGravity(Gravity.CENTER);
                            //rowprod.setBackgroundDrawable(getResources().getDrawable(R.drawable.sfodo_rendimenti1));
                            TextView id_ = new TextView(rootView.getContext());//TextView) rootView.findViewById(R.id.row_prod_dati_id);
                            final int idRow=datiProd.getInt(0);
                            id_.setText(""+idRow);
                            id_.setVisibility(View.GONE);
                            TextView data_ = new TextView((rootView.getContext()));//rootView.findViewById(R.id.row_prod_dati_data)getContext());
                            data_.setText(new ConvertiData().long_data(datiProd.getInt(2)));
                            data_.setWidth(270);data_.setTextSize(17);//data_.setPadding(10,0,0,0);
                            data_.setBackground(getResources().getDrawable(R.color.ocra));
                            TextView f1_ = new TextView(rootView.getContext());
                            f1_.setGravity(Gravity.RIGHT);
                            f1_.setText("" + datiProd.getInt(3));
                            f1_.setWidth(150);f1_.setTextSize(17);
                            TextView f2_ = new TextView(rootView.getContext());
                            f2_.setGravity(Gravity.RIGHT);
                            f2_.setText("" + datiProd.getInt(4));
                            f2_.setWidth(150);f2_.setTextSize(17);
                            TextView f3_ = new TextView(rootView.getContext());
                            f3_.setGravity(Gravity.RIGHT);
                            f3_.setText("" + datiProd.getInt(5));
                            f3_.setWidth(150);f3_.setTextSize(17);
                            rowprod.addView(id_);
                            rowprod.addView(data_);
                            rowprod.addView(f1_);
                            rowprod.addView(f2_);
                            rowprod.addView(f3_);
                            //rowprod.setBackground(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.selector_row_prod));
                            rowprod.setOnLongClickListener(new TableRow.OnLongClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public boolean onLongClick(View v) {

                                    apriPopup(rowprod,idRow,getArguments().getInt(ARG_SECTION_NUMBER) );

                                    return false;
                                }
                            });
                            tableLayoutProd.addView(rowprod);
                        } while (datiProd.moveToNext());
                    }else{
                        TableRow rowprod = new TableRow(rootView.getContext());
                        //rowprod.setGravity(Gravity.CENTER);
                        //rowprod.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));
                        TextView id_ = new TextView(rootView.getContext());
                        id_.setText("");
                        id_.setVisibility(View.GONE);
                        TextView data_ = new TextView(rootView.getContext());
                        data_.setText("");
                        data_.setWidth(270);data_.setTextSize(17);
                        data_.setBackground(getResources().getDrawable(R.color.ocra));
                        TextView f1_ = new TextView(rootView.getContext());
                        f1_.setGravity(Gravity.RIGHT);
                        f1_.setText("");
                        f1_.setWidth(150);f1_.setTextSize(17);
                        TextView f2_ = new TextView(rootView.getContext());
                        f2_.setGravity(Gravity.RIGHT);
                        f2_.setText("");f2_.setTextSize(17);
                        f2_.setWidth(150);
                        TextView f3_ = new TextView(rootView.getContext());
                        f3_.setGravity(Gravity.RIGHT);
                        f3_.setText("");f3_.setTextSize(17);
                        f3_.setWidth(150);
                        rowprod.addView(id_);
                        rowprod.addView(data_);
                        rowprod.addView(f1_);
                        rowprod.addView(f2_);
                        rowprod.addView(f3_);

                        tableLayoutProd.addView(rowprod);

                    }
                    datiProd.close();
                } catch(SQLiteException e){
                    Toast.makeText(rootView.getContext(), "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
                }

                dbLayer.close();

            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(it.android.j940549.fotovoltaico_material_design.R.layout.fragment_dati_letture_immis, container, false);
                tableLayoutImmis = (TableLayout) rootView.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.lista_immis_precedenti);
                tableLayoutImmis.setStretchAllColumns(true);
                tableLayoutImmis.bringToFront();
                //tableLayoutImmis.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));


                DBLayer dbLayer = null;

                Cursor datiImmis= null;

                try {
                    dbLayer = new DBLayer(rootView.getContext());
                    dbLayer.open();
                    datiImmis= dbLayer.getImmissioniTot(HomeActivityNav.NOMEIMPIANTO);
                    Log.i("righe dei datiImmiss", ""+datiImmis.getCount());

                    if(datiImmis.getCount()>0){
                        datiImmis.moveToPosition(0);

                        do {
                            final TableRow rowimm = new TableRow(rootView.getContext());
                          //  rowimm.setGravity(Gravity.CENTER);
                    //        rowimm.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));
                            TextView id_ = new TextView(rootView.getContext());
                            final int idRow=datiImmis.getInt(0);
                            id_.setText(""+idRow);
                            id_.setVisibility(View.GONE);
                            TextView data_ = new TextView(rootView.getContext());
                            data_.setText(new ConvertiData().long_data(datiImmis.getInt(2)));
                            data_.setWidth(270);data_.setTextSize(17);
                            data_.setBackground(getResources().getDrawable(R.color.ocra));
                            TextView f1_ = new TextView(rootView.getContext());
                            f1_.setGravity(Gravity.RIGHT);
                            f1_.setText(""+datiImmis.getInt(3));
                            f1_.setWidth(150);f1_.setTextSize(17);
                            TextView f2_ = new TextView(rootView.getContext());
                            f2_.setGravity(Gravity.RIGHT);
                            f2_.setText(""+datiImmis.getInt(4));
                            f2_.setWidth(150);f2_.setTextSize(17);
                            TextView f3_ = new TextView(rootView.getContext());
                            f3_.setGravity(Gravity.RIGHT);
                            f3_.setText(""+datiImmis.getInt(5));
                            f3_.setWidth(150);f3_.setTextSize(17);
                            rowimm.addView(id_);
                            rowimm.addView(data_);
                            rowimm.addView(f1_);
                            rowimm.addView(f2_);
                            rowimm.addView(f3_);
//                            rowimm.setBackground(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.selector_row_imm_));
                            rowimm.setOnLongClickListener(new TableRow.OnLongClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public boolean onLongClick(View v) {
                                    rowimm.setBackground(getResources().getDrawable(it.android.j940549.fotovoltaico_material_design.R.drawable.sfodo_immissione));
                                    apriPopup(rowimm,idRow,getArguments().getInt(ARG_SECTION_NUMBER) );

                                    return false;
                                }
                            });
                            tableLayoutImmis.addView(rowimm);
                        }while (datiImmis.moveToNext());
                    }else{
                        TableRow rowimm = new TableRow(rootView.getContext());
                       // rowimm.setGravity(Gravity.CENTER);
  //                      rowimm.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));
                        TextView id_ = new TextView(rootView.getContext());
                        id_.setText("");
                        id_.setVisibility(View.GONE);
                        TextView data_ = new TextView(rootView.getContext());
                        data_.setText("");
                        data_.setWidth(270);data_.setTextSize(17);
                        data_.setBackground(getResources().getDrawable(R.color.ocra));
                        TextView f1_ = new TextView(rootView.getContext());
                        f1_.setGravity(Gravity.RIGHT);
                        f1_.setText("");
                        f1_.setWidth(150);f1_.setTextSize(17);
                        TextView f2_ = new TextView(rootView.getContext());
                        f2_.setGravity(Gravity.RIGHT);
                        f2_.setText("");
                        f2_.setWidth(150);f2_.setTextSize(17);
                        TextView f3_ = new TextView(rootView.getContext());
                        f3_.setGravity(Gravity.RIGHT);
                        f3_.setText("");
                        f3_.setWidth(150);f3_.setTextSize(17);
                        rowimm.addView(id_);
                        rowimm.addView(data_);
                        rowimm.addView(f1_);
                        rowimm.addView(f2_);
                        rowimm.addView(f3_);

                        tableLayoutImmis.addView(rowimm);

                    }
                    datiImmis.close();
                } catch (SQLiteException e) {
                    Toast.makeText(rootView.getContext(), "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                dbLayer.close();

            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(it.android.j940549.fotovoltaico_material_design.R.layout.fragment_dati_letture_prel, container, false);

                tableLayoutPrel = (TableLayout) rootView.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.lista_prel_precedenti);
                tableLayoutPrel.setStretchAllColumns(true);
                tableLayoutPrel.bringToFront();
                DBLayer dbLayer = null;

                Cursor datiPrel = null;

                try {
                    dbLayer = new DBLayer(rootView.getContext());
                    dbLayer.open();
                    datiPrel= dbLayer.getPrelieviTot(HomeActivityNav.NOMEIMPIANTO);

                    Log.i("righe dei datiPrel", ""+datiPrel.getCount());

                    if(datiPrel.getCount()>0){
                        datiPrel.moveToPosition(0);
                        do {
                            final TableRow rowprel = new TableRow(rootView.getContext());
                         //   rowprel.setGravity(Gravity.CENTER);
                    //        rowprel.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));
                            TextView id_ = new TextView(rootView.getContext());
                            final int idRow=datiPrel.getInt(0);
                            id_.setText(""+idRow);
                            id_.setVisibility(View.GONE);

                            TextView data_ = new TextView(rootView.getContext());
                            data_.setText(new ConvertiData().long_data(datiPrel.getInt(2)));
                            data_.setWidth(270);data_.setTextSize(17);
                            data_.setBackground(getResources().getDrawable(R.color.ocra));
                            TextView f1_ = new TextView(rootView.getContext());
                            f1_.setGravity(Gravity.RIGHT);
                            f1_.setText(""+datiPrel.getInt(3));
                            f1_.setWidth(150);f1_.setTextSize(17);
                            TextView f2_ = new TextView(rootView.getContext());
                            f2_.setGravity(Gravity.RIGHT);
                            f2_.setText(""+datiPrel.getInt(4));
                            f2_.setWidth(150);f2_.setTextSize(17);
                            TextView f3_ = new TextView(rootView.getContext());
                            f3_.setGravity(Gravity.RIGHT);
                            f3_.setText(""+datiPrel.getInt(5));
                            f3_.setWidth(150);f3_.setTextSize(17);
                            rowprel.addView(id_);
                            rowprel.addView(data_);
                            rowprel.addView(f1_);
                            rowprel.addView(f2_);
                            rowprel.addView(f3_);
//                            rowprel.setBackground(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.selector_row_prel_));
                            rowprel.setOnLongClickListener(new TableRow.OnLongClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public boolean onLongClick(View v) {
                                    rowprel.setBackground(getResources().getDrawable(it.android.j940549.fotovoltaico_material_design.R.drawable.sfodo_prelievo));
                                    apriPopup(rowprel,idRow,getArguments().getInt(ARG_SECTION_NUMBER) );

                                    return false;
                                }
                            });

                            tableLayoutPrel.addView(rowprel);
                        }while (datiPrel.moveToNext());
                    }else{
                        TableRow rowprel = new TableRow(rootView.getContext());
                        //rowprel.setGravity(Gravity.CENTER);
  //                      rowprel.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));
                        TextView id_ = new TextView(rootView.getContext());
                        id_.setText("");
                        id_.setVisibility(View.GONE);
                        TextView data_ = new TextView(rootView.getContext());
                        data_.setText("");
                        data_.setWidth(270);data_.setTextSize(17);
                        data_.setBackground(getResources().getDrawable(R.color.ocra));
                        TextView f1_ = new TextView(rootView.getContext());
                        f1_.setGravity(Gravity.RIGHT);
                        f1_.setText("");
                        f1_.setWidth(150);f1_.setTextSize(17);
                        TextView f2_ = new TextView(rootView.getContext());
                        f2_.setGravity(Gravity.RIGHT);
                        f2_.setText("");
                        f2_.setWidth(150);f2_.setTextSize(17);
                        TextView f3_ = new TextView(rootView.getContext());
                        f3_.setGravity(Gravity.RIGHT);
                        f3_.setText("");
                        f3_.setWidth(150);f3_.setTextSize(17);
                        rowprel.addView(id_);
                        rowprel.addView(data_);
                        rowprel.addView(f1_);
                        rowprel.addView(f2_);
                        rowprel.addView(f3_);

                        tableLayoutPrel.addView(rowprel);

                    }
                    datiPrel.close();
                } catch (SQLiteException e) {
                    Toast.makeText(rootView.getContext(), "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
                }
                dbLayer.close();
            }
            return rootView;
        }
        public void apriPopup(View v, final int idRow, final int argselection){
            final android.support.v7.widget.PopupMenu popupMenu=new android.support.v7.widget.PopupMenu(getContext(),v);
            MenuInflater menuInflater=popupMenu.getMenuInflater();
            menuInflater.inflate(it.android.j940549.fotovoltaico_material_design.R.menu.menu_popup,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId()== it.android.j940549.fotovoltaico_material_design.R.id.delete) {
                        //      Toast.makeText(getContext(), "delete\n"+idRow+".."+argselection, Toast.LENGTH_SHORT).show();
                        DBLayer dbLayer = null;

                        try {
                            dbLayer = new DBLayer(getContext());
                            dbLayer.open();
                            if(argselection==1){
                                dbLayer.deleteDatoProd(idRow);
                            }
                            if(argselection==2){
                                dbLayer.deleteDatoImmis(idRow);
                            }
                            if(argselection==3){
                                dbLayer.deleteDatoPrel(idRow);
                            }

                        } catch (SQLException ex) {
                            Toast.makeText(getContext(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        Toast.makeText(getContext(), "dato cancellato!", Toast.LENGTH_SHORT).show();

                        dbLayer.close();

                        Intent ritornaaLetture = new Intent(getContext(), DatiLettureActivity.class);

                        startActivity(ritornaaLetture);
                        getActivity().finish();

                    }
                    if (item.getItemId()== it.android.j940549.fotovoltaico_material_design.R.id.modifica) {
                        Toast.makeText( getContext(), "modifica\n"+idRow+".."+argselection, Toast.LENGTH_SHORT).show();
                    /*DBLayer dbLayer = null;

                    try {
                        dbLayer = new DBLayer(getBaseContext());
                        dbLayer.open();
                        dbLayer.deleteDataPersona(idApp);
                    } catch (SQLException ex) {
                        Toast.makeText(getBaseContext(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Toast.makeText(getBaseContext(), "dato cancelalto!", Toast.LENGTH_SHORT).show();

                    dbLayer.close();
                    Intent vaiaPersona = new Intent(getBaseContext(), ViewPersonale.class);
                    vaiaPersona.putExtra("user", user);
                    startActivity(vaiaPersona);
                    finish();*/

                    }

                    return true;
                }

            });
            popupMenu.show();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PRODUZIONE";
                case 1:
                    return "IMMISSIONI";
                case 2:
                    return "PRELIEVI";
            }
            return null;
        }
    }

    public void vaiaNewLettura(View v){
        Intent vaiNewLettura=new Intent(this, NewLetturaActivity.class);

        startActivity(vaiNewLettura);
        //finish();

    }
    public void vaiaDatiLetture(View v){
        String letturaXX=""+v.getId();
        Intent vaiaLettureTot =new Intent(this, DatiLettureActivity.class);
        vaiaLettureTot.putExtra("letturaXX",letturaXX);
        startActivity(vaiaLettureTot);

    }



}