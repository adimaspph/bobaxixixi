package apap.tugas.bobaxixixi.service;

import apap.tugas.bobaxixixi.model.ToppingModel;

import java.util.List;

public interface ToppingService {
    void addTopping(ToppingModel topping);

    boolean updateTopping(ToppingModel topping);

    List<ToppingModel> getToppingList();

    ToppingModel getToppingByIdTopping(long idTopping);

    void deleteTopping(Long idTopping);
}