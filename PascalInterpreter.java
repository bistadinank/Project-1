import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

class PascalClass {
    private String name;
    public PascalClass(String name) { this.name = name; }
}

class PascalObject {
    private String className;
    private String value;

    public PascalObject(String className, String value) {
        this.className = className;
        this.value = value;
        //System.out.println("Object created of class: " + className + " with value: " + value);
    }

    public String getString() { return value; }
    public String getClassName() { return className; }
}

public class PascalInterpreter extends pascalBaseVisitor<Object> {
    private Map<String, PascalClass> classMap = new HashMap<>();
    private Map<String, PascalObject> objectMap = new HashMap<>();
    private Map<String, ParseTree> functionMap = new HashMap<>();
    private Map<String, String> valuemap = new HashMap<>();
    private Map<String, List<String>> funcpar = new HashMap<>();
    private List<String> ppvariables = new ArrayList<>();
    private List<String>consdesfunc = new ArrayList<>();
    private String classname;
    private Map<String, String> parameterreturn = new HashMap<>();
    @Override
    public Object visitClasstype(pascalParser.ClasstypeContext ctx) {
        //System.out.println("class:"+ctx.getChild(1).getText());
        String className = ctx.getParent().getChild(0).getText();
        //System.out.println("className:"+className);
        classMap.put(className, new PascalClass(className));
        //visit(ctx);
       // return  visitChildren(ctx);
        //System.out.println(ctx.getChild(1).getChild(1).getText());
        Pattern pattern = Pattern.compile("(private|protected)(.*?)(?=public|protected|private|constructor|destructor|$)");
        Matcher matcher = pattern.matcher(ctx.getChild(1).getText());


        while (matcher.find()) {
            String declarations = matcher.group(2); // Get everything after private/protected
            String[] parts = declarations.split(";"); // Split variables
            for (String part : parts) {
                String[] varParts = part.split(":");
                if (varParts.length > 1) {
                    ppvariables.add(varParts[0].trim()); // Extract variable name
                }
            }
        }

        // Print extracted variables
        //System.out.println("Private/Protected Variables: " + ppvariables);


        return null;
    }



    @Override
    //visitConstructorDeclaration(pascalParser.ConstructorDeclarationContext ctx)
    public Object visitConstructorDeclaration(pascalParser.ConstructorDeclarationContext ctx) {
        //System.out.println("lllllllll");
        String className = ctx.identifier().getText();

        pascalParser.FunctionDeclarationContext functionCtx =
                (pascalParser.FunctionDeclarationContext) ctx.getParent();

        if (functionCtx != null && functionCtx.block() != null) {
            functionMap.put(className + ".Create", functionCtx.block());

        } else {
            System.err.println("Error: Constructor body not found for " + className + ".Create");
        }

        return null;
    }

    @Override
    public Object visitDestructorDeclaration(pascalParser.DestructorDeclarationContext ctx) {
        String className = ctx.identifier().getText();
        pascalParser.FunctionDeclarationContext functionCtx =
                (pascalParser.FunctionDeclarationContext) ctx.getParent();

        if (functionCtx != null && functionCtx.block() != null) {
            functionMap.put(className + ".Destroy", functionCtx.block());
        } else {
            System.err.println("Error: Destructor body not found for " + className + ".Destroy");
        }

        return null;
    }

