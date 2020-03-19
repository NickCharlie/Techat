package ink.techat.client.factory.data.message;

import ink.techat.client.factory.data.DbDataSource;
import ink.techat.client.factory.model.db.Message;

/**
 * 消息的数据源定义 他的实现是 MessageRepository
 * @author NickCharlie
 */
public interface MessageDataSource extends DbDataSource<Message> {

}
