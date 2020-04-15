package application;
	import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class ConexionBBDD {


	private String url= "jdbc:oracle:thin:@localhost:1521:XE";
	private String usr = "SYSTEM";
	private String pwd = "1234";

	private Connection conexion;


	public ConexionBBDD()  {

			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conexion = DriverManager.getConnection(url, usr, pwd);

				if(!conexion.isClosed()) {
					System.out.println("Conexión establecida");


				}
				else
					System.out.println("Fallo en Conexión");


			}catch (Exception e) {
				System.out.println("ERROR en conexión con ORACLE");
				e.printStackTrace();
			}

		}



	public int InsertarFoto(String nombre,File archivofoto) throws SQLException{


		//ejecuto la sentencia
		try{
			// Preparo la sentencia SQL
			String insertsql = "INSERT INTO DAVID.FOTITOS VALUES (?,?)";
			// Prepoparo la sentencia para ejecutar en la base de datos
			PreparedStatement pstmt = conexion.prepareStatement (insertsql);
			pstmt.setString(1,nombre);

			FileInputStream convertir_imagen = new FileInputStream (archivofoto);
			pstmt.setBlob(2, convertir_imagen, archivofoto.length());

			int resultado = pstmt.executeUpdate();

			if(resultado != 1)
				System.out.println("Error en la inserción " + resultado);
			else
				System.out.println("Persona insertada con éxito!!!");


		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);

			if(codeErrorSQL.equals("ORA-00001") ){
				System.out.println("Ya existe una persona con  ese email!!");
				return 1;
			}
			else{
				System.out.println("Ha habido algún problema con  Oracle al hacer la insercion");
				return 2;
			}

		}catch(FileNotFoundException FnfEx){
			System.out.println("Fichero no existe");
			return 3;
		}

		return 0;
	}


	public ArrayList<String> LeerPersona() throws SQLException{

		ArrayList<String> lista = new ArrayList<String>();

		//ejecuto la sentencia
		try{
			// Preparo la sentencia SQL
			String insertsql = "SELECT NOMBRE FROM DAVID.FOTITOS";
			// Prepoparo la sentencia para ejecutar en la base de datos
			PreparedStatement pstmt = conexion.prepareStatement (insertsql);


			ResultSet resultado = pstmt.executeQuery();

			int contador = 0;
			while(resultado.next()){
				contador++;
				lista.add(resultado.getString(1));

			}

			if(contador==0)
				System.out.println("no data found");


		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);


				System.out.println( codeErrorSQL);
		}

		return lista;
	}

	public byte[] LeerFoto(String nombre) throws SQLException{

		byte[] byteImage = null;
		//ejecuto la sentencia
		try{
			// Preparo la sentencia SQL
			String insertsql = "SELECT FOTO FROM DAVID.FOTITOS WHERE NOMBRE=?";
			// Prepoparo la sentencia para ejecutar en la base de datos
			PreparedStatement pstmt = conexion.prepareStatement (insertsql);
			pstmt.setString(1, nombre);
			ResultSet resultado = pstmt.executeQuery();

			int contador = 0;
			while(resultado.next()){
				contador++;
				// obtener la columna imagen, luego el arreglo de bytes
			    Blob blob = resultado.getBlob(1);
			    byteImage = blob.getBytes(1, (int) blob.length());

			}

			if(contador==0)
				System.out.println("no data found");

		}catch(SQLException sqle){

			int pos = sqle.getMessage().indexOf(":");
			String codeErrorSQL = sqle.getMessage().substring(0,pos);


				System.out.println( codeErrorSQL);
		}

		return byteImage;
	}






	public void CloseBBDD() throws SQLException{

		conexion.close();
	}

}