    @Override
    public Object visitFunctionDeclaration(pascalParser.FunctionDeclarationContext ctx) {
        String functionName;
        //System.err.println(ctx.getText());
        if (ctx.identifier().size() > 1) {
            functionName = ctx.identifier(0).getText() + "." + ctx.identifier(1).getText();
        } else {
            functionName = ctx.identifier(0).getText();
        }
        //String functionName = ctx.identifier().get(0).getText();
        //System.out.println("functionName:"+functionName);
        if(ctx.getText().contains("constructor"))
        {
         // System.err.println("Inside CONSTRUCTOR");
            //System.err.println(ctx.formalParameterList().getChild(1).getText());
            consdesfunc.add(ctx.identifier(1).getText());
            for(int i=1;i<ctx.formalParameterList().getChildCount();i++)
            {
                String input=ctx.formalParameterList().getChild(i).getText();
                int index = input.indexOf(':');
                if (index != -1) {
                    String result = input.substring(0, index);
                    String para="";
                    //funcpar.put(ctx.identifier(0).getText(), result);
                    funcpar.putIfAbsent(ctx.identifier(0).getText(), new ArrayList<>());
                    funcpar.get(ctx.identifier(0).getText()).add(result);
                }

            }

        }
        if(ctx.getText().contains("destructor"))
        {
            consdesfunc.add(ctx.identifier(1).getText());
        }

        else if(ctx.getText().contains("function"))
        {
           // System.err.println(ctx.identifier(1).getText());
            //System.err.println(ctx.block().compoundStatement().statements().getText());
            Pattern pattern = Pattern.compile("(\\w+)\\s*:=\\s*(\\w+)");
            Matcher matcher = pattern.matcher(ctx.block().compoundStatement().statements().getText());

            List<String> words = new ArrayList<>();

            while (matcher.find()) {
                words.add(matcher.group(1)); // Left-side word
                words.add(matcher.group(2)); // Right-side word
                parameterreturn.put(ctx.identifier(1).getText(),matcher.group(2));
            }

            // Print extracted words
           // System.out.println(words);

        }
        //System.err.println(functionName);
        functionMap.put(functionName, ctx.block());
        return null;
    }




    @Override
    public Object visitProcedureDeclaration(pascalParser.ProcedureDeclarationContext ctx) {
        String procedureName = ctx.identifier().getText();
        functionMap.put(procedureName, ctx.block());
        return null;
    }

    @Override
    public Object visitAssignmentStatement(pascalParser.AssignmentStatementContext ctx) {
        String varName = ctx.variable().getText();
        String value = ctx.expression().getText();
        //System.out.println("Assignment statement: " + varName + "  vvv " + value);
        String[] consdes = consdesfunc.toArray(new String[0]);
        if (value.contains(consdes[0])) {
            //System.out.println("hi");
            String[] parts = value.split("\\.");
            String className = parts[0];
            classname = className;
            String constructorArg = value.substring(value.indexOf("(") + 1, value.indexOf(")")).replace("'", "");
            PascalObject obj = new PascalObject(className, constructorArg);
            objectMap.put(varName, obj);
            //for loop
            String[] eachPara = constructorArg.split(",");
            List<String> s=funcpar.get(className);
            if(s!=null)
            {
                String[] valuesArray= s.toArray(new String[0]);
                for(int i=0;i<eachPara.length;i++)
                {
                    valuemap.put(valuesArray[i], eachPara[i]);
                }


            }
           // System.err.println("Clss: " + className + "  para " + obj.getString());
           // System.err.println(funcpar.get("TAtomicMass").getString());
           // System.out.println(className + ".Create");
            executeFunction(className + "."+consdes[0]);
        } else if (value.contains(consdes[1])) {
            PascalObject obj = objectMap.get(varName);
            if (obj != null) {
                executeFunction(obj.getClassName() + "."+consdes[1]);
                objectMap.remove(varName);
            }

        }

        else if(value.contains(".") && value.contains("("))
        {
            String a=classname;
            //System.out.println(value);
            String[] parts = value.split("\\.");
            String functionName = parts[1].replace("(", "").replace(")", "");
            executeFunction(classname + "."+functionName);
            if(parameterreturn.containsKey(functionName))
            {
                String para=parameterreturn.get(functionName);
                value=valuemap.get(para);
                //System.out.println(value1);
            }
            valuemap.put(varName,value);
        }

        else {

            if(value.contains("."))
            {

                String[] parts = value.split("\\.");
                value=parts[1];
                if(ppvariables.contains(value))
                {
                    System.err.println("Trying to access private/protected variable");
                    value="";
                }
            }


            if(valuemap.get(value) != null)
            {
                //System.out.println("Assigning " + value + " to " + varName);
                String s = valuemap.get(value);
                valuemap.put(varName, s);
                //System.out.println(s);
            }
            else {
                valuemap.put(varName,value);
                //System.out.println("Assigning " + value + " to " + varName);

            }

        }
        return null;
    }

