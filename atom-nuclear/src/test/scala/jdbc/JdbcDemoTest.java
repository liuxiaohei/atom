package jdbc;

import beans.jdbc.ConnectionBean;
import org.atom.functions.BaseTrait;
import org.atom.functions.Try;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class JdbcDemoTest implements BaseTrait {

    @Test
    public void mysqlDemo() throws Exception{
        ConnectionBean con = new ConnectionBean();
        con.setJdbcUrl("jdbc:mysql://localhost:3306/");
        con.setUsername("root");
        con.setPassword("******");
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
     * http://www.it1352.com/594867.html
     * https://stackoverflow.com/questions/30567224/java-databasemetadata-getschemas-returns-empty-resultset-expected-non-empty-r
     */
    @Test
    public void mysqlDemo1() throws Exception {
        ConnectionBean con = new ConnectionBean();
        con.setJdbcUrl("jdbc:mysql://localhost:3306/");
        con.setUsername("root");
        con.setPassword("******");
        Optional<DatabaseMetaData> databaseMetaData = Optional.of(con)
                .map(Try.of(e -> DriverManager.getConnection(
                        e.getJdbcUrl(),
                        e.getUsername(),
                        e.getPassword())))
                .map(Try.of(Connection::getMetaData));

        databaseMetaData
                .map(Try.of(DatabaseMetaData::getCatalogs))
                .ifPresent(rs -> {
                    try {
                        while (rs.next()) {
                            System.out.println("TABLE_CAT = "
                                    + rs.getString("TABLE_CAT"));
                        }
                    } catch (Exception e) {
                e.printStackTrace();
            }
        });

        databaseMetaData
                .map(Try.of(DatabaseMetaData::getSchemas))
                .ifPresent(rs -> {
                    System.out.println("List of schemas: ");
                    try {
                        while (rs.next()){
                            String tableSchem = rs.getString("TABLE_SCHEM");
                            System.out.println(tableSchem);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

    }
    /**
     *  由于oracle的驱动中心库不存在的原因
     *  1 先cd到这个包的目录下
     *  2 mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.1.0 -Dpackaging=jar -Dfile=ojdbc6.jar
     */
    @Test
    public void oracleDemo() throws Exception{
        ConnectionBean con = new ConnectionBean();
        con.setJdbcUrl("jdbc:oracle:thin:@172.26.2.29:1521:orcl");
        con.setUsername("tdt_ora");
        con.setPassword("123456");
        Class.forName("oracle.jdbc.driver.OracleDriver");
        final Connection conn =  DriverManager.getConnection(
                con.getJdbcUrl(),
                con.getUsername(),
                con.getPassword());
        Optional.of(conn)
                .map(Try.of(Connection::getMetaData))
                .map(this::printMetaDatainfo)
                .map(Try.of(e -> e.getTables(
                        "111",
                        "OUSER1",
                        "%",
                        new String[]{"TABLE", "VIEW"})))
                .ifPresent(rs -> {
                    try {
                        while (rs.next()) {
                            String tableName = rs.getString("TABLE_NAME");  //表名
                            String tableType = rs.getString("TABLE_TYPE");  //表类型
                            String remarks = rs.getString("REMARKS");       //表备注
                            System.out.println(tableName + " - " + tableType + " - " + remarks);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
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
     * https://blog.csdn.net/chen_zw/article/details/18816599
     */
    private DatabaseMetaData printMetaDatainfo(DatabaseMetaData dbmd) {
        System.out.println("⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇");
        try {
            System.out.println("数据库属性信息："
                    + dbmd.getDatabaseMajorVersion() + " "
                    + dbmd.getDatabaseMinorVersion() + " "
                    + dbmd.getDatabaseProductName() + " "
                    + dbmd.getDatabaseProductVersion());
            System.out.println("数据库已知的用户: "+ dbmd.getUserName());
            System.out.println("数据库的系统函数的逗号分隔列表: "+ dbmd.getSystemFunctions());
            System.out.println("数据库的时间和日期函数的逗号分隔列表: "+ dbmd.getTimeDateFunctions());
            System.out.println("数据库的字符串函数的逗号分隔列表: "+ dbmd.getStringFunctions());
            System.out.println("数据库供应商用于 'schema' 的首选术语: "+ dbmd.getSchemaTerm());
            System.out.println("数据库URL: " + dbmd.getURL());
            System.out.println("是否允许只读:" + dbmd.isReadOnly());
            System.out.println("数据库的产品名称:" + dbmd.getDatabaseProductName());
            System.out.println("数据库的版本:" + dbmd.getDatabaseProductVersion());
            System.out.println("驱动程序的名称:" + dbmd.getDriverName());
            System.out.println("驱动程序的版本:" + dbmd.getDriverVersion());
            System.out.println("数据库中使用的表类型");
            ResultSet rs = dbmd.getTableTypes();
            while(rs.next()) {
                System.out.println(rs.getString("TABLE_TYPE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("驱动信息："
                + dbmd.getDriverMajorVersion() + " "
                + dbmd.getDriverMinorVersion());
        System.out.println("⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆");
        return dbmd;
    }

    /**
     * 执行一段sql检索语句并打印出其结果
     */
    private static void selectSqlResult(Connection conn,String sql) {
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            System.out.println("============================");
            while (rs.next()) {
                IntStream.rangeClosed(1,col).forEach(i -> {
                    try {
                        System.out.print(rs.getString(i) + "\t");
                        if ((i == 2) && (rs.getString(i).length() < 8)) {
                            System.out.print("\t");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("");
            }
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
