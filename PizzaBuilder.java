public interface PizzaBuilder {
    PizzaBuilder reset();

    PizzaBuilder addTopping(String name, double price);

    /**
     * Build a preview pizza based on current selections.
     * This does not clear selections.
     */
    Pizza preview();

    /**
     * Build the final pizza based on current selections.
     * This does not clear selections (caller can reset if desired).
     */
    Pizza build();
}

