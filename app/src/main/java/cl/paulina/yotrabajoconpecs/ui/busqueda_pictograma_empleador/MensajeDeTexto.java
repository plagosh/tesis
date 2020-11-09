package cl.paulina.yotrabajoconpecs.ui.busqueda_pictograma_empleador;

public class MensajeDeTexto {
    private String id;
    private String mensaje;
    //emisor: 1, receptor: 2
    private int tipoMensaje;
    private String HoraDelMensaje;

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
}
