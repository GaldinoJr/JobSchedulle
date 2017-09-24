package com.galdino.jobschedulle.network;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by galdino on 24/09/17.
 */

public class HttpConnection
{
    public static String getSetDataWeb(String url, String method)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String resposta = "";

        try
        {
            ArrayList<NameValuePair> valores = new ArrayList<>();
            valores.add(new BasicNameValuePair("method",method));

            httpPost.setEntity(new UrlEncodedFormEntity(valores));
            HttpResponse response = httpClient.execute(httpPost);
            resposta = EntityUtils.toString(response.getEntity());
        }
        catch(NumberFormatException e){ e.printStackTrace(); }
        catch(ClientProtocolException e){ e.printStackTrace(); }
        catch(IOException e){ e.printStackTrace(); }
        catch(NullPointerException e){ e.printStackTrace(); }

        return resposta;
    }
}
