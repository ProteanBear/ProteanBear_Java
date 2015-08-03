/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pb.system.limit.manager;

import pb.data.PoolJdbcConnector;

/**
 * @author maqiang
 */
public class CountTest
{
    public static void main(String[] args)
    {
        //创建连接
        PoolJdbcConnector connecter=new PoolJdbcConnector(
                "jdbc:mysql://127.0.0.1:3306/ProteanBear?useUnicode=true&characterEncoding=UTF-8",
                "pbadmin",
                "admin147258"
        );
        connecter.connect();
        if(connecter.isConnected())
        {
            SystemAreaFacadeLocal test=new SystemAreaFacade(connecter);
            System.out.println(test.count());
        }
        else
        {
            System.out.println(connecter.getError());
        }

        connecter.close();
    }
}
