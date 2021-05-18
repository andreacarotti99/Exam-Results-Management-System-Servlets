package it.polimi.tiw.beans;

public class SavedOrder {
	private boolean ordineCrescente;
	private int clickedColumn;
	
	public SavedOrder() {
		ordineCrescente = true;
		clickedColumn = 1;
	}
	
	public void invertiOrdine() {
		if (ordineCrescente == true) {
			ordineCrescente = false;
		}
		else {
			ordineCrescente = true;
		}
	}
	
	
	//I get lastclicked from the request parameter and I execute the function before any kind of search in the db
	public void checkLastClicked(int lastClicked) {
		if(clickedColumn == lastClicked) {
			invertiOrdine();
		}
		else if (clickedColumn != lastClicked){
			this.clickedColumn = lastClicked;
			this.ordineCrescente = true;
		}
	}
	
	
	public boolean getOrdineCrescente() {
		return this.ordineCrescente;
	}
	
	public int getClickedColumn() {
		return this.clickedColumn;
	}
	
	
}
