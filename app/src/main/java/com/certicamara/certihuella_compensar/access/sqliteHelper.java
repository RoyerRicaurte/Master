package com.certicamara.certihuella_compensar.access;

/**
 * Created by Montreal Office on 1/12/2016.
 */


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
        import java.util.LinkedList;
        import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
        import javax.crypto.spec.SecretKeySpec;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.util.Base64;

public class sqliteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String  DATABASE_NAME="CERTIHUELLA";
    private static final String TABLE_IMAGEN = "IMAGEN";
    private static final String TABLE_PARAMETRO = "PARAMETRO";

    public sqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    public sqliteHelper(Context context,String nombreBd) {
        super(context, nombreBd, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_TABLE = "CREATE TABLE "+TABLE_IMAGEN+" ( " +
                "id_solicitud INTEGER," +
                "id_tipo INTEGER," +
                "id_imagen INTEGER," +
                "nombre TEXT," +
                "estado TEXT )";

        db.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name


    // Books Table Columns names
    private static final String KEY_SOLICITUD = "id_solicitud";
    private static final String KEY_TIPO = "id_tipo";
    private static final String KEY_IMAGEN = "id_imagen";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_ESTADO = "estado";

    private static final String[] COLUMNS_IMAGEN = {KEY_SOLICITUD,KEY_TIPO,KEY_IMAGEN,KEY_NOMBRE,KEY_ESTADO};


    public void addImagen(Imagen imagen){

        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_SOLICITUD, imagen.get_idSolicitud());
        values.put(KEY_TIPO, imagen.get_idTipo());
        values.put(KEY_IMAGEN, imagen.get_idImagen());
        values.put(KEY_NOMBRE, imagen.get_nombre());
        values.put(KEY_ESTADO, "Pendiente");

        // 3. insert
        db.insert(TABLE_IMAGEN, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public List<Imagen> getImagenSolicitud(int idSolicitud,int idTipo){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        // 2. build query
        List<Imagen> Imagenes = new LinkedList<Imagen>();

        Cursor cursor =
                db.query(TABLE_IMAGEN, // a. table
                        COLUMNS_IMAGEN, // b. column names
                        " id_solicitud = ? and id_tipo = ?", // c. selections
                        new String[] { String.valueOf(idSolicitud),String.valueOf(idTipo)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit


        //Log.d("getSolicitud("+idSolicitud+")", solicitud.toString());

        Imagen imagen = null;
        if (cursor.moveToFirst()) {
            do {
                imagen = new Imagen();
                imagen.set_idSolicitud(Integer.parseInt(cursor.getString(0)));
                imagen.set_idTipo(Integer.parseInt(cursor.getString(1)));
                imagen.set_idImagen(Integer.parseInt(cursor.getString(2)));
                imagen.set_nombre(cursor.getString(3));
                imagen.set_estado(cursor.getString(4));


                Imagenes.add(imagen);
            } while (cursor.moveToNext());
        }

        // return books
        return Imagenes;

    }



    public List<Imagen> getImagenesSinSincronizar(){

        // 2. build query
        String query = "SELECT  id_solicitud,id_tipo,id_imagen,nombre FROM " + TABLE_IMAGEN+" where estado='Pendiente'";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);



        List<Imagen> Imagenes = new LinkedList<Imagen>();

        Imagen imagen = null;
        if (cursor.moveToFirst()) {
            do {
                imagen = new Imagen();
                imagen.set_idSolicitud(Integer.parseInt(cursor.getString(0)));
                imagen.set_idTipo(Integer.parseInt(cursor.getString(1)));
                imagen.set_idImagen(Integer.parseInt(cursor.getString(2)));
                imagen.set_nombre(cursor.getString(3));

                Imagenes.add(imagen);
            } while (cursor.moveToNext());
        }


        // 5. return book
        return Imagenes;
    }




    public List<Imagen> countImagenTipo(int idSolicitud){

        // 2. build query
        String query = "SELECT  id_solicitud,id_tipo,count(id_solicitud)  FROM " + TABLE_IMAGEN+" where id_solicitud="+String.valueOf(idSolicitud)+" group by id_tipo,id_solicitud";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);



        List<Imagen> Imagenes = new LinkedList<Imagen>();

        Imagen imagen = null;
        if (cursor.moveToFirst()) {
            do {
                imagen = new Imagen();
                imagen.set_idSolicitud(Integer.parseInt(cursor.getString(0)));
                imagen.set_idTipo(Integer.parseInt(cursor.getString(1)));
                imagen.set_idImagen(Integer.parseInt(cursor.getString(2)));


                Imagenes.add(imagen);
            } while (cursor.moveToNext());
        }


        // 5. return book
        return Imagenes;
    }



    public List<Imagen> getImagenSolicitud(int idSolicitud){

        // 2. build query
        String query = "SELECT  id_solicitud,id_tipo,id_imagen,nombre  FROM " + TABLE_IMAGEN+" where id_solicitud="+String.valueOf(idSolicitud)+" ";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);



        List<Imagen> Imagenes = new LinkedList<Imagen>();

        Imagen imagen = null;
        if (cursor.moveToFirst()) {
            do {
                imagen = new Imagen();
                imagen.set_idSolicitud(Integer.parseInt(cursor.getString(0)));
                imagen.set_idTipo(Integer.parseInt(cursor.getString(1)));
                imagen.set_idImagen(Integer.parseInt(cursor.getString(2)));
                imagen.set_nombre(cursor.getString(3));

                Imagenes.add(imagen);
            } while (cursor.moveToNext());
        }


        // 5. return book
        return Imagenes;
    }



    public int getNextImageId(int idSolicitud,int idTipo){



        // 2. build query
        String query = "SELECT  max(id_imagen) FROM " + TABLE_IMAGEN+" WHERE "+KEY_SOLICITUD+" = "+String.valueOf(idSolicitud)+" and "+KEY_TIPO+" = "+String.valueOf(idTipo);

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        int maxSolicitud=1;
        if(!cursor.isNull(0));
        {
            maxSolicitud=cursor.getInt(0)+1;
        }


        // 5. return book
        return maxSolicitud;
    }



    // Updating single book
    public int updateImagenEstado(Imagen imagen) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ESTADO, imagen.get_estado()); // get title

        // 3. updating row
        int i = db.update(TABLE_IMAGEN, //table
                values, // column/value
                KEY_IMAGEN+" = ? ", // selections
                new String[] { String.valueOf(imagen.get_idImagen()) }); //selection args

        // 4. close
        db.close();

        return i;
    }



    public void deleteImagen(Imagen imagen) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_IMAGEN,
                KEY_IMAGEN+" = ?",
                new String[] { String.valueOf(imagen.get_idImagen()) });

        // 3. close
        db.close();
    }



    public String getParametro(int parametro){


        String query = "SELECT valor FROM " + TABLE_PARAMETRO +" where id_parametro ="+String.valueOf(parametro);


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);



        if (cursor != null)
            cursor.moveToFirst();


        String serial="";
        try
        {
            if(!cursor.isNull(0));
            {
                serial=cursor.getString(0);
            }
        }
        catch(Exception ex)
        {
            serial="";
        }



        return serial;
    }


    public void updateParametro(int parametro,String valor) {

        deleteParametro(parametro);
        addParametro(parametro,valor);

    }

    public void deleteParametro(int idParametro) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_PARAMETRO,
                "id_parametro = ?",
                new String[] { String.valueOf(idParametro) });


        db.close();
    }



    public void addParametro(int idParametro,String valor){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("id_parametro", idParametro);
        values.put("valor", valor);

        // 3. insert
        db.insert(TABLE_PARAMETRO, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();


    }


    private static byte[] key = "C1t1M0t1".getBytes();// 64 bit
    private static byte[] iv = "C1t1M0t1".getBytes();

    public static String encrypt(String in) {
        String cypert = in;
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec k = new SecretKeySpec(key, "DES");
            Cipher c = Cipher.getInstance("DES/CBC/PKCS7Padding");
            c.init(Cipher.ENCRYPT_MODE, k, ivSpec);
            byte[] encryptedData = c.doFinal(in.getBytes());
            cypert = Base64.encodeToString(encryptedData,Base64.DEFAULT);
        } catch (Exception e) {
            String prueba=e.getMessage();
        }
        return cypert;
    }


    public static String decrypt(String in) throws Exception {
        String plain=in;
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keys = new SecretKeySpec(key, "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, keys, ivSpec);
            // decryption pass
            byte[] cipherText = Base64.decode(in,Base64.DEFAULT);
            int ctLength = cipherText.length;
            byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(cipher.doFinal(cipherText));
            plainText = bos.toByteArray();
            bos.close();
            plain = new String(plainText, "UTF8");
        } catch (Exception e) {
            String prueba=e.getMessage();
        }
        return plain;
    }



}

