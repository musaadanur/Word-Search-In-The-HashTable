package muhendislik_projesi;

import com.sun.org.apache.bcel.internal.generic.GOTO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class hashTablosundaKelimeArama {
    //words.txt deki kelimeler (default kelimeler)
    static ArrayList<String> words=new ArrayList<String>();
    
    //words.txt deki kelimeler (hash table yontemine gore yerlestirilmis) kelimenin kendisi.Ornegin 1. indiste 'cat' kelimesi var
    static ArrayList<String> hashList=new ArrayList<String>(); 
    
    //words.txt deki kelimeler (hash table yontemine gore yerlestirilmis) sum degerleri.Orneğin cat kelimesinin sum degeri (123) var.
    static ArrayList<Integer> hashListSum=new ArrayList<Integer>(); 
    
        // words.txt yi okur words isimli array'e elemanları yerlestirir.
	public static void oku() throws IOException
	{
            File file = new File("words.txt");
            BufferedReader reader =new BufferedReader(new FileReader(file));
            words.add(reader.readLine()); // ilk satırı okur
            int i=0;
                while (words.get(i)!=null) // words.txt dolu ise okumaya devam et
                {
                    words.add(reader.readLine());
                    i=i+1;
                }
            reader.close();
            words.remove(words.size()-1);  // arraylistin sonunda olusan null degerini listeden cikardik
            System.out.println("|||||<<  words.txt içeriği:  >>|||||");
		for(int j=0; j<words.size(); j++)
                {
                    System.out.println(words.get(j));
		}
            System.out.println("|||||||||||||||||||||||||||||||||||||\n");
         }
        
        // words listesinin 2 katından sonra gelen ilk asal sayıyı donderir. (quadratic probing yontemine gore mod degerini bulmus olduk)
        //Ornegin mod değeri 7 ise hashList ve hashlistSum array'lerinin boyutları 7 olur.
        public static int asalmod() 
        {
           int mod = (  (words.size())  *2)+1;
                for (int i = 2; i < mod; i++)
                {
                    if(mod%i==0)
                    {
                        mod++;
                        i=2;
                    }
                } 
           return mod; 
        }
        
	public static void hashHesap() throws IOException // Hash table yontemine gore yerlestirme
        {
            String kelime;
            int mod=asalmod();
            int ascii;
            int sum=0;
            boolean flag=true;
            
                for (int i = 0; i < mod; i++) // hashlist'e default olarak tum elemanlarına "." karakterini koyduk
                { 
                    hashList.add("-");
		}
                
                for (int i = 0; i < mod; i++) // hashlistSum'a default olarak tum elemanlarina 0 sayısını koyduk
                { 
                    hashListSum.add(0);
		}
                
                for (int i=0; i<words.size(); i++)  // tum kelimeler icin for dongusu
                    { 
                        kelime=words.get(i);   // ilk kelime okuma
                        for (int j = 0; j < kelime.length(); j++)//ilk tur icin ilk kelimenin sum degerini bulduk 
                            {
                                ascii=kelime.charAt(j);  
                                sum=(ascii*((j+1)*(j+1)))+sum;  //cat icin sum-> c=99 a=97 t=116 ->  (1^2)*99 + (2^2)*97 + (3^2)*116 = sum 
                            }
                        // bulunan sum degeri ile mod degerinin bolumunden kalan sayıyı indis olarak kullanırız.
                        // Bu indis degeri hashlistte bos ise kelimeyi yerlestir.
                        if(hashList.get(sum%mod)=="-")  
                            {
                                hashList.set((sum%mod),kelime);
                                hashListSum.set((sum%mod),sum);
                            }
                        else if(hashList.get(sum%mod)!="-") // indis degeri dolu ise
                            {
                                int sayac=0;
                                while(flag==true) // kelimemiz için bos indis no'su bulunana kadar doner
                                    {
                                        //quadratic probing yontemine gore kelimenin yerleseceği indis dolu ise diger indisi bulma yontemi 
                                        if(hashList.get(  ( (sum%mod) + (sayac*sayac) ) % mod  )=="-") 
                                        {
                                            hashList.set( ( ( (sum%mod) + (sayac*sayac) ) % mod ),kelime);
                                            hashListSum.set( ( ( (sum%mod) + (sayac*sayac) ) % mod ),sum);
                                            flag=false; // kelime yarlesti while dongusunden cikmak icin flagi false yapiyoruz.
                                        }
                                        sayac++;
                                    }
                            }
                        flag=true;
                        sum=0;
                   }//Tum kelimeler icin Hash hesabına gore yerlestirme  bitti.
                
                System.out.println("<<|||||||||||||||||||||  HASH LİSTT (string kelime) |||||||||||||||||||||>>");   
                for(String d : hashList) // hash listi ekranda göster. Kelime olarak.
                    {
                        System.out.println(d);
                    }
                
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                
                System.out.println("<<|||||||||||||||||||||  HASH LİSTT (sum degerleri) |||||||||||||||||||||>>");  
                for(Integer d : hashListSum) // hash listi ekranda goster. Sum degerlerini.
                    {
                        System.out.println(d);
                    }
                System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");     
        
        
        }  
        
        public static void ara()
        {
            boolean flag=false;
            boolean cıkıs=true; // programda aramayı bitirmek için kullanıldı
            while(cıkıs==true)
            {
                int mod=asalmod();
                int ascii;
                int sum=0;
                String cik="ex"; // programda aramayı bitirmek için kullanıldı
                String girilen;

                Scanner input = new Scanner(System.in); // kullanıcan kelime alma
                System.out.println("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                System.out.println("çıkmak için 'ex' yazın ");
                System.out.print("kelime gir: ");
                girilen = input.next();
                girilen=girilen.toLowerCase(); // girilen kelime büyük harf içeriyorsa küçük harfe çevir
                
                for (int j = 0; j < girilen.length(); j++)// kelimenin sum degerini bulduk
                    {
                        ascii=girilen.charAt(j);  
                        sum=(ascii*((j+1)*(j+1)))+sum;  // girilen kelime cat ise sum-> c=99 a=97 t=116 ->  (1^2)*99 + (2^2)*97 + (3^2)*116
                    }
                
                //aşağıdaki for ile yapılan kontrol girilen kelimenin harf harf kontrolü, arama yaparken çıkabilecek hataları önlemek içindir!!
                //Ne gibi hata örnek olarak: musa aranacak olsun musa'dan gelen sum degeri 61, indis degeride 10 olsun
                //                         : 10.indise musadan önce sum degeri aynı olan başka bir kelime yerleşmiş ise (o kelimede gergedan olsun)
                //                         : aramada musa girip gergedan var sonucunu görebiliriz.
                if(girilen.equals(hashList.get(sum%mod)))
                {
                    flag=true;
                }
                
                
                if(girilen.equals(cik)) // programda aramayı bitirmek için kullanıldı
                        {
                            cıkıs=false;
                        }
                
                else if(hashListSum.get(sum%mod)==0) // aranan kelimeden bulunan indis değeri hashlistSum listesinde 0 ise eleman yok demek
                {
                    System.out.println(girilen +"  ---->       YOK\n");
                    eksiltAra(girilen);  // eleman yok ise aranacak diğer olasılıklar bu 2 fonksiyon çağırılarak kontrol edilir
                    degisAra(girilen);
                }
                
                else if(sum == hashListSum.get(sum%mod) && flag ) // girilenin sum degeri ve kelimenin kendisi hashlistteki ilgili indisteki elemanla uyuşuyor mu
                {
                    System.out.println("Girdiğiniz "+girilen+" kelimesi metin dosyasında bulunmaktadır\n");
                }
                
                
                else // dolu ise aşağıdaki kontroller ile arama yaparız
                {
                    int sayac=0;
                                while(true) 
                                    {
                                        sayac++;
                                        //quadratic probing yöntemine yeni indis degeri bulunup o indis degeri ile girilen kelimenin sum degerini ve kelimenin
                                        //kendisi uyuşuyor mu kontrol edilir
                                        if(girilen.equals(hashList.get( ( (sum%mod) + (sayac*sayac) ) % mod )))
                                        {
                                            flag=true;
                                        }else {flag=false; }
                                        
                                        if(  sum == hashListSum.get(  ( (sum%mod) + (sayac*sayac) ) % mod  ) && flag )
                                        {
                                            System.out.println("Girdiğiniz "+girilen+" kelimesi metin dosyasında bulunmaktadır\n");
                                            break;
                                        }
                                        // aranan kelimeden bulunan indis değeri hashlistSum listesinde 0 ise eleman yok demek
                                        if( hashListSum.get(  ( (sum%mod) + (sayac*sayac) ) % mod  ) == 0) 
                                        {
                                            System.out.println("---->      YOK\n");
                                            eksiltAra(girilen);// eleman yok ise aranacak diğer olasılıklar bu 2 fonksiyon çağırılarak kontrol edilir
                                            degisAra(girilen);
                                            break;
                                        }
                                    }
                }
                flag=false; //bir sonraki arama için flag false degerine çekilir
            }
            
        }
        
        public static void eksiltAra(String girilen)
        {
            boolean flag=false; // girilen kelimeyi kontrol etmek için kullandık (ekstra olarak eklenmiş bir kontroldür)
            int sayac1=0; // girilen kelimeyi kontrol etmek için kullandık (ekstra olarak eklenmiş bir kontroldür)
            int mod=asalmod();
            int ascii;
            int summ=0;
            ArrayList<Character> girilenKopya=new ArrayList<Character>(); // girilen kelimenin harflerini tutar- > musa girildi -> 0. indiste m vardır
       
            for(int m=0; m<girilen.length(); m++) // çıkarılacak harfleri ilişkilendirdiğimiz for
            {
                for(int i=0; i<girilen.length(); i++) // girilen stringi arraya attık 0.indis stringin ilk harfini tutuyor.
                {
                    girilenKopya.add(girilen.charAt(i));
                }
                
                girilenKopya.remove(m); // harf çıkarma işlemi
                
                for(int n=0; n<girilen.length()-1; n++) //örnek: musa girildi usa nın sum degerini buluruz ilk dongude
                {
                    ascii=girilenKopya.get(n);  
                    summ=(ascii*((n+1)*(n+1)))+summ;
                }
                
                //aşağıdaki for ile yapılan kontrol girilen kelimenin harf harf kontrolü, arama yaparken çıkabilecek hataları önlemek içindir!!
                //Ne gibi hata örnek olarak: musa aranacak olsun musa'dan gelen sum degeri 61, indis degeride 10 olsun
                //                               : 10.indise musadan önce sum degeri aynı olan başka bir kelime yerleşmiş ise (o kelimede gergedan olsun)
                //                               : aramada musa girip gergedan var sonucunu görebilriz.
                
                for(int k=0; k<girilen.length()-1; k++) // budöngünün amacı: musa girildi ise ilk tur için usa kelimesini harf harf hashlistteki ilgili
                                                        // indis degerindeki kelimenin harfleri benziyormu diye kontrol eder
                {
                    if(girilenKopya.get(k).equals(hashList.get(summ%mod).charAt(k)))
                    {
                        sayac1++;
                    }
                    else
                    {
                        break;
                    }
                    if(sayac1==girilen.length()-1)
                    {
                        flag=true; // aran kelime hashlistte var ise true degerini alır
                    }
                }
                ////////////////////////////////////////////////////////////////
                
                if(hashListSum.get(summ%mod)==0) // boyle bir eleman yok
                {}
                
                // girilenin sum degeri ve harfleri, hashlistteki ilgili indisteki elemanla uyuşuyor mu
                else if(  summ == hashListSum.get(summ%mod) && flag ) 
                {
                    for (Character character : girilenKopya)
                    {
                        System.out.print(character);
                    }
                    System.out.print(" olarak bulunmuştur. \n");
                }
                else // dolu ise aşağıdaki kontroller ile arama yaparız
                {
                    int sayac=0;
                                while(true) 
                                    {
                                        sayac++;
                                        for(int k=0; k<girilen.length()-1; k++)
                                            {
                                                if(girilenKopya.get(k).equals(hashList.get( ( (summ%mod) + (sayac*sayac) ) % mod  ).charAt(k)))
                                                {
                                                    sayac1++;
                                                }
                                                else
                                                {
                                                    flag=false;
                                                    break;
                                                }
                                                if(sayac1==girilen.length()-1)
                                                {
                                                    flag=true; // aran kelime hashlistte var ise true degerini alır
                                                }
                                            }
                                        
                                        // girilenin sum degeri ve harfleri, hashlistteki ilgili indisteki elemanla uyuşuyor mu
                                        if(  summ == hashListSum.get(  ( (summ%mod) + (sayac*sayac) ) % mod  ) && flag  )
                                        {
                                            for (Character character : girilenKopya)
                                            {
                                                System.out.print(character);
                                            }
                                            System.out.print(" olarak bulunmuştur. \n");
                                            break;
                                        }
                                        // aranan kelimeden bulunan indis değeri hashlistSum listesinde 0 ise eleman yok demek
                                        if( hashListSum.get(  ( (summ%mod) + (sayac*sayac) ) % mod  ) == 0 ) 
                                        {
                                            break;
                                        }
                                    }
                }        
                summ=0;
                girilenKopya.clear();
                flag=false;
                sayac1=0;
            }
        }
        
        public static void degisAra(String girilen)
        {
            boolean flag=false; // girilen kelimeyi kontrol etmek için kullandık (ekstra olarak eklenmiş bir kontroldür)
            int sayac1=0; // girilen kelimeyi kontrol etmek için kullandık (ekstra olarak eklenmiş bir kontroldür)
            int mod=asalmod();
            int ascii;
            int summm=0;
            char tut;
            ArrayList<Character> girilenDeis=new ArrayList<Character>();
            
            for(int j=0; j<girilen.length(); j++) // girilen stringi arraya attık 0.indis stringin ilk harfini tutuyor.
                {
                    girilenDeis.add(girilen.charAt(j));
                }
            
            for(int i=0; i<girilen.length()-1; i++) // 4 harfli kelime için 4-1 yer deişim bulundugu için "girilen.length()-1" kuşulu.
            {
                tut=girilenDeis.get(i);                     // harf deişme işlemi
                girilenDeis.set(i, girilenDeis.get(i+1));
                girilenDeis.set(i+1, tut);
                
                // sum degeri bulma
                for(int n=0; n<girilenDeis.size(); n++) // örnek= kedi girdik: ekdi nin sum degeri bulunur ilk tur için
                {
                    ascii=girilenDeis.get(n);  
                    summm=(ascii*((n+1)*(n+1)))+summm;
                }
                
                //örn: musa->  ilk dongu için umsa yı haslistte ilgili kelime ile karakter karater kontrol 
                for(int k=0; k<girilenDeis.size(); k++) 
                {
                    if(girilenDeis.get(k).equals(hashList.get(summm%mod).charAt(k)))
                    {
                        sayac1++;
                    }
                    else
                    {
                        break;
                    }
                    if(sayac1==girilenDeis.size())
                    {
                        flag=true;
                    }
                }
                
                ////////////////  ARAMA
                
                if(hashListSum.get(summm%mod)==0) // boyle bir eleman yok
                {}
                
                // girilenin sum degeri ve harfleri, hashlistteki ilgili indisteki elemanla uyuşuyor mu
                else if( summm == hashListSum.get(summm%mod) && flag) 
                {
                    for (Character character : girilenDeis)
                    {
                        System.out.print(character);
                    }
                    System.out.print(" olarak bulunmuştur. \n");
                }
                else  // dolu ise aşağıdaki kontroller ile arama yaparız
                {
                    int sayac=0;
                                while(true) 
                                    {
                                        sayac++;
                                        
                                        for(int k=0; k<girilenDeis.size(); k++) 
                                        {
                                            if(girilenDeis.get(k).equals(hashList.get( ( (summm%mod) + (sayac*sayac) ) % mod ).charAt(k)))
                                            {
                                                sayac1++;
                                            }
                                            else
                                            {
                                                flag=false;
                                                break;
                                            }
                                            if(sayac1==girilenDeis.size())
                                            {
                                                flag=true;
                                            }
                                        }
                                        
                                        // girilenin sum degeri ve harfleri, hashlistteki ilgili indisteki elemanla uyuşuyor mu
                                        if( summm == hashListSum.get(  ( (summm%mod) + (sayac*sayac) ) % mod  ) && flag  )
                                        {
                                            for (Character character : girilenDeis)
                                            {
                                                System.out.print(character);
                                            }
                                            System.out.print(" olarak bulunmuştur. \n");
                                            break;
                                        }
                                        // aranan kelimeden bulunan indis değeri hashlistSum listesinde 0 ise eleman yok demek
                                        if( hashListSum.get(  ( (summm%mod) + (sayac*sayac) ) % mod  ) == 0 ) 
                                        {
                                            break;
                                        }
                                    }
                }      
                ////////////////      ARAMA BİTİŞ 
                
                summm=0;
                sayac1=0;
                flag=false;
                
                
                girilenDeis.clear();
                for(int j=0; j<girilen.length(); j++) //Bir sonraki kontrol için girilen kelimenin yerleri değişmiş harflerini eski haline alıyoruz
                {
                    girilenDeis.add(girilen.charAt(j));
                }
                
            }
            
            
        }
}
        
