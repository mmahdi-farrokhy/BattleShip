package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Cell;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private VBox enemyFieldVbox;

    @FXML
    private VBox myFieldVbox;

    @FXML
    private Label scoreLbl;

    @FXML
    private Label usernameLbl;

    @FXML
    private Button randomBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private Button gameStartBtn;

    @FXML
    private Button pauseBtn;

    private User loggedInUser;
    private Cell[][] myCells;
    private boolean gameIsStarted;
    private int myScore = 0;
    private int enemyScore = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepareFields();
        pauseBtn.setDisable(true);
        randomBtn.setOnAction(e -> prepareFields());
        exitBtn.setOnAction(e -> closeApplication());
        gameStartBtn.setOnAction(e -> startGame());
        pauseBtn.setOnAction(e -> pauseGame());
    }

    private void pauseGame() {
        randomBtn.setDisable(false);
        gameStartBtn.setDisable(false);
        exitBtn.setDisable(false);
        pauseBtn.setDisable(true);
        gameIsStarted = false;
    }

    private void startGame() {
        randomBtn.setDisable(true);
        gameStartBtn.setDisable(true);
        exitBtn.setDisable(true);
        pauseBtn.setDisable(false);
        gameIsStarted = true;
    }

    private void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    public void initPage(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        usernameLbl.setText(loggedInUser.getUserName());
        scoreLbl.setText(Integer.toString(loggedInUser.getScore()));
    }

    private void clearFields() {
        myFieldVbox.getChildren().clear();
        enemyFieldVbox.getChildren().clear();
    }

    private void prepareFields() {
        clearFields();
        myCells = getRandomField(false);
        Cell[][] enemyCells = getRandomField(true);

        for (int row = 0; row < 10; row++) {
            HBox hBoxMyField = new HBox();
            HBox hBoxEnemyField = new HBox();
            for (int column = 0; column < 10; column++) {
                hBoxMyField.getChildren().add(myCells[row][column]);
                hBoxEnemyField.getChildren().add(enemyCells[row][column]);
            }
            myFieldVbox.getChildren().add(hBoxMyField);
            enemyFieldVbox.getChildren().add(hBoxEnemyField);
        }
    }

    private void cellClick(Cell cell) {
        cell.setOnAction(e -> {
            if (cell.isEnemyCell() && !cell.isSelected() && gameIsStarted) {
                if (cell.getPoint() != 0) {
                    cell.setStyle("-fx-background-color: green");
                    loggedInUser.setScore(loggedInUser.getScore() + cell.getPoint());
                    scoreLbl.setText(Integer.toString(loggedInUser.getScore()));
                    myScore += cell.getPoint();
                    if (myScore == 50) {
                        showWinningMessage("Congratulations!", "You have won :)");
                        finishGame();
                    }
                } else {
                    cell.setStyle("-fx-background-color: red");
                }
                cell.setSelected(true);

                while (true) {
                    int x = (int) (Math.random() * 10);
                    int y = (int) (Math.random() * 10);
                    Cell currentCell = myCells[x][y];
                    if (!currentCell.isSelected()) {
                        if (currentCell.getPoint() != 0) {
                            currentCell.setStyle("-fx-background-color: green");
                            enemyScore += currentCell.getPoint();
                            if (enemyScore == 50) {
                                showWinningMessage("Shame!", "You have lost :(");
                                finishGame();
                            }
                        } else if (gameIsStarted)
                            currentCell.setStyle("-fx-background-color: red");

                        myCells[x][y].setSelected(true);
                        break;
                    }
                }
            }
        });
    }

    private void showWinningMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void finishGame() {
        randomBtn.setDisable(false);
        gameStartBtn.setDisable(false);
        exitBtn.setDisable(false);
        pauseBtn.setDisable(true);
        gameIsStarted = false;
        prepareFields();
        myScore = 0;
        enemyScore = 0;
    }

    private Cell[][] getRandomField(boolean isEnemyCell) {
        Cell[][] fieldCells = new Cell[10][10];
        for (int row = 0; row < 10; row++)
            for (int column = 0; column < 10; column++) {
                fieldCells[row][column] = new Cell(row, column, isEnemyCell);
                cellClick(fieldCells[row][column]);
            }
        int ship4 = 1;
        int ship3 = 2;
        int ship2 = 3;
        int ship1 = 4;

        while (true) {
            int x = (int) (Math.random() * 10);
            int y = (int) (Math.random() * 10);

            if (ship4 > 0) {
                if (assertShip(fieldCells, x, y, 4))
                    ship4--;
            } else if (ship3 > 0) {
                if (assertShip(fieldCells, x, y, 3))
                    ship3--;
            } else if (ship2 > 0) {
                if (assertShip(fieldCells, x, y, 2))
                    ship2--;
            } else if (ship1 > 0) {
                if (assertShip(fieldCells, x, y, 1))
                    ship1--;
            } else break;
        }

        return fieldCells;
    }

    private boolean assertShip(Cell[][] cells, int x, int y, int len) {
        return checkUpAndAssignShip(cells, x, y, len) || checkRightAndAssignShip(cells, x, y, len) ||
                checkDownAndAssignShip(cells, x, y, len) || checkLeftAndAssignShip(cells, x, y, len);
    }

    private boolean checkRectangle(Cell[][] cells, int xStart, int xEnd, int yStart, int yEnd, int len, boolean assertShip) {
        for (int i = xStart; i < xEnd; i++)
            for (int j = yStart; j < yEnd; j++) {
                try {
                    if (!assertShip) {
                        if (isShipCore(i, j, xStart, xEnd, yStart, yEnd) && !cells[i][j].isEditable())
                            return false;
                    } else {
                        cells[i][j].setEditable(false);
                        if (isShipCore(i, j, xStart, xEnd, yStart, yEnd))
                            cells[i][j].setPoint(getPoint(len));
                    }
                } catch (Exception ignored) {
                    if (isShipCore(i, j, xStart, xEnd, yStart, yEnd))
                        return false;
                }
            }

        return true;
    }

    private int getPoint(int len) {
        if (len == 4)
            return 1;
        if (len == 3)
            return 2;
        if (len == 2)
            return 3;
        if (len == 1)
            return 4;
        return 0;
    }

    private boolean isShipCore(int xPosition, int yPosition, int xStart, int xEnd, int yStart, int yEnd) {
        return (xPosition != xStart) && (xPosition != xEnd) && (yPosition != yStart) && (yPosition != yEnd);
    }

    private boolean checkUpAndAssignShip(Cell[][] cells, int x, int y, int len) {
        if (checkRectangle(cells, x - 1, x + 1, y - len, y + 1, len, false))
            return checkRectangle(cells, x - 1, x + 1, y - len, y + 1, len, true);

        else return false;
    }

    private boolean checkRightAndAssignShip(Cell[][] cells, int x, int y, int len) {
        if (checkRectangle(cells, x - 1, x + len, y - 1, y + 1, len, false))
            return checkRectangle(cells, x - 1, x + len, y - 1, y + 1, len, true);

        else return false;
    }

    private boolean checkLeftAndAssignShip(Cell[][] cells, int x, int y, int len) {
        if (checkRectangle(cells, x - len, x + 1, y - 1, y + 1, len, false))
            return checkRectangle(cells, x - len, x + 1, y - 1, y + 1, len, true);

        else return false;
    }

    private boolean checkDownAndAssignShip(Cell[][] cells, int x, int y, int len) {
        if (checkRectangle(cells, x - 1, x + 1, y - 1, y + len, len, false))
            return checkRectangle(cells, x - 1, x + 1, y - 1, y + len, len, true);

        else return false;
    }

}
