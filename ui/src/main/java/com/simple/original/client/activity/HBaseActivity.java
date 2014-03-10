package com.simple.original.client.activity;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.simple.original.client.place.HBasePlace;
import com.simple.original.client.view.IHBaseView;

public class HBaseActivity extends AbstractActivity<HBasePlace, IHBaseView> {

	@Inject
	public HBaseActivity(IHBaseView view) {
		super(view);
	}

	@Override
	protected void bindToView() {

		try {
			fetchMetrics();
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void fetchMetrics() throws RequestException {

		String key = URL.encode("1");
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "http://localhost:8080/simple/hbase/metrics/" + key + "/");
		builder.setHeader("Accept", "application/json");

		Request request = builder.sendRequest(null, new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				// GWT.log("response is  " + response.getText());
				JSONValue value = JSONParser.parseLenient(response.getText());
				// GWT.log("value is " + value.toString());
				JSONObject root = value.isObject();

				GWT.log("Object is " + root);
				if (root != null) {
					JSONArray rows = root.get("Row").isArray();

					for (int r = 0; r < rows.size(); r++) {
						JSONObject rowObject = rows.get(r).isObject();
						GWT.log("Row Key => " + b64decode(rowObject.get("key").isString().stringValue()));
						
						JSONArray cells = rowObject.get("Cell").isArray();
						for (int i = 0; i < cells.size(); i++) {
							JSONObject column = cells.get(i).isObject();

							String columnName = column.get("column").isString().stringValue();
							String columnValue = column.get("$").toString();
							GWT.log("Cell name is " + b64decode(columnName));
							GWT.log("Column value is " + columnValue);
							Image image = new Image(columnValue);
							PopupPanel popup = new PopupPanel();
							popup.setWidget(image);
							popup.center();
						}

					}
				}

			}

			@Override
			public void onError(Request request, Throwable exception) {
				GWT.log("error response is  " + exception.getMessage());
			}
		});

		// HBaseRestService service = HBaseRestService.Util.get("localhost",
		// "52280");

		// String key = SafeHtmlUtils.htmlEscape("/tmp/instrument.png");
		// service.query("metrics", key , "rexp", "value", new
		// MethodCallback<String>() {
		//
		// @Override
		// public void onFailure(Method method, Throwable exception) {
		// GWT.log("Failure is " + exception.getMessage());
		//
		// }
		//
		// @Override
		// public void onSuccess(Method method, String response) {
		// GWT.log("Response is " + response);
		// }
		//
		// });
	}

	private static native String b64decode(String a) /*-{
		return window.atob(a);
	}-*/;
}
