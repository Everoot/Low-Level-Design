package pizza.model;

import pizza.enums.PizzaSize;
import pizza.enums.ToppingType;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private PizzaSize size;
    private List<ToppingType> toppings;

    public Pizza(PizzaSize size) {
        this.size = size;
        this.toppings = new ArrayList<>();
    }

    public void addTopping(ToppingType topping) {
        toppings.add(topping);
    }

    public PizzaSize getSize() {
        return size;
    }

    public List<ToppingType> getToppings() {
        return toppings;
    }
}
