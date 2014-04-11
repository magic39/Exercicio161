
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SERGIOFI
 */

interface Bolsa {
    boolean iniciar();
    boolean actualizar();
    boolean novo(String login, String clave, float capital);
    boolean identificar(String login, String clave);
}
    
interface Inversor {
    boolean comprar(int id, int cantidade);
    boolean vender(int id, int cantidade);
    float valorar();
}

interface Resumible {
    String resumir();
}



class BolsaEnBD implements Bolsa, Resumible {
    private String login;
    private String clave;
    private float capital;
    public  static Connection con;

    public String resumir() {
        String resultado = ".....................\n";
        resultado += "Este é o resumo da bolsa implicada:\n";
        resultado += "Número de inversores: ";
        try {
            PreparedStatement ps = (PreparedStatement) BolsaEnBD.con.prepareStatement("SELECT COUNT(login) AS usuarios FROM usuarios");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado += rs.getInt("usuarios") + "\n";
            }
            
        } catch(SQLException e){
            resultado += "Non foi posible establecer conexión coa base de datos\n";
        }
        resultado += ".....................\n";
        return resultado;
     }
    
    public @Override
    boolean iniciar(){
        boolean correcto = false;
        try {
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3307/bolsa","root","qwerty");
            correcto = true;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return correcto; 
    }
    
    public @Override
    boolean novo(String login, String clave, float capital){
        PreparedStatement ps = null;
        boolean correcto = false;
        try {
            ps = (PreparedStatement) con.prepareStatement("SELECT * FROM usuario WHERE login = ? AND clave = ?");
            ps.setString(1, login);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("O usuario xa existe");
            }else{
            ps = (PreparedStatement) con.prepareStatement("INSERT INTO usuario VALUES(null,?,?,?)");
            ps.setString(1, login);
            ps.setString(2, clave);
            ps.setFloat(3, capital);
            ps.executeUpdate();
            correcto = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(BolsaEnBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }


    public boolean actualizar() {
        PreparedStatement ps = null;
        boolean actualizar = false;
        try {
            ps = (PreparedStatement) con.prepareStatement("SELECT * FROM valores");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                actualizar = true;
                ps = (PreparedStatement) con.prepareStatement("UPDATE valores SET valor = ? WHERE id = ?");
                ps.setDouble(1, 1 + (Math.random()*10/10) - (Math.random()*10/100)) ;
                ps.setInt(2, rs.getInt("id"));
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BolsaEnBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return actualizar;
    }


    public boolean identificar(String login, String clave) {
        PreparedStatement ps = null;
        boolean correcto = false;
        try {
            ps = (PreparedStatement) con.prepareStatement("SELECT * FROM usuario WHERE login = ? AND clave = ?");
            ps.setString(1, login);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("Benvido "+rs.getString("login"));
                correcto = true;
            }else{
                System.out.println("Datos Incorrectos");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BolsaEnBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
    
}




class InversorPrivado implements Inversor, Resumible{
    
    private String login;
    
    public String resumir() {
        return "Este é o resumo da bolsa implicada";
    }
    
    
    public @Override
    boolean comprar(int id, int cantidade){
        boolean comprar = false;
        try {
            PreparedStatement comprobar  = (PreparedStatement) BolsaEnBD.con.prepareStatement("SELECT * FROM usuarios_valores WHERE id_valor = ?");
            comprobar.setInt(1, id);
            ResultSet rs = comprobar.executeQuery();
            if(rs.next()){
                PreparedStatement ps = (PreparedStatement) BolsaEnBD.con.prepareStatement("UPDATE usuarios_valores SET login = ?, id_valor = ?, cantidade = ?");
                ps.setString(1, login);
                ps.setInt(2, id);
                ps.setInt(3, cantidade+rs.getInt("cantidade"));
                ps.executeUpdate();
                System.out.println("Datos modificados correctamente");
                
            }else{
                PreparedStatement ps = (PreparedStatement) BolsaEnBD.con.prepareStatement("INSERT INTO usuarios_valores VALUES(?,?,?)");
                ps.setString(1, login);
                ps.setInt(2, id);
                ps.setInt(3, cantidade);
                ps.executeUpdate();
                System.out.println("Datos ingresados correctamente");
            }
            
            
            
                PreparedStatement capital = (PreparedStatement) BolsaEnBD.con.prepareStatement(
                            "UPDATE usuario SET capital = capital -" +
                            " (SELECT valor FROM valores WHERE id=?) * ?" +
                            " WHERE login=?");
                capital.setInt(1, id);
                capital.setInt(2, cantidade);
                capital.setString(3, login);            
                capital.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(InversorPrivado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comprar;
    }

    @Override
    public boolean vender(int id, int cantidade) {
        boolean vender = false;
        try {
            PreparedStatement comprobar  = (PreparedStatement) BolsaEnBD.con.prepareStatement("SELECT * FROM usuarios_valores WHERE id_valor = ?");
            comprobar.setInt(1, id);
            ResultSet rs = comprobar.executeQuery();
                if(rs.next()){
                        PreparedStatement ps = (PreparedStatement) BolsaEnBD.con.prepareStatement("UPDATE usuarios_valores SET login = ?, id_valor = ?, cantidade = ?");
                        ps.setString(1, login);
                        ps.setInt(2, id);
                        ps.setInt(3, (rs.getInt("cantidade")-(cantidade)));
                        ps.executeUpdate();
                        System.out.println("Datos modificados correctamente");
                }else{
                    System.out.println("No tienes acciones para vender");
                }
                
                PreparedStatement capital = (PreparedStatement) BolsaEnBD.con.prepareStatement(
                            "UPDATE usuario SET capital = capital +" +
                            " (SELECT valor FROM valores WHERE id=?) * ?" +
                            " WHERE login=?");
                    capital.setInt(1, id);
                    capital.setInt(2, cantidade);
                    capital.setString(3, login);            
                    capital.executeUpdate();
                    
        } catch (SQLException ex) {
            Logger.getLogger(InversorPrivado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vender;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public float valorar() {
        float resultado = 0;
        try {
            PreparedStatement ps = (PreparedStatement) BolsaEnBD.con.prepareStatement(
                    "SELECT SUM(cantidade*valor) AS total" +
                    " FROM usuarios_valores uv INNER JOIN valores v" +
                    " ON uv.id_valor=v.id WHERE login=?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getFloat("total");
            }   
        } catch(SQLException e){
            System.out.println("O usuario " + login + " comprou valores");
        }
        return resultado;
    }
    
    
}

public class Exercicio161 {

    /**
     * @param args the command line arguments
     */
    

    
    
    public static void main(String[] args) {
        BolsaEnBD bolsa1 = new BolsaEnBD();
        if(bolsa1.iniciar()){
            bolsa1.actualizar();
            bolsa1.resumir();
            if(bolsa1.identificar("Antonio", "abc123.")){
                InversorPrivado i1 = new InversorPrivado();
                i1.setLogin("Antonio");
                i1.vender(1, 6000);
                i1.resumir();
                i1.valorar();
            }
        }
    }
}
