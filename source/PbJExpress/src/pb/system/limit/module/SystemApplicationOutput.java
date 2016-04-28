package pb.system.limit.module;

import java.util.List;

import pb.system.limit.entity.SystemApplication;
import pb.system.limit.entity.SystemApplicationPlatform;

/**
 * 数据表输出类——应用信息表。<br>
 *
 * @author proteanBear(马强)
 * @version 1.00 2014/07/24
 */
public class SystemApplicationOutput extends SystemApplication
{
    /**
     * 域(受保护)<br>
     * 名称:    appPlat<br>
     * 描述:    记录应用对应的平台列表<br>
     */
    protected Integer[] appPlat;

    /**
     * 构造函数<br>
     *
     * @param app - 应用信息
     */
    public SystemApplicationOutput(SystemApplication app)
    {
        this.appCode=app.getAppCode();
        this.appIcon=app.getAppIcon();
        this.appName=app.getAppName();
        this.appThumbnail=app.getAppThumbnail();
        this.areaId=app.getAreaId();
        this.appId=app.getAppId();
        this.dataDelete=app.getDataDelete();
        this.dataRemark=app.getDataRemark();
        this.appAlias=app.getAppAlias();
        this.appWeb=app.getAppWeb();
        //this.roleDescription=role.getRoleDescription();
    }

    /**
     * 访问器<br>
     * 目标：   appPlat<br>
     *
     * @return Integer - 记录应用对应的平台列表
     */
    public Integer[] getAppPlat()
    {
        return appPlat;
    }

    /**
     * 更改器<br>
     * 目标：   appPlat<br>
     *
     * @param appPlat - 记录应用对应的平台列表
     */
    public void setAppPlat(List<SystemApplicationPlatform> appPlat)
    {
        if(appPlat!=null)
        {
            this.appPlat=new Integer[appPlat.size()];
            for(int i=0;i<appPlat.size();i++)
            {
                this.appPlat[i]=appPlat.get(i).getPlatId();
            }
        }
    }
}
