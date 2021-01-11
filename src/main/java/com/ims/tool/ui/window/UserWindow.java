package com.ims.tool.ui.window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.ims.tool.domain.ToolConfig.ConfigParams;
import com.ims.tool.util.ResourcePaths;
import com.ims.tool.util.SetupUtil;

public class UserWindow extends Stage {

	public UserWindow() {
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
		Label userName = new Label("User");
		grid.add(userName, 0, 1);

		TextField user = new TextField();
		grid.add(user, 1, 1);

		Label pw = new Label("Password");
		grid.add(pw, 0, 2);

		PasswordField password = new PasswordField();
		grid.add(password, 1, 2);

		Button save = new Button("Save");
		save.getStyleClass().add("save-button");
		save.setCursor(Cursor.HAND);
		grid.add(save, 1, 3);

		user.setText(SetupUtil.getToolConfig().get(ConfigParams.USER));
		password.setText(SetupUtil.getToolConfig().get(ConfigParams.PASS));

		save.setOnAction(e -> {
			SetupUtil.getToolConfig().set(ConfigParams.USER, user.getText());
			SetupUtil.getToolConfig().set(ConfigParams.PASS, password.getText());
			SetupUtil.updateToolConfig();
			close();
		});
	}

	private void initStage(GridPane grid) {
		Scene scene = new Scene(grid, 350, 120);
		scene.getStylesheets().add(ResourcePaths.STYLE_CSS);
		setTitle("User credentials");
		initStyle(StageStyle.UTILITY);
		initModality(Modality.APPLICATION_MODAL);
		setResizable(false);
		setScene(scene);
	}
}
