/*
 *
 * Copyright (c) 2015 by FHNW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ch.fhnw.cuie.module03.resizing_a;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * @author Dieter Holz
 */
public class ResizingTemplateStarter extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Region rootPanel = new ResizingTemplatePane();

		Scene scene = new Scene(rootPanel);

		String fonts = getClass().getResource("fonts.css").toExternalForm();
		scene.getStylesheets().add(fonts);

		String stylesheet = getClass().getResource("style.css").toExternalForm();
		scene.getStylesheets().add(stylesheet);


		primaryStage.minHeightProperty().bind(Bindings.max(0, primaryStage.heightProperty().subtract(scene.heightProperty()).add(rootPanel.minHeightProperty())));
		primaryStage.minWidthProperty().bind(Bindings.max(0, primaryStage.widthProperty().subtract(scene.widthProperty()).add(rootPanel.minWidthProperty())));
		primaryStage.setTitle("Resizing Demo");
		primaryStage.setScene(scene);
		//primaryStage.sizeToScene();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
