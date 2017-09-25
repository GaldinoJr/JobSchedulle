package com.galdino.jobschedulle;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.galdino.jobschedulle.domain.MessageEB;
import com.galdino.jobschedulle.service.JobSchedulerService;

import de.greenrobot.event.EventBus;

//https://developer.android.com/reference/android/app/job/JobInfo.Builder.html
public class MainActivity extends AppCompatActivity
{
    private static final int JOB_ID = 1;
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onJobExecute(View view)
    {
        Log.i("LOG_JOB","onJobExecute");
        ComponentName componentName = new ComponentName(this, JobSchedulerService.class);

        PersistableBundle pb = new PersistableBundle();
        pb.putString("string","Qualquer coisa lalala");

        JobInfo.Builder jobInfo =
                new JobInfo.Builder(JOB_ID, componentName)
                        // Regra de execução, tempo de looping aparentemente
                        .setBackoffCriteria(4000, JobInfo.BACKOFF_POLICY_LINEAR)

                        .setPersisted(true) //Defina se deve ou não persistir este trabalho durante as reinicializações do dispositivo.

//                        .setPeriodic(4000)

                .setMinimumLatency(2000) // Latencia mínima de agendamento
                .setOverrideDeadline(15000) // Latencia máxima de agendamento

                // Precisa de wifi para funcionar
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)

                // Precisa de qualquer conexão para funcionar
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

                .setExtras(pb)
                // só é ativado quando o celular estiver carregando?
                .setRequiresCharging(false)

                .setRequiresDeviceIdle(false);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCancelAll(View view){
        JobScheduler js = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        JobScheduler js = JobScheduler.getInstance(this);
        js.cancel(1);
    }
}
