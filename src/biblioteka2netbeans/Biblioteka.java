package Biblioteka2NetBeans;
import java.util.Vector;

public class Biblioteka extends Thread{
	private Vector<Ksiazka> listaKsiazek=new Vector<Ksiazka>();
	private Vector<Integer> listaOczekujacych=new Vector <Integer>();
	private int index;
	private int sek;	//czas pracy bibliotekarza
	private double [] ileWinni; /// zrobic osobną liste czytelników i w czytelnikach osobną listę bibliotek;
	private boolean jeszczeDziala;
	
	public Biblioteka(int index){
		this.index=index;
		sek=(int)(Math.random()*200)+300;
                ileWinni=new double[Program.liczbaCzytelnikow];
		for(int i=0;i<ileWinni.length; i++){
			ileWinni[i]=0;
                }
	}
        public boolean isJeszczeDziala(){
            return jeszczeDziala;
        }
	public double[] getIleWinni(){
		return ileWinni;
	}
	public int getIndex(){
		return index;
	}
	public Vector<Integer> getListaOczekujacych(){
		return listaOczekujacych;
	}
	public Vector<Ksiazka> getListaKsiazek(){
		return listaKsiazek;
	}	
	@Override
	public void run(){
                jeszczeDziala=true;
		while(true){
			synchronized(Program.KONSOLA){
				while(listaOczekujacych.isEmpty()){
					 //TEST:
                                        System.out.println("Biblioteka "+index+" nie ma nikogo na liscie Oczekujacych");
                                        //====
					Program.KONSOLA.notifyAll();	
					try{
						Program.KONSOLA.wait();
					}catch(InterruptedException e){}
                                            int k=0;
                                            for(int i=0; i<Program.czytelnicy.size(); i++){
                                                    if(Program.czytelnicy.get(i).getListaWypozyczonych().size()<=0 && Program.czytelnicy.get(i).getListaPotrzebnych().size()<=0){
							k++;
						}
                                            }
                                            if(k>=Program.czytelnicy.size()){
                                                    Program.KONSOLA.notifyAll();
                                                    //TEST
                                                    System.out.println("DZIAŁANIE BIBLIOTEKI "+index+" SIE SKOŃCZYŁO");
                                                    //====
                                                    jeszczeDziala=false;
                                                    return;
                                            }
				}
                                
				//!!! lista ksiazek do usuniecia z listy wypozyczonych czytelnika;
                                Vector<Integer>lista=new Vector<Integer>();
				//===
                                
				for(int i=0; i<Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaWypozyczonych().size(); i++){
					boolean ksiazkaOddana=false;
					for(int j=0; j<getListaKsiazek().size(); j++){
                                            System.out.println("numer czytelnika w kolejce "+Program.czytelnicy.get(getListaOczekujacych().firstElement()).getIndex());
						if(Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaWypozyczonych().get(i).getISBN().equals(getListaKsiazek().get(j).getISBN())&&
						   Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaWypozyczonych().get(i).getNrBiblioteki()==index&&
						   ksiazkaOddana==false){
                                                        
							long liczba=System.currentTimeMillis()-Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaWypozyczonych().get(i).getDataPlanowanegoOddania();
						//TEST
                                                System.out.println("Różnica czasu oddania "+liczba);
                                                //
                                                        if(liczba>0){
                                                            ileWinni[getListaOczekujacych().firstElement()]+=((double)Math.round(liczba/10))/100;
							}
                                                //TEST        
                                                System.out.print("ILE winien bibliotece "+index);
                                                System.out.format("%.2f%n",ileWinni[getListaOczekujacych().firstElement()]);
                                                System.out.println();
                                                //        
							Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaWypozyczonych().get(i).setCzyJestNaStanie(true);
							ksiazkaOddana=true;
							lista.add(i);
						}
					}	
				}
				for(int i=lista.size()-1; i>=0; i--){
					int index=lista.get(i);
					Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaWypozyczonych().remove(index);
				}
                                
				lista = new Vector<Integer>(); 
				//===
				for(int i=0; i<Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaPotrzebnych().size(); i++){
					boolean mamTaKsiazke=false;
					for(int j=0; j<getListaKsiazek().size(); j++){
						if(getListaKsiazek().get(j).isCzyJestNaStanie() && Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaPotrzebnych().get(i).getTitle().equals(getListaKsiazek().get(j).getTitle())&& mamTaKsiazke==false){
							Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaWypozyczonych().add(getListaKsiazek().get(j));
							mamTaKsiazke=true;
							getListaKsiazek().get(j).setDataPlanowanegoOddania(System.currentTimeMillis()+((5*getListaKsiazek().get(j).getPages())));
/*setKtoJaMa rtn Czytelnik*/                            getListaKsiazek().get(j).setKtoJaMa(Program.czytelnicy.get(getListaOczekujacych().firstElement()));
							getListaKsiazek().get(j).setCzyJestNaStanie(false);
							lista.add(i);
							
						}
					}
				}	
				for(int i=lista.size()-1; i>=0; i--){
					int index=lista.get(i);
					Program.czytelnicy.get(getListaOczekujacych().firstElement()).getListaPotrzebnych().remove(index);
				}
				
				try{
					Thread.sleep(sek);	
				}catch(InterruptedException e){}	
				//U S U W A M Y   G O Ś C I A   Z   L I S T Y   O C Z E K U J Ą C Y C H !!!!!!!!
				getListaOczekujacych().remove(0); 
				
				// P O K A Z A N I E   C Z A S U 
				 System.out.println("Aktualny czas"+(System.currentTimeMillis()/1000));				
				//O P I S Y W A N I E    S T A N U    W S Z Y S T K I C H    B I B L I O T E K 
				System.out.println("J E S T E Ś M Y   W   B I B L I O T E C E :  "+index+" w ktorej bibliotekarz pracuje tyle: "+sek);
				System.out.println(":: B I B L I O T E K I ::");
				for(int i=0; i<Program.biblioteki.size(); i++){
                                        System.out.println("BIBLIOTEKA "+i);
					for(int j=0; j<Program.biblioteki.get(i).getListaKsiazek().size(); j++){
						if(Program.biblioteki.get(i).getListaKsiazek().get(j).isCzyJestNaStanie()){
							System.out.println(Program.biblioteki.get(i).getListaKsiazek().get(j).getISBN()+" "+Program.biblioteki.get(i).getListaKsiazek().get(j).getTitle()+" - jest na stanie");
						}
						if(Program.biblioteki.get(i).getListaKsiazek().get(j).isCzyJestNaStanie()==false){
							System.out.println(Program.biblioteki.get(i).getListaKsiazek().get(j).getISBN()+" "+Program.biblioteki.get(i).getListaKsiazek().get(j).getTitle()+" - jest wypożyczona");
						}
					}
					System.out.print("Biblioteka "+i+" zarobiła: ");
					double suma=0;
					for(int j=0; j<Program.biblioteki.get(i).getIleWinni().length; j++){
                                                suma+=Program.biblioteki.get(i).getIleWinni()[j];
					}
					System.out.format("%.2f%n", suma);
				}
				//O P I S Y W A N I E   W S Z Y S T K I C H   C Z Y T E L N I K Ó W
				System.out.println(":: C Z Y T E L N I C Y ::");
				for(int i=0; i<Program.czytelnicy.size(); i++){
					System.out.println("Cztelnik "+(i));
					System.out.println("TO CZEGO NIE UDAŁO MU SIĘ WYPOŻYCZYĆ: ");
					for(int j=0; j<Program.czytelnicy.get(i).getListaPotrzebnych().size(); j++){
						System.out.println(Program.czytelnicy.get(i).getListaPotrzebnych().get(j).getTitle());
					}
					System.out.println("TO CO WYPOŻYCZYŁ:");
					for(int j=0; j<Program.czytelnicy.get(i).getListaWypozyczonych().size(); j++){
						System.out.println(Program.czytelnicy.get(i).getListaWypozyczonych().get(j).getTitle());
					}
                                        double suma=0;
                                        for(int j=0; j<Program.biblioteki.size(); j++){
							suma+=Program.biblioteki.get(j).getIleWinni()[i];
					}
					System.out.println("Jest winien bibliotekom: ");
                                        System.out.format("%.2f%n",suma);
                                        System.out.println();
				}
				//==============================
							
				Program.KONSOLA.notifyAll();	 // - wypuszczam wątki - POZWALAM SIĘ IM TERAZ OTWIERAĆ SIĘ TYM CO CHCĄ ( CZYTELNICY MOGĄ SIĘ ZAPISYWAĆ NA LISTY DO INNYCH BIBLIOTEK A BIBLIOTEKI MOGĄ SPRAWDZAĆ CZY MAJĄ KOGOŚ NA LISTACH OCZEKUJACYCH
				try{
					Program.KONSOLA.wait();
				}catch(InterruptedException e){}
			}
		}
	}
}
