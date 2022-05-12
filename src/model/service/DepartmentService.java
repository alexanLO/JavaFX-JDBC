package Model.service;

import java.util.List;

import Model.Components.Department;
import Model.dao.DaoFactory;
import Model.dao.DepartmentDao;

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

    public void remove(Department obj){
        dao.deleteById(obj.getId());
    }
}
