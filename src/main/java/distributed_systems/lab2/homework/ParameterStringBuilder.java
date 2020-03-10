package distributed_systems.lab2.homework;

import java.util.Map;

public class ParameterStringBuilder {
    public static String getParametersString(Map<String, String> parameters) {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            result.append("/");
            result.append(entry.getValue());
        }

        return result.toString();
    }
}
