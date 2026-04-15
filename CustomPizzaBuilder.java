import java.util.ArrayList;
import java.util.List;

public class CustomPizzaBuilder implements PizzaBuilder {
    private final List<ToppingSelection> selections = new ArrayList<>();

    @Override
    public PizzaBuilder reset() {
        selections.clear();
        return this;
    }

    @Override
    public PizzaBuilder addTopping(String name, double price) {
        selections.add(new ToppingSelection(name, price));
        return this;
    }

    @Override
    public Pizza preview() {
        return buildInternal();
    }

    @Override
    public Pizza build() {
        return buildInternal();
    }

    private Pizza buildInternal() {
        Pizza pizza = new CustomizationPizza();
        for (ToppingSelection t : selections) {
            pizza = new MenuTopping(pizza, t.name, t.price);
        }
        return pizza;
    }

    private static class ToppingSelection {
        final String name;
        final double price;

        private ToppingSelection(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }
}

