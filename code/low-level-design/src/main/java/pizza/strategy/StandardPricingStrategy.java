package pizza.strategy;

import pizza.enums.ToppingType;
import pizza.model.Pizza;

public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(Pizza pizza) {
        double price = 0;

        switch (pizza.getSize()) {
            case SMALL:
                price = 8;
                break;
            case MEDIUM:
                price = 10;
                break;
            case LARGE:
                price = 12;
                break;
        }

        for (ToppingType topping : pizza.getToppings()) {
            switch (topping) {
                case CHEESE:
                    price += 2;
                    break;
                case PEPPERONI:
                    price += 3;
                    break;
                case MUSHROOM:
                    price += 1.5;
                    break;
                case ONION:
                    price += 1;
                    break;
            }
        }

        return price;
    }
}
