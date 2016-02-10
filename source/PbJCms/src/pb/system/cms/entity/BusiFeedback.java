package pb.system.cms.entity;

/**
 * 数据表映射类——记录应用的意见反馈信息
 *
 * @author ProteanBear
 * @version 1.00 2014-11-20
 */
public class BusiFeedback
{
    /**
     * 域(受保护)
     * 名称: custId
     * 描述: 自增主键
     */
    protected int custId;

    /**
     * 访问器
     * 目标: custId
     *
     * @return int - 自增主键
     */
    public int getCustId()
    {
        return custId;
    }

    /**
     * 更改器
     * 目标: custId
     *
     * @param custId - 自增主键
     */
    public void setCustId(int custId)
    {
        this.custId=custId;
    }

    /**
     * 更改器
     * 目标: custId
     *
     * @param custId - 自增主键
     */
    public void setCustId(Integer custId)
    {
        this.custId=custId.intValue();
    }

    /**
     * 更改器
     * 目标: custId
     *
     * @param custId - 自增主键
     */
    public void setCustId(String custId)
    {
        this.custId=new Integer(custId);
    }

    /**
     * 域(受保护)
     * 名称: feedbackApp
     * 描述: 反馈所属应用的授权编码。
     */
    protected String feedbackApp;

    /**
     * 访问器
     * 目标: feedbackApp
     *
     * @return String - 反馈所属应用的授权编码。
     */
    public String getFeedbackApp()
    {
        return feedbackApp;
    }

    /**
     * 更改器
     * 目标: feedbackApp
     *
     * @param feedbackApp - 反馈所属应用的授权编码。
     */
    public void setFeedbackApp(String feedbackApp)
    {
        this.feedbackApp=feedbackApp;
    }

    /**
     * 域(受保护)
     * 名称: feedbackEmail
     * 描述: 提交反馈的电子邮箱
     */
    protected String feedbackEmail;

    /**
     * 访问器
     * 目标: feedbackEmail
     *
     * @return String - 提交反馈的电子邮箱
     */
    public String getFeedbackEmail()
    {
        return feedbackEmail;
    }

    /**
     * 更改器
     * 目标: feedbackEmail
     *
     * @param feedbackEmail - 提交反馈的电子邮箱
     */
    public void setFeedbackEmail(String feedbackEmail)
    {
        this.feedbackEmail=feedbackEmail;
    }

    /**
     * 域(受保护)
     * 名称: feedbackContent
     * 描述: 提交反馈的内容
     */
    protected String feedbackContent;

    /**
     * 访问器
     * 目标: feedbackContent
     *
     * @return String - 提交反馈的内容
     */
    public String getFeedbackContent()
    {
        return feedbackContent;
    }

    /**
     * 更改器
     * 目标: feedbackContent
     *
     * @param feedbackContent - 提交反馈的内容
     */
    public void setFeedbackContent(String feedbackContent)
    {
        this.feedbackContent=feedbackContent;
    }

    /**
     * 域(受保护)
     * 名称: feedbackIp
     * 描述: 提交反馈的IP地址
     */
    protected String feedbackIp;

    /**
     * 访问器
     * 目标: feedbackIp
     *
     * @return String - 提交反馈的IP地址
     */
    public String getFeedbackIp()
    {
        return feedbackIp;
    }

    /**
     * 更改器
     * 目标: feedbackIp
     *
     * @param feedbackIp - 提交反馈的IP地址
     */
    public void setFeedbackIp(String feedbackIp)
    {
        this.feedbackIp=feedbackIp;
    }

    /**
     * 域(受保护)
     * 名称: feedbackCity
     * 描述: 提交反馈的地区
     */
    protected String feedbackCity;

    /**
     * 访问器
     * 目标: feedbackCity
     *
     * @return String - 提交反馈的地区
     */
    public String getFeedbackCity()
    {
        return feedbackCity;
    }

    /**
     * 更改器
     * 目标: feedbackCity
     *
     * @param feedbackCity - 提交反馈的地区
     */
    public void setFeedbackCity(String feedbackCity)
    {
        this.feedbackCity=feedbackCity;
    }

    /**
     * 域(受保护)
     * 名称: feedbackMembar
     * 描述: 提交反馈的会员标识
     */
    protected int feedbackMembar;

    /**
     * 访问器
     * 目标: feedbackMembar
     *
     * @return int - 提交反馈的会员标识
     */
    public int getFeedbackMembar()
    {
        return feedbackMembar;
    }

    /**
     * 更改器
     * 目标: feedbackMembar
     *
     * @param feedbackMembar - 提交反馈的会员标识
     */
    public void setFeedbackMembar(int feedbackMembar)
    {
        this.feedbackMembar=feedbackMembar;
    }

    /**
     * 更改器
     * 目标: feedbackMembar
     *
     * @param feedbackMembar - 提交反馈的会员标识
     */
    public void setFeedbackMembar(String feedbackMembar)
    {
        this.feedbackMembar=new Integer(feedbackMembar);
    }

    /**
     * 域(受保护)
     * 名称: dataRemark
     * 描述: 数据备注信息
     */
    protected String dataRemark;

    /**
     * 访问器
     * 目标: dataRemark
     *
     * @return String - 数据备注信息
     */
    public String getDataRemark()
    {
        return dataRemark;
    }

    /**
     * 更改器
     * 目标: dataRemark
     *
     * @param dataRemark - 数据备注信息
     */
    public void setDataRemark(String dataRemark)
    {
        this.dataRemark=dataRemark;
    }

    /**
     * 域(受保护)
     * 名称: dataDelete
     * 描述: 数据删除标志位
     */
    protected int dataDelete;

    /**
     * 访问器
     * 目标: dataDelete
     *
     * @return int - 数据删除标志位
     */
    public int getDataDelete()
    {
        return dataDelete;
    }

    /**
     * 更改器
     * 目标: dataDelete
     *
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(int dataDelete)
    {
        this.dataDelete=dataDelete;
    }

    /**
     * 更改器
     * 目标: dataDelete
     *
     * @param dataDelete - 数据删除标志位
     */
    public void setDataDelete(String dataDelete)
    {
        this.dataDelete=new Integer(dataDelete);
    }

}