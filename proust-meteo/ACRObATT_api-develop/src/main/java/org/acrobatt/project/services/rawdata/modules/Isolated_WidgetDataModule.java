package org.acrobatt.project.services.rawdata.modules;

import org.acrobatt.project.dao.mysql.DelayDAO;
import org.acrobatt.project.model.mysql.Delay;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.struct.TableKey;
import org.acrobatt.project.utils.struct.TableValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SPECIAL CASE: we create new methods to circumvent standard processing while keeping the data flow intact
 */
public class Isolated_WidgetDataModule {

    private static Logger logger = LogManager.getLogger(Isolated_WidgetDataModule.class);
    private static DelayDAO ddao = DelayDAO.getInstance();

    private static Isolated_WidgetDataModule instance = null;

    /**
     * Generates an instance of this module
     * @return an instance of the module
     */
    public static Isolated_WidgetDataModule getInstance() {
        if(instance == null) instance = new Isolated_WidgetDataModule();
        return instance;
    }

    /**
     * A custom implementation (not overridden) of convertData(...).
     * This one is specifically made for the widget and uses a custom HashMap
     * @param globalMap the data map
     * @param criteria the list of criteria
     * @return a converted JSON object with all the supplied data
     */
    public JSONObject convertData(HashMap<TableKey<String,Integer>, TableValue<Double, String>> globalMap, HashMap<String, Object> criteria) {
        try {
            //create root and sub-containers
            JSONObject tabularDataRoot = new JSONObject();
            JSONArray tabularDataContainer = new JSONArray();

            //get unique headers from the rawdata (to put bounds for the next loops)
            List<Object> rowHeaders = getUniqueHeaders(globalMap, true);
            List<Object> columnHeaders = getUniqueHeaders(globalMap, false);
            //loop through the map to fill the JSON structure
            List<TableKey<String,Integer>> keysSorteds = globalMap.entrySet().stream()
                    .map(Map.Entry::getKey)
                    .sorted(Comparator.comparingInt(TableKey::getColumnKey))
                    .collect(Collectors.toList());
            for (Object rowHeader : rowHeaders) {
                JSONObject row = new JSONObject();

                //loop through the entries to find the corresponding ones
                List<Object> values = new ArrayList<>();
                for(TableKey<String,Integer> key : keysSorteds){
                    String tkRowHeader = key.getRowKey();
                    TableValue<Double,String> value = globalMap.get(key);
                    if (tkRowHeader.equals(rowHeader)) {
                        values.add(value.getValueValue());
                        if(!row.has("unit")) row.put("unit", value.getUnitValue());
                    }
                }

                row.put("rawdata", values);
                row.put("label", rowHeader);
                tabularDataContainer.put(row);
            }

            tabularDataRoot.put("series_type", getTableSeriesType());
            tabularDataRoot.put("column_headers", columnHeaders);
            tabularDataRoot.put("dateRequest", criteria.get("datetime"));
            tabularDataRoot.put("delay", ddao.getAll().stream().map(Delay::getValue).collect(Collectors.toList()));
            tabularDataRoot.put("city", criteria.get("city"));
            tabularDataRoot.put("properties", tabularDataContainer);

            return tabularDataRoot;
        } catch(Exception e) {
            logger.fatal("Failure while converting widget data : "+e.getMessage());
        }
        return null;
    }

    /**
     *  A custom implementation (not overridden) of getUniqueHeaders(...).
     *  This one is specifically made for the widget.
     * @param data data in the form of a HashMap
     * @param isForRows specifies if the headers to find are for rows or columns.
     *                  This is due to the fact that we use TableKeys and TableValues as keys and values, respectively.
     * @return a list of unique Objects
     */
    private ArrayList<Object> getUniqueHeaders(@NotNull HashMap<TableKey<String,Integer>, TableValue<Double, String>> data, @NotNull boolean isForRows) {
        if(data.size() == 0) throw new NullPointerException("cannot get headers for empty data");
        List<TableKey<String,Integer>> keysSorteds = data.entrySet().stream()
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(TableKey::getColumnKey))
                .collect(Collectors.toList());
        ArrayList<Object> mapColumnValues = new ArrayList<>();

        for(TableKey<String,Integer> key : keysSorteds){
            Object toExamine = (isForRows ? key.getRowKey() : key.getColumnKey());
            if(!mapColumnValues.contains(toExamine)) mapColumnValues.add(toExamine);
        }
        return mapColumnValues;
    }

    /**
     * A custom implementation (not overridden) of getTableSeriesType(...).
     * It simply returns a fixed list, since the module is for the widget only
     * @return a fixed list : ["Date/Heure", "Prévisions optimisées"]
     */
    private List<String> getTableSeriesType() {
        return Arrays.asList(
                GlobalVariableRegistry.DATETIME_ALIAS,
                GlobalVariableRegistry.SP_WIDGET_FORECASTS_ALIAS
        );
    }
}
