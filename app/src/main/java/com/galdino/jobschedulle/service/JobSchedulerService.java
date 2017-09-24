package com.galdino.jobschedulle.service;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.galdino.jobschedulle.domain.MessageEB;
import com.galdino.jobschedulle.network.HttpConnection;

import de.greenrobot.event.EventBus;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by galdino on 24/09/17.
 */
public class JobSchedulerService extends JobService
{
    private static String LOG = "LOG_JOB";
    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        Log.i(LOG,"Start Service");
        new MyAsyncTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters)
    {
        Log.i(LOG,"Stop Service");
        return true;
    }

    private static class MyAsyncTask extends AsyncTask<JobParameters,Void, String>
    {
        private JobSchedulerService mJss;
        public MyAsyncTask(JobSchedulerService jss)
        {
            mJss = jss;
        }

        @Override
        protected String doInBackground(JobParameters... jobParameterses)
        {
            Log.i(LOG,"doInBackground");

            SystemClock.sleep(5000);

            String resposta =
                    HttpConnection.getSetDataWeb("http://www.villopim.com.br/android/ExampleJobScheduler/get-random.php",
                    "method");

            mJss.jobFinished(jobParameterses[0],true);
            return resposta;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(LOG,"onPostExecute");

            EventBus.getDefault().post(new MessageEB(s));
        }
    }
}
