package com.ims.tool.ui.window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.ims.tool.domain.ToolConfig.ConfigParams;
import com.ims.tool.util.ResourcePaths;
import com.ims.tool.util.SetupUtil;

public class UrlWindow extends Stage {

	public UrlWindow() {
		GridPane grid = createRoot();
		addChildren(grid);
		initStage(grid);
	}

	private GridPane createRoot() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(15, 15, 15, 15));
		return grid;
	}

	private void addChildren(GridPane grid) {
		Label urlL = new Label("URL");
		grid.add(urlL, 0, 1);
		TextField url = new TextField();
		url.setPromptText("https://");
		url.setMinWidth(300);
		grid.add(url, 1, 1);

		Button save = new Button("Save");
		save.getStyleClass().add("save-button");
		save.setCursor(Cursor.HAND);
		grid.add(save, 1, 3);
		save.setOnAction(e -> {
			SetupUtil.getToolConfig().set(ConfigParams.URL, url.getText());
			SetupUtil.updateToolConfig();
			close();
		});
		url.setText(SetupUtil.getToolConfig().get(ConfigParams.URL));
	}

	private void initStage(GridPane grid) {
		Scene scene = new Scene(grid, 400, 120);
		scene.getStylesheets().add(ResourcePaths.STYLE_CSS);
		setTitle("SOAP Url");
		initStyle(StageStyle.UTILITY);
		initModality(Modality.APPLICATION_MODAL);
		setResizable(false);
		setScene(scene);
	}
}
