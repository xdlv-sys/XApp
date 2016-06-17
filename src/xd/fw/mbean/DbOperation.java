package xd.fw.mbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
@ManagedResource(objectName = "xapp:name=DbOperation", description = "derby db operations")
public class DbOperation {
    @Autowired
    JdbcTemplate derbyTpl;

    @ManagedOperation(description = "Select Sql")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name="sql",description = "sql"),
            @ManagedOperationParameter(name="rowNumber",description = "rowNumber")
    })
    public String selectSql(String sql, int rowNumber){
        StringBuffer buffer = new StringBuffer();
        derbyTpl.query(sql,(rs)->{
            for (int i=0;i<rowNumber;i++){
                buffer.append(rs.getString(i + 1)).append(",");
            }
            buffer.append("\n");
        });
        return buffer.toString();
    }

    @ManagedOperation(description = "Run Sql")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name="sql",description = "sql")
    })
    public String runSql(String sql){
        derbyTpl.execute(sql);
        return "OK";
    }

}
