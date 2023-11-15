import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private String identificacion;
    private String direccion;
    private String ciudad;
    private List<Cuenta> cuentas;
    private String puntuacion;

    public Cliente(String nombre, String identificacion, String direccion, String ciudad) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.cuentas = new ArrayList<>();
    }

    public void agregarCuenta(String tipo, int saldoCupo, String banco, String redTarjeta, String categoriaTarjeta) {
        Cuenta cuenta = new Cuenta(tipo, saldoCupo, banco, redTarjeta, categoriaTarjeta);
        cuentas.add(cuenta);
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public String getPuntuacion() {
        return puntuacion;
    }
}