package org.acrobatt.project.weatherdata;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;

public class WeatherDataComputeJob implements Job {

    private static Logger logger = LogManager.getLogger(WeatherDataComputeJob.class);
    WeatherDataService weatherDataService;

    /**
     * TRhe computing job used to retrieve data from the APIs every six hours
     * @param jobExecutionContext the execution context
     * @throws JobExecutionException if the job didn't execute properly
     */
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Job launch");
        try {
            try {
                weatherDataService.computeApisData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
