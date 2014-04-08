package com.simple.original.client.activity;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.simple.original.client.place.OrchestratorPlace;
import com.simple.original.client.service.rest.Metric;
import com.simple.original.client.service.rest.MetricRaw;
import com.simple.original.client.service.rest.OrchestratorService;
import com.simple.original.client.utils.StringTokenizer;
import com.simple.original.client.view.IOrchestratorView;

public class OrchestratorActivity extends AbstractActivity<OrchestratorPlace, IOrchestratorView> {

	class SelectOperation {
		
		String metricKey;
		
		
	}
	
	class QueryParser  {
		
		
		public void parse(String query) {

			StringTokenizer strtok = new StringTokenizer(query);
			
			SelectOperation operation;
			while(strtok.hasMoreElements()) {
				String token = strtok.nextToken();
				if (token.toLowerCase().equals("select")) {
					operation = new SelectOperation();
					operation.metricKey = strtok.nextToken();
					
				}
				
				
			}
		}
	}
	
	@Inject
	public OrchestratorActivity(IOrchestratorView view) {
		super(view);
	}

	@Override
	protected void bindToView() {

		display.getQueryExecute().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				runQuery(display.getQueryText());
			}
			
		});	
		
		try {
			fetchMetrics();
		} catch (RequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void runQuery(String text) {
		QueryParser qp = new QueryParser();
		qp.parse(text);
		
	}

	private void fetchMetrics() throws RequestException {

		String key = URL.encode("1");
		
		OrchestratorService service = OrchestratorService.Util.get();
		
		service.fetchMetric("1", new MethodCallback<Metric>() {
			
			@Override
			public void onSuccess(Method method, Metric response) {
				
				if (response instanceof MetricRaw) {

					MetricRaw raw = (MetricRaw) response;
					PopupPanel pp = new PopupPanel();
					Image image = new Image();
					image.setUrl("data:image/png;base64," + raw.getValue());
					pp.add(image);
					pp.center();	
				}
				
			}
			
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert("Failure " + exception.getMessage());
			}
		});
	}

}
