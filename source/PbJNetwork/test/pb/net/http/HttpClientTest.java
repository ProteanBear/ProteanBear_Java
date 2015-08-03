/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pb.net.http;

import java.io.IOException;
import pb.helper.KV;
import pb.json.JSONObject;

/**
 *
 * @author maqiang
 */
public class HttpClientTest
{
    public static void main(String[] args)
    {
        try
        {
            String responseString=HttpClient.postSend("http://118.112.185.142/mdms/busiUserLogin",
                    new KV("loginUser","administrator"),
                    new KV("loginPass","asdfghjk"));
            JSONObject response=new JSONObject(responseString);
            boolean isSuccess=response.getBoolean("success");
            if(isSuccess)
            {
                System.out.println("登录成功！");
            }
        }
        catch (IOException ex)
        {
            System.out.print(ex.getMessage());
        }
    }
}
