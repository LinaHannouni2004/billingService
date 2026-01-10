package ma.emsi.linahannouni.mcpserver.tools;

import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class McpTools {
    @McpTool(name = "getEmployee",
            description = "get information about a given employee")
    public Employee getEmployee (@McpArg(description = "the name of the employee") String name)
    {
        return new Employee(name,12300,4);
    }

    @McpTool(description = "get all employees")
    public List<Employee> getAllEmployees()
    {
        return List.of(
                new Employee("Hassan",12300,2),
                new Employee("Mohamed",1200,1)
        );






}
}
record Employee(String name, double salary,int seniority){}





