package Model.dao;

import java.util.List;

import Model.Components.Department;
import Model.Components.Seller;

public interface SellerDao {
    
    void insert(Seller seller);
    void update(Seller seller);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}
