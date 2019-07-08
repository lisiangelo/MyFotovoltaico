package it.android.j940549.fotovoltaico_material_design;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.android.j940549.fotovoltaico_material_design.Model.ConvertiData;
import it.android.j940549.fotovoltaico_material_design.SQLite.DBLayer;


public class BilancioFragment extends Fragment {
    public static String annorife;
    Spinner annoSpinner;
    ArrayAdapter annoAdapter;
    ArrayList arrayDatiBollette=new ArrayList<>();
    ArrayList arrayDatiBonifici=new ArrayList<>();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Context context;


    public BilancioFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BilancioFragment newInstance() {
        BilancioFragment fragment = new BilancioFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_bilancio, container, false);
        context=getContext();

        annoSpinner = (Spinner) view.findViewById(R.id.annorifeBilancio);
        annoAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, HomeActivityNav.ANNI);
        annoSpinner.setAdapter(annoAdapter);

        Calendar c=Calendar.getInstance();
        int annopos=annoAdapter.getPosition(""+c.get(Calendar.YEAR));
        annoSpinner.setSelection(annopos);

        annorife=annoSpinner.getSelectedItem().toString();
        /// Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),annorife,arrayDatiBollette,arrayDatiBonifici);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        annoSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayDatiBonifici.removeAll(arrayDatiBonifici);
                arrayDatiBollette.removeAll(arrayDatiBollette);
                caricaDatiBollette(annoSpinner.getSelectedItem().toString());
                caricaDatiBonifici(annoSpinner.getSelectedItem().toString());
                annorife=annoSpinner.getSelectedItem().toString();
                mSectionsPagerAdapter=new SectionsPagerAdapter(getChildFragmentManager(),annorife,arrayDatiBollette,arrayDatiBonifici);
                mViewPager.setAdapter(mSectionsPagerAdapter);//mSectionsPagerAdapter.notifyDataSetChanged();
                //onRestart();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static TableLayout tableDatiBollette;
        private static TableLayout tableDatiBonifico;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String annorife, ArrayList datiboll, ArrayList datibon) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("ANNORIFE",annorife);
            args.putStringArrayList("DATIBOLLETTE",datiboll);
            args.putStringArrayList("DATIBONIFICI",datibon);
            fragment.setArguments(args);
            return fragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = null;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {

                rootView = inflater.inflate(it.android.j940549.fotovoltaico_material_design.R.layout.fragment_ssp_bollette, container, false);
                String [] bimestri = {"Gen-Feb","Mar-Apr","Mag-Giu","Lug-Ago","Set-Ott","Nov-Dic"};
                final Spinner bimestriSpinner= (Spinner) rootView.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.bimestreBollette);
                ArrayAdapter bimestriAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, bimestri);
                bimestriSpinner.setAdapter(bimestriAdapter);
                Button inserisci= (Button) rootView.findViewById(R.id.btninserisciBollette);

                final EditText editImporto= (EditText) rootView.findViewById(R.id.importobolletta);
                //  final View finalRootView = rootView;
                inserisci.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     Log.i("anno",annorife);
                                                     String annoIns=annorife;
                                                     String bimestre = bimestriSpinner.getSelectedItem().toString();
                                                     String importo = editImporto.getText().toString();

                                                     if (importo.equals("")) {
                                                         Toast.makeText(getContext(), "Dati incompleti", Toast.LENGTH_SHORT).show();
                                                     } else {

                                                         DBLayer dbLayer = null;
                                                         boolean inserBoll = false;

                                                         try {
                                                             dbLayer = new DBLayer(getContext());
                                                             dbLayer.open();
                                                             inserBoll = dbLayer.insertBolletta(HomeActivityNav.NOMEIMPIANTO,annoIns
                                                                     , bimestre, importo);

                                                         } catch (SQLException ex) {
                                                             Toast.makeText(getContext(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
                                                         }
                                                         if (inserBoll == true) {
                                                             Toast.makeText(getContext(), "dati inseriti correttamente", Toast.LENGTH_SHORT).show();
                                                             BilancioFragment fragment= BilancioFragment.newInstance();
                                                             FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                             fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                                                             /*Intent ritornaaBilancio = new Intent(getContext(), BilancioSPActivity.class);
                                                             ritornaaBilancio.putExtra("ANNORIFE",annoIns);
                                                             startActivity(ritornaaBilancio);
                                                             getActivity().finish();*/
                                                         }

                                                     }
//                                                     caricaDatiBollette(annorife);
                                                 }


                                             }
                );
                tableDatiBollette = (TableLayout) rootView.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.tabelladatiBolletteSSP);
                tableDatiBollette.setStretchAllColumns(true);
                tableDatiBollette.bringToFront();
