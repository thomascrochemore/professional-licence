package org.acrobatt.project.services.rawdata;

import org.json.JSONObject;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An abstract module used as the parent of -most of- the mapping modules
 */
public abstract class DataModule {

    /**
     * An abstract function that should convert the supplied list of JPA Tuple objects to a JSONObject
     * @param data the list of tuples
     * @return the mapped data in JSON
     */
    public abstract JSONObject convertData(List<Tuple> data);

    /**
     * An abstract function that transforms a list of tuples, a list of column headers and a list of row headers into an iterable map.
     * This function should be called inside convertData(...) before mapping data to JSON
     * @param data the list of JPA Tuples
     * @param rowHeaders the list of unique row headers pulled from the list of Tuples
     * @param colHeaders the list of unique column headers pulled from the list of Tuples
     * @return an iterable implementation of a Map, using any key->value scheme
     */
    public abstract Map mapData(List<Tuple> data, List<Object> rowHeaders, List<Object> colHeaders);

    /**
     * Returns a list of two Strings indicating the nature of the mapped JSON data
     * @param refData one JPA Tuple element to get the aliases of its TupleElements
     * @return a list of two elements (format: ["ROW", "COLUMN"])
     */
    public abstract List<String> getTableSeriesType(Tuple refData);

    /**
     * An overrideable function retrieving the unique headers of a list of JPA Tuples, based on the TupleElement index to iterate on
     * @param data the list of tuples
     * @param index the column index to parse
     * @return a list of Objects indicating the headers
     */
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
     * Returns the column aliases of a JPA Tuple
     * @param refData a JPA Tuple
     * @return the list of aliases
     */
    public List<String> getAliases(@NotNull Tuple refData) {
        List<TupleElement<?>> elems = refData.getElements();
        List<String> aliases = new ArrayList<>();

        for(TupleElement<?> e : elems) {
            aliases.add(e.getAlias());
        }

        return aliases;
    }
}