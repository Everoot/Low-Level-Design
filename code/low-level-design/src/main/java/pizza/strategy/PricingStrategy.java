package pizza.strategy;

import pizza.model.Pizza;

public interface PricingStrategy {
    double calculatePrice(Pizza pizza);
}
