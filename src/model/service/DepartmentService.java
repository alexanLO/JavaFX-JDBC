package model.service;

import java.util.List;

import model.components.Department;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;

public class DepartmentService {
    
    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll() {    
        return dao.findAll();
    };

    public void saveOrUpdate(Department obj){
        if(obj.getId() == null){
            dao.insert(obj);
        } else{
            dao.update(obj);
        }
    }
}
