package application;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class Controladora {

	@FXML
	private Button btnInsertar;

	@FXML
	private Button btnArchivo;

	@FXML
	private Button btnDerecha;

	@FXML
	private Button btnIzquierdo;

	@FXML
	private TextField txtf_nombre;

	@FXML
	private TextField txtf_ruta;

	@FXML
	private ImageView Imagen;

	@FXML
	private Label lb_nombre;

	private int position;

	private int maximo;

	private ConexionBBDD con;


	private File file;

	ArrayList<String> lista;




	public void initialize() throws SQLException{

		con = new ConexionBBDD();
		lista = con.LeerPersona();

		position = 0;
		maximo = lista.size();

		if(maximo > 0){


			// crear el Image y mostrarlo en el ImageView
		    Image img = new Image(new ByteArrayInputStream(con.LeerFoto(lista.get(0))));
		    Imagen.setImage(img);

		}


	}

	public void seleccionarimagen(){


		// TODO Auto-generated constructor stub
		// muestra el cuadro de diálogo de archivos, para que el usuario pueda elegir el archivo a abrir
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Buscar Imagen");

		// Agregar filtros para facilitar la busqueda
		fileChooser.getExtensionFilters().addAll(
		                new FileChooser.ExtensionFilter("JPG", "*.jpeg"),
		                new FileChooser.ExtensionFilter("PNG", "*.png"),
		                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
		 );

		 Window stage = null;

		 // Obtener el archivo seleccionado
		 file = fileChooser.showOpenDialog(stage);

		 txtf_ruta.setText("fichero seleccionado");

	}

	public void insertar() throws SQLException{

		if(txtf_nombre.getText()!="" && txtf_ruta.getText()!=""){
			con.InsertarFoto(txtf_nombre.getText(),file);
		}

	}


	public void derecha() throws SQLException{

		position++;
		if(position == maximo){
			position--;
		}

		// crear el Image y mostrarlo en el ImageView
		lb_nombre.setText(lista.get(position));
	    Image img = new Image(new ByteArrayInputStream(con.LeerFoto(lista.get(position))));
	    Imagen.setImage(img);

	}

	public void izquierda() throws SQLException{

		position--;
		if(position == -1){
			position++;
		}

		// crear el Image y mostrarlo en el ImageView
		lb_nombre.setText(lista.get(position));
	    Image img = new Image(new ByteArrayInputStream(con.LeerFoto(lista.get(position))));
	    Imagen.setImage(img);


	}
}
