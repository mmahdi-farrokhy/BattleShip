package model;

import javafx.scene.control.Button;

public class Cell extends Button {
    private int point;
    private int xPosition;
    private int yPosition;
    private boolean isSelected;
    private boolean isEditable = true;
    private boolean isEnemyCell;

    public boolean isEnemyCell() {
        return isEnemyCell;
    }

    public void setEnemyCell(boolean enemyCell) {
        isEnemyCell = enemyCell;
    }

    public Cell(int xPosition, int yPosition, boolean isEnemyCell) {
        super("");
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.isEnemyCell = isEnemyCell;
        setPrefWidth(100);
        setPrefHeight(100);
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
        if (!isEnemyCell) {
            this.setText(String.valueOf(point));
            this.setStyle("-fx-background-color: gray");
        }
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
