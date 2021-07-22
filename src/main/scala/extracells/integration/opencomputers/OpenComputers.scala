package extracells.integration.opencomputers

import appeng.api.AEApi
import extracells.integration.Integration
import extracells.item.ItemOCUpgrade
import li.cil.oc.api.Driver
object OpenComputers {
	
	def init{
		Driver.add(new DriverFluidExportBus)
		Driver.add(new DriverOreDictExportBus)
		Driver.add(new DriverFluidInterface)
		Driver.add(ItemOCUpgrade)
		AEApi.instance.registries.wireless.registerWirelessHandler(WirelessHandlerUpgradeAE)
		OCRecipes.loadRecipes
		ExtraCellsPathProvider
	}

}
