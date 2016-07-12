package Biblioteka2NetBeans;
import java.util.Vector;
public class Czytelnik extends Thread{
	private Vector<Ksiazka> listaPotrzebnych=new Vector<Ksiazka>();
	private Vector<Ksiazka> listaWypozyczonych=new Vector<Ksiazka>();
	private int nrAktualOdwiedzBiblioteki=0;
	private int index;
		
        public int getIndex(){
            return index;
        }
	public Vector<Ksiazka> getListaPotrzebnych(){
		return listaPotrzebnych;
	}
	public Vector<Ksiazka> getListaWypozyczonych(){
		return listaWypozyczonych;
	}	
	public Czytelnik(int index){
		this.index=index;
	}
	@Override
	public void run(){
               //TEST: 
               System.out.println("stworzył się czytelnik "+index);
               //====
                synchronized(Program.KONSOLA){
                    
                    
                    
                    
		while(listaPotrzebnych.size()>0||listaWypozyczonych.size()>0){
                                /*
                                int z=0;
                                for(int k=0; k<Program.biblioteki.size(); k++){
                                    if(Program.biblioteki.get(k).isAlive()==false){
                                        z++;
                                    }
                                }
                                if(z>=Program.biblioteki.size()){
                                    Program.KONSOLA.notifyAll();
                                    //TEST
                                    System.out.println("Wszystkie biblioteki nieczynne więc IDE SPAC");
                                    //===
                                    return;
                                }
                                */
				boolean nieMaGoNaListach=true;
				while(nieMaGoNaListach){
					//jeżeli znajduję się na którejkolwiek liście biblioteki to nie można go dodać na liście innej biblioteki 
					for(int i=0; i<Program.liczbaBibliotek; i++){			
						if(Program.biblioteki.get(i).getListaOczekujacych().size()!=0){
							for(int k=0; k<Program.biblioteki.get(i).getListaOczekujacych().size(); k++){
								if(Program.biblioteki.get(i).getListaOczekujacych().get(k)==index){
									nieMaGoNaListach=false; //czyli jest na któreś z list oczekujących
								} 
							}
						}
					}
					if(nieMaGoNaListach==false){
						nieMaGoNaListach=true;
						Program.KONSOLA.notifyAll();
						try{
							Program.KONSOLA.wait();
						}catch(InterruptedException e){}
					}
					else{
						nieMaGoNaListach=false;
					}
				}		
				if(listaWypozyczonych.isEmpty()==false){
					nrAktualOdwiedzBiblioteki=listaWypozyczonych.firstElement().getNrBiblioteki();
                                        try{
                                            Thread.sleep(listaWypozyczonych.firstElement().getPages());
					}catch(InterruptedException ert){}
						
				}
				Program.biblioteki.get(nrAktualOdwiedzBiblioteki).getListaOczekujacych().add(index);
                                 //T E S T ::
                                System.out.println("czytelnik"+index+" dodał sie na liste biblioteki"+ nrAktualOdwiedzBiblioteki);
                                //==========
				nrAktualOdwiedzBiblioteki++;
                                if(nrAktualOdwiedzBiblioteki>=Program.biblioteki.size()){
                                    nrAktualOdwiedzBiblioteki=0;
                                }
				Program.KONSOLA.notifyAll();
				try{
					Program.KONSOLA.wait();
				}catch(InterruptedException e){}
		}
                Program.KONSOLA.notifyAll();
                System.out.println("KONIEC CZYTELNIKA "+index);
                return;
            }
	}
//==================
}