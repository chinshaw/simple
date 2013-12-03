package com.simple.original.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.client.proxy.AnalyticsOperationInputProxy;
import com.simple.original.client.proxy.UIComplexInputModelProxy;
import com.simple.original.client.proxy.UIDateInputModelProxy;
import com.simple.original.client.proxy.UIUserInputModelProxy;
import com.simple.original.client.service.PublicRequestFactory;

public class ClientUtils {

	/**
	 * This will test to make sure that the session appears valid to the client
	 * side. For speed purposes we will use this to validate on the client side.
	 * The back end must make sure that it validates all information coming across
	 * the wire.
	 * 
	 * @return
	 */
	public static String getSessionValue() {
		return Cookies.getCookie("JSESSIONID");
	}

	/**
	 * This is a quick check to see if the AuthToken session cookie and the
	 * current JSESSIONID token match if not. Then server is out of wack and we
	 * don't have the correct data.
	 * 
	 * @return boolean depending on if the server appears valid on client side.
	 */
	public static boolean isSessionValid(PublicRequestFactory.PublicRequest request, AsyncCallback<Boolean> isValid) {
		
		return true;

	}
	
	public static String getClassSimpleName(Class<?> clazz) {
		return clazz.getName().substring(clazz.getName().lastIndexOf(".")+1);
	}
	
	
    /**
     * Since request factory does not have a clone method we are using this
     * temporary to copy the bean to the new context. The reason we have to do
     * this is because the input beans are locked by the previous request
     * context and we can't easily pass a request context in a place. This will
     * really clone the input name and default value to send to the server when
     * executing a task.
     * 
     * @param request
     *            The new request that will be used to create the new copies of
     *            the inputs.
     * @param inputs
     *            The new inputs that will be copied into. This is mainly used
     *            for recursion.
     * @param arguments
     *            The arguments that will be "cloned".
     */
    public static void cloneOperationInputs(RequestContext request, List<AnalyticsOperationInputProxy> inputs, List<AnalyticsOperationInputProxy> arguments) {

        for (AnalyticsOperationInputProxy argument : arguments) {
            try {
                if (argument instanceof UIUserInputModelProxy) {
                    UIUserInputModelProxy value = (UIUserInputModelProxy) argument;
                    UIUserInputModelProxy input = request.create(UIUserInputModelProxy.class);
                    input.setValue(value.getValue());
                    input.setInputName(value.getInputName());
                    input.setDisplayName(value.getDisplayName());
                    inputs.add(input);
                } else if (argument instanceof UIDateInputModelProxy) {
                    UIDateInputModelProxy value = (UIDateInputModelProxy) argument;
                    UIDateInputModelProxy input = request.create(UIDateInputModelProxy.class);
                    input.setValue(value.getValue());
                    input.setInputName(value.getInputName());
                    input.setDisplayName(value.getDisplayName());
                    inputs.add(input);
                } else if (argument instanceof UIComplexInputModelProxy) {
                    UIComplexInputModelProxy value = (UIComplexInputModelProxy) argument;
                    UIComplexInputModelProxy complexInput = request.create(UIComplexInputModelProxy.class);
                    complexInput.setInputs(new ArrayList<AnalyticsOperationInputProxy>());
                    complexInput.setInputName(value.getInputName());
                    complexInput.setDisplayName(value.getDisplayName());
                    // !! Careful recursion.
                    cloneOperationInputs(request, complexInput.getInputs(), value.getInputs());
                    inputs.add(complexInput);
                } else {
                    throw new IllegalArgumentException("Unable to clone input named " + argument.getInputName() + " because it's type was unknown");
                }

            } catch (AssertionError e) {
                GWT.log("Testing input was " + argument);
            }
        }
    }

	public static boolean hasValidSessionId() {
		boolean isValid = false;
		
		String sessionId = Cookies.getCookie("rememberme");
		
		if (sessionId != null && !sessionId.equals("")) {
			isValid = true;
		}
		return isValid;
	}
}
