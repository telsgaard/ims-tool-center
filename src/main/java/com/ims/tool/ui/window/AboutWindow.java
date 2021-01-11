package com.ims.tool.ui.window;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.ims.tool.util.ResourcePaths;

public class AboutWindow extends Stage {

	public AboutWindow() {
		VBox vBox = createRoot();
		addChildren(vBox);
		initStage(vBox);
	}

	private VBox createRoot() {
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(15, 15, 15, 15));
		return vBox;
	}

	private void addChildren(VBox vBox) {
		ImageView toolbox = new ImageView(ResourcePaths.TOOLBOX_IMAGE);

		Label appName = new Label("TIMEZONE4 Site Reliability Engineering");
		appName.setTextAlignment(TextAlignment.CENTER);
		appName.setFont(Font.font(null, FontWeight.BOLD, 13));

		Label company = new Label("Copyright © 2017-2018 IMS Tool Center");
		company.setFont(Font.font(null, FontWeight.NORMAL, 10));
		company.setTextAlignment(TextAlignment.CENTER);

		Label version = new Label("1.0.0");
		version.setFont(Font.font(null, FontWeight.BOLD, 12));
		version.setTextAlignment(TextAlignment.CENTER);
		
		Label contact = new Label("contact@timezone4.com");
		contact.setFont(Font.font(null, FontWeight.NORMAL, 11));
		contact.setTextAlignment(TextAlignment.CENTER);

		vBox.getChildren().addAll(toolbox, appName, version, contact, company);
	}

	private void initStage(VBox vBox) {
		Scene scene = new Scene(vBox, 350, 260);
		scene.getStylesheets().add(ResourcePaths.STYLE_CSS);
		setTitle("About");
		initStyle(StageStyle.UTILITY);
		initModality(Modality.APPLICATION_MODAL);
		setResizable(false);
		setScene(scene);
	}
}