//                tableDatiBollette.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.sfodo_spinner));
                TableRow rowTitolo = new TableRow(rootView.getContext());

                rowTitolo.setGravity(Gravity.CENTER);

                //              rowTitolo.setBackgroundDrawable(getResources().getDrawable(R.drawable.sfondo_tabella));
                TextView id = new TextView(rootView.getContext());
                id.setText("id");
                id.setVisibility(View.GONE);
                TextView bimestrelbl = new TextView(rootView.getContext());
                bimestrelbl.setText("BIMESTRE ");
                bimestrelbl.setWidth(270);
                bimestrelbl.setHeight(50);
                //bimestrelbl.setTextSize(13);
                bimestrelbl.setBackground(getResources().getDrawable(R.color.ocra_alfa));
                TextView importolbl = new TextView(rootView.getContext());
                importolbl.setText(" IMPORTO ");
                importolbl.setWidth(150);
                importolbl.setTextSize(13);
                importolbl.setGravity(Gravity.CENTER);
                TextView ivalbl = new TextView(rootView.getContext());
                ivalbl.setText(" I.V.A. ");
                ivalbl.setWidth(150);
                ivalbl.setTextSize(13);
                ivalbl.setGravity(Gravity.CENTER);
                TextView totlbl = new TextView(rootView.getContext());
                totlbl.setText(" TOT. FATT. ");
                totlbl.setWidth(150);
                //totlbl.setTextSize(13);
                totlbl.setGravity(Gravity.CENTER);
                rowTitolo.addView(id);
                rowTitolo.addView(bimestrelbl);
                rowTitolo.addView(importolbl);
                rowTitolo.addView(ivalbl);
                rowTitolo.addView(totlbl);
                tableDatiBollette.addView(rowTitolo);
                ArrayList arrayDatiBollette=getArguments().getStringArrayList("DATIBOLLETTE");

                if(arrayDatiBollette.size()!=0){//caricaDatiBollette(annorife, rootView);

                    for(int i=0;i<=arrayDatiBollette.size()-1;i++) {
                        Log.i("datiboll",(String)arrayDatiBollette.get(i));
                        String dato=(String)arrayDatiBollette.get(i);
                        String dati[]=dato.split(";");

                        final TableRow rowprod = new TableRow(rootView.getContext());
                        rowprod.setGravity(Gravity.CENTER);
                        TextView id_ = new TextView(rootView.getContext());
                        final int idRow = Integer.parseInt(dati[0]);
                        id_.setText(dati[0]);
                        id_.setVisibility(View.GONE);
                        TextView bimes_ = new TextView(rootView.getContext());
                        bimes_.setText(dati[1]);
                        bimes_.setWidth(270);
                        bimes_.setTextSize(17);
                        bimes_.setBackground(getResources().getDrawable(R.color.ocra));
                        TextView importo_ = new TextView(rootView.getContext());
                        importo_.setGravity(Gravity.RIGHT);
                        importo_.setText(dati[2]);
                        importo_.setWidth(150);
                        importo_.setTextSize(17);
                        TextView costo_ = new TextView(rootView.getContext());
                        costo_.setGravity(Gravity.RIGHT);
                        costo_.setText(dati[4]);
                        costo_.setWidth(150);
                        costo_.setTextSize(17);
                        TextView iva_ = new TextView(rootView.getContext());
                        iva_.setGravity(Gravity.RIGHT);
                        iva_.setText(dati[3]);
                        iva_.setWidth(150);
                        iva_.setTextSize(17);
                        rowprod.addView(id_);
                        rowprod.addView(bimes_);
                        rowprod.addView(costo_);
                        rowprod.addView(iva_);
                        rowprod.addView(importo_);
//                rowprod.setBackground(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.selector_row_prod));
                        rowprod.setOnLongClickListener(new TableRow.OnLongClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public boolean onLongClick(View v) {

                                apriPopup(rowprod, idRow, getArguments().getInt(ARG_SECTION_NUMBER));

                                return false;
                            }
                        });
                        tableDatiBollette.addView(rowprod);
                    }
                } else {
                    final TableRow rowprod = new TableRow(rootView.getContext());
                    rowprod.setGravity(Gravity.CENTER);
                    TextView id_ = new TextView(rootView.getContext());
                    id_.setVisibility(View.GONE);
                    TextView bimes_ = new TextView(rootView.getContext());
                    bimes_.setText("");
                    bimes_.setWidth(270);
                    bimes_.setTextSize(17);
                    bimes_.setBackground(getResources().getDrawable(R.color.ocra));
                    TextView importo_ = new TextView(rootView.getContext());
                    importo_.setGravity(Gravity.RIGHT);
                    importo_.setText("0" );
                    importo_.setWidth(150);
                    importo_.setTextSize(17);
                    importo_.setTextColor(Color.BLUE);
                    float iva=0;
                    float costo=0;
                    TextView costo_ = new TextView(rootView.getContext());
                    costo_.setGravity(Gravity.RIGHT);
                    costo_.setText("0");
                    costo_.setWidth(150);
                    costo_.setTextSize(17);
                    costo_.setTextColor(Color.BLUE);
                    TextView iva_ = new TextView(rootView.getContext());
                    iva_.setGravity(Gravity.RIGHT);
                    iva_.setText("0");
                    iva_.setWidth(150);
                    iva_.setTextSize(17);
                    iva_.setTextColor(Color.BLUE);
                    rowprod.addView(id_);
                    rowprod.addView(bimes_);
                    rowprod.addView(costo_);
                    rowprod.addView(iva_);
                    rowprod.addView(importo_);

                    tableDatiBollette.addView(rowprod);

                }

            }
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                final int mYear;
                final int mMonth;
                final int mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                rootView = inflater.inflate(it.android.j940549.fotovoltaico_material_design.R.layout.fragment_ssp_bonifici, container, false);
                String [] bonifici = {"I° Acconto","II° Acconto","III° Acconto","IV° Acconto","Conguaglio"};
                final Spinner bonificoSpinner= (Spinner) rootView.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.bonificoSSP);
                ArrayAdapter bonificoAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, bonifici);
                bonificoSpinner.setAdapter(bonificoAdapter);

                Button inserisci= (Button) rootView.findViewById(R.id.btninserisciBonifici);
                final EditText editImporto= (EditText) rootView.findViewById(R.id.importoBonifico);
                final EditText editdata= (EditText) rootView.findViewById(R.id.dataBonifico);
                editdata.setText(
                        new StringBuilder()
                                .append(mDay).append("/")
                                .append(mMonth + 1).append("/")
                                .append(mYear).append(" "));

                editdata.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {

                        Calendar newCalendar = Calendar.getInstance();

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        editdata.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                    }
                                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();


                    }
                });
                //final View finalRootView1 = rootView;
                inserisci.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     String periodo = bonificoSpinner.getSelectedItem().toString();
                                                     String importo = editImporto.getText().toString();
                                                     String datab=editdata.getText().toString();

                                                     if (importo.equals("")||editdata.getText().toString().equals("")) {
                                                         Toast.makeText(getContext(), "Dati incompleti", Toast.LENGTH_SHORT).show();
                                                     } else {
                                                         //long data=new ConvertiData().data_longtoString(;
                                                         DBLayer dbLayer = null;
                                                         boolean inserBonif = false;

                                                         try {
                                                             dbLayer = new DBLayer(getContext());
                                                             dbLayer.open();
                                                             inserBonif= dbLayer.insertBonifico(HomeActivityNav.NOMEIMPIANTO,
                                                                     annorife,periodo, datab, importo);

                                                         } catch (SQLException ex) {
                                                             Toast.makeText(getContext(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
                                                         }
                                                         if (inserBonif== true) {
                                                             Toast.makeText(getContext(), "dati inseriti correttamente", Toast.LENGTH_SHORT).show();
                                                             BilancioFragment fragment= BilancioFragment.newInstance();

                                                             FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                             fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                                                         }

                                                     }
                                                     //caricaDatiBonifici(annorife);
                                                 }


                                             }

                );


                tableDatiBonifico = (TableLayout) rootView.findViewById(it.android.j940549.fotovoltaico_material_design.R.id.tabelladatiBonificiSSP);
                tableDatiBonifico.setStretchAllColumns(true);
                tableDatiBonifico.bringToFront();
