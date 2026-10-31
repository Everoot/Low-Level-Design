package pizza.service;

import pizza.model.Pizza;
import pizza.strategy.PricingStrategy;

public class PizzaService {
    private PricingStrategy pricingStrategy;

    public PizzaService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double getPrice(Pizza pizza) {
        return pricingStrategy.calculatePrice(pizza);
    }
}
