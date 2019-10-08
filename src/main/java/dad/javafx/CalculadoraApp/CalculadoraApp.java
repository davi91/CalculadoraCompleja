package dad.javafx.CalculadoraApp;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class CalculadoraApp extends Application {

	private TextField c1Text, c1TextI;
	private TextField c2Text, c2TextI;
	private TextField rText, rTextI;
	private ComboBox<String> operandoText;
	
	private Complejo operando1 = new Complejo();
	private Complejo operando2 = new Complejo();
	private Complejo resultado = new Complejo();
	private StringProperty operacion = new SimpleStringProperty();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Components setup
		
		c1Text = new TextField();
		c1Text.setPrefColumnCount(4);
		
		c1TextI = new TextField();
		c1TextI.setPrefColumnCount(4);
		
		c2Text = new TextField();
		c2Text.setPrefColumnCount(4);
		
		c2TextI = new TextField();
		c2TextI.setPrefColumnCount(4);
		
		rText = new TextField();
		rText.setDisable(true);
		rText.setPrefColumnCount(4);
		
		rTextI = new TextField();
		rTextI.setDisable(true);
		rTextI.setPrefColumnCount(4);
		
		rTextI.setDisable(true);
		
		operandoText = new ComboBox<>();
		operandoText.getItems().addAll("+", "-", "*", "/");
		
		VBox cbBox = new VBox(operandoText);
		cbBox.setAlignment(Pos.CENTER);
		cbBox.setFillWidth(false);
		
		HBox c1_box = new HBox(5, c1Text, new Label("+"), c1TextI, new Label("i"));
		HBox c2_box = new HBox(5, c2Text, new Label("+"), c2TextI, new Label("i"));
		HBox r_box = new HBox(5, rText, new Label("+"), rTextI, new Label("i"));
		
		c1_box.setAlignment(Pos.CENTER);
		c1_box.setFillHeight(false);
		
		c2_box.setAlignment(Pos.CENTER);
		c2_box.setFillHeight(false);
		
		r_box.setAlignment(Pos.CENTER);
		r_box.setFillHeight(false);
		
		Separator sep = new Separator();
		
		VBox nBox = new VBox(5, c1_box, c2_box, sep, r_box);
		nBox.setAlignment(Pos.CENTER);
		
		HBox root = new HBox(5, cbBox, nBox);
		root.setAlignment(Pos.CENTER);
		root.setFillHeight(false);
		
		Scene scene = new Scene(root, 400, 200);
		
		primaryStage.setTitle("Calculaora compleja");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// Bindings
		c1Text.textProperty().bindBidirectional(operando1.realProperty(), new NumberStringConverter());
		c1TextI.textProperty().bindBidirectional(operando1.imaginarioProperty(), new NumberStringConverter());
		
		c2Text.textProperty().bindBidirectional(operando2.realProperty(), new NumberStringConverter());
		c2TextI.textProperty().bindBidirectional(operando2.imaginarioProperty(), new NumberStringConverter());
		
		rText.textProperty().bindBidirectional(resultado.realProperty(), new NumberStringConverter());
		rTextI.textProperty().bindBidirectional(resultado.imaginarioProperty(), new NumberStringConverter());
		
		// Ahora ajustamos la operación
		operacion.bind(operandoText.getSelectionModel().selectedItemProperty());
		operacion.addListener( (o, lv, nv) -> onOperacionChanged(nv) );
		
		operandoText.getSelectionModel().selectFirst(); // Tiene que ser después de añadirle el listener
	}

	private void onOperacionChanged(String nv) {
		
		// Aquí realizamos las operaciones
		switch(nv) {
			
		case "+":
			resultado.realProperty().bind(operando1.realProperty().add(operando2.realProperty()));
			resultado.imaginarioProperty().bind(operando1.imaginarioProperty().add(operando2.imaginarioProperty()));
			break;
		
		case "-":
			resultado.realProperty().bind(operando1.realProperty().subtract(operando2.realProperty()));
			resultado.imaginarioProperty().bind(operando1.imaginarioProperty().subtract(operando2.imaginarioProperty()));
			break;
		
			
		case "*":
			resultado.realProperty().bind((operando1.realProperty().multiply(operando2.realProperty()).subtract(
					 operando1.imaginarioProperty().multiply(operando2.imaginarioProperty()))));

			resultado.imaginarioProperty().bind((operando1.realProperty().multiply(operando2.imaginarioProperty()).add(
					operando1.imaginarioProperty().multiply(operando2.realProperty()))));
			break;
		
		case "/":
			
		  	 DoubleProperty c = new SimpleDoubleProperty();
		  	 DoubleProperty d = new SimpleDoubleProperty();
		  	 
			 c.bind((operando1.realProperty().multiply(operando2.realProperty())).add(
								operando1.imaginarioProperty().multiply(operando2.imaginarioProperty())));
			
			 d.bind((operando2.imaginarioProperty().multiply(operando2.imaginarioProperty())).add(
								 operando2.realProperty().multiply(operando2.realProperty())));
			
			 resultado.realProperty().bind(c.divide(d));
			 
		  	 // Vuelvo a declarar nuevas
			 
		  	 c = new SimpleDoubleProperty();
		  	 d = new SimpleDoubleProperty();
		  	 
			 c.bind((operando1.imaginarioProperty().multiply(operando2.realProperty())).subtract(
						operando1.realProperty().multiply(operando2.imaginarioProperty())));
	
			 d.bind((operando2.realProperty().multiply(operando2.realProperty())).add(
						 operando2.imaginarioProperty().multiply(operando2.imaginarioProperty())));
	 
			
			resultado.imaginarioProperty().bind(c.divide(d));
					
			break;
		}
		
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
