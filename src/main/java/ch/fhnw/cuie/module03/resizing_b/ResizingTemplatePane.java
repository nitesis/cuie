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

package ch.fhnw.cuie.module03.resizing_b;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * @author Dieter Holz
 */
public class ResizingTemplatePane extends Region {
	private static final double PREFERRED_SIZE = 300;
	private static final double MINIMUM_SIZE   = 75;
	private static final double MAXIMUM_SIZE   = 800;
	private static final Color  COLOR          = Color.rgb(2, 138, 204);
	private static final double BORDER_WIDTH   = 2.0;
	private static final double LINE_WIDTH     = 1.0;
	private static final double CENTER_RADIUS  = 5.0;

	private Rectangle border;
	private Line      line1;
	private Line      line2;
	private Circle    center;
	private Rectangle innerRect;

	private Pane drawingPane;

	public ResizingTemplatePane() {
		init();
		initializeControls();
		layoutControls();
	}

	private void init() {
		Insets padding           = getPadding();
		double verticalPadding   = padding.getTop() + padding.getBottom();
		double horizontalPadding = padding.getLeft() + padding.getRight();
		setMinSize(MINIMUM_SIZE + horizontalPadding, MINIMUM_SIZE + verticalPadding);
		setPrefSize(PREFERRED_SIZE + horizontalPadding, PREFERRED_SIZE + verticalPadding);
		setMaxSize(MAXIMUM_SIZE + horizontalPadding, MAXIMUM_SIZE + verticalPadding);
	}

	private void initializeControls() {
		border = new Rectangle(0.0, 0.0, PREFERRED_SIZE, PREFERRED_SIZE);
		border.setFill(COLOR.deriveColor(0, 0.3, 1.5, 1));
		border.setStroke(COLOR);
		border.setStrokeWidth(BORDER_WIDTH);

		line1 = new Line(0.0, 0.0, PREFERRED_SIZE, PREFERRED_SIZE);
		line1.setStroke(COLOR);
		line1.setStrokeWidth(LINE_WIDTH);

		line2 = new Line(0.0, PREFERRED_SIZE, PREFERRED_SIZE, 0.0);
		line2.setStroke(COLOR);
		line2.setStrokeWidth(LINE_WIDTH);

		center = new Circle(PREFERRED_SIZE * 0.5, PREFERRED_SIZE * 0.5, CENTER_RADIUS);
		center.setFill(Color.TRANSPARENT);
		center.setStroke(COLOR);
		center.setStrokeWidth(LINE_WIDTH);

		innerRect = new Rectangle(PREFERRED_SIZE * 0.25, PREFERRED_SIZE * 0.25, PREFERRED_SIZE * 0.5, PREFERRED_SIZE * 0.5);
		innerRect.setFill(Color.TRANSPARENT);
		innerRect.setStroke(COLOR);
		innerRect.setStrokeWidth(LINE_WIDTH);

		drawingPane = new Pane();
		drawingPane.setMaxSize(PREFERRED_SIZE, PREFERRED_SIZE);
		drawingPane.setMinSize(PREFERRED_SIZE, PREFERRED_SIZE);
		drawingPane.setPrefSize(PREFERRED_SIZE, PREFERRED_SIZE);
	}

	private void layoutControls() {
		drawingPane.getChildren().addAll(border, line1, line2, center, innerRect);
		getChildren().add(drawingPane);
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();
		resize();
	}

	private void resize() {
		double width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
		double height = getHeight() - getInsets().getTop() - getInsets().getBottom();
		double size = Math.max(Math.min(Math.min(width, height), MAXIMUM_SIZE), MINIMUM_SIZE);

		double scalingFactor = size / PREFERRED_SIZE;

		if(width > 0 && height > 0){
			drawingPane.relocate((getWidth() - PREFERRED_SIZE) * 0.5, (getHeight() - PREFERRED_SIZE) * 0.5);
			drawingPane.setScaleX(scalingFactor);
			drawingPane.setScaleY(scalingFactor);
		}
	}
}
