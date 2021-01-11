package com.ims.tool.ui.toolbar;

import java.io.File;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import com.ims.tool.ui.IOControl;
import com.ims.tool.ui.window.AboutWindow;
import com.ims.tool.ui.window.UrlWindow;
import com.ims.tool.ui.window.UserWindow;
import com.ims.tool.util.ResourcePaths;
import com.ims.tool.util.XMLFormatter;

public class ConfigToolBar extends ToolBar {

	private final IOControl ioControl;

	private final Stage rootStage;

	public ConfigToolBar(IOControl ioControl, Stage rootStage) {
		super();
		this.rootStage = rootStage;
		this.ioControl = ioControl;
		getItems().addAll(createUserButton(), createURLButton(), createFormatterButton(), createAddTabButton(), createUploadFileButton(),
				createAppInfoButton());
		setBackground(new Background(new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
	}

	private Node createAppInfoButton() {
		Button appInfo = new Button();
		appInfo.getStyleClass().add("toolbar-button");
		appInfo.setTooltip(new Tooltip("About this application"));
		appInfo.setCursor(Cursor.HAND);
		appInfo.setGraphic(new ImageView(ResourcePaths.INFO_IMAGE));
		appInfo.setOnAction(e -> {
			AboutWindow aboutWindow = new AboutWindow();
			aboutWindow.initOwner(rootStage);
			aboutWindow.centerOnScreen();
			aboutWindow.show();
		});
		return appInfo;
	}

	private Button createURLButton() {
		Button url = new Button();
		url.getStyleClass().add("toolbar-button");
		url.setTooltip(new Tooltip("Set SOAP web service URL."));
		url.setCursor(Cursor.HAND);
		url.setGraphic(new ImageView(ResourcePaths.LINK_IMAGE));
		url.setOnAction(e -> {
			UrlWindow urlWindow = new UrlWindow();
			urlWindow.initOwner(rootStage);
			urlWindow.centerOnScreen();
			urlWindow.show();
		});
		return url;
	}

	private Button createUserButton() {
		Button user = new Button();
		user.getStyleClass().add("toolbar-button");
		user.setTooltip(new Tooltip("Set user credentials."));
		user.setCursor(Cursor.HAND);
		user.setGraphic(new ImageView(ResourcePaths.USER_IMAGE));
		user.setOnAction(e -> {
			UserWindow userWindow = new UserWindow();
			userWindow.initOwner(rootStage);
			userWindow.centerOnScreen();
			userWindow.show();
		});
		return user;
	}

	private Button createFormatterButton() {
		Button formatter = new Button();
		formatter.getStyleClass().add("toolbar-button");
		formatter.setTooltip(new Tooltip("Click to format request XML"));
		formatter.setCursor(Cursor.HAND);
		formatter.setGraphic(new ImageView(ResourcePaths.FORMATTER_IMAGE));
		formatter.setOnAction(e -> {
			String requestText = ioControl.getRequestText();
			ioControl.setRequestText(XMLFormatter.format(requestText));
		});
		return formatter;
	}

	private Button createAddTabButton() {
		Button addTab = new Button();
		addTab.getStyleClass().add("toolbar-button");
		addTab.setTooltip(new Tooltip("Click to add new request tab"));
		addTab.setCursor(Cursor.HAND);
		addTab.setGraphic(new ImageView(ResourcePaths.PLUS_IMAGE));
		addTab.setOnAction(e -> {
			ioControl.addTab();
		});
		return addTab;
	}

	private Button createUploadFileButton() {
		Button upload = new Button();
		upload.getStyleClass().add("toolbar-button");
		upload.setTooltip(new Tooltip("Click to upload request XML."));
		upload.setCursor(Cursor.HAND);
		upload.setGraphic(new ImageView(ResourcePaths.UPLOAD_IMAGE));
		upload.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Request XML File");
			fileChooser.setSelectedExtensionFilter(new ExtensionFilter("XML Files (*.xml)", "*.xml"));
			File xmlFile = fileChooser.showOpenDialog(rootStage);
			ioControl.addTab(xmlFile);
		});
		return upload;
	}

}