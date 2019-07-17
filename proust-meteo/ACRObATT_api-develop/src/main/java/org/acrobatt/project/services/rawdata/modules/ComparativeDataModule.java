package org.acrobatt.project.services.rawdata.modules;

import org.acrobatt.project.services.rawdata.DataModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.validation.constraints.NotNull;
import java.util.*;

public class ComparativeDataModule extends DataModule {

    private static Logger logger = LogManager.getLogger(ComparativeDataModule.class);

    private static ComparativeDataModule instance = null;

    /**
     * Generates an instance of this module
     * @return an instance of the module
     */
    public static ComparativeDataModule getInstance() {
        if(instance == null) instance = new ComparativeDataModule();
        return instance;
    }

    /**
     * An overridden function of the parent class. This implementation creates a singular-entry JSON "table"
     * @see DataModule
     * @param data the list of tuples
     * @return the converted data in JSON
     * @throws NoResultException if nothing can be found inside the database
     */
    @Override
    public JSONObject convertData(List<Tuple> data) throws NoResultException {
        if(data == null || data.isEmpty()) throw new NoResultException("Unable to convert empty data");
        for(Tuple t : data) { logger.debug("array = "+Arrays.toString(t.toArray())); }

        try {
            //create root and sub-containers
            JSONObject tabularDataRoot = new JSONObject();
            JSONArray tabularDataContainer = new JSONArray();

            //get unique headers from the data (to put bounds for the next loops)
            List<Object> rowHeaders = getUniqueHeaders(data, 0);
            List<String> colHeaders = super.getAliases(data.get(0));
            logger.debug("rowHeaders = "+rowHeaders);
            logger.debug("colHeaders = "+colHeaders);

            //map data to a custom <String, Object[]> hashmap
            HashMap<String, Object[]> globalMap =
                    (HashMap<String, Object[]>) mapData(data, rowHeaders, new ArrayList<>(colHeaders.subList(1, colHeaders.size())));

            //loop through the map to fill the JSON structure
            for (Object rowHeader : rowHeaders) {
                JSONObject row = new JSONObject();

                //loop through the entries to find the corresponding ones
                List<Object> values = new ArrayList<>();
                for (Map.Entry<String, Object[]> entry : globalMap.entrySet()) {
                    String tkRowHeader = entry.getKey();
                    if (tkRowHeader.equals(rowHeader.toString())) {
                        values.addAll(Arrays.asList(entry.getValue()));
                    }
                }

                row.put("values", values);
                row.put("label", rowHeader);
                tabularDataContainer.put(row);
            }

            tabularDataRoot.put("series_type", getTableSeriesType(data.get(0)));
            tabularDataRoot.put("column_headers", colHeaders.subList(1, colHeaders.size()));
            tabularDataRoot.put("data", tabularDataContainer);

            return tabularDataRoot;
        } catch(Exception e) {
            logger.fatal("Failure while converting tabular data : ("+e.getClass().getName()+") ", e);
        }
        return null;
    }

    /**
     * An overridden function of the parent class. This implementation creates a map of String->Object[]
     * @see DataModule
     * @param data the list of JPA Tuples
     * @param rowHeaders the list of unique row headers pulled from the list of Tuples
     * @param colHeaders the list of unique column headers pulled from the list of Tuples
     * @return the mapped data
     */
    @Override
    public Map<String, Object[]> mapData(List<Tuple> data, List<Object> rowHeaders, List<Object> colHeaders) {
        //initialize the hashmap
        HashMap<String, Object[]> dataMap = new HashMap<>();
        for(Object o : rowHeaders) {
            for(Object p : colHeaders) {
                dataMap.put(
                        o.toString(),
                        new Object[data.get(0).getElements().size() - 1]);
            }
        }

        //fill the hashmap
        for(Tuple d : data) {
            Object[] dArray = d.toArray();

            //fill value array
            Object[] values = new Object[d.getElements().size() - 1];
            System.arraycopy(dArray, 1, values, 0, values.length);

            //replace values
            dataMap.replace(dArray[0].toString(), values);
        }

        return dataMap;
    }

    /**
     * An overridden function of the parent class. This implementation returns the first and second aliases
     * @param refData one JPA Tuple element to get the aliases of its TupleElements
     * @return the series of the JSON "table"
     */
    @Override
    public List<String> getTableSeriesType(@NotNull Tuple refData) {
        List<TupleElement<?>> elems = refData.getElements();
        return Arrays.asList(elems.get(0).getAlias(),elems.get(elems.size()-1).getAlias());
    }
}
