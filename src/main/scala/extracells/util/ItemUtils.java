package extracells.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemUtils {

    // todo: rename areStacksMergable
    public static boolean areItemEqualsIgnoreStackSize(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null && stack2 == null)
            return true;
        else if (stack1 == null || stack2 == null)
            return false;
        ItemStack s1 = stack1.copy();
        ItemStack s2 = stack2.copy();
        s1.stackSize = 1;
        s2.stackSize = 1;
        return ItemStack.areItemStacksEqual(s1, s2);
    }

    public static ItemStack copyAmount(ItemStack stack, int amount) {
        ItemStack copy = stack.copy();
        copy.stackSize = amount;
        return copy;
    }

    public static boolean isStackInvalid(Object aStack) {
        return !(aStack instanceof ItemStack) ||
                ((ItemStack) aStack).getItem() == null ||
                ((ItemStack) aStack).stackSize < 0;
    }

    public static void addItemToPlayerInventory(EntityPlayer aPlayer, ItemStack aStack) {
        if (isStackInvalid(aStack)) return;
        if (!aPlayer.inventory.addItemStackToInventory(aStack) && !aPlayer.worldObj.isRemote) {
            EntityItem dropItem = aPlayer.entityDropItem(aStack, 0);
            dropItem.delayBeforeCanPickup = 0;
        }
    }

}
