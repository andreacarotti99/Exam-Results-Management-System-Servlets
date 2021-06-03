package it.polimi.tiw.beans;

public class SavedOrder {
	private boolean ascendantOrder;
	private int clickedColumn;
	
	public SavedOrder() {
		ascendantOrder = true;
		clickedColumn = 1;
	}
	
	public void invertOrder() {
		if (ascendantOrder == true) {
			ascendantOrder = false;
		}
		else {
			ascendantOrder = true;
		}
	}
	
	
	//this method saves the correct order info based on the clicked column from the user and the last order that was displayed
	public void checkLastClicked(int newClicked) {
		if(clickedColumn == newClicked) {
			invertOrder();
		}
		else if (clickedColumn != newClicked){
			this.clickedColumn = newClicked;
			this.ascendantOrder = true;
		}
	}
	
	
	public boolean isAscendantOrder() {
		return this.ascendantOrder;
	}
	
	public int getClickedColumn() {
		return this.clickedColumn;
	}
	
	
}
