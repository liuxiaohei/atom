package beans.jdbc

import scala.beans.BeanProperty

/*
 * @author ld
 * Scala bean 实体
 */
class ConnectionBean {
  @BeanProperty var id : Long = _
  @BeanProperty var jdbcUrl : String = _
  @BeanProperty var username : String = _
  @BeanProperty var password : String = _
  @BeanProperty var driverClass : String = _
  @BeanProperty var driverFiles : String = _
}
