package com.certicamara.certihuella_compensar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.certicamara.certihuella_compensar.access.ImageFilePath;
import com.certicamara.certihuella_compensar.access.Imagen;
import com.certicamara.certihuella_compensar.access.image_adapter;
import com.certicamara.certihuella_compensar.access.sincronizar;
import com.certicamara.certihuella_compensar.access.sqliteHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link documento_cargar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link documento_cargar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class documento_cargar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GET_FILE = 2;

    sqliteHelper bd;

    // TODO: Rename and change types of parameters
    private File photoFile;
    private String mParam1;
    private String mParam2;
    private int idTipo;
    private ImageView imgPrincipal;
    private ListView listImagenes;
    private Imagen imagenSeleccionada;
    private List<Imagen> listaImagenes;
    private ImageButton btnEliminar;
    private ImageButton btnCargarArchivo;



    private OnFragmentInteractionListener mListener;

    public documento_cargar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CargarDocumento.
     */
    // TODO: Rename and change types and number of parameters
    public static documento_cargar newInstance(String param1, String param2) {
        documento_cargar fragment = new documento_cargar();
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View mainView=inflater.inflate(R.layout.fragment_cargar_documento, container, false);
        idTipo= Integer.parseInt(getArguments().getString("idTipo"));
       // list.setRotation(-90);

        bd= new sqliteHelper(getActivity());

        DeclararEventos(mainView);

        CargarImagenes();

        return mainView;
    }



    private void CargarImagenes()
    {
        listaImagenes= bd.getImagenSolicitud(1,idTipo);

        String[] imageName = new String[listaImagenes.size()];
        int i=0;
        String storageDirTemp = Environment.getExternalStorageDirectory().toString()+"/Android/data/"+getActivity().getPackageName()+"/files/Pictures/";

        //File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        for(Imagen img : listaImagenes){

            imageName[i]=storageDirTemp+img.get_nombre();
            i+=1;
        }

        image_adapter adapter = new image_adapter(getActivity(), imageName);

        listImagenes.setAdapter(adapter);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);




        if(imgPrincipal.getDrawable() == null) {
            if (listaImagenes.size() > 0) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imageName[0]);
                imgPrincipal.setImageBitmap(myBitmap);
                imagenSeleccionada = listaImagenes.get(0);
                btnEliminar.setVisibility(View.VISIBLE);
            }
        }

    }

    private void DeclararEventos(View mainView)
    {

        ImageButton btnCamara=(ImageButton)mainView.findViewById(R.id.btnCamara);
        imgPrincipal =(ImageView)mainView.findViewById(R.id.imgPrincipal);
        listImagenes=(ListView)mainView.findViewById(R.id.lstImagenes);
        btnEliminar=(ImageButton)mainView.findViewById(R.id.btnEliminar);
        btnCargarArchivo=(ImageButton)mainView.findViewById((R.id.btnCargarArchivo));

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CapturarImagen();
            }

        });


        listImagenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  itemValue    = (String) listImagenes.getItemAtPosition(i);
                imagenSeleccionada= listaImagenes.get(i);

                String ext= "."+getFileExt(itemValue);

                if(ext.contains(".pdf"))
                {
                    imgPrincipal.setImageResource(R.drawable.ic_pdf);
                }
                else if(ext.contains(".tif"))
                {
                    imgPrincipal.setImageResource(R.drawable.ic_tiff);
                }
                else
                {
                    Bitmap myBitmap = BitmapFactory.decodeFile(itemValue);
                    imgPrincipal.setImageBitmap(myBitmap);
                }


            }
        });


        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EliminarImagen();
            }
        });


        btnCargarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarImagen();
            }
        });
        imgPrincipal.setLongClickable(true);
        imgPrincipal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String storageDirTemp = Environment.getExternalStorageDirectory().toString()+"/Android/data/"+getActivity().getPackageName()+"/files/Pictures/";
                //File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(storageDirTemp+imagenSeleccionada.get_nombre());
                Intent intent = new Intent(Intent.ACTION_VIEW);

                String ext="."+getFileExt(imagenSeleccionada.get_nombre());

                if(ext.contains(".pdf"))
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                else if(ext.contains(".tif"))
                    intent.setDataAndType(Uri.fromFile(file), "image/tiff");
                else
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return false;
            }
        });


    }


    private void CargarImagen()
    {
        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mediaIntent.setType("*/*");
        String[] mimetypes = {"image/png", "image/jpeg","application/pdf","image/tiff"};
        mediaIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(mediaIntent,REQUEST_GET_FILE);
    }


    private void EliminarImagen()
    {
        bd.deleteImagen(imagenSeleccionada);
        CargarImagenes();

        if(listaImagenes.size()==0)
        {
            btnEliminar.setVisibility(View.INVISIBLE);
            imgPrincipal.setImageResource(0);
        }
        else
        {
            String  itemValue    = (String) listImagenes.getItemAtPosition(0);
            Bitmap myBitmap = BitmapFactory.decodeFile(itemValue);
            imgPrincipal.setImageBitmap(myBitmap);
            imagenSeleccionada= listaImagenes.get(0);
        }


    }


    private void CapturarImagen()
    {
        Context context = getActivity().getApplicationContext();
        try {
            photoFile = createImageFile(".jpg");
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        if(photoFile!=null) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.certicamara.certihuella_compensar.fileprovider",
                        photoFile);
                List<ResolveInfo> resolvedIntentActivities = context.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;
                    context.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else
        {
            Toast toast = Toast.makeText(context, "Ocurrio un error al crear carpeta imagenes.", Toast.LENGTH_LONG);
            toast.show();

        }
    }

    String mCurrentPhotoPath;

    private File createImageFile(String format) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "File_" + timeStamp + "_";
        String storageDirTemp = Environment.getExternalStorageDirectory().toString()+"/Android/data/"+getActivity().getPackageName()+"/files/Pictures/";
        //File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);


        File storageDir=new File(storageDirTemp);

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }

        if(success) {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    format,         /* suffix */
                    storageDir      /* directory */
            );
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();
            return image;
        }
        return null;
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Context context = getActivity().getApplicationContext();

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            Sincronizar(".jpg");

        }
        if (requestCode == REQUEST_GET_FILE && resultCode == Activity.RESULT_OK) {

            Uri dataUri = data.getData();
            int duration = Toast.LENGTH_LONG;
            String selectedImagePath = ImageFilePath.getPath(context.getApplicationContext(), dataUri);
            Toast toast = Toast.makeText(context, selectedImagePath, duration);
            toast.show();


            File fileSelected= new File(selectedImagePath);
            String ext= "."+getFileExt(fileSelected.getAbsolutePath());


            try {
                photoFile= createImageFile(ext);
                copy(fileSelected,photoFile);
            } catch (IOException e) {
                e.printStackTrace();
                toast = Toast.makeText(context, e.toString(), duration);
                toast.show();
            }
            Sincronizar(ext);

        }
    }



    private void Sincronizar(String ext)
    {

        Imagen imagen= new Imagen();
        imagen.set_idSolicitud(1);
        imagen.set_idTipo(idTipo);
        imagen.set_idImagen(bd.getNextImageId(1,idTipo));
        imagen.set_nombre(photoFile.getName());

        bd.addImagen(imagen);

        CargarImagenes();
        btnEliminar.setVisibility(View.VISIBLE);
        imagenSeleccionada= listaImagenes.get(listaImagenes.size()-1);


        if(ext.contains(".pdf"))
        {
            imgPrincipal.setImageResource(R.drawable.ic_pdf);
        }
        else if(ext.contains(".tif"))
        {
            imgPrincipal.setImageResource(R.drawable.ic_tiff);
        }
        else
        {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imgPrincipal.setImageBitmap(myBitmap);
        }

        final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "Por favor espere ...", "Guardando Información ...", true);
        ringProgressDialog.setCancelable(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Context context = getActivity().getApplicationContext();
                sincronizar.EnviarImagenesPendientes(context)     ;
                ringProgressDialog.dismiss();
            }

        }).start();

    }

    public String getPath(Context context,Uri uri) {

        String path = null;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    public String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
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
