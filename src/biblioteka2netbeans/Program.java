package Biblioteka2NetBeans;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;


public class Program {
	public static Object KONSOLA=new Object();
	
	public static Vector<Ksiazka> wszyKsiazki=new Vector<Ksiazka>();
	
	public static Vector<Biblioteka> biblioteki = new Vector<Biblioteka>();
	public static Vector<Czytelnik> czytelnicy = new Vector<Czytelnik>();
	public final static int liczbaBibliotek =  5;             //(((int)Math.random()*50)+1);
	public final static int liczbaCzytelnikow = 8;            //(((int)Math.random()*50)+51);
	
	public static Vector<Ksiazka> odczyt(String plik){
		FileInputStream fis=null;
		try{
			fis=new FileInputStream(plik);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		StringBuffer tekst=new StringBuffer();
		int bajt=0;
		try{
			bajt=fis.read();
			while(bajt!=-1){
				tekst.append((char)bajt);
				bajt=fis.read();			
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			fis.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		//---------------------
		StringTokenizer st= new StringTokenizer(tekst.toString());
		int licznik=0;
		Vector<Ksiazka> lista=new Vector<Ksiazka>();
		Ksiazka przejsc=new Ksiazka();
		while(st.hasMoreTokens()){
			if(licznik%5==0){
				przejsc=new Ksiazka();
				przejsc.setTitle(st.nextToken());
			}
			if(licznik%5==1){
				przejsc.setAuthor(st.nextToken());
			}
			if(licznik%5==2){
				przejsc.setISBN(st.nextToken());
			}
			if(licznik%5==3){
				przejsc.setPages(Integer.parseInt(st.nextToken()));;
			}
			if(licznik%5==4){
				przejsc.setCopies(Integer.parseInt(st.nextToken()));;
				przejsc.setCzyJestNaStanie(true);
				lista.add(przejsc);
			}
		  licznik++;
		}	
		return lista;
	}
	public static void ustawListeKsiazek(String plik){
		Vector<Ksiazka> lista=odczyt(plik);
		for(int i=0; i<lista.size(); i++){
			if(lista.get(i).getCopies()>1){
				for(int j=0; j<lista.get(i).getCopies(); j++){
					wszyKsiazki.add(new Ksiazka(lista.get(i).getTitle(), 
                                                lista.get(i).getAuthor(),
                                                lista.get(i).getISBN(),
                                                lista.get(i).getPages(),1));
				}
			}
			else{
				wszyKsiazki.add(lista.get(i));
			}
		}
	}
	public static void ustawBibliotekiIIchKsiazki(){
		for(int i=0; i<liczbaBibliotek; i++){
			biblioteki.add(new Biblioteka(i));
		}
		//=========================rozdzielenie książek po bibliotekach============================
		int licznik=0;   //dotyczy wszyKsiazek
		int licznik2=0;	 //dotyczy liczbyBibliotek
	
		while(licznik<wszyKsiazki.size()){
			wszyKsiazki.get(licznik).setNrBiblioteki(licznik2);
			biblioteki.get(licznik2++).getListaKsiazek().add(wszyKsiazki.get(licznik));
			licznik++;
			if(licznik2>=biblioteki.size()){
				licznik2=0;
			}
		}	
	}
	public static void ustawKsiazkiDlaCzytelnikow(){
		for(int i=0; i<liczbaCzytelnikow; i++){
			czytelnicy.add(new Czytelnik(i));
		}
		//=========================rozdzielenie potrzebnych Ksiazek miedzy Czytelnikow============
		int licznik=0;
		int licznik2=0;
		while(licznik<czytelnicy.size()){
			int liczba=(int)(Math.random()*3)+1;
			while(liczba>wszyKsiazki.size()){
				liczba=(int)(Math.random()*3)+1;
			}
			for(int i=0; i<liczba; i++){
				czytelnicy.get(licznik).getListaPotrzebnych().add(wszyKsiazki.get(licznik2));
                                licznik2++;
				if(licznik2>=wszyKsiazki.size()){
					licznik2=0;
				}
			}
			licznik++;
		}
	}
	public static void zapisywanieKoncowe(){
		PrintWriter zapis=null;
		try{
			zapis=new PrintWriter("zapis.txt");
		}catch(FileNotFoundException e){
		}
		zapis.println(":: C Z Y T E L N I C Y ::\n");
		for(int i=0; i<Program.czytelnicy.size(); i++){
			zapis.println("czytelnik"+ (i+1)+"\n");
			zapis.println("TO CZEGO NIE UDAŁO IM SIĘ WYPOŻYCZYĆ: \n");
			for(int j=0; j<Program.czytelnicy.get(i).getListaPotrzebnych().size(); j++){
				zapis.println(Program.czytelnicy.get(i).getListaPotrzebnych().get(j).getTitle()+"\n");
			}
			zapis.println("ILE JEST WINIEN BIBLIOTEKOM: ");
                        double suma=0;
			for(int j=0; j<Program.biblioteki.size(); j++){
                                //TEST
                                System.out.println("Czytelnik "+i+" ,bibliotece "+j+" jest winien: "+ Program.biblioteki.get(j).getIleWinni()[i]);
                                //====
                                        zapis.println("Bibliotece "+j+" jest winien: "+ Program.biblioteki.get(j).getIleWinni()[i]+" zł");
					suma+=Program.biblioteki.get(j).getIleWinni()[i];
			}
			zapis.println("Jest winien bibliotekom: "+suma+" zł");
                       
		}
		zapis.println(":: B I B L I O T E K I ::\n");
		for(int i=0; i<Program.biblioteki.size(); i++){
			zapis.println("biblioteka"+(i+1)+"\n");
			zapis.println("KSIAZKI NA STANIE?, CZY WYPOŻYCZONE?:\n");
			for(int j=0; j<Program.biblioteki.get(i).getListaKsiazek().size(); j++){
				if(Program.biblioteki.get(i).getListaKsiazek().get(j).isCzyJestNaStanie()){
					zapis.println(Program.biblioteki.get(i).getListaKsiazek().get(j).getISBN()+" "+Program.biblioteki.get(i).getListaKsiazek().get(j).getTitle()+" - jest na stanie");
				}
				else{
					zapis.println(Program.biblioteki.get(i).getListaKsiazek().get(j).getISBN()+" "+Program.biblioteki.get(i).getListaKsiazek().get(j).getTitle()+" - jest wypożyczona");
				}
			}
			zapis.println("Ile zarobi biblioteka "+i+": ");
			double suma=0;
			for(int j=0; j<Program.biblioteki.get(i).getIleWinni().length; j++){
					suma+=Program.biblioteki.get(i).getIleWinni()[j];
			}
			zapis.println(suma+" zł");
		}
		zapis.close();
	}
	public static void main(String [] args){
	 
	 
	//P R O G R A M ::
		long start=System.currentTimeMillis();
		ustawListeKsiazek("plik.txt");
		ustawBibliotekiIIchKsiazki();
                ustawKsiazkiDlaCzytelnikow();
		//teraz wczytac plik z jakie ksiazki chca wypozyczyc czytelnicy\
                for(int i=0; i<czytelnicy.size(); i++)
                    czytelnicy.get(i).start();
                    for(int i=0; i<biblioteki.size(); i++)
                        biblioteki.get(i).start();
//                    if(czytelnicy.size()>biblioteki.size()){
//                        for(int i=0; i<czytelnicy.size(); i++){
//                            if(i<biblioteki.size()){
//                                biblioteki.get(i).start();
//                            }
//                            czytelnicy.get(i).start();
//                        }
//                    }else{
//                        for(int i=0; i<biblioteki.size(); i++){
//                            if(i<czytelnicy.size()){
//                                czytelnicy.get(i).start();
//                            }
//                             biblioteki.get(i).start();
//                        }
//                    }
                    //ZAPISYWANIE DO PLIKU
                    synchronized(KONSOLA){
                    int z=0;
                    while(z<liczbaBibliotek){
                            System.out.println("z jest ciągle MNIEJSZE niz liczba bibliotek");
                            KONSOLA.notifyAll();
                            try{
                                KONSOLA.wait();
                            }catch(InterruptedException err){
                                System.out.println("Problemy z waitowaniem");
                            }
                            z=0;
                            for(int i=0; i<liczbaBibliotek; i++){
                                    if(Program.biblioteki.get(i).isJeszczeDziala()==false){
                                            z++;
                                    }
                            }
                    }   
                        System.out.println("K O N I E C     Z A P I S Y W A N I E      K O N I E C      Z A P I S Y W A N I E       K O N I E C");
                        zapisywanieKoncowe();
                        long stop=System.currentTimeMillis();
                        System.out.println("CZAS DZIAŁANIA CAŁEGO PROGRAMU: "+((stop-start)/1000));
                    }
	//K O N I E C ::
	 
	 
	
	 /*
		// PROBY FUNKCJI ODCZYTYWANIA I ROZSTAWIANIA KSIĄŻEK PO BIBLIOTEKACH - POPRAWIĆ I ZROBIĆ TO NA regexa'ach
		ustawListeKsiazek("plik.txt");
			for(int i=0; i<wszyKsiazki.size(); i++){
				System.out.println(wszyKsiazki.get(i));
			}
		ustawBibliotekiIIchKsiazki();
		for(int i=0; i<liczbaBibliotek; i++){
			for(int j=0; j<biblioteki.get(i).getListaKsiazek().size(); j++){
				System.out.println(biblioteki.get(i).getListaKsiazek().get(j));
			}
			System.out.println();
		}
		ustawKsiazkiDlaCzytelnikow();
		for(int i=0; i<liczbaCzytelnikow; i++){
			for(int j=0; j<czytelnicy.get(i).getListaPotrzebnych().size(); j++){
				System.out.println(czytelnicy.get(i).getListaPotrzebnych().get(j));
			}
			System.out.println();
		}
	 */
	}
}
