/**
 * This class creates items for the Brooklyn Murder GUI based game.
 *
 * Items have a weight and an amount which
 * can range between 0 and their weight.
 *
 * A boolean determines whether or not they
 * cn be picked up by players.
 *
 * @author Henri Boistel de Belloy
 * @version 2018.11.30
 */

public class Item {
    private String name;
    private String pluralName;
    private String description;
    private int currentAmount;
    private int maxAmount;
    private int extraAmount; // used to store amount of items that don't fit on the stack when adding amounts to the object.
    private boolean pickable;

    /**
     * Creates an item object that can be picked up by players or given to NPCs.
     * @param name the name of the object.
     * @param pluralName the plural form of the object's name.
     * @param description the description of the item.
     * @param currentAmount the items amount.
     * @param maxAmount the item's max amount or weight.
     */
    public Item(String name, String pluralName, String description, int currentAmount, int maxAmount){
        this.name = name;
        this.pluralName = pluralName;
        this.description = description;
        this.currentAmount = currentAmount;
        this.maxAmount = maxAmount;
        pickable = true;
    }

    /**
     * Returns the amount of items in the stack followed by the item's name.
     * @return Item amount + item name
     */
    public String toString(){
        return currentAmount + " " + this.getStringName(currentAmount);
    }

    // ------ accessor methods: ------

    /**
     * Returns amount of leftovers when an amount is added to this item.
     * @return amount of leftovers when an amount is added to this item.
     */
    public int getExtraAmount(){
        return extraAmount;
    }

    /**
     * Returns the name of the item in plural or singular based on the amount carried.
     * @param amount Amount of items to get name for (plural/singular).
     * @return Name of the item corrected for plural/singular.
     */
    public String getStringName(int amount){
        if (amount == 1){
            return name;
        }
        else {
            return pluralName;
        }
    }

    /**
     * Returns the item's name in singular form.
     * @return the item's name in singular form.
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the item's name in plural form.
     * @return the item's name in plural form.
     */
    public String getPluralName() {
        return pluralName;
    }

    /**
     * Returns current amount of a specified item.
     * @Return Amount of the item.
     */
    public int getAmount(){
        return currentAmount;
    }

    /**
     * Returns the item's weight.
     * @return the item's weight.
     */
    public int getMaxAmount() {
        return maxAmount;
    }

    /**
     * Returns the item's description.
     * @return the item's description.
     */
    public String getDescription(){
        return description;
    }

    /**
     * Returns whether or not an item is pickable.
     * @return whether or not an item is pickable.
     */
    public boolean isPickable() {
        return pickable;
    }

    // ------ mutator methods: ------

    /**
     * Sets whether or not an item should be pickable.
     * @param pickable Boolean to set.
     */
    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    // ------ item mechanics: ------

    /**
     * Add an amount to this item object.
     * @param amountToAdd the amount to add.
     * @return any message to display on the GUI.
     */
    public String take(int amountToAdd){

        //Checks whether current amount is already reached
        //or will be surpassed and acts accordingly
        if (currentAmount == maxAmount) {
            return ("You can not store any more "+pluralName+" in your inventory.\n");
        } else if ((currentAmount + amountToAdd) > maxAmount){
            extraAmount = amountToAdd + currentAmount - maxAmount;
            int amountAdded = maxAmount-currentAmount;
            currentAmount += amountAdded;
            return (amountAdded + " " + getStringName(amountAdded)+" was added to your inventory.\n"+"You can not store any more "+pluralName+" in your inventory.\n");
        } else {
            currentAmount += amountToAdd;
            extraAmount = 0;
            return (amountToAdd + " " + getStringName(amountToAdd)+" was added to your inventory.\n");
        }
    }

    /**
     * Remove a certain amount from the item's current amount.
     * @param amountToRemove amount to remove.
     */
    public void remove(int amountToRemove){
        currentAmount -= amountToRemove;
    }
}
