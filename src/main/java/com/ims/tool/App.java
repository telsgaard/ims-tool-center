package com.ims.tool;

import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import com.ims.tool.ui.IOPane;
import com.ims.tool.ui.ParamPane;
import com.ims.tool.ui.toolbar.BottomToolbar;
import com.ims.tool.ui.toolbar.ConfigToolBar;
import com.ims.tool.util.ResourcePaths;

public class App extends Application {

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final double minWidth = 750, minheight = 600;
		final double width = 1164, height = 734;
		ParamPane paramPane = new ParamPane();
		IOPane ioPane = new IOPane();
		BottomToolbar bottomToolbar = new BottomToolbar(ioPane);
		ConfigToolBar configToolBar = new ConfigToolBar(ioPane, primaryStage);

		SplitPane splitPane = new SplitPane(ioPane, paramPane);
		splitPane.setOrientation(Orientation.HORIZONTAL);
		splitPane.setDividerPositions(0.75);
		splitPane.setBackground(new Background(new BackgroundFill(
				Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));

		VBox vbox = new VBox(configToolBar, splitPane, bottomToolbar);
		vbox.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
		VBox.setVgrow(splitPane, Priority.ALWAYS);

		Scene scene = new Scene(vbox, width, height);
		scene.getStylesheets().add(ResourcePaths.STYLE_CSS);

		primaryStage.setTitle("TIMEZONE4 Site Reliability Engineering - IMS Tool Center");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setMinWidth(minWidth);
		primaryStage.setMinHeight(minheight);
		primaryStage.setMaximized(false);
		primaryStage.getIcons().add(new Image(ResourcePaths.TOOLBOX_IMAGE));
		primaryStage.show();
	}
}
