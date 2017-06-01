package com.android.proyectoalimentar.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.proyectoalimentar.Configuration;
import com.android.proyectoalimentar.di.component.DaggerAppComponent;
import com.android.proyectoalimentar.di.module.AppModule;
import com.android.proyectoalimentar.di.module.NetworkModule;
import com.android.proyectoalimentar.model.Qualification;
import com.android.proyectoalimentar.repository.DonationsRepository;
import com.android.proyectoalimentar.repository.RepoCallback;

import com.android.proyectoalimentar.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QualifyVolunteerView extends FrameLayout{

    Integer donationId;
    String userName;

    @BindView(R.id.question_text)
    TextView questionTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Inject
    DonationsRepository donationRepository;

    OnResponseCallback onResponseCallback;

    public QualifyVolunteerView(Context context) {
        super(context);
    }

    public QualifyVolunteerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QualifyVolunteerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.qualify_volunteer, this);
        ButterKnife.bind(this, view);
        DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .networkModule(new NetworkModule())
                .build()
                .inject(this);

        setOnClickListener(v -> setVisibility(GONE));
    }

    private void fillQuestionText(){
        String question = getResources().getString(R.string.qualify_question);
        question = String.format(question,userName);
        questionTextView.setText(question);
    }

    @OnClick(R.id.good)
    public void QualifyAsGood(){
        Qualification qualification = new Qualification(Configuration.MAX_QUALIFICATION);
        if(donationId != null) {
            progressBar.setVisibility(VISIBLE);
            donationRepository.qualifyDonation(donationId,qualification, new RepoCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean answered) {
                    if (answered) {
                        QualifyVolunteerView.this.setVisibility(GONE);
                        if (onResponseCallback != null) {
                            onResponseCallback.onResponse(true);
                        }
                    } else {
                        onError(null);
                    }
                }

                @Override
                public void onError(String error) {
                    QualifyVolunteerView.this.setVisibility(GONE);
                    Toast.makeText(getContext(), R.string.qualify_error, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @OnClick(R.id.bad)
    public void QualifyAsBad(){
        Qualification qualification = new Qualification(Configuration.MIN_QUALIFICATION);
        if(donationId != null) {
            progressBar.setVisibility(VISIBLE);
            donationRepository.qualifyDonation(donationId, qualification, new RepoCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean answered) {
                    progressBar.setVisibility(GONE);
                    if (answered) {
                        QualifyVolunteerView.this.setVisibility(GONE);
                        if (onResponseCallback != null) {
                            onResponseCallback.onResponse(true);
                        }
                    } else {
                        onError(null);
                    }
                }

                @Override
                public void onError(String error) {
                    progressBar.setVisibility(GONE);
                    QualifyVolunteerView.this.setVisibility(GONE);
                    Toast.makeText(getContext(), R.string.qualify_error, Toast.LENGTH_SHORT);
                }
            });
        }
        this.setVisibility(FrameLayout.GONE);
    }

    public void setInformation(String donationId, String userName) {
        try {
            this.donationId = Integer.valueOf(donationId);
            this.userName = userName;
        }catch (NumberFormatException e){
            throw new IllegalArgumentException();
        }
        fillQuestionText();
    }

    public void setOnResponseCallback(OnResponseCallback onResponseCallback) {
        this.onResponseCallback = onResponseCallback;
    }

    public interface OnResponseCallback{
        void onResponse(boolean confirmActivation);
    }
}
