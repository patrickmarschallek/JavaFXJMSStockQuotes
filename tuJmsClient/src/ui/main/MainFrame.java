package ui.main;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.ColumnConstraintsBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.RowConstraintsBuilder;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Stock;
import ui.components.ListPanel;
import ui.components.Table;
import ui.handler.CloseHandler;
import util.Serializer;
import util.StockQuote;

public class MainFrame extends Application {

	private ArrayList<Stock> stocks = new ArrayList<Stock>();
	private HashMap<String, XYChart.Series<String, Number>> serieMap = new HashMap<String, XYChart.Series<String, Number>>();

	private Stage primaryStage;
	private LineChart<String, Number> lineChart;
	private Table table;
	private HBox buttonPanel = new HBox();
	private Button btnClose;
	private ListView<CheckBox> listView;

	@Override
	public void init() throws Exception {
		super.init();
		// init components
		this.initLineChart();
		this.initTable();
		this.initBottomPanel();
		this.initList();
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.primaryStage = stage;

		// setting layout
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(20);
		grid.setVgap(15);

		// row constraints
		RowConstraints fixLineConstraint = RowConstraintsBuilder.create()
				.prefHeight(60).build();

		RowConstraints chartLineConstraint = RowConstraintsBuilder.create()
				.fillHeight(true).minHeight(300).build();
		chartLineConstraint.setVgrow(Priority.ALWAYS);

		RowConstraints tableLineConstraint = RowConstraintsBuilder.create()
				.percentHeight(15).build();

		// column constraints
		ColumnConstraints fullConstraint = ColumnConstraintsBuilder.create()
				.minWidth(300).fillWidth(true).build();

		ColumnConstraints listConstraint = ColumnConstraintsBuilder.create()
				.minWidth(250).build();
		fullConstraint.setHgrow(Priority.ALWAYS);

		grid.getRowConstraints().addAll(chartLineConstraint,
				tableLineConstraint, fixLineConstraint);

		grid.getColumnConstraints().addAll(listConstraint, fullConstraint);
		grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		grid.setPadding(new Insets(10, 10, 10, 10));

		grid.add(this.lineChart, 1, 0);
		grid.add(this.table, 1, 1);
		grid.add(this.buttonPanel, 1, 2);
		grid.add(this.listView, 0, 0, 1, 3);

		Scene scene = new Scene(grid, 900, 600);

		// Window Properties
		this.primaryStage.setTitle("JMS Stock Client");
		this.primaryStage.setScene(scene);
		this.primaryStage.sizeToScene();
		this.primaryStage.setResizable(false);
		this.primaryStage.show();

		// click close event handle
		this.primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});

	}

	public void initList() {
		ArrayList<StockQuote> list = new ArrayList<StockQuote>();

		Serializer ser = new Serializer(list, "stockList.ser");
		try {
			list = ser.readObject(this);
		} catch (IOException e) {
			// Dialog.showErrorDialog(this.primaryStage, "deserialize Error",
			// "cant read", "Error");
		}
		this.listView = new ListPanel(this, list);
	}

	public void initTable() {
		this.table = new Table(this);
	}

	public void initLineChart() {
		// defining the axes
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("time of day");

		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("price");

		// creating the chart
		this.lineChart = new LineChart<String, Number>(xAxis, yAxis);
		this.lineChart.setTitle("Stock Monitoring, 2013");

		// defining a series
		for (Stock stock : this.stocks) {
			this.createSerie(stock);
		}
	}

	public void initBottomPanel() {

		this.btnClose = new Button();
		this.btnClose.setText("Save & Close");
		this.btnClose.setOnAction(new CloseHandler(this));

		this.buttonPanel.getChildren().addAll(this.btnClose);
		this.buttonPanel.setAlignment(Pos.CENTER_LEFT);
	}

	public void fillSerie(final XYChart.Series<String, Number> serie,
			long time, double quote) {
		Time value = new Time(time);
		// populating the series with data
		final Data<String, Number> data = new XYChart.Data<String, Number>(
				value.toString(), quote);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				serie.getData().add(data);
			}
		});
	}

	public void dispose() {
		try {
			this.getPrimaryStage().hide();
			this.getPrimaryStage().close();
			this.stop();
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XYChart.Series<String, Number> createSerie(Stock stock) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName(stock.getName());

		this.serieMap.put(stock.getName(), series);
		this.lineChart.getData().add(series);
		return series;
	}

	public HashMap<String, XYChart.Series<String, Number>> getSerieMap() {
		return serieMap;
	}

	public ArrayList<Stock> getStocks() {
		return stocks;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public LineChart<String, Number> getLineChart() {
		return lineChart;
	}

	public Table getTable() {
		return this.table;
	}

	public HBox getButtonPanel() {
		return buttonPanel;
	}

	public Button getBtnClose() {
		return btnClose;
	}

}
