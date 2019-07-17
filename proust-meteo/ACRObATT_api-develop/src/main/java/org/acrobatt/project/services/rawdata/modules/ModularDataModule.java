package org.acrobatt.project.services.rawdata.modules;

import org.acrobatt.project.services.rawdata.DataModule;
import org.acrobatt.project.utils.struct.TableKey;
import org.acrobatt.project.utils.struct.TableValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.validation.constraints.NotNull;
import java.util.*;

public class ModularDataModule extends DataModule {

    private static Logger logger = LogManager.getLogger(ModularDataModule.class);
    private static ModularDataModule instance = null;

    /**
     * Generates an instance of this module
     * @return an instance of the module
     */
    public static ModularDataModule getInstance() {
        if(instance == null) instance = new ModularDataModule();
        return instance;
    }

    /**
     * An overridden function of the parent class. This implementation creates a double-entry JSON "table"
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

            //get unique headers from the rawdata (to put bounds for the next loops)
            List<Object> rowHeaders = getUniqueHeaders(data, 1);
            List<Object> columnHeaders = getUniqueHeaders(data, 0);

            //map rawdata to a custom <TableKey<String, String>, TableValue<Object, Object> hashmap
            HashMap<TableKey<String, String>, TableValue<Object, Object>> globalMap =
                    (HashMap<TableKey<String, String>,  TableValue<Object, Object>>) mapData(data, rowHeaders, columnHeaders);

            //loop through the map to fill the JSON structure
            for (Object rowHeader : rowHeaders) {
                JSONObject row = new JSONObject();

                //loop through the entries to find the corresponding ones
                List<Object> values = new ArrayList<>();
                for (Map.Entry<TableKey<String, String>,  TableValue<Object, Object>> entry : globalMap.entrySet()) {
                    String tkRowHeader = entry.getKey().getRowKey();
                    if (tkRowHeader.equals(rowHeader)) {
                        values.add(entry.getValue().getValueValue());
                        if(!row.has("unit")) row.put("unit", entry.getValue().getUnitValue());
                    }
                }

                row.put("values", values);
                row.put("label", rowHeader);
                tabularDataContainer.put(row);
            }

            tabularDataRoot.put("series_type", getTableSeriesType(data.get(0)));
            tabularDataRoot.put("column_headers", columnHeaders);
            tabularDataRoot.put("data", tabularDataContainer);

            return tabularDataRoot;
        } catch(Exception e) {
            logger.fatal("Failure while converting tabular data : "+e.getMessage());
        }
        return null;
    }

    /**
     * An overridden function of the parent class. This implementation creates a map of TableKey->TableValue
     * @see DataModule
     * @param data the list of JPA Tuples
     * @param rowHeaders the list of unique row headers pulled from the list of Tuples
     * @param colHeaders the list of unique column headers pulled from the list of Tuples
     * @return the mapped data
     */
    @Override
    public Map<TableKey<String, String>, TableValue<Object, Object>> mapData(List<Tuple> data, List<Object> rowHeaders, List<Object> colHeaders) {
        //initialize the hashmap
        HashMap<TableKey<String, String>, TableValue<Object, Object>> dataMap = new HashMap<>();
        for(Object o : rowHeaders) {
            for(Object p : colHeaders) {
                dataMap.put(
                        new TableKey<>(o.toString(), p.toString()),
                        new TableValue<>(null, null));
            }
        }

        //fill the hashmap
        for(Tuple d : data) {
            Object[] dArray = d.toArray();
            dataMap.replace(
                    new TableKey<>(dArray[1].toString(), dArray[0].toString()),
                    new TableValue<>(dArray[dArray.length-1], dArray[2]));
        }

        return dataMap;
    }

    /**
     * An overridden function of the parent class. This implementation returns the unique headers based on an index
     * @param data the list of tuples
     * @param index the column index to parse
     * @return the list of unique headers
     */
    @Override
    public ArrayList<Object> getUniqueHeaders(@NotNull List<Tuple> data, @NotNull Integer index) {
        if(data.isEmpty()) throw new NullPointerException("cannot get headers for empty data");
        if(index < 0 || index > data.get(0).getElements().size()) throw new IllegalArgumentException("illegal tuple index");

        ArrayList<Object> tupleColumnValues = new ArrayList<>();
        for(Tuple d : data) {
            Object tupleValueToExamine = d.toArray()[index];
            if(!tupleColumnValues.contains(tupleValueToExamine)) tupleColumnValues.add(tupleValueToExamine);
        }
        return tupleColumnValues;
    }

    /**
     * An overridden function of the parent class. This implementation returns the second and first aliases, in order
     * @param refData one JPA Tuple element to get the aliases of its TupleElements
     * @return the series of the JSON "table"
     */
    public List<String> getTableSeriesType(@NotNull Tuple refData) {
        List<TupleElement<?>> elems = refData.getElements();
        return Arrays.asList(elems.get(1).getAlias(),elems.get(0).getAlias());
    }
}
