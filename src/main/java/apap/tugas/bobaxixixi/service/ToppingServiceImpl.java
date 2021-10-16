package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.ManagerModel;
import apap.tugas.bobaxixixi.model.StoreModel;
import apap.tugas.bobaxixixi.repository.ManagerDB;
import apap.tugas.bobaxixixi.repository.StoreDB;
import apap.tugas.bobaxixixi.repository.ToppingDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import apap.tugas.bobaxixixi.model.ToppingModel;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ToppingServiceImpl implements ToppingService {

    @Autowired
    ToppingDB toppingDB;

    @Override
    public void addTopping(ToppingModel topping) {
        toppingDB.save(topping);
    }

    @Override
    public boolean updateTopping(ToppingModel topping) {
        toppingDB.save(topping);
        return true;
    }

    @Override
    public List<ToppingModel> getToppingList() {
        return toppingDB.findAll();
    }

    @Override
    public ToppingModel getToppingByIdTopping(long idTopping) {
        Optional<ToppingModel> topping = toppingDB.findByIdTopping(idTopping);
        if (topping.isPresent()) {
            return topping.get();
        }
        return null;
    }

    @Override
    public void deleteTopping(Long idTopping) {
        toppingDB.deleteById(idTopping);
    }
}
