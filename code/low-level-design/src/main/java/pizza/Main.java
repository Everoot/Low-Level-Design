package pizza;

import pizza.enums.PizzaSize;
import pizza.enums.ToppingType;
import pizza.model.Pizza;
import pizza.service.PizzaService;
import pizza.strategy.StandardPricingStrategy;

public class Main {
    public static void main(String[] args) {
        Pizza pizza = new Pizza(PizzaSize.LARGE);

        pizza.addTopping(ToppingType.CHEESE);
        pizza.addTopping(ToppingType.PEPPERONI);

        PizzaService service = new PizzaService(new StandardPricingStrategy());

        double price = service.getPrice(pizza);

        System.out.println("Final price: $" + price);
    }
}
