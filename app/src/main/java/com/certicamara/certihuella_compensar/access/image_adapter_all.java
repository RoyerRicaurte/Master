package com.certicamara.certihuella_compensar.access;

/**
 * Created by Montreal Office on 28/11/2016.
 */



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.certicamara.certihuella_compensar.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class image_adapter_all extends ArrayAdapter<Imagen>{

    Activity context;

    public image_adapter_all(Activity context,
                             ArrayList<Imagen> imagen) {
        super(context, 0, imagen);
        this.context=context;
}

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_images_all, null, true);
      //  TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        String directorioImagenes = Environment.getExternalStorageDirectory().toString()+"/Android/data/"+context.getPackageName()+"/files/Pictures/";
        Imagen imagen = getItem(position);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        TextView txtTipo = (TextView) rowView.findViewById(R.id.txtTipo);
        TextView txtImagen = (TextView) rowView.findViewById(R.id.txtidImagen);


        txtImagen.setText(String.valueOf(imagen.get_idSolicitud())+String.valueOf(imagen.get_idTipo())+String.valueOf(imagen.get_idSolicitud()));
        txtTipo.setText(imagen.get_TipoImagen());

        Bitmap myBitmap=null;
        if(imagen.get_nombre().contains(".pdf"))
        {
            imageView.setImageResource(R.drawable.ic_pdf);
        }
        else if(imagen.get_nombre().contains(".tif")) {
            imageView.setImageResource(R.drawable.ic_tiff);
        }
        else {
            myBitmap = BitmapFactory.decodeFile(directorioImagenes+imagen.get_nombre());
            imageView.setImageBitmap(myBitmap);
        }
        //Bundle extras = data.getExtras();

        //txtTitle.setText(web[position]);
        //imageView.setImageResource(imageId[position]);
        //imageView.setRotation(90);
        return rowView;
    }


}