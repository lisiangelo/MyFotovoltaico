package it.android.j940549.fotovoltaico_material_design.fragmentAnalisi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import it.android.j940549.fotovoltaico_material_design.BilancioFragment;
import it.android.j940549.fotovoltaico_material_design.HomeActivityNav;
import it.android.j940549.fotovoltaico_material_design.R;


public class AnalisiDatiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private AnalisiDatiFragment.SectionsPagerAdapter mSectionsPagerAdapter;
    private String anno1,anno2;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    public AnalisiDatiFragment() {
        // Required empty public constructor
    }

    public static AnalisiDatiFragment newInstance(String anno1,String anno2) {
        AnalisiDatiFragment fragment = new AnalisiDatiFragment();
        Bundle args = new Bundle();
        args.putString("anno1", anno1);
        args.putString("anno2", anno2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            anno1 = getArguments().getString("anno1");
            anno2= getArguments().getString("anno2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

    final    View view=inflater.inflate(R.layout.fragment_analisi_dati2, container, false);


        Spinner annoSpinner_dal = (Spinner) view.findViewById(R.id.annorifeAnalisi_DAL);
        ArrayAdapter annoAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, HomeActivityNav.ANNI);
        annoSpinner_dal.setAdapter(annoAdapter);
        int spinnerPosition = annoAdapter.getPosition(anno1);
        annoSpinner_dal.setSelection(spinnerPosition);


        Spinner annoSpinner_al = (Spinner) view.findViewById(R.id.annorifeAnalisi_AL);
        ArrayAdapter annoAdapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, HomeActivityNav.ANNI);
        annoSpinner_al.setAdapter(annoAdapter2);
        int spinnerPosition2 = annoAdapter2.getPosition(anno2);
        annoSpinner_al.setSelection(spinnerPosition2);
        String annorifeIb=anno1;//annoSpinner_dal.getSelectedItem().toString();
        Log.i("anno", annorifeIb);
        String annorifeIIb=anno2;//annoSpinner_al.getSelectedItem().toString();
        Log.i("anno2", annorifeIIb);


        //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),annorifeIb,annorifeIIb);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager,true);

//        tabLayout.setTabMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        tabLayout.getTabAt(0).setIcon(R.drawable.icon_produzione);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_autoconsumo);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_immissione);
        tabLayout.getTabAt(3).setIcon(R.drawable.icon_prelievi);
        tabLayout.getTabAt(4).setIcon(R.drawable.icon_consumi_tot);
        tabLayout.getTabAt(5).setIcon(R.drawable.icon_imp_prod);
        tabLayout.getTabAt(6).setIcon(R.drawable.icon_comp_cons);


        ImageButton btnAnalizza= (ImageButton) view.findViewById(R.id.btnAnalizza);
        btnAnalizza.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Spinner annoSpinner_al = (Spinner) view.findViewById(R.id.annorifeAnalisi_AL);
                                               Spinner annoSpinner_dal = (Spinner) view.findViewById(R.id.annorifeAnalisi_DAL);

                                               String annorife1=annoSpinner_dal.getSelectedItem().toString();
                                               Log.i("anno", annorife1);
                                               String annorife2=annoSpinner_al.getSelectedItem().toString();
                                               Log.i("anno2", annorife2);
                                               AnalisiDatiFragment fragment= AnalisiDatiFragment.newInstance(annorife1,annorife2);
                                               FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                               fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                                           }
                                       }
        );

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
/*        if (mListener != null) {
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


    public static class PlaceholderFragment extends Fragment {
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
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_analisi_dati2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            return rootView;
        }
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {
        String annoI,annoII;
        public SectionsPagerAdapter(FragmentManager fm, String annorifeI, String annorifeII) {
            super(fm);
            annoI=annorifeI;
            annoII=annorifeII;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0: {
                    fragment = new FragmentProduzioni();
                    Bundle args = new Bundle();

                    args.putString("ANNO1", annoI);
                    args.putString("ANNO2", annoII);
                    fragment.setArguments(args);

                    return fragment;
                    //break;
                }
                case 1: {
                    fragment = new FragmentAutoconsumo();
                    Bundle args = new Bundle();

                    args.putString("ANNO1", annoI);
                    args.putString("ANNO2", annoII);
                    fragment.setArguments(args);
                    return fragment;

                }
                case 2: {

                    fragment = new FragmentImmissioni();
                    Bundle args = new Bundle();

                    args.putString("ANNO1", annoI);
                    args.putString("ANNO2", annoII);
                    fragment.setArguments(args);
                    return fragment;
                    //break;

                }
                case 3: {
                    fragment = new FragmentPrelievi();
                    Bundle args = new Bundle();

                    args.putString("ANNO1", annoI);
                    args.putString("ANNO2", annoII);
                    fragment.setArguments(args);
                    return fragment;
                    //break;

                }
                case 4: {
                    fragment = new FragmentConsumiTot();
                    Bundle args = new Bundle();

                    args.putString("ANNO1", annoI);
                    args.putString("ANNO2", annoII);
                    fragment.setArguments(args);
                    return fragment;
                }
                case 5: {
                    fragment = new FragmentAutoc_Imm();
                    Bundle args = new Bundle();

                    args.putString("ANNO1", annoI);
                    args.putString("ANNO2", annoII);
                    fragment.setArguments(args);
                    return fragment;
                }
                case 6: {
                    fragment = new FragmentPrel_Autoc();
                    Bundle args = new Bundle();

                    args.putString("ANNO1", annoI);
                    args.putString("ANNO2", annoII);
                    fragment.setArguments(args);
                    return fragment;
                }
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Produz.";
                case 1:
                    return "Autocons.";
                case 2:
                    return "Immiss.";
                case 3:
                    return "Prelievi";
                case 4:
                    return "Cons. Tot.";
                case 5:
                    return "Prod. in %";
                case 6:
                    return "Cons. in %";
            }
            return null;
        }
    }

}