    @Override
    public Object visitFunctionDesignator(pascalParser.FunctionDesignatorContext ctx) {
        String objName = ctx.identifier(0).getText();
        String methodName = ctx.identifier(1).getText();
/*
        List<String> parameters = new ArrayList<>();

        // Check if the function has parameters
        if (ctx.parameterList() != null) {
            for (pascalParser.ActualParameterContext paramCtx : ctx.parameterList().actualParameter()) {
                String paramType = paramCtx.getText();  // Get the parameter name
                //String paramType = paramCtx.type_().getText();  // Get the parameter type
                parameters.add(paramType);
            }
        }

        if (methodName.equals("Create")) {  // If it's a constructor
            PascalObject obj = new PascalObject(objName);
            objectMap.put(objName, obj);  // Store the object
            obj.setParameters(parameters); // Store constructor parameters
            return obj;
        }
*/
        if (objectMap.containsKey(objName)) {
            PascalObject obj = objectMap.get(objName);
            if (methodName.equals("GetString")) {
                return obj.getString();
            }
        }





        if (objectMap.containsKey(objName)) {
            PascalObject obj = objectMap.get(objName);
            if (methodName.equals("GetString")) {
                return obj.getString();
            }
        }
        return null;
    }

    @Override
    public Object visitProcedureStatement(pascalParser.ProcedureStatementContext ctx) {
        String procName = ctx.identifier().getText();
       // System.out.println("hello");
        if (procName.equals("writeln")) {

            StringBuilder output = new StringBuilder();
            pascalParser.ParameterListContext paramListCtx = ctx.parameterList();

            if (paramListCtx != null) {
                for (ParseTree param : paramListCtx.children) {
                    String paramText = param.getText();

                    if (paramText.startsWith("'") && paramText.endsWith("'")) {
                        output.append(paramText, 1, paramText.length() - 1);
                    }
                    else {
                        if(valuemap.containsKey(paramText))
                        {
                            String value = valuemap.get(paramText);
                            output.append(value);
                        }

                        else if (objectMap.containsKey(paramText)) {
                            PascalObject obj = objectMap.get(paramText);
                            output.append(obj.toString());
                        }
                        else if (classMap.containsKey(paramText)) {
                            PascalClass cls = classMap.get(paramText);
                            output.append(cls.toString());
                        }
                        else if (functionMap.containsKey(paramText)) {
                            output.append("[Function: ").append(paramText).append("]");
                        }
                        else {
                           // output.append("[Undefined: ").append(paramText).append("]");
                        }
                    }
                }
            }

            System.out.println(output.toString());
        }

        executeFunction(procName);
        return null;
    }



    private void executeFunction(String functionName) {
        //System.out.println(functionName);
        if (functionMap.containsKey(functionName)) {

            //System.out.println(functionName);
            visit(functionMap.get(functionName));
        }
    }

    public static void main(String[] args) throws Exception {
        String pasFilePath = "C:/Users/Ahmed/Downloads/test3.pas";
        String pascalCode = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(pasFilePath)));

        pascalLexer lexer = new pascalLexer(CharStreams.fromString(pascalCode));
        pascalParser parser = new pascalParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.block();
        //System.out.println("hi"+tree.toStringTree(parser));

        PascalInterpreter interpreter = new PascalInterpreter();
        interpreter.visit(tree);
    }
}
