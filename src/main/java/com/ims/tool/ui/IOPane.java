package com.ims.tool.ui;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import org.apache.commons.io.FileUtils;

import com.ims.tool.util.XMLFormatter;

public class IOPane extends TabPane implements IOControl {

	private static final Logger logger = Logger.getLogger(IOPane.class.getName());

	private static int requestNoCounter = 1;

	public IOPane() {
		init();
		addChildren();
	}

	@Override
	public void setResponseText(String responseXML) {
		Tab selectedItem = getSelectionModel().getSelectedItem();
		TextArea responseArea = (TextArea) ((SplitPane) selectedItem.getContent()).getItems().get(1);
		responseArea.setText(XMLFormatter.format(responseXML));
	}

	@Override
	public String getRequestText() {
		Tab selectedItem = getSelectionModel().getSelectedItem();
		TextArea requestArea = (TextArea) ((SplitPane) selectedItem.getContent()).getItems().get(0);
		return requestArea.getText();
	}

	@Override
	public void setRequestText(String requestText) {
		Tab selectedItem = getSelectionModel().getSelectedItem();
		TextArea requestArea = (TextArea) ((SplitPane) selectedItem.getContent()).getItems().get(0);
		requestArea.setText(requestText);
	}

	@Override
	public void clear() {
		Tab selectedItem = getSelectionModel().getSelectedItem();
		TextArea responseArea = (TextArea) ((SplitPane) selectedItem.getContent()).getItems().get(1);
		responseArea.clear();
	}

	@Override
	public void addTab() {
		Node ioTab = createIOTab();
		Tab tab = new Tab("Request " + requestNoCounter++);
		tab.setContent(ioTab);
		getTabs().add(tab);
		getSelectionModel().select(tab);
	}

	@Override
	public void addTab(File xmlFile) {
		try {
			String request = XMLFormatter.format(FileUtils.readFileToString(xmlFile, StandardCharsets.UTF_8));
			Node ioTab = createIOTab();
			Tab tab = new Tab(xmlFile.getName());
			tab.setContent(ioTab);
			tab.setTooltip(new Tooltip(xmlFile.getPath()));
			getTabs().add(tab);
			getSelectionModel().select(tab);
			setRequestText(request);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error in opening file.", e);
		}
	}

	private void init() {
		initRequestArea();
		initResponseArea();
	}

	private void addChildren() {
		Node ioTab = createIOTab();

		Tab tab = new Tab("Request " + requestNoCounter++);
		tab.setContent(ioTab);
		tab.setClosable(false);
		getTabs().add(tab);
	}

	private Node createIOTab() {
		TextArea requestArea = initRequestArea();
		TextArea responseArea = initResponseArea();

		SplitPane splitPane = new SplitPane(requestArea, responseArea);
		splitPane.setBackground(new Background(new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
		splitPane.setPadding(new Insets(5));
		splitPane.setOrientation(Orientation.VERTICAL);
		splitPane.setDividerPositions(0.7);
		return splitPane;
	}

	private TextArea initResponseArea() {
		TextArea responseArea = new TextArea();
		responseArea.setPromptText("Response will be shown here.");
		responseArea.getStyleClass().add("response-area");
		responseArea.setEditable(false);
		return responseArea;
	}

	private TextArea initRequestArea() {
		TextArea requestArea = new TextArea();
		requestArea.setPromptText("Enter SOAP Envelop here.");
		requestArea.getStyleClass().add("request-area");
		return requestArea;
	}

}
