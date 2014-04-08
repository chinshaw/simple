package com.simple.original.client.place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.impl.BaseProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.EntityProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.ValueProxyCategory;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.InputCollectionProxy;

/**
 * 
 * @author chinshaw
 * 
 */
public class DashboardPlace extends ApplicationPlace {

	// Declare the factory type
	@AutoBeanFactory.Category(value = { EntityProxyCategory.class, ValueProxyCategory.class, BaseProxyCategory.class })
	@AutoBeanFactory.NoWrap(EntityProxyId.class)
	interface InputBeanFactory extends AutoBeanFactory {
		AutoBean<InputCollectionProxy> inputs();

		AutoBean<AnalyticsOperationInputProxy> input();
	}

	private Long analyticsTaskId;

	private List<AnalyticsOperationInputProxy> arguments;

	public DashboardPlace(Long analyticsTaskId, List<AnalyticsOperationInputProxy> arguments) {
		this.analyticsTaskId = analyticsTaskId;
		this.arguments = arguments;
	}

	public Long getAnalyticsTaskId() {
		return analyticsTaskId;
	}

	public List<AnalyticsOperationInputProxy> getArguments() {
		return arguments;
	}


	public static class Tokenizer implements PlaceTokenizer<DashboardPlace> {
		InputBeanFactory factory = GWT.create(InputBeanFactory.class);

		@Override
		public String getToken(DashboardPlace place) {
			// String payload = serializeInputs(place.getArguments());
			// decodeInputs(payload);

			AutoBean<InputCollectionProxy> inputCollection = factory.create(InputCollectionProxy.class);
			InputCollectionProxy inputs = inputCollection.as();
			inputs.setInputs(place.getArguments());

			AutoBean<InputCollectionProxy> bean = AutoBeanUtils.getAutoBean(inputs);

			String arguments = AutoBeanCodex.encode(bean).getPayload();

			return "analyticsTaskId=" + place.getAnalyticsTaskId().toString() + "&arguments=" + arguments;
		}

		@Override
		public DashboardPlace getPlace(String token) {
			
			HashMap<String, String> parameters = PlaceUtils.getParameterPairs(token);
			Long analyticsTaskId = Long.parseLong(parameters.get("analyticsTaskId"));
			
			ArrayList<AnalyticsOperationInputProxy> detatchedInputs = null;

			AutoBean<InputCollectionProxy> bean = AutoBeanCodex.decode(factory, InputCollectionProxy.class, parameters.get("arguments"));

			InputCollectionProxy inputCollection = bean.as();

			List<AnalyticsOperationInputProxy> inputs = inputCollection.getInputs();

			if (inputs != null) {
				detatchedInputs = new ArrayList<AnalyticsOperationInputProxy>();
				for (AnalyticsOperationInputProxy inputBean : inputs) {
					AutoBean<AnalyticsOperationInputProxy> opBean = AutoBeanUtils.getAutoBean(inputBean);
					// opBean.setFrozen(false);

					AnalyticsOperationInputProxy input = opBean.as();
					// WeakMapping.set(input, AutoBean.class.getName(), null);
					detatchedInputs.add(input);
				}
			}

			return new DashboardPlace(analyticsTaskId, detatchedInputs);
		}
	}

	@Override
	public String getApplicationTitle() {
		return "Dashboard";
	}

	/*
	public static String serializeInputs(List<AnalyticsOperationInputProxy> inputs) {
		String payload = "";
		if (inputs != null) {
			DaoRequestFactory dao = ClientFactory.INSTANCE.daoRequestFactory();
			DefaultProxyStore store = new DefaultProxyStore();

			ProxySerializer ser = dao.getSerializer(store);
			for (AnalyticsOperationInputProxy input : inputs) {
				ser.serialize(input);
			}
			payload = store.encode();
		}

		return payload;
	}
	*/

	/*
	public static List<AnalyticsOperationInputProxy> decodeInputs(String payload) {
		List<AnalyticsOperationInputProxy> inputs = new ArrayList<AnalyticsOperationInputProxy>();
		if (payload != null) {

			DaoRequestFactory dao = ClientFactory.INSTANCE.daoRequestFactory();
			DefaultProxyStore store = new DefaultProxyStore(payload);

			OperationMessage messageBean = AutoBeanCodex.decode(MessageFactoryHolder.FACTORY, OperationMessage.class, payload).as();

			Map<String, Splittable> map = messageBean.getPropertyMap();
			ProxySerializer ser = dao.getSerializer(store);

			for (String key : map.keySet()) {
				AnalyticsOperationInputProxy proxy = ser.deserialize(AnalyticsOperationInputProxy.class, key);
				inputs.add(proxy);
			}
		}

		return inputs;
	}
	*/
}