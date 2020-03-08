package ink.techat.client.factory.model;

import java.util.Date;

/**
 * 基础用户接口
 * @author NickCharlie
 */
 public interface Author {
     String getId();

     void setId(String id);

     int getUserPermissionType();

     String getName();

     void setName(String name);

     String getPortrait();

     void setPortrait(String portrait);
}
