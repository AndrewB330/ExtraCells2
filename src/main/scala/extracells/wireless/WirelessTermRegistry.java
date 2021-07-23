package extracells.wireless;

import extracells.api.IWirelessOldFluidTermHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WirelessTermRegistry {

    public static IWirelessOldFluidTermHandler getWirelessTermHandler(ItemStack is) {
        if (is == null)
            return null;
        for (IWirelessOldFluidTermHandler handler : handlers) {
            if (handler.canHandle(is))
                return handler;
        }
        return null;
    }

    public static boolean isWirelessItem(ItemStack is) {
        if (is == null)
            return false;
        for (IWirelessOldFluidTermHandler handler : handlers) {
            if (handler.canHandle(is))
                return true;
        }
        return false;
    }

    public static void registerWirelessTermHandler(
            IWirelessOldFluidTermHandler handler) {
        if (!handlers.contains(handler))
            handlers.add(handler);
    }

    static List<IWirelessOldFluidTermHandler> handlers = new ArrayList<IWirelessOldFluidTermHandler>();

}
