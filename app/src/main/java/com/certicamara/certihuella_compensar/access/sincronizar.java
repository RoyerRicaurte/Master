package com.certicamara.certihuella_compensar.access;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.List;

/**
 * Created by Montreal Office on 4/12/2016.
 */

public class sincronizar {

    public static String urlServicio="http://190.131.205.170:8083/Android/Service.svc";
    public static String directorioImagenes;

    public static String EnviarImagenesPendientes(Context context)
    {
        String resultado="OK";
        sqliteHelper bd= new sqliteHelper(context);
        List<Imagen> imagenesDisponibles=bd.getImagenesSinSincronizar();

        directorioImagenes = Environment.getExternalStorageDirectory().toString()+"/Android/data/"+context.getPackageName()+"/files/Pictures/";

        if(imagenesDisponibles.size()>0)
        {
            if(TestConexion()) {
                for(Imagen img : imagenesDisponibles) {
                    if(EnviarImagen(context, img.get_idSolicitud(),img.get_idTipo(),img.get_idImagen(),img.get_nombre()))
                    {
                        img.set_estado("Enviado");
                        bd.updateImagenEstado(img);
                    }
                }
            }
            else
                resultado="ERROR_CONEXION";

        }
        return  resultado;
    }

    public static boolean TransferirDocumento(int idSolicitud,String tipoDocumento)
    {

        String urlServidor = urlServicio+"/TransferirDocumentos/"+String.valueOf(idSolicitud)+"/"+tipoDocumento;
        // String urlServidor="http://192.168.170.111:1001/CoreCitiMotion.svc/PruebasPDF";
        URL url;
        String respuesta = "";
        try {
            url = new URL(urlServidor);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoInput(true);
            conexion.setDoOutput(false);
            InputStreamReader canal = new InputStreamReader(conexion.getInputStream());
            BufferedReader lectura = new BufferedReader(canal);
            String linea = lectura.readLine();
            while (linea != null) {
                respuesta = respuesta + linea;
                linea = lectura.readLine();
            }
            lectura.close();
            conexion.disconnect();
            respuesta = respuesta
                    .replaceAll(
                            "<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">",
                            "");
            respuesta = respuesta.replaceAll("</string>", "");
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (respuesta.equals("OK"))
            return true;

        return false;
    }


    public static String CumpleCondicionesSolicitud(Context context,int idSolicitud)
    {
        String resultado="OK";
        boolean existe2EnCedula=false;
        boolean existe1CertificadoLaboral=false;
        boolean existe1Desprendible=false;
        sqliteHelper bd= new sqliteHelper(context);
        List<Imagen> imagenes= bd.countImagenTipo(idSolicitud);
        for(Imagen img: imagenes)
        {
            if(img.get_idTipo()==1 )
            {
                if(img.get_idImagen()==2)
                    existe2EnCedula=true;
            }
            else if(img.get_idTipo()==2 ) {
                if (img.get_idImagen() > 0)
                    existe1CertificadoLaboral = true;
            }
            else if(img.get_idTipo()==3 ) {
                if (img.get_idImagen() > 0)
                    existe1Desprendible = true;
            }

        }

        if(!existe2EnCedula)
            resultado="Debe agregar 2 documentos en Cedula de Ciudadania";
        else if(!existe1CertificadoLaboral)
            resultado="Debe agregar 1 documento en Desprendibles de Nomina";
        else if(!existe1Desprendible)
            resultado="Debe agregar 1 documento en Desprendibles de Nomina";

        return  resultado;

    }


    private static boolean TestConexion()
    {

        String urlServidor = urlServicio+"/test/OK";
        // String urlServidor="http://192.168.170.111:1001/CoreCitiMotion.svc/PruebasPDF";
        URL url;
        String respuesta = "";
        try {
            url = new URL(urlServidor);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoInput(true);
            conexion.setDoOutput(false);
            InputStreamReader canal = new InputStreamReader(conexion.getInputStream());
            BufferedReader lectura = new BufferedReader(canal);
            String linea = lectura.readLine();
            while (linea != null) {
                respuesta = respuesta + linea;
                linea = lectura.readLine();
            }
            lectura.close();
            conexion.disconnect();
            respuesta = respuesta
                    .replaceAll(
                            "<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">",
                            "");
            respuesta = respuesta.replaceAll("</string>", "");
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (respuesta.equals("OK;OK"))
            return  true;

        return  false;
    }

    private static boolean EnviarImagen(Context context, int idSolicitud,int idTipo,int idImagen,String nombre)
    {
        String urlServidor = urlServicio+"/InsertarImagen/"+String.valueOf(idSolicitud)+
                "/"+String.valueOf(idTipo)+"/"+String.valueOf(idImagen)+"/";
        try {
            urlServidor = urlServidor
                    + URLEncoder.encode(nombre, "utf-8").replaceAll("\\+",
                    "%20");
        }
        catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            return   false;
        }

        urlServidor = urlServidor+"/";

        URL url;
        try {
            FileInputStream fileInputStream = new FileInputStream(directorioImagenes+nombre);
            url = new URL(urlServidor);
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setDoInput(true);
            conexion.setDoOutput(true);
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Connection", "Keep-Alive");
            conexion.setRequestProperty("ENCTYPE", "multipart/form-data");
            conexion.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
            conexion.setRequestProperty("uploaded_file", directorioImagenes+nombre);
            DataOutputStream dos = new DataOutputStream(conexion.getOutputStream());
            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1 * 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            int serverResponseCode = conexion.getResponseCode();
            String serverResponseMessage = conexion.getResponseMessage();
            if (serverResponseCode == 200) {
                String prueba = "OK";
            }
            fileInputStream.close();
            // conexion.disconnect();
            dos.flush();
            dos.close();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  false;
        }
        return  true;

    }



}
