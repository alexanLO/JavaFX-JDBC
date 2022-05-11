package Model.dao;

import DB.DB;
import Model.dao.Resource.DepartmentDaoJDBC;
import Model.dao.Resource.SellerDaoJDBC;

public class DaoFactory { // daofactory tera functions static para instanciar o dao

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection()); // isso é para criar um seller sem expor a implementação,
                                                      // mostrando apenas a interface
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
