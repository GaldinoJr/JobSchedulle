package com.galdino.jobschedulle;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.galdino.jobschedulle.domain.MessageEB;
import com.galdino.jobschedulle.service.JobSchedulerService;

import de.greenrobot.event.EventBus;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import me.tatarka.support.os.PersistableBundle;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Registra o evento para receber dados da thread
        EventBus.getDefault().register(this);
        textView = (TextView) findViewById(R.id.tv_job_scheduler_answer);

    }

    public void onEvent(MessageEB messageEB)
    {
        textView.setText(messageEB.getResult());
    }


    public void onJobExecute(View view)
    {
        ComponentName componentName = new ComponentName(this, JobSchedulerService.class);

        PersistableBundle pb = new PersistableBundle();
        pb.putString("string","Qualquer coisa lalala");

        JobInfo jobInfo = new JobInfo.Builder(1,componentName)
                .setBackoffCriteria(4000,JobInfo.BACKOFF_POLICY_LINEAR)
                .setExtras(pb)
                //.setMinimumLatency(2000)
                //.setOverrideDeadline(2000)
                .setPersisted(true)
                // Caso mJss.jobFinished(jobParameterses[0],true); for false
                .setPeriodic(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                // só é ativado quando o celular estiver carregando?
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .build();

        JobScheduler jobScheduler = JobScheduler.getInstance(this);
        jobScheduler.schedule(jobInfo);
    }

    public void onCancelAll(View view){
        //JobScheduler js = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobScheduler js = JobScheduler.getInstance(this);
        js.cancel(1);
    }
}
