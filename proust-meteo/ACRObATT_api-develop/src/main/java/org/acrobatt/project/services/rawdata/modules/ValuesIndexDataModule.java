package org.acrobatt.project.services.rawdata.modules;

import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValuesIndexDataModule {

    private static Logger logger = LogManager.getLogger(Isolated_WidgetDataModule.class);

    public static ValuesIndexDataModule instance = null;
    public static ValuesIndexDataModule getInstance() {
        if(instance == null) instance = new ValuesIndexDataModule();
        return instance;
    }

    //specific functions (using HashMap)
    public JSONObject convertData(List<Tuple> values,List<Tuple> indexes) {
        try {
            //create root and sub-containers
            JSONObject tabularDataRoot = new JSONObject();
            JSONArray tabularDataContainer = new JSONArray();

            //get unique headers from the rawdata (to put bounds for the next loops)
            List<String> columnHeaders = getColumnHeaders(values, indexes);
            //loop through the map to fill the JSON structure
            for (Tuple value : values) {
                JSONObject row = new JSONObject();
                String rowHeader = value.get(GlobalVariableRegistry.API_ALIAS).toString();

                //loop through the entries to find the corresponding ones
                List<Float> rowValues = new ArrayList<>();
                Float dataValue = ((Number)value.get(GlobalVariableRegistry.VALUE_ALIAS)).floatValue();
                Float indexValue = ((Number) indexes.stream()
                        .filter((index) ->
                                index.get(GlobalVariableRegistry.API_ALIAS).equals(value.get(GlobalVariableRegistry.API_ALIAS)))
                        .mapToDouble((index) ->
                                ((Number)index.get(GlobalVariableRegistry.SP_INDEX_ALIAS)).doubleValue())
                        .sum()).floatValue();
                rowValues.add(dataValue);
                rowValues.add(indexValue);
                row.put("data", rowValues);
                row.put("label", rowHeader);
                tabularDataContainer.put(row);
            }
            tabularDataRoot.put("datas",tabularDataContainer);
            tabularDataRoot.put("series_type", getTableSeriesType(values,indexes));
            tabularDataRoot.put("column_headers", columnHeaders);

            return tabularDataRoot;
        } catch(Exception e) {
            logger.fatal("Failure while converting values and indexes data : "+e.getMessage(),e);
        }
        return null;
    }

    private ArrayList<String> getColumnHeaders(List<Tuple> values, List<Tuple> indexes) {
        if(values.size() == 0) throw new NoResultException("cannot get headers for empty data");
        Tuple tuple = values.get(0);
        ArrayList<String> headers = new ArrayList<>();
        headers.add(GlobalVariableRegistry.API_ALIAS);
        headers.add(tuple.get(GlobalVariableRegistry.PROPERTY_ALIAS).toString()+ "("+tuple.get(GlobalVariableRegistry.UNIT_ALIAS)+")");
        headers.add(GlobalVariableRegistry.SP_INDEX_ALIAS);
        return headers;
    }

    private List<String> getTableSeriesType(List<Tuple> values,List<Tuple> indexes) {
        if(values.size() == 0) throw new NoResultException("cannot get headers for empty data");
        Tuple tuple = values.get(0);

        return Arrays.asList(
                GlobalVariableRegistry.API_ALIAS,
                GlobalVariableRegistry.VALUE_ALIAS
        );
    }
}
