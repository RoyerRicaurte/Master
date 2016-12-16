package com.certicamara.certihuella_compensar.access;

/**
 * Created by Montreal Office on 1/12/2016.
 */



public class Imagen {
    private int _idSolicitud;
    private int _idTipo;
    private int _idImagen;
    private String _nombre;
    private String _estado;

    public int get_idSolicitud() {
        return _idSolicitud;
    }

    public void set_idSolicitud(int _idSolicitud) {
        this._idSolicitud = _idSolicitud;
    }

    public int get_idTipo() {
        return _idTipo;
    }

    public void set_idTipo(int _idTipo) {
        this._idTipo = _idTipo;
    }

    public int get_idImagen() {
        return _idImagen;
    }

    public void set_idImagen(int _idImagen) {
        this._idImagen = _idImagen;
    }

    public String get_nombre() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }


    public String get_estado() {
        return _estado;
    }

    public void set_estado(String _estado) {
        this._estado = _estado;
    }


    public String get_TipoImagen()
    {
        String m_Tipo="";

        switch(_idTipo)
        {
            case 1:
                m_Tipo="Cedula Ciudadania";
                break;
            case 2:
                m_Tipo="Certificado Laboral";
                break;
            case 3:
                m_Tipo="Desprendibles de nómina";
                break;
            case 4:
                m_Tipo="Otros(extractos, certificado de Ingresos)";
                break;
            case 5:
                m_Tipo="Correcciones de soportes";
                break;
            case 6:
                m_Tipo="Otros documentos adicionales a los soportes";
                break;
            case 7:
                m_Tipo="Libranza";
                break;
            case 8:
                m_Tipo="Pagaré y carta de instrucciones";
                break;
            case 9:
                m_Tipo="Seguro";
                break;
        }


        return m_Tipo;
    }

}