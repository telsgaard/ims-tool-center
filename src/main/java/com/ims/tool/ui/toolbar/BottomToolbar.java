package com.ims.tool.ui.toolbar;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpStatus;

import com.ims.tool.client.Response;
import com.ims.tool.client.SOAPClient;
import com.ims.tool.ui.IOControl;

public class BottomToolbar extends HBox {

	private final IOControl ioControl;

	private final SOAPClient soapClient;

	private Label requestTime;

	private Circle light;

	public BottomToolbar(IOControl ioControl) {
		super();
		this.ioControl = ioControl;
		soapClient = new SOAPClient();
		init();
		addChildren();
	}

	private void init() {
		setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		setPadding(new Insets(5));
		setSpacing(10);
	}

	private void addChildren() {
		requestTime = new Label();
		requestTime.setText(responseTime(0));
		requestTime.setTextFill(Color.ANTIQUEWHITE);
		requestTime.setPadding(new Insets(5, 0, 0, 0));

		light = new Circle(7, Color.DARKGRAY);
		light.setStroke(Color.BISQUE);

		Region region = new Region();
		getChildren().addAll(sendButton(), clearButton(), region, requestTime, light);
		HBox.setHgrow(region, Priority.ALWAYS);
		HBox.setMargin(light, new Insets(6, 0, 0, 0));
	}

	private String responseTime(long millis) {
		return String.format("Response time : %dms", millis);
	}

	private Node sendButton() {
		Button send = new Button("Send");
		send.getStyleClass().add("send-button");
		send.setTooltip(new Tooltip("Click to send SOAP request."));
		send.setCursor(Cursor.HAND);

		send.setOnAction(e -> {
			if ("".equals(ioControl.getRequestText().trim())) {
				alertForEmptyInput();
				requestTime.setText(responseTime(0));
				light.setFill(Color.DARKGRAY);
			} else {
				Response response;
				try {
					response = soapClient.executeRequest(ioControl
							.getRequestText());
					if (response.getHttpStatus() == HttpStatus.SC_OK) {
						light.setFill(Color.rgb(17, 244, 127));
					} else {
						light.setFill(Color.RED);
					}
					String time = String.format(responseTime(soapClient
							.getLastRequestTime()));
					requestTime.setText(time);
					ioControl.setResponseText(response.getResponseBody());
				} catch (ConnectTimeoutException ce) {
					alertForConnectionTimeout();
				}
			}
		});

		return send;
	}

	private Node clearButton() {
		Button clear = new Button("Clear");
		clear.getStyleClass().add("clear-button");
		clear.setOnAction(e -> ioControl.clear());
		clear.setTooltip(new Tooltip("Clear request and response text."));
		clear.setCursor(Cursor.HAND);
		return clear;
	}

	private void alertForEmptyInput() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.show();
		alert.setTitle("Oops!");
		alert.setHeaderText("No input to process");
		alert.setContentText("Please enter SOAP envelope XML.");
	}

	private void alertForConnectionTimeout() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.show();
		alert.setTitle("Oops!");
		alert.setHeaderText("Connection failed!");
		alert.setContentText("Failed to connect SOAP Server.");
	}
}
