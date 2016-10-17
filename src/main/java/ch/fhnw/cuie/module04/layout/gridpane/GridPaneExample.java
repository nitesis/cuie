package ch.fhnw.cuie.module04.layout.gridpane;

import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class GridPaneExample extends GridPane {
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;
	private Button button5;
	private Button button6;

	public GridPaneExample() {
		initializeControls();
		layoutControls();
	}

	private void initializeControls() {
		button1 = createButton("first");
		button2 = createButton("second");
		button3 = createButton("third");
		button4 = createButton("fourth");
		button5 = createButton("fifth");
		button6 = createButton("sixth");
	}

	private void layoutControls() {
		ColumnConstraints cc = new ColumnConstraints();
		cc.setHgrow(Priority.ALWAYS);
		getColumnConstraints().addAll(cc, cc, cc);

		RowConstraints rc = new RowConstraints();
		rc.setVgrow(Priority.ALWAYS);
		getRowConstraints().addAll(rc, rc, rc);

		add(button1, 0, 0);         //colIndex, rowIndex
		add(button2, 1, 0);
		add(button5, 2, 0, 1, 2);  //colIndex, rowIndex, colSpan, rowSpan
		add(button3, 0, 1);
		add(button4, 1, 1);
		add(button6, 0, 2, 3, 1);
	}

	private Button createButton(String text){
		Button button = new Button(text);
		button.setMaxWidth(Double.MAX_VALUE);
		button.setMaxHeight(Double.MAX_VALUE);

		return button;
	}
}
