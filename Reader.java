import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Reader {
    private String startDate;
    private String endDate;
    private String currency;

    public Reader(){}

    public Reader(String currency,String startDate,String endDate){
        this.startDate=startDate;
        this.endDate=endDate;
        this.currency=currency;
    }

    public String[] readCurrency() throws Exception{
        String url="http://api.nbp.pl/api/exchangerates/tables/C/today/?format=json";
        StringBuffer stringBuffer = setConnection(url);
        String response = stringBuffer.substring(1,stringBuffer.length()-1);


        JSONObject myResponseObject = new JSONObject(response);
        JSONArray myArray = myResponseObject.getJSONArray("rates");

        String[] currencyTab=new String[myArray.length()];
        for(int i=0;i<myArray.length();i++) {
            JSONObject object=myArray.getJSONObject(i);
            currencyTab[i]=object.getString("code");
        }
        return currencyTab;
    }

    public double[] readPrice(String bidOrAsk) throws Exception{
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://api.nbp.pl/api/exchangerates/rates/C/")
                .append(currency).append("/").append(startDate).append("/").append(endDate).append("/?format=json");
        String url = stringBuilder.toString();

        StringBuffer response=new StringBuffer();
        response = setConnection(url);


        JSONObject myResponse = new JSONObject(response.toString());
        JSONArray myArray = myResponse.getJSONArray("rates");
        double[] priceTab=new double[myArray.length()];

        for(int i=0;i<myArray.length();i++) {
            JSONObject object=myArray.getJSONObject(i);
            priceTab[i]=object.getFloat(bidOrAsk);
        }
        return priceTab;
    }

    private StringBuffer setConnection(String url) throws Exception{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        try {
            try {
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            finally{
                in.close();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return response;
    }


}
