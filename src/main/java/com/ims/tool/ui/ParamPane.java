package com.ims.tool.ui;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.ims.tool.domain.Param;
import com.ims.tool.util.SetupUtil;

public class ParamPane extends VBox {

	private TableView<Param> tableView;

	@SuppressWarnings("unchecked")
	public ParamPane() {
		TableColumn<Param, String> paramCol = new TableColumn<>("Parameter");
		paramCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		paramCol.setStyle("-fx-font-weight:bold;");
		paramCol.setCellFactory(TextFieldTableCell.<Param> forTableColumn());

		TableColumn<Param, String> valueCol = new TableColumn<>("Value");
		valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
		valueCol.setCellFactory(TextFieldTableCell.<Param> forTableColumn());

		EventHandler<TableColumn.CellEditEvent<Param, String>> handler = new EventHandler<TableColumn.CellEditEvent<Param, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Param, String> change) {
				Param param = (Param) change.getTableView().getItems()
						.get(change.getTablePosition().getRow());

				SetupUtil.getParameterConfig().removeParam(param);

				if (change.getTablePosition().getColumn() == 0) {
					param.setName(change.getNewValue());
				} else {
					param.setValue(change.getNewValue());
				}

				SetupUtil.getParameterConfig().addParam(param);
				SetupUtil.updateParameterConfig();
				
				if(change.getTablePosition().getRow() == tableView.getItems().size() - 1) {
					Param blank = new Param();
					tableView.getItems().add(blank);
				}
			}
		};

		valueCol.setOnEditCommit(handler);
		paramCol.setOnEditCommit(handler);

		valueCol.setSortable(false);
		paramCol.setSortable(false);

		tableView = new TableView<>();
		tableView.getColumns().addAll(paramCol, valueCol);

		ObservableList<Param> params = FXCollections
				.observableArrayList(SetupUtil.getParameterConfig().getParams());
		tableView.setItems(params);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableView.setEditable(true);
		tableView.setFixedCellSize(31);
		
		Param blank = new Param();
		tableView.getItems().add(blank);

		getChildren().add(tableView);
		VBox.setVgrow(tableView, Priority.ALWAYS);

		tableView.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.DELETE)) {
				Param param = tableView.getSelectionModel().getSelectedItem();

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation");
				alert.setHeaderText("Do you really want to delete '"
						+ param.getName() + "' parameter?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					SetupUtil.getParameterConfig().removeParam(param);
					tableView.getItems().remove(param);
				}
			}
		});
	}
}