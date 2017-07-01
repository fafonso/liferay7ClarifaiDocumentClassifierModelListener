package com.liferay.demo;

import java.io.IOException;
import java.util.List;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;


public class ClarifaiIntegrator {


	public static List<ClarifaiOutput<Concept>> tagDocument(byte[] bytes) throws IOException {
		ClarifaiClient client = getClient(); 
		if(client==null)return null;
		List<ClarifaiOutput<Concept>> results = client.getDefaultModels().generalModel()
		.predict()
		.withInputs( ClarifaiInput.forImage( ClarifaiImage.of(bytes)) ).executeSync().get();
		//if we wanted to be asynchronous, we'd execute "execute" instead (better performant)
		//and we'd get a Future
		return results;
	}

	public static ClarifaiClient getClient() {
		if( (Constants.appID==null) ||  (Constants.appSecret==null) ){
			_log.error("PLEASE GO TO CLARIFAI's WEBSITE AND CREATE A FREE TEST ACCOUNT");
			_log.error("Then set your Constants.appID and your Constants.appSecret");
			return null;
		}
		return new ClarifaiBuilder(Constants.appID, Constants.appSecret)
    		.client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
    		.buildSync();// or use .build() to get a Future<ClarifaiClient>
	}

	private static final Log _log = LogFactoryUtil.getLog(ClarifaiIntegrator.class);

}
