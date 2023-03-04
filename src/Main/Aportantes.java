package Main;

public class Aportantes {

    private int cod_aportante,documento;
    private String Apellido, Nombre;

    public Aportantes(int cod_aportante, int documento, String apellido, String nombre) {
        this.cod_aportante = cod_aportante;
        this.documento = documento;
        Apellido = apellido;
        Nombre = nombre;
    }

    public Aportantes() {
    }

    public int getCod_aportante() {
        return cod_aportante;
    }

    public void setCod_aportante(int cod_aportante) {
        this.cod_aportante = cod_aportante;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }


}
