package Biblioteka2NetBeans;

public class Ksiazka {
	private String title;
	private String author;
	private String ISBN;
	private int pages;
	private int copies;
	private int nrBiblioteki;
	private long dataPlanowanegoOddania;
	private Czytelnik ktoJaMa;
	private boolean czyJestNaStanie;
	//==================
	public Ksiazka(String title, String author, String ISBN, int pages, int copies){
		this.title=title;
		this.author=author;
		this.ISBN=ISBN;
		this.pages=pages;
		this.copies=copies;
		this.czyJestNaStanie=true;
	}
	public Ksiazka(){
	}
	
	public long getDataPlanowanegoOddania() {
		return dataPlanowanegoOddania;
	}
	public void setDataPlanowanegoOddania(long dataPlanowanegoOddania) {
		this.dataPlanowanegoOddania = dataPlanowanegoOddania;
	}
	public void setNrBiblioteki(int nrBiblioteki){
		this.nrBiblioteki=nrBiblioteki;
	}
	public int getNrBiblioteki(){
		return nrBiblioteki;
	}
	public boolean isCzyJestNaStanie() {
		return czyJestNaStanie;
	}
	public void setCzyJestNaStanie(boolean czyJestNaStanie) {
		this.czyJestNaStanie = czyJestNaStanie;
	}
	public Czytelnik getKtoJaMa(){
		return ktoJaMa;
	}
	public void setKtoJaMa(Czytelnik ktoJaMa){
		this.ktoJaMa=ktoJaMa;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
	@Override
	public String toString(){
		return title+" "+author+" "+ISBN+" "+pages+" "+copies;
	}
}
