package org.acrobatt.project.services.rawdata;

import org.acrobatt.project.services.rawdata.modules.ComparativeDataModule;
import org.acrobatt.project.services.rawdata.modules.ModularDataModule;
import org.acrobatt.project.utils.enums.DataProcess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A module factory used to provision the right module for the right process
 */
public class DataModuleFactory {

    private static Logger logger = LogManager.getLogger(DataModuleFactory.class);

    private DataModuleFactory() {}

    /**
     * Returns the right data module based on a DataProcess
     * @param dp the process
     * @return an instantiation of the appropriate module
     */
    public static DataModule getDataModule(DataProcess dp) {
        DataModule module = null;
        switch(dp) {
            case MODULAR_ENTRY:           module = ModularDataModule.getInstance();         break;
            case SINGLE_ENTRY:            module = ComparativeDataModule.getInstance();     break;
            case COMPARATIVE:             module = ComparativeDataModule.getInstance();     break;
            default: logger.fatal("Failed to determine DataModule");
        }
        return module;
    }
}
