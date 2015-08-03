/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pb.data;

/**
 * @author maqiang
 */
public class EntityManager_Test
{
    public static void main(String[] args)
    {
        Connector conn=new JdbcConnector("jdbc:oracle:thin:@192.168.1.210:1521:orcl","gpsuser","cdcyl2009");
        TestDataFacadeLocal testDataManager=new TestDataFacade(conn);

        //添加数据
        /*TestData data=new TestData();
        data.setSysName("新增数据测试3");
        System.out.println(testDataManager.create(data));*/

        //读取单个数据
        /*TestData data=testDataManager.find(2);
        System.out.println(data.getCustId()+"    "+data.getSysName());*/

        //修改单个数据
        /*TestData data=testDataManager.find(2);
        System.out.println(data.getCustId()+"    "+data.getSysName());
        data.setSysName(data.getSysName()+"1");
        testDataManager.edit(data);
        data=testDataManager.find(data.getCustId());
        System.out.println(data.getCustId()+"    "+data.getSysName());*/

        //读取全部数据
        /*List<TestData> datalist=testDataManager.findAll();
        for (int i = 0; i < datalist.size(); i++)        
        {
            System.out.println(datalist.get(i).getCustId()+"    "+datalist.get(i).getSysName());
        }*/

        //读取无条件分页数据
        /*List<TestData> datalist=testDataManager.find(1,2);
        for (int i = 0; i < datalist.size(); i++)        
        {
            System.out.println(datalist.get(i).getCustId()+"    "+datalist.get(i).getSysName());
        }*/

        //读取有条件全部数据
        /*Map<String,Object> conTest=new HashMap<String,Object>();
        conTest.put("sysName like ?","%数据%");
        List<TestData> datalist=testDataManager.find(conTest);
        for (int i = 0; i < datalist.size(); i++)        
        {
            System.out.println(datalist.get(i).getCustId()+"    "+datalist.get(i).getSysName());
        }*/

        //读取有条件分页数据
        /*Map<String,Object> conTest=new HashMap<String,Object>();
        conTest.put("sysName like ?","%数据%");
        List<TestData> datalist=testDataManager.find(conTest,1,2);
        for (int i = 0; i < datalist.size(); i++)        
        {
            System.out.println(datalist.get(i).getCustId()+"    "+datalist.get(i).getSysName());
        }*/

        //查看无条件数据数量
        /*int num=testDataManager.count();
        System.out.println(num);*/

        //查看有条件数据数量
        /*Map<String,Object> conTest=new HashMap<String,Object>();
        conTest.put("sysName like ?","%数据%");
        int num=testDataManager.count(conTest);
        System.out.println(num);*/

        //删除单个数据
        /*System.out.println("删除前——");
        List<TestData> datalist=testDataManager.findAll();
        for (int i = 0; i < datalist.size(); i++)        
        {
            System.out.println(datalist.get(i).getCustId()+"    "+datalist.get(i).getSysName());
        }
        testDataManager.remove(datalist.get(datalist.size()-1));
        System.out.println("删除后——");
        datalist=testDataManager.findAll();
        for (int i = 0; i < datalist.size(); i++)        
        {
            System.out.println(datalist.get(i).getCustId()+"    "+datalist.get(i).getSysName());
        }*/

        conn.close();
    }
}
