package com.certicamara.certihuella_compensar.access;

/**
 * Created by Montreal Office on 28/11/2016.
 */



import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.certicamara.certihuella_compensar.R;

public class image_adapter extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] imageName;
    public image_adapter(Activity context,
                         String[] imageName) {
        super(context, R.layout.list_images, imageName);
        this.context = context;
        this.imageName = imageName;
}

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_images, null, true);
      //  TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        Bitmap myBitmap=null;
        if(imageName[position].contains(".pdf"))
        {
            imageView.setImageResource(R.drawable.ic_pdf);
        }
        else if(imageName[position].contains(".tif")) {
            imageView.setImageResource(R.drawable.ic_tiff);
        }
        else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 3;
            options.inPurgeable = true;

            myBitmap = BitmapFactory.decodeFile(imageName[position],options);
            imageView.setImageBitmap(myBitmap);
        }
        //Bundle extras = data.getExtras();

        //txtTitle.setText(web[position]);
        //imageView.setImageResource(imageId[position]);
        //imageView.setRotation(90);
        return rowView;
    }
}