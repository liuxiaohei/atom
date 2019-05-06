package jdbc;

import beans.jdbc.ConnectionBean;
import org.atom.functions.BaseTrait;
import org.atom.functions.Try;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcDemoTest implements BaseTrait {

    @Test
    public void mysqlDemo() throws Exception{
        ConnectionBean con = new ConnectionBean();
        con.setJdbcUrl("jdbc:mysql://localhost:3306/");
        con.setUsername("root");
        con.setPassword("password");
        Optional.of(con)
                .map(Try.of(e -> DriverManager.getConnection(
                        e.getJdbcUrl(),
                        e.getUsername(),
                        e.getPassword())))
                .map(Try.of(Connection::getMetaData))
                .map(this::printMetaDatainfo)
                .map(Try.of(e -> e.getTables(
                        "36kr",
                        null,
                        "%",
                        new String[]{"TABLE", "VIEW"})))
                .map(e -> getResultSetProperty(e,"TABLE_NAME"))
                .ifPresent(list -> list.forEach(System.out::println));
    }

    /**
     *
     *  mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.1.0 -Dpackaging=jar -Dfile=ojdbc6.jar
     */
    @Test
    public void oracleDemo() throws Exception{
        ConnectionBean con = new ConnectionBean();
        con.setJdbcUrl("jdbc:oracle:thin:@172.26.2.29:1521:orcl");
        con.setUsername("tdt_ora");
        con.setPassword("123456");
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Optional.of(con)
                .map(Try.of(e -> DriverManager.getConnection(
                        e.getJdbcUrl(),
                        e.getUsername(),
                        e.getPassword())))
                .map(Try.of(Connection::getMetaData))
                .map(this::printMetaDatainfo)
                .map(Try.of(e -> e.getTables(
                        "36kr",
                        null,
                        "%",
                        new String[]{"TABLE", "VIEW"})))
                .map(e -> getResultSetProperty(e,"TABLE_NAME"))
                .ifPresent(list -> list.forEach(System.out::println));
    }


    /**
     * 获取指定的属性
     */
    private List<String> getResultSetProperty(ResultSet result,String property) {
        List<String> list = new ArrayList<>();
        try {
            while (result.next()) {
                String str = result.getString(property);
                if(isNotEmpty(str)) {
                    list.add(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 打印数据库元数据的信息
     */
    private DatabaseMetaData printMetaDatainfo(DatabaseMetaData databaseMetaData) {
        System.out.println("⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇");
        try {
            System.out.println("数据库属性信息："
                    + databaseMetaData.getDatabaseMajorVersion() + " "
                    + databaseMetaData.getDatabaseMinorVersion() + " "
                    + databaseMetaData.getDatabaseProductName() + " "
                    + databaseMetaData.getDatabaseProductVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("驱动信息："
                + databaseMetaData.getDriverMajorVersion() + " "
                + databaseMetaData.getDriverMinorVersion());
        System.out.println("⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆");
        return databaseMetaData;
    }
}
