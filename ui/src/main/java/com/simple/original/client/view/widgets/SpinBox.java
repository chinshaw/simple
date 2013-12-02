package com.simple.original.client.view.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.simple.original.client.resources.Resources;

/**
 * SpinBox is a simple widget that allow to choose an integer value
 * 
 * @author Rajesh
 */
public class SpinBox extends Composite implements ClickHandler, ChangeHandler{

        private static final String DEFAULT_STYLE ="spinBox" ;
        
	
	private List<SpinBoxListener> observers ;
	
	private int minValue ;
	private int maxValue ;
	private PushButton buttonUp ;
	private PushButton buttonDown ;
	private int value ;
	private TextBox inputField ;
	int backupValue ;
	private Resources resources;
	
    /**
     * Build a default SpinBox
     * This spin box allows values between 0 and 100, initialy it shows 0
     */
	public SpinBox(Resources resources){
		this(0,100,0, resources);
	}

    /**
     * Build a SpinBox with specified values
     * @param min the minimum value
     * @param max the maximum value
     * @param initial the initial value
     */
	@UiConstructor	
	public SpinBox(int min, int max, int initial, Resources resources){
		this.resources = resources;
		this.observers = new ArrayList<SpinBoxListener>();
		maxValue = max ;
		minValue = min ;
		value = initial ;
		backupValue = value ;
		setupUi();
		setupListeners() ;
        inputField.setStyleName(DEFAULT_STYLE);
	}

        /**
         * Retrieve the maximum value
         * @return the maximum value accepted by the SpinBox
         */
	public int getMaxValue() {
		return maxValue;
	}

        /**
         * 
         * @param maxValue
         */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		if (value > maxValue){
			value = maxValue ;
            onValueChanged() ;
		}else{
            onValueChangeWithoutNotification();
        }
		
	}

    public void setMinValue(int minValue){
        this.minValue = minValue ;
        if(value < minValue){
            value = minValue ;
            onValueChanged();
        }else{
            onValueChangeWithoutNotification();
        }
        
    }

	public int getValue() {
		return value;
	}

    public void setValue (int newValue){
        value = newValue;
        onValueChanged();
    }

	private void setupUi(){
		HorizontalPanel mainPanel = new HorizontalPanel();
		VerticalPanel buttonsPanel = new VerticalPanel();
		buttonUp = new PushButton(new Image(resources.spinBoxUp()));
        buttonUp.setStyleName("spinbox-button");
		buttonDown = new PushButton(new Image(resources.spinBoxDown()));
        buttonDown.setStyleName("spinbox-button");
		buttonsPanel.add(buttonUp);
		buttonsPanel.add(buttonDown);
		inputField = new TextBox() ;
		inputField.setVisibleLength(2) ;
		inputField.setWidth("1.5em");
		inputField.setText(""+value);
		inputField.setAlignment(TextAlignment.RIGHT);
		mainPanel.add(inputField);
		mainPanel.add(buttonsPanel);
		initWidget(mainPanel);
		onValueChanged() ;
	}

	private void setupListeners(){
		buttonUp.addClickHandler(this);
		buttonDown.addClickHandler(this);
		inputField.addChangeHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == buttonUp) {
			if (value < maxValue){
				value ++ ;
				onValueChanged() ;
			}
			
		} else {
			if (value>minValue) {
				value-- ;
				onValueChanged() ;
			}
		}
	}

    private void onValueChangeWithoutNotification(){
    //value is the new backup now
	backupValue = value ;
	if (value == maxValue && value == minValue){
		buttonUp.setEnabled(false);
		buttonDown.setEnabled(false) ;
	}else if (value == maxValue){
		buttonUp.setEnabled(false);
		buttonDown.setEnabled(true) ;
	}else if(value == minValue){
		buttonUp.setEnabled(true);
		buttonDown.setEnabled(false) ;
	}else{
		buttonUp.setEnabled(true);
		buttonDown.setEnabled(true);
	}
	inputField.setText(""+value);
    }
	
	
	private void onValueChanged(){
		// value is the new backup now
		backupValue = value ;
		if (value == maxValue && value == minValue){
			buttonUp.setEnabled(false);
			buttonDown.setEnabled(false) ;
		}else if (value == maxValue){
			buttonUp.setEnabled(false);
			buttonDown.setEnabled(true) ;
		}else if(value == minValue){
			buttonUp.setEnabled(true);
			buttonDown.setEnabled(false) ;
		}else{
			buttonUp.setEnabled(true);
			buttonDown.setEnabled(true);
		}
		inputField.setText(""+value);
		// send event
		fireChange() ;
	}


	@Override
	public void onChange(ChangeEvent event) {
		if (inputField.getText().matches("^[0-9]+")){
			Integer tempValue = new Integer(inputField.getText()) ;
			if (tempValue >= minValue && tempValue <= maxValue){
				value = tempValue ;
			}else{
				// restaure backup :
				value = backupValue ;
			}
		}else{
			value = backupValue ;
		}
		onValueChanged() ;
	}
	
	public void addListener(SpinBoxListener l){
		this.observers.add(l);
	}
	
	public void removeListener(SpinBoxListener l){
		this.observers.remove(l);
	}
	
	private void fireChange(){
		SpinBoxEvent ev = new SpinBoxEvent(this, value);
		for (SpinBoxListener l : observers) {
			l.onValueChanged(ev);
		}
		
	}
}
