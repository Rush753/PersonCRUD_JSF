package PersonCRUD_JSF;



import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ManagedBean
@RequestScoped
public class DAO_Person implements Serializable {

    private List<Person> people = new ArrayList<Person>();

    private int id;
    private String fname;
    private String lname;
    private int age;

    @PostConstruct
    public void init() {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/person?user=root");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PERSON");
            while (rs.next())
            {
                int id = rs.getInt("ID");
                String fname = rs.getString("FName");
                String lname = rs.getString(3);
                int age = rs.getInt(4);

                Person p = new Person(id, fname, lname, age);
                people.add(p);
            }
            conn.close();
        } catch ( SQLException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void create(){
        Person p = new Person(id, fname,lname,age);
        people.add(p);
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/person?user=root");
            String str = "";
            Statement stmt = con.createStatement();
            Class cl = p.getClass();
            Field fld[] = cl.getDeclaredFields();
            for (int i = 0; i < fld.length; i++)
            {
                fld[i].setAccessible(true);
                if (fld[i].getType().getName().equals("int"))
                {
                    str += "'" + new Integer(fld[i].getInt(p)) + "',";
                } else
                {
                    str += "'" + (String) fld[i].get(p) + "',";
                }
            }
            str = str.substring(0, str.length() - 1);
            stmt.executeUpdate("INSERT INTO PERSON VALUES (" + str + ")");
            stmt.close();
            con.close();
        } catch (IllegalAccessException e1)
        {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void update(){
        Person p = new Person(id, fname,lname,age);
        String str = "";
        String strId = "";
        for (Person person : people)
        {
            Class cl = p.getClass();
            Class cl1 = person.getClass();
            Field fild[] = cl.getDeclaredFields();
            Field fild1[] = cl1.getDeclaredFields();
            try
            {
                fild[0].setAccessible(true);
                fild1[0].setAccessible(true);
                if (fild[0].getInt(p) == fild1[0].getInt(person))
                {
                    strId += new Integer(fild[0].getInt(p));
                    for (int i = 1; i < fild.length; i++)
                    {
                        fild[i].setAccessible(true);
                        fild1[i].setAccessible(true);
                        if (fild[i].getType().getName().equals("int") & fild1[i].getType().getName().equals("int"))
                        {
                            fild1[i].set(person, fild[i].getInt(p));
                            str += fild1[i].getName() + "=" + new Integer(fild1[i].getInt(person)) + ",";
                        } else
                        {
                            fild1[i].set(person, fild[i].get(p));
                            str += fild1[i].getName() + "=" + "'" + (String) fild[i].get(p) + "',";
                        }
                    }
                }
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (SecurityException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/person?user=root");
            Statement stmt = con.createStatement();
            str = str.substring(0, str.length() - 1);
            stmt.executeUpdate("UPDATE PERSON SET " + str + " WHERE ID =" + strId);
            stmt.close();
            con.close();
        } catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete(){
        Person p = new Person(id, fname,lname,age);
        String id = "";
        for (Iterator iterator = people.iterator(); iterator.hasNext();)
        {
            Person person = (Person) iterator.next();
            Class cl = p.getClass();
            Class cl1 = person.getClass();
            Field fld[] = cl.getDeclaredFields();
            Field fld1[] = cl1.getDeclaredFields();
            try
            {
                fld[0].setAccessible(true);
                fld1[0].setAccessible(true);
                if (fld[0].getInt(p) == fld1[0].getInt(person))
                {
                    id += new Integer(fld[0].getInt(p));
                    iterator.remove();
                }
            } catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/person?user=root");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM PERSON WHERE ID = " + id);
        } catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
