package mapeamento;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn {
	
	public String serverName = "localhost";
	public String userName = "root";
	public String userPwd = "";
	public String database = "projeto";
	
	public Connection getConnection(){
		Connection conn = null;
		
		try{
			String drivename = "com.mysql.jdbc.Driver";
			Class.forName(drivename);
			
			//Configurando banco de dados:
			
			String url = "jdbc:mysql://"+this.serverName+"/"+this.database;
			conn = DriverManager.getConnection(url, this.userName, this.userPwd);
			
		} catch (ClassNotFoundException e){// Driver não existe
			System.out.println("O driver não está instalado:\n"+e.getMessage());
		} catch(SQLException e){
			System.out.println("Não foi possivel conectar com servidro MySQL:\n"+e.getMessage());
		}
		
		return conn;
	}

}
