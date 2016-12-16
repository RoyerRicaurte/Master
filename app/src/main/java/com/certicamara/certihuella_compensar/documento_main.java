package com.certicamara.certihuella_compensar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.certicamara.certihuella_compensar.R;
import com.certicamara.certihuella_compensar.access.Imagen;
import com.certicamara.certihuella_compensar.access.sqliteHelper;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link documento_main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link documento_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class documento_main extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    sqliteHelper bd;

    private OnFragmentInteractionListener mListener;

    public documento_main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment documento_main.
     */
    // TODO: Rename and change types and number of parameters
    public static documento_main newInstance(String param1, String param2) {
        documento_main fragment = new documento_main();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private void EventosMainFragment(View mainView)
    {
        TextView btnCedulaCiudadania = (TextView) mainView.findViewById(R.id.btnCedulaCiudadania);
        TextView btnCertificadoLaboral = (TextView) mainView.findViewById(R.id.btnCertificadoLaboral);
        TextView btnDesprendiblesNomina = (TextView) mainView.findViewById(R.id.btnDesprendiblesNomina);
        TextView btnOtrosSolicitudes = (TextView) mainView.findViewById(R.id.btnOtrosSolicitudes);
        TextView btnCorreccionesSoportes = (TextView) mainView.findViewById(R.id.btnCorreccionesSoportes);
        TextView btnAdicionalesSoportes = (TextView) mainView.findViewById(R.id.btnAdicionealesSoportes);
        TextView btnLibranza = (TextView) mainView.findViewById(R.id.btnLibranza);
        TextView btnPagare = (TextView) mainView.findViewById(R.id.btnPagare);
        TextView btnSeguros = (TextView) mainView.findViewById(R.id.btnSeguros);

        btnCedulaCiudadania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("1");
            }

        });

        btnCertificadoLaboral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("2");
            }

        });

        btnDesprendiblesNomina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("3");
            }

        });



        btnOtrosSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("4");
            }

        });

        btnCorreccionesSoportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("5");
            }

        });

        btnAdicionalesSoportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("6");
            }

        });

        btnLibranza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("7");
            }

        });

        btnPagare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("8");
            }
        });

        btnSeguros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarFramento("9");
            }
        });

        List<Imagen> listImagenes= bd.countImagenTipo(1);
        for(int i=0;i<listImagenes.size();i++)
        {
            if(listImagenes.get(i).get_idTipo()==1) //CedulaCiudadania
            {
                btnCedulaCiudadania.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }
            else if(listImagenes.get(i).get_idTipo()==2) //CertificadoLaboral
            {
                btnCertificadoLaboral.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }
            else if(listImagenes.get(i).get_idTipo()==3) //DesprendiblesNomina
            {
                btnDesprendiblesNomina.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }
            else if(listImagenes.get(i).get_idTipo()==4) //Otros Solicitudes
            {
                btnOtrosSolicitudes.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }
            else if(listImagenes.get(i).get_idTipo()==5) //Otros Solicitudes
            {
                btnCorreccionesSoportes.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }
            else if(listImagenes.get(i).get_idTipo()==6) //Otros Solicitudes
            {
                btnAdicionalesSoportes.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }
            else if(listImagenes.get(i).get_idTipo()==7) //Otros Solicitudes
            {
                btnLibranza.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }
            else if(listImagenes.get(i).get_idTipo()==8) //Otros Solicitudes
            {
                btnPagare.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }
            else if(listImagenes.get(i).get_idTipo()==9) //Otros Solicitudes
            {
                btnSeguros.setText(String.valueOf(listImagenes.get(i).get_idImagen()));
            }

        }

    }
    private void CargarFramento(String idTipo)
    {
        Context context = getActivity().getApplicationContext();

        Fragment fragment = null;
        Class fragmentClass=documento_cargar.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        Bundle args = new Bundle();
        args.putString("idTipo", idTipo);
        fragment.setArguments(args);

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mainView=inflater.inflate(R.layout.fragment_documento_main, container, false);

        bd= new sqliteHelper(getActivity());

        EventosMainFragment(mainView);
        return mainView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