//                tableDatiBonifico.setBackgroundDrawable(getResources().getDrawable(R.drawable.sfondo_tabella));
                TableRow rowTitolo = new TableRow(rootView.getContext());

                rowTitolo.setGravity(Gravity.CENTER);
                TextView id = new TextView(rootView.getContext());
                id.setText("id");
                id.setVisibility(View.GONE);
                TextView periodolbl = new TextView(rootView.getContext());
                periodolbl.setText("PERIODO");
                periodolbl.setWidth(270);
                periodolbl.setHeight(50);
                //periodolbl.setTextSize(15);
                periodolbl.setBackground(getResources().getDrawable(R.color.ocra_alfa));//TextColor(Color.WHITE);
                TextView databonlbl = new TextView(rootView.getContext());
                databonlbl.setText(" DATA ");
                databonlbl.setWidth(150);
                //databonlbl.setTextSize(15);
                databonlbl.setGravity(Gravity.CENTER);
                TextView importoBonlbl = new TextView(rootView.getContext());
                importoBonlbl.setText(" IMPORTO ");
                importoBonlbl.setWidth(150);
                //importoBonlbl.setTextSize(15);
                importoBonlbl.setGravity(Gravity.CENTER);
                rowTitolo.addView(id);
                rowTitolo.addView(periodolbl);
                rowTitolo.addView(databonlbl);
                rowTitolo.addView(importoBonlbl);

                tableDatiBonifico.addView(rowTitolo);
                //caricaDatiBonifici(annorife, rootView);
                ArrayList arrayDatiBonifici=getArguments().getStringArrayList("DATIBONIFICI");

                if(arrayDatiBonifici.size()>0){
                    for(int i=0;i<=arrayDatiBonifici.size()-1;i++) {
                        Log.i("datibonif",(String)arrayDatiBonifici.get(i));

                        String dato=(String)arrayDatiBonifici.get(i);
                        String dati[]=dato.split(";");
                        final TableRow rowprod = new TableRow(rootView.getContext());
                        rowprod.setGravity(Gravity.CENTER);
                        TextView id_ = new TextView(rootView.getContext());
                        final int idRow = Integer.parseInt(dati[0]);
                        id_.setText(dati[0]);
                        id_.setVisibility(View.GONE);
                        TextView periodo_ = new TextView(rootView.getContext());
                        periodo_.setText(dati[1]);
                        periodo_.setWidth(270);
                        periodo_.setTextSize(17);
                        periodo_.setBackground(getResources().getDrawable(R.color.ocra));
                        TextView dataBon_ = new TextView(rootView.getContext());
                        dataBon_.setText(dati[2]);
                        dataBon_.setWidth(270);
                        dataBon_.setTextSize(17);
                        TextView importo_ = new TextView(rootView.getContext());
                        importo_.setGravity(Gravity.RIGHT);
                        importo_.setText(dati[3]);
                        importo_.setWidth(150);
                        importo_.setTextSize(17);
                        rowprod.addView(id_);
                        rowprod.addView(periodo_);
                        rowprod.addView(dataBon_);
                        rowprod.addView(importo_);
                        //                      rowprod.setBackground(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.selector_row_prod));
                        rowprod.setOnLongClickListener(new TableRow.OnLongClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public boolean onLongClick(View v) {

                                apriPopup(rowprod, idRow, getArguments().getInt(ARG_SECTION_NUMBER));

                                return false;
                            }
                        });
                        tableDatiBonifico.addView(rowprod);
                    }
                } else {
                    final TableRow rowprod = new TableRow(rootView.getContext());
                    rowprod.setGravity(Gravity.CENTER);
//                rowprod.setBackgroundDrawable(getResources().getDrawable(it.android.j940549.fotovoltaico.R.drawable.bordo));
                    TextView id_ = new TextView(rootView.getContext());
                    id_.setText("" );
                    id_.setVisibility(View.GONE);
                    TextView periodo_ = new TextView(rootView.getContext());
                    periodo_.setText("0");
                    periodo_.setWidth(270);
                    periodo_.setTextSize(17);
                    periodo_.setBackground(getResources().getDrawable(R.color.ocra));
                    TextView dataBon_ = new TextView(rootView.getContext());
                    dataBon_.setText("0");
                    dataBon_.setWidth(270);
                    dataBon_.setTextSize(17);
                    TextView importo_ = new TextView(rootView.getContext());
                    importo_.setGravity(Gravity.RIGHT);
                    importo_.setText("");
                    importo_.setWidth(150);
                    importo_.setTextSize(17);
                    rowprod.addView(id_);
                    rowprod.addView(periodo_);
                    rowprod.addView(dataBon_);
                    rowprod.addView(importo_);

                    tableDatiBonifico.addView(rowprod);

                }
            }


            if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(it.android.j940549.fotovoltaico_material_design.R.layout.fragment_ssp_bilancio, container, false);

                ArrayList arrayDatiBonifici=getArguments().getStringArrayList("DATIBONIFICI");
                ArrayList arrayDatiBollette=getArguments().getStringArrayList("DATIBOLLETTE");
                float totBonifici=0;
                float totBollette=0;
                DecimalFormat twoDForm = new DecimalFormat("#.##");
                TextView textrimborsoSSP= (TextView) rootView.findViewById(R.id.rimborsiSSP);
                textrimborsoSSP.setText(""+twoDForm.format(totBonifici));

                TextView textCostoenergia= (TextView) rootView.findViewById(R.id.costoenergiaprelevata);
                textCostoenergia.setText(""+twoDForm.format(totBollette));
                TextView textbilancio= (TextView) rootView.findViewById(R.id.saldoSSP);
                textbilancio.setText(""+0);
                if(arrayDatiBonifici.size()>0) {
                    for (int i = 0; i <= arrayDatiBonifici.size() - 1; i++) {
                        String dato = (String) arrayDatiBonifici.get(i);
                        String dati[] = dato.split(";");
                        float importo_ = Float.parseFloat(dati[3].replace(",","."));
                        Log.i("boni_im",""+importo_);
                        totBonifici =totBonifici+ importo_;
                        Log.i("boni_tot",""+totBonifici);
                    }
                    // textCostoenergia= (TextView) rootView.findViewById(R.id.costoenergiaprelevata);
                    textrimborsoSSP.setText(""+twoDForm.format(totBonifici));
                }
                if(arrayDatiBollette.size()>0) {
                    for (int i = 0; i <= arrayDatiBollette.size() - 1; i++) {
                        String dato = (String) arrayDatiBollette.get(i);
                        String dati[] = dato.split(";");
                        float importo_ = Float.parseFloat(dati[2].replace(",","."));
                        Log.i("boll_im",""+importo_);
                        totBollette = totBollette+importo_;
                        Log.i("boll_tot",""+totBollette);
                    }
                    //textbilancio= (TextView) rootView.findViewById(R.id.saldoSSP);
                    textCostoenergia.setText(""+twoDForm.format(totBollette));
                    float datobilancio=totBonifici -totBollette;

                    textbilancio.setText(""+twoDForm.format(datobilancio));



                    String bilancio=textbilancio.getText().toString();
                    if(bilancio.contains("-")){
                        textbilancio.setTextColor(Color.RED);
                    }else{
                        textbilancio.setTextColor(Color.GREEN);
                    }
                }

            }

            return rootView;
        }




