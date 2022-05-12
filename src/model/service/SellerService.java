package Model.service;

import java.util.List;

import Model.Components.Seller;
import Model.dao.DaoFactory;
import Model.dao.SellerDao;

public class SellerService {
    
    private SellerDao dao = DaoFactory.createSellerDao();

    public List<Seller> findAll() {    
        return dao.findAll();
    };

    public void saveOrUpdate(Seller obj){
        if(obj.getId() == null){
            dao.insert(obj);
        } else{
            dao.update(obj);
        }
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId());
    }
}
