package org.acrobatt.project.services;

import org.acrobatt.project.dao.mysql.analysis.ComparativeDataDAO;
import org.acrobatt.project.utils.GlobalVariableRegistry;
import org.acrobatt.project.utils.db.HibernateUtils;
import org.acrobatt.project.utils.struct.TableKey;
import org.acrobatt.project.utils.struct.TableValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.Tuple;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsTest {

    WidgetDataService widgetDataService = WidgetDataService.getInstance();
    ComparativeDataDAO comparativeDataDAO = ComparativeDataDAO.getInstance();

    public StatisticsTest() throws IOException {
    }

    @BeforeAll
    public static void initHibernate(){
        HibernateUtils.loadConfiguration(new File(GlobalVariableRegistry.HIBERNATE_CFG_TEST_LOCAL));
        HibernateUtils.getSessionFactory();
    }
    @Test
    public void scoreTest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date before = dateFormat.parse("15-02-2018");
        Date after = dateFormat.parse("15-03-2018");
        System.out.println(before);
        System.out.println(after);
        HashMap<String,Object> criteria = new HashMap<>();
        //criteria.put("city","Strasbourg");
        //criteria.put("property","temperature");
        criteria.put("datetime",before);
        criteria.put("datetime_to",after);
        //criteria.put("delay",24);
        List<Tuple> tuples = comparativeDataDAO.getScoreDataForOptimization(criteria);
        System.out.println(tuples.size());
        for(Tuple tuple : tuples){
            System.out.println(tuple.get(GlobalVariableRegistry.API_ALIAS) + " : " + tuple.get(GlobalVariableRegistry.SP_SCORING_ALIAS));
        }
    }

    @Test
    public void indexTest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date before = dateFormat.parse("15-02-2018");
        Date after = dateFormat.parse("15-03-2018");
        System.out.println(before);
        System.out.println(after);
        HashMap<String,Object> criteria = new HashMap<>();
        criteria.put("city","Strasbourg");
        criteria.put("property","temperature");
        criteria.put("datetime",before);
        criteria.put("datetime_to",after);
        criteria.put("delay",24);
        List<Tuple> tuples = comparativeDataDAO.getIndexDataForOptimization(criteria);
        System.out.println(tuples.size());
        for(Tuple tuple : tuples){
            System.out.println(tuple.get(GlobalVariableRegistry.API_ALIAS) + " : " + tuple.get(GlobalVariableRegistry.SP_INDEX_ALIAS));
        }
        System.out.println(tuples.stream().mapToDouble((tuple) -> (Double) tuple.get(GlobalVariableRegistry.SP_INDEX_ALIAS)).sum());
    }

    @Test
    public void optimalPropertyTest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date_req = dateFormat.parse("15-02-2018 01:32:36");
        HashMap<String,Object> criteria = new HashMap<>();
        criteria.put("city","Strasbourg");
        //criteria.put("property","temperature");
        criteria.put("datetime",date_req);
        //criteria.put("delay",24);
        HashMap<TableKey<String,Integer>,TableValue<Double,String>> table = widgetDataService.getOptimalData(criteria);
        for(Map.Entry<TableKey<String,Integer>,TableValue<Double,String>> row : table.entrySet()){
            System.out.println(row.getKey().getRowKey() + " - " + row.getKey().getColumnKey() + " - " + row.getValue().getValueValue() + " - " + row.getValue().getUnitValue());
        }
        System.out.println(date_req);

    }
}
