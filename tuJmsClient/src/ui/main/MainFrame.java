package ui.main;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.ColumnConstraintsBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.RowConstraintsBuilder;
import javafx.stage.Stage;
import model.Stock;
import model.StockQuote;
import ui.components.Table;
import ui.handler.CloseHandler;
import ui.handler.SearchSubmit;
import ui.handler.UnsubscribeHandler;

public class MainFrame extends Application {

	private static final long serialVersionUID = 1L;

	private ArrayList<Stock> stocks = new ArrayList<Stock>();
	private HashMap<String, XYChart.Series<String, Number>> serieMap = new HashMap<String, XYChart.Series<String, Number>>();

	private Stage primaryStage;
	private LineChart<String, Number> lineChart;
	private Table table;
	private HBox buttonPanel = new HBox();
	private HBox searchBar = new HBox();

	private Label lblStockSubscribe;

	private TextField txtSearchField;

	private Button btnClose;

	private Button btnUnsubscribe;

	private TextField txtUnsubscribe;

	@Override
	public void init() throws Exception {
		super.init();
		// init components
		this.initSearchBar();
		this.initLineChart();
		this.initTable();
		this.initBottomPanel();
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
				.fillHeight(true)
				.minHeight(300)
				.build();
		chartLineConstraint.setVgrow(Priority.ALWAYS);
		
		RowConstraints tableLineConstraint = RowConstraintsBuilder.create()
				.percentHeight(25).build();
		
		// column constraints
		ColumnConstraints fullConstraint = ColumnConstraintsBuilder.create()
				.percentWidth(95).build();
		fullConstraint.setHgrow(Priority.ALWAYS);
		
		grid.getRowConstraints().addAll(fixLineConstraint,chartLineConstraint,tableLineConstraint,fixLineConstraint);
		grid.getColumnConstraints().add(fullConstraint);

		grid.add(this.searchBar, 0, 0);
		grid.add(this.lineChart, 0, 1);
		grid.add(this.table, 0, 2);
		grid.add(this.buttonPanel, 0, 3);

		Scene scene = new Scene(grid, 900, 600);

		// Window Properties
		primaryStage.setTitle("JMS Stock Client");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setResizable(false);
		primaryStage.show();

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
		lineChart = new LineChart<String, Number>(xAxis, yAxis);
		lineChart.setTitle("Stock Monitoring, 2013");

		// defining a series
		for (Stock stock : this.stocks) {
			this.createSerie(stock);
		}
	}

	public void initSearchBar() {
		this.lblStockSubscribe = new Label(" Enter a Stockname to subscribe:");

		this.txtSearchField = new TextField();
		txtSearchField.setEditable(true);

		Button btnSubscribe = new Button();
		btnSubscribe.setText("Subscribe");

		btnSubscribe.setOnAction(new SearchSubmit(this));

		this.searchBar.getChildren().addAll(this.lblStockSubscribe,
				this.txtSearchField, btnSubscribe);
		this.searchBar.setAlignment(Pos.CENTER_LEFT);
	}

	public void initBottomPanel() {

		this.btnClose = new Button();
		this.btnClose.setText("Save & Close");
		this.btnClose.setOnAction(new CloseHandler(this));

		this.btnUnsubscribe = new Button();
		this.btnUnsubscribe.setText("Unsubscribe");
		this.btnUnsubscribe.setOnAction(new UnsubscribeHandler(this));

		this.txtUnsubscribe = new TextField();

		this.buttonPanel.getChildren().addAll(this.btnUnsubscribe,
				this.txtUnsubscribe, this.btnClose);
		this.buttonPanel.setAlignment(Pos.CENTER_LEFT);
	}

	public void updateTableObjects(final StockQuote quote) {
		for (final StockQuote stockQuote : this.table.getItems()) {
			if (stockQuote.getName().equals(quote.getName())) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						stockQuote.setQuote(quote.getQuotePlain());
						stockQuote.setTimeInMillis(quote.getTimeInMillisPlain());
						if (stockQuote.getWkn().isEmpty()) {
							stockQuote.setWkn(quote.getWkn());
						}
						if (stockQuote.getIsin().isEmpty()) {
							stockQuote.setIsin(quote.getIsin());
						}
					}
				});
			}
		}
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

	public HBox getSearchBar() {
		return searchBar;
	}

	public Label getLblStockSubscribe() {
		return lblStockSubscribe;
	}

	public TextField getTxtSearchField() {
		return txtSearchField;
	}

	public Button getBtnClose() {
		return btnClose;
	}

	public Button getBtnUnsubscribe() {
		return btnUnsubscribe;
	}

	public TextField getTxtUnsubscribe() {
		return txtUnsubscribe;
	}

}
