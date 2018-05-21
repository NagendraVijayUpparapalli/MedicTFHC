package com.example.cool.patient;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cool.patient.helper.LocaleHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Language_Optional extends AppCompatActivity {


	@BindView(R.id.toTRButton)
    Button mToTRButton;
	@BindView(R.id.toENButton)
    Button mToENButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_language__optional);


		Button button3 = (Button) findViewById(R.id.toENButton1);
		button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {



				Intent i1 = new Intent(Language_Optional.this,MainActivity.class);
				startActivity(i1);

			}
		});


		ButterKnife.bind(this);

	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.onAttach(base));
	}


@OnClick(R.id.toTRButton)
	public void onChangeToTRClicked() {

		updateViews("te");
	}




	@OnClick(R.id.toENButton)
	public void onChangeToENClicked() {

		updateViews("en");
	}

	private void updateViews(String languageCode) {
		Context context = LocaleHelper.setLocale(this, languageCode);
		Resources resources = context.getResources();
        resources.getText(R.string.app_name);
        resources.getText(R.string.my_diagnostic);
        resources.getText(R.string.My_doctor);

		mToTRButton.setText(resources.getString(R.string.main_activity_to_tr_button));
		mToENButton.setText(resources.getString(R.string.main_activity_to_en_button));
		setTitle(getString(R.string.app_name_Select_language));



	}
}
