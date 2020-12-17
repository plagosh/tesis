package cl.paulina.yotrabajoconpecs.MisClases.LibroEmpleador;

public class MensajeDeTexto {
    private String id;
    private String mensaje;
    //emisor: 1, receptor: 2
    private int tipoMensaje;
    private String HoraDelMensaje;
    private String categoriaMensaje;

    public MensajeDeTexto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(int tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getHoraDelMensaje() {
        return HoraDelMensaje;
    }

    public void setHoraDelMensaje(String horaDelMensaje) {
        HoraDelMensaje = horaDelMensaje;
    }

    public String getCategoriaMensaje() {
        return categoriaMensaje;
    }

    public void setCategoriaMensaje(String categoriaMensaje) {
        this.categoriaMensaje = categoriaMensaje;
    }
}