//caricadatibonifici

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
                                dbLayer.deleteDatoBollette(idRow);
                            }
                            if(argselection==2){
                                dbLayer.deleteDatoBonifico(idRow);
                            }


                        } catch (SQLException ex) {
                            Toast.makeText(getContext(), "" + ex.toString(), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        Toast.makeText(getContext(), "dato cancellato!", Toast.LENGTH_SHORT).show();

                        dbLayer.close();

                      /*  Intent ritornaaBilancio = new Intent(getContext(), BilancioSPActivity.class);
                        ritornaaBilancio.putExtra("ANNORIFE",annorife);
                        startActivity(ritornaaBilancio);
                        getActivity().finish();*/

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
        ArrayList bollette=new ArrayList();
        ArrayList bonifici=new ArrayList();
        String anno;
        String annorife;
        public SectionsPagerAdapter(FragmentManager fm, String annorife, ArrayList datiBoll, ArrayList datiBon) {
            super(fm);
            anno=annorife;
            bollette=datiBoll;
            bonifici=datiBon;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1,anno,bollette,bonifici);
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
                    return "BOLLETTE ENEL";
                case 1:
                    return "BONIFICI GSE-SP";
                case 2:
                    return "BILANCIO";
            }
            return null;
        }
    }

    public void caricaDatiBollette(String annorife){
        DBLayer dbLayer = null;
        Cursor datiBollette = null;

        try {
            dbLayer = new DBLayer(context);
            dbLayer.open();

            datiBollette = dbLayer.getBollette(HomeActivityNav.NOMEIMPIANTO,annorife);
            Log.i("righe dei datiBollette", "" + datiBollette.getCount());

            if (datiBollette.getCount() > 0) {


                datiBollette.moveToPosition(0);
                do {
                    String datoStringBollette="";
                    datoStringBollette=datoStringBollette+"" + datiBollette.getInt(0)+";";
                    datoStringBollette=datoStringBollette+(new ConvertiData().strBimestre(datiBollette.getInt(3)))+";";
                    datoStringBollette=datoStringBollette+(String.format("%.02f",datiBollette.getFloat(4)))+";";
                    float iva = (datiBollette.getFloat(4) / 1.10f);
                    float costo = datiBollette.getFloat(4) - iva;
                    datoStringBollette=datoStringBollette+(String.format("%.02f", costo))+";";
                    datoStringBollette=datoStringBollette+(String.format("%.02f", iva));
                    Log.i("strinbolle",datoStringBollette);
                    arrayDatiBollette.add(datoStringBollette);
                } while (datiBollette.moveToNext());
            }
            datiBollette.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbLayer.close();

    }


    public void caricaDatiBonifici(String annorife){
        DBLayer dbLayer = null;

        Cursor datiBonifici = null;

        try {
            dbLayer = new DBLayer(context);
            dbLayer.open();

            datiBonifici = dbLayer.getBonifici(HomeActivityNav.NOMEIMPIANTO,annorife);
            Log.i("righe dei datiBonifici", "" + datiBonifici.getCount());

            if (datiBonifici.getCount() > 0) {

                datiBonifici.moveToPosition(0);
                do {
                    String datoStringBonifico="";

//                    RowBonifici row = new RowBonifici();
                    datoStringBonifico=("" + datiBonifici.getInt(0))+";";
                    datoStringBonifico=datoStringBonifico+(new ConvertiData().strPeriodo(datiBonifici.getInt(3)))+";";
                    datoStringBonifico=datoStringBonifico+(new ConvertiData().long_data(datiBonifici.getInt(4)))+";";
                    datoStringBonifico=datoStringBonifico+(String.format("%.02f", datiBonifici.getFloat(5)));
                    arrayDatiBonifici.add(datoStringBonifico);
                } while (datiBonifici.moveToNext());
            }
            datiBonifici.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, "errore Sqlite/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        dbLayer.close();

    }



}
