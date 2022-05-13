package Model.dao.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.DB;
import DB.DbException;
import Model.Components.Department;
import Model.Components.Seller;
import Model.dao.SellerDao;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection; // não precisa fechar a conexão nos function, pois assim poderá ser reutilizada
                                   // deixamos para que o Program.java feche a conexão

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {

        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                    "INSERT INTO seller"
                            + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
                            + "VALUES"
                            + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

            int rowsEffected = st.executeUpdate();

            if (rowsEffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha foi afetada");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement(
                    " UPDATE seller "
                            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            + "WHERE Id = ? ");

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");

            st.setInt(1, id);

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement( // conectando ao banco de dados
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department department = instantiateDepartment(rs);
                Seller seller = instantiateSeller(rs, department);
                return seller;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement( // conectando ao banco de dados
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name");

            rs = st.executeQuery();

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                listSeller.add(seller);
            }
            return listSeller;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = connection.prepareStatement( // conectando ao banco de dados
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE DepartmentId = ? "
                            + "ORDER BY Name");

            st.setInt(1, department.getId());

            rs = st.executeQuery();

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                listSeller.add(seller);
            }
            return listSeller;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

}
