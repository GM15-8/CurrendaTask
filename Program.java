import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import static java.lang.Math.*;

public class Program {

    public static void main(String[] args) throws Exception {
        //pobranie dostepnych walut
        Reader readerTemp=new Reader();
        String[] currency=readerTemp.readCurrency();
        String[] dataTab={""};
        //sprawdzenie poprawności danych wejściowcyh, jeśli ok wykonanie programu i wyświetlenie rezultatu
        while(!dataTab[0].equals("0")) {
            System.out.println("0 - Wyjście");
            dataTab=getInput();
            if(dataTab[0].equals("0")){
                System.out.println("Koniec programu");
            }
            else if (checkInput(currency, dataTab) && checkData(dataTab[1]) && checkData(dataTab[2])) {
                Reader reader = new Reader(dataTab[0], dataTab[1], dataTab[2]);
                double[] bid = reader.readPrice("bid");
                double[] ask = reader.readPrice("ask");
                showResult(countAverage(bid), countStandardDeviation(ask));
            }
        }

    }

    private static String[] getInput() throws IOException{
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Proszę podać dane wejściowe: WALUTA YYYY-MM-DD YYYY-MM-DD(np.: EUR 2017-11-20 2017-11-24)");
        String input=bufferedReader.readLine();
        String[] dataTab;
        dataTab=input.split(" ");
        return dataTab;
    }

    private static boolean checkInput(String[] currency,String[] dataTab){
        boolean isCurrency=false;
        for(String c:currency){
            if(c.equalsIgnoreCase(dataTab[0])){
                isCurrency=true;
            }
        }
        if(!isCurrency){
            System.out.print( "Podana waluta nie istnieje. Spróbuj ponownie\n");
            return false;
        }
        return true;
    }

    private static boolean checkData(String date){
        int year,month,day;
        String[] tab;
        tab=date.split("-");
        year=Integer.parseInt(tab[0]);
        month=Integer.parseInt(tab[1]);
        day=Integer.parseInt(tab[2]);
        if(day<1 || day>31 || month<1 || month>12){
            System.out.print( "Podana data została wpisana błednie lub w niepoprawnym formacie\n");
            return false;
        }
        else{
            Calendar currentDay=Calendar.getInstance();
            Calendar checkDay=Calendar.getInstance();
            checkDay.set(year,month-1,day,0,0);
            if(currentDay.compareTo(checkDay)<0){
                System.out.print( "Podana data została wpisana błednie lub w niepoprawnym formacie\n");
                return false;
            }
            else{
                return true;
            }

        }

    }

    private static void showResult(double average,double standardDeviation){
        System.out.printf("%6.4f > średni kurs kupna\n",average);
        System.out.printf("%6.4f > odchylenie standardowe kursów sprzedaży\n",standardDeviation);
    }

    private static double countAverage(double[] tab){
        double result=0;
        for(double value:tab){
            result+=value;
        }
        result=result/(tab.length);

        return result;
    }

    private static double countStandardDeviation(double[] tab){
        double result=0;
        double average=countAverage(tab);
        double[] tempTab=new double[tab.length];
        for(int i=0;i<tab.length;i++){
            tempTab[i]=pow(tab[i]-average,2.0);
        }
        for(double price:tempTab){
            result+=price;
        }
        result/=tab.length;

        return sqrt(result);
    }
}
