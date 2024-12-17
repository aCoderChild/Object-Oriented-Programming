import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Week10 {
    // Regex pattern to match the package declaration in the file content.
    private static final Pattern PACKAGE_PATTERN = Pattern.compile(
            "^package\\s+(.*);$",
            Pattern.MULTILINE
    );

    // Regex pattern to match the import statements in the file content.
    private static final Pattern IMPORT_PATTERN = Pattern.compile(
            "^import\\s+(?:static\\s+)?((?:\\w+\\.)+(\\w+));$",
            Pattern.MULTILINE
    );

    // Regex pattern to match class, interface, or enum declarations.
    private static final Pattern CLASS_PATTERN = Pattern.compile(
            "^(?:\\s{2})*(?:(?:abstract|public|private|protected|static|final)\\s+)*"
                    + "(class|interface|enum)\\s+([^<\\s]+)([^{]+)?\\s*\\{",
            Pattern.MULTILINE
    );

    // Regex pattern to match static method signatures within the file content.
    private static final Pattern STATIC_METHOD_PATTERN = Pattern.compile(
            "^(?<!\\s{0,20}/\\*\\n)" // Negative lookbehind to avoid multi-line comments
                    + "(?:\\s{2})+(?:(?:public|private|protected)\\s+)*"
                    + "static\\s+(?:final\\s+)?[\\w<>,.?\\[\\]\\s]+\\s+(\\w+)"
                    + "\\s*\\(([\\w<>,.?\\[\\]\\s]*)\\)\\s*\\{",
            Pattern.MULTILINE
    );

    // Map to store mappings between simple data types and their fully qualified types.
    private static final Map<String, String> dataTypeMap = new HashMap<>();

    /**
     * Converts a simple data type to its fully qualified type.
     * If the type is generic, recursively converts its components.
     *
     * @param dataType The simple data type string to convert.
     * @return The fully qualified type string.
     */
    private static String toFullType(String dataType) {
        if (dataTypeMap.containsKey(dataType)) {
            return dataTypeMap.get(dataType); // Return fully qualified type if found in the map.
        } else if (dataType.matches("[A-Z]\\w+")) {
            return "java.lang." + dataType; // Default types like String or Integer.
        } else if (dataType.contains("<")) {
            // Handle generic types (e.g., List<String>).
            String[] parts = dataType.split("<");
            parts[0] = toFullType(parts[0]); // Convert the main type.
            parts[1] = toFullType(parts[1].replace(">", "")); // Convert the generic component.

            // Return the reconstructed fully qualified generic type.
            return String.format(
                    "%s<%s>",
                    parts[0],
                    parts[1]
            );
        } else {
            return dataType; // Return the type as is if no transformation is needed.
        }
    }

    /**
     * Extracts all static method signatures from the given Java program content.
     *
     * This includes reading package names, import statements, and class declarations
     * to create fully qualified parameter types for the extracted methods.
     *
     * @param fileContent The content of the Java program file.
     * @return A list of all static method signatures in the file.
     */
    public static List<String> getAllFunctions(String fileContent) {
        List<String> allMethods = new ArrayList<>();

        // Extract package name if it exists.
        String packageName = "";
        Matcher matcher = PACKAGE_PATTERN.matcher(fileContent);
        if (matcher.find()) {
            packageName = matcher.group(1);
        }

        // Extract all import statements and populate the dataTypeMap.
        matcher = IMPORT_PATTERN.matcher(fileContent);
        while (matcher.find()) {
            String fullImport = matcher.group(1); // Full import path.
            String nameImport = matcher.group(2); // Short name of the import.
            dataTypeMap.put(nameImport, fullImport); // Map the short name to its full path.
        }

        // Extract class, interface, and enum names and add them to the dataTypeMap.
        matcher = CLASS_PATTERN.matcher(fileContent);
        while (matcher.find()) {
            String className = matcher.group(2); // Class or interface name.
            dataTypeMap.put(className, packageName + "." + className); // Map to its package.
        }

        // Extract all static method signatures from the file content.
        matcher = STATIC_METHOD_PATTERN.matcher(fileContent);
        while (matcher.find()) {
            String methodName = matcher.group(1); // Method name.

            String allParams = matcher.group(2); // Method parameters.
            StringBuilder allTypes = new StringBuilder();
            allTypes.append("(");

            if (!allParams.isEmpty()) {
                // Clean up parameter formatting and split into individual parameters.
                allParams = allParams.replaceAll("\\.{3}", ""); // Remove varargs notation.
                allParams = allParams.replace("\n", "").trim(); // Remove newlines and trim whitespace.

                String[] params = allParams.split(", "); // Split parameters by comma.
                for (int i = 0; i < params.length; i++) {
                    params[i] = toFullType(params[i].split(" ")[0]); // Resolve parameter type.
                }

                // Append fully qualified parameter types to the method signature.
                for (String param : params) {
                    allTypes.append(param).append(",");
                }

                allTypes.deleteCharAt(allTypes.length() - 1); // Remove the trailing comma.
            }

            allTypes.append(")");
            allMethods.add(methodName + allTypes.toString()); // Add the method signature to the list.
        }

        return allMethods; // Return the list of static method signatures.
    }
}
