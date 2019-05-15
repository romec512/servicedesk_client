package com.example.roman.service_desk_client;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendData extends AsyncTask<Void, Void, Void> {

    String resultString = null;
    private String server;
    public int result = 0;
    private String parammetrs;
    Context context;
    public SendData(String params, String method, Context _context){
        this.server = Globals.ServerAddress + "/" + method;
        parammetrs = params;
        context = _context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void  doInBackground(Void... params) {
        try {

            String myURL = server;

            byte[] data = null;
            InputStream is = null;



            try {
                URL url = new URL(server);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                conn.setRequestProperty("Content-Length", "" + Integer.toString(parammetrs.getBytes().length));
                conn.setDoOutput(true);
                conn.setDoInput(true);


                // конвертируем передаваемую строку в UTF-8
                data = parammetrs.getBytes("UTF-8");


                OutputStream os = conn.getOutputStream();


                // передаем данные на сервер
                os.write(data);
                os.flush();
                os.close();
                data = null;
                conn.connect();
                int responseCode= conn.getResponseCode();


                // передаем ответ сервер
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                if (responseCode == 200) {    // Если все ОК (ответ 200)
                    is = conn.getInputStream();

                    byte[] buffer = new byte[8192]; // размер буфера


                    // Далее так читаем ответ
                    int bytesRead;



                    while ((bytesRead = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }


                    data = baos.toByteArray();
                    resultString = new String(data, "UTF-8");  // сохраняем в переменную ответ сервера, у нас "OK"


                } else {
                }

                conn.disconnect();

            } catch (MalformedURLException e) {

                resultString = "MalformedURLException:" + e.getMessage();
            } catch (IOException e) {

                resultString = "IOException:" + e.getMessage();
            } catch (Exception e) {

                resultString = "Exception:" + e.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
//        Toast.makeText(context, resultString, Toast.LENGTH_LONG).show();
    }
}
