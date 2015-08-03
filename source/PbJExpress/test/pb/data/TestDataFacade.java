/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pb.data;

/**
 * @author maqiang
 */
public class TestDataFacade extends AbstractEntityManager<TestData> implements TestDataFacadeLocal
{
    @Override
    public String getPrimaryKeyName()
    {
        return "custId";
    }

    public TestDataFacade(Connector connector)
    {
        super(connector,TestData.class);
    }

    @Override
    public String getKeyGenerator()
    {
        return "TEST_DATA_SEQUENCE.NEXTVAL";
    }
}
