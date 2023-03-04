package Main;

public class Alumnos {
        private String Nombre, Apellido , Carrera , Facultad;
        private int documento;


        public Alumnos(String nombre, String apellido, String carrera, String facultad, int documento) {
                Nombre = nombre;
                Apellido = apellido;
                Carrera = carrera;
                Facultad = facultad;
                this.documento = documento;
        }
        public Alumnos() {
                Nombre = "";
                Apellido = "";
                Carrera = "";
                Facultad = "";
                this.documento = 0;
        }


        public String getNombre() {
                return Nombre;
        }

        public void setNombre(String nombre) {
                Nombre = nombre;
        }

        public String getApellido() {
                return Apellido;
        }

        public void setApellido(String apellido) {
                Apellido = apellido;
        }

        public String getCarrera() {
                return Carrera;
        }

        public void setCarrera(String carrera) {
                Carrera = carrera;
        }

        public String getFacultad() {
                return Facultad;
        }

        public void setFacultad(String facultad) {
                Facultad = facultad;
        }

        public int getDocumento() {
                return documento;
        }

        public void setDocumento(int documento) {
                this.documento = documento;
        }
}
