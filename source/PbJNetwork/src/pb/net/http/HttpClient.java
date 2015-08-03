package pb.net.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import pb.helper.KV;

/**
 * HTTP通讯客户端类。<br/>
 *
 * @author proteanBear(马强)
 * @version 1.00 2014/08/09
 */
public class HttpClient
{
    /**
     * @param url
     * @param properties
     * @return
     * @throws java.net.MalformedURLException
     */
    public final static String postSend(String url,KV<String,String>... properties)
            throws IOException
    {
        //HTTP连接
        URL connUrl=new URL(url);
        HttpURLConnection conn=(HttpURLConnection)connUrl.openConnection();
        conn.setRequestProperty("contentType","UTF-8");
        //conn.setConnectTimeout(5 * 1000);

        //设置连接为输出模式
        conn.setDoOutput(true);

        //发送Post数据
        try(OutputStreamWriter out=new OutputStreamWriter
                (conn.getOutputStream(),"UTF-8"))
        {
            String outString="";
            int i=0;
            for(KV<String,String> kv : properties)
            {
                if(i!=0) outString+="&";
                outString+=kv.getKey()+"="+kv.getValue();
                i++;
            }
            out.write(outString);
            out.flush();
        }

        //接收服务器返回数据
        String sCurrentLine;
        String sTotalString;
        sCurrentLine="";
        sTotalString="";
        InputStream l_urlStream;
        l_urlStream=conn.getInputStream();
        // 传说中的三层包装阿！  
        BufferedReader l_reader=new BufferedReader(
                new InputStreamReader(
                        l_urlStream,"UTF-8"
                )
        );
        while((sCurrentLine=l_reader.readLine())!=null)
        {
            sTotalString+=sCurrentLine;

        }
        return sTotalString;
    }
}