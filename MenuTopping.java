public class MenuTopping extends BaseTopping {
    private final String toppingName;
    private final double toppingCost;

    public MenuTopping(Pizza pizza, String toppingName, double toppingCost) {
        this.pizza = pizza;
        this.toppingName = toppingName;
        this.toppingCost = toppingCost;
    }

    @Override
    public String getToppingName() {
        return toppingName;
    }

    @Override
    public double getToppingCost() {
        return toppingCost;
    }

    @Override
    public String getName() {
        return pizza.getName() + " + " + toppingName;
    }

    @Override
    public double getCost() {
        return pizza.getCost() + toppingCost;
    }
}
