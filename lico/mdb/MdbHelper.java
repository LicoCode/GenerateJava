package lico.mdb;

import java.sql.*;


import java.util.*;

import lico.classutil.ClassAttribute;
import lico.classutil.ClassMethod;
import net.ucanaccess.jdbc.UcanaccessDriver;


public class MdbHelper {

	private static void dump(ResultSet rs,String exName) throws SQLException {
		System.out.println("-------------------------------------------------");
		System.out.println();
		System.out.println(exName+" result:");
		System.out.println();

		while (rs.next()) {
			System.out.print("| ");
			int j=rs.getMetaData().getColumnCount();
			for (int i = 1; i <=j ; ++i) {
				Object o = rs.getObject(i);
				System.out.print(o + " | ");
			}
			System.out.println();
			System.out.println();
		}
	}

	private static Connection getUcanaccessConnection(String pathNewDB,String user,String password) throws SQLException{
		   String url = UcanaccessDriver.URL_PREFIX + pathNewDB+";newDatabaseVersion=V2003";

		   return DriverManager.getConnection(url, user, password);
	}

	private Connection ucaConn;

	public MdbHelper(String pathNewDB,String user,String password) {
		try {
			this.ucaConn=getUcanaccessConnection(pathNewDB,user,password);
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		Statement st = null;
		ResultSet rs;
		try {
			st =this.ucaConn.createStatement();
			rs=st.executeQuery(sql);
			//dump(rs,"executeQuery");
		} finally {
			if (st != null)
				st.close();
		}

		return rs;
	}

	public ArrayList<CDODefinition> getClassDef(int startRow, int count){
		ArrayList<CDODefinition> classlist = new ArrayList<>();
		try {
			String sql = null;
			if(startRow>1) {
				sql = "select top " + count + " cdo.CDODefID,parent.CDOName,cdo.CDOName,cdo.CDODescription,db.DBTableName,cdo.IsAbstract from " +
						"CDODefinition as cdo " +
						"left join CDODefinition as parent on cdo.ParentCDOID = parent.CDODefID " +
						"left join DBTableDefinition as db on db.DBTableID = cdo.DefaultTableID " +
						"where cdo.CDODefID not in (select top " + (startRow - 1) + " c.CDODefID from CDODefinition as c order by c.CDODefID) " +
						"order by cdo.CDODefID";
			}else{
				sql = "select top " + count + " cdo.CDODefID,parent.CDOName,cdo.CDOName,cdo.CDODescription,db.DBTableName,cdo.IsAbstract from " +
						"CDODefinition as cdo " +
						"left join CDODefinition as parent on cdo.ParentCDOID = parent.CDODefID " +
						"left join DBTableDefinition as db on db.DBTableID = cdo.DefaultTableID " +
						"order by cdo.CDODefID";
			}
			System.out.println(sql);
			ResultSet rs = executeQuery(sql);
			while (rs.next()) {
				CDODefinition classdef = new CDODefinition();
				Object o = rs.getObject(1);
				classdef.id = Integer.parseInt(o.toString());
				o = rs.getObject(2);
				classdef.parentClass = o != null ? o.toString() : "";
				o = rs.getObject(3);
				classdef.className = o.toString();
				o = rs.getObject(4);
				classdef.annotation = o != null ? o.toString() : "";
				o = rs.getObject(5);
				classdef.dbTable = o != null ? o.toString() : "";
				o = rs.getObject(6);
				classdef.isAbstract = o != null ? Boolean.parseBoolean(o.toString()) : false;
				classdef.packageName = "lico.designer.cdo";
				classdef.importNames = new HashSet<>();
				classdef.importNames.add("java.util.Date");
				classdef.importNames.add("java.util.ArrayList");
				classlist.add(classdef);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return classlist;
	}

	public HashSet<ClassAttribute> getClassField(CDODefinition cdo) {
		HashSet<ClassAttribute> cdoFields = new HashSet<>();
		try {
			String sql = "select cdo.CDOName,cdof.FieldID,cdof.DefaultValue,cdof.FieldName," +
					"cdof.CPPDataTypeID,fielddef.FieldDefName,cdof.IsListType," +
					"cdof.FieldDescription,db.DBTableName,cdof.DefaultValueExpression,cdof.CurrentValueExpression,dbc.ColumnName from CDODefinition as cdo " +
					"inner join CDOFields as cdof on cdof.CDODefID = cdo.CDODefID " +
					"left join FieldDefinitions as fielddef on cdof.FieldDefID = fielddef.FieldDefID " +
					"left join DBTableDefinition as db on db.DBTableID = cdof.DBTableID " +
					"left join DBColumns as dbc on dbc.DBColumnID = cdof.DBColumnID " +
					"where cdo.CDODefID = '"+cdo.id +"'";
			System.out.println(sql);
			ResultSet rs = executeQuery(sql);
			while (rs.next()) {
				CDOField classField = new CDOField();
				Object o = rs.getObject(2);
				classField.id = o != null ? Integer.parseInt(o.toString()):new Integer(0);
				o = rs.getObject(4);
				classField.attributeName = o != null ? o.toString() : "";
				o = rs.getObject(5);
				switch (o.toString()){
					case "5" :
						if(classField.attributeName.equals("InstanceID")){
							classField.attributeType = "Integer";
						}else{
							o = rs.getObject(6);
							classField.attributeType = o != null ? o.toString() : "";
						}
						break;
					case "1" :
						classField.attributeType = "Integer";break;
					case "2" :
						classField.attributeType = "Float";break;
					case "3" :
						classField.attributeType = "Short";break;
					case "4" :
						classField.attributeType = "String";break;
					case "6" :
						classField.attributeType = "Date";break;
					case "7" :
						classField.attributeType = "Boolean";break;
					case "9" :
						classField.attributeType = "Double";break;
					default  :break;
				}
				if("Boolean".equals(classField.attributeType)){
					o = rs.getObject(3);
					classField.defaultValue = o != null && "1".equals(o.toString()) ? "true" : "";
					o = rs.getObject(10);
					classField.defaultValueExpression = o != null ? "1".equals(o.toString())?"true":"":"";
					o = rs.getObject(11);
					classField.currentValueExpression = o != null ? "1".equals(o.toString())?"true":"":"";
				}else{
					o = rs.getObject(3);
					classField.defaultValue = o != null ? o.toString() : "";
					o = rs.getObject(10);
					classField.defaultValueExpression = o != null ? o.toString() : "";
					o = rs.getObject(11);
					classField.currentValueExpression = o != null ? o.toString() : "";
				}
				o = rs.getObject(7);
				classField.isList = o != null ? "true".equals(o.toString())?true:false:false;
				o = rs.getObject(8);
				classField.annotation = o != null ? o.toString() : "";
				o = rs.getObject(12);
				classField.dbColumn = o != null ? o.toString() : "";
				classField.listType = "ArrayList";
				classField.permission = "public";
				cdoFields.add(classField);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cdoFields;
	}

	//Todo getFunction
	public CDODefinition getFunction() {
		return null;
	}

	public HashSet<ClassMethod> getMethod(CDODefinition cdo) {
		HashSet<ClassMethod> cdoMethods = new HashSet<>();
		cdoMethods.addAll(getFieldMethod(cdo));
		cdoMethods.addAll(getCDOMethod(cdo));
		cdoMethods.addAll(getUserCDOMethod(cdo));

		//Todo add method paramter and methodbody
		return cdoMethods;
	}

	private HashSet<ClassMethod> getUserCDOMethod(CDODefinition cdo) {
		String sql = "select cdo.CDOName,cvm.Name,cvm.Description ,ce.CallerType,cvm.CLFEventMapID from CDODefinition as cdo "+
				"inner join CLFEventMap as cvm on cvm.CDODefID = cdo.CDODefID "+
				"inner join CLFEvents as ce on cvm.CLFEventID = ce.CLFEventID "+
				"where cdo.CDODefID = '" + cdo.id + "' and ce.Name = 'UserDefinedCDOMethod'";
		System.out.println(sql);
		HashSet<ClassMethod> cdoMethods = new HashSet<>();
		try {
			ResultSet rs = executeQuery(sql);
			while (rs.next()) {
				CDOMethod cdoMethod = new CDOMethod();
				Object o = rs.getObject(3);
				cdoMethod.annotation = o != null ? o.toString() : "";
				o = rs.getObject(4);
				cdoMethod.methodType = o != null ? "1".equals(o.toString())?"Class":"Field" : "";
				o = rs.getObject(2);
				cdoMethod.methodName = o != null ? o.toString() : "";
				o = rs.getObject(5);
				cdoMethod.id = Integer.parseInt(o.toString());
				cdoMethod.returnType = "void";
				cdoMethod.permission = "public";
				cdoMethods.add(cdoMethod);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cdoMethods;
	}

	private HashSet<ClassMethod> getCDOMethod(CDODefinition cdo) {
		String sql = "select cdo.CDOName,ce.Name,cvm.Description,ce.CallerType,cvm.CLFEventMapID from CDODefinition as cdo "+
				"inner join CLFEventMap as cvm on cvm.CDODefID = cdo.CDODefID "+
				"inner join CLFEvents as ce on cvm.CLFEventID = ce.CLFEventID "+
				"where cdo.CDODefID = '" + cdo.id + "' and ce.CallerType = 1 and ce.Name <> 'UserDefinedCDOMethod'";

		HashSet<ClassMethod> cdoMethods = new HashSet<>();

		System.out.println(sql);
		try {
			ResultSet rs = executeQuery(sql);
			while (rs.next()) {
				CDOMethod cdoMethod = new CDOMethod();
				Object o = rs.getObject(2);
				cdoMethod.methodName = o != null ? o.toString() : "";
				o = rs.getObject(3);
				cdoMethod.annotation = o != null ? o.toString() : "";
				o = rs.getObject(4);
				cdoMethod.methodType = o != null ? "1".equals(o.toString())?"Class":"Field" : "";
				o = rs.getObject(5);
				cdoMethod.id = Integer.parseInt(o.toString());
				cdoMethod.returnType = "void";
				cdoMethod.permission = "public";
				cdoMethods.add(cdoMethod);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cdoMethods;
	}

	private HashSet<ClassMethod> getFieldMethod(CDODefinition cdo) {
		HashSet<ClassMethod> cdoMethods = new HashSet<>();
		String sql = "select cdo.CDOName,cdof.FieldName,cvm.Description ,ce.CallerType,fielddef.FieldDefName ,cvm.CLFEventMapID,cdof.CPPDataTypeID,cdof.IsListType from CDODefinition as cdo "+
				"inner join CLFEventMap as cvm on cvm.CDODefID = cdo.CDODefID "+
				"inner join CLFEvents as ce on cvm.CLFEventID = ce.CLFEventID "+
				"inner join CDOFields as cdof on cvm.CallerID = cdof.FieldID " +
				"left join FieldDefinitions as fielddef on cdof.FieldDefID = fielddef.FieldDefID " +
				"where cdo.CDODefID = '" + cdo.id + "' and ce.Name = 'OnGetValue'";
		// ce.Name = OnGetValue)
		System.out.println(sql);
		try {
			ResultSet rs = executeQuery(sql);
			while (rs.next()) {
				CDOMethod cdoMethod = new CDOMethod();
				Object o = rs.getObject(2);
				cdoMethod.methodName = o != null ? "get"+o.toString() : "";
				cdoMethod.methodBody = new ArrayList<>();
				cdoMethod.methodBody.add("return "+(o != null ? o.toString() : ""));
				o = rs.getObject(3);
				cdoMethod.annotation = o != null ? o.toString() : "";
				o = rs.getObject(4);
				cdoMethod.methodType = o != null ? "1".equals(o.toString())?"Class":"Field" : "";



				cdoMethod.permission = "public";
				o = rs.getObject(7);
				switch (o.toString()){
					case "5" :
						o = rs.getObject(5);
						cdoMethod.returnType = o != null ? o.toString() : "";break;
					case "1" :
						cdoMethod.returnType = "Integer";break;
					case "2" :
						cdoMethod.returnType = "Float";break;
					case "3" :
						cdoMethod.returnType = "Short";break;
					case "4" :
						cdoMethod.returnType = "String";break;
					case "6" :
						cdoMethod.returnType = "Date";break;
					case "7" :
						cdoMethod.returnType = "Boolean";break;
					case "9" :
						cdoMethod.returnType = "Double";break;
					default  :break;
				}
				if(rs.getBoolean(8)){
					cdoMethod.returnType = "ArrayList<"+cdoMethod.returnType+">";
				}
				cdoMethods.add(cdoMethod);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cdoMethods;
	}

	private ArrayList<String> getMethodBody(CDOMethod classMethod) {
		ArrayList<String> methods = new ArrayList<>();
		String sql = "select * from CLFEventMap as cvm " +
				"inner join CLFFunctions clff on clff.CLFID = cvm.CLFID " +
				"inner join FunctionDefinition as fd on fd.FunctionID = clff.FunctionID " +
				"inner join CLFFunParm as cfp on clff.";
		System.out.println(sql);
		try {
			ResultSet rs = executeQuery(sql);
			while (rs.next()) {

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return  null;
	}


}
