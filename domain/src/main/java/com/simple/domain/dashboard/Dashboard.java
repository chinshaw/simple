/**
 * 
 */
package com.simple.domain.dashboard;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.simple.domain.AnalyticsOperationInput;
import com.simple.domain.AnalyticsTask;

/**
 * @author chinshaw
 * 
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({ @NamedQuery(name = "findDashboardByTaskId", query = "select dash from Dashboard as dash where dash.analyticsTask.id = :taskId") })
@Table(name = "dashboard_dashboardwidget")
public class Dashboard extends Widget {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -6765335626492088176L;

	@NotNull(message = "Name must be provided")
	private String name;

	@OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "analyticstask_id")
	private AnalyticsTask analyticsTask;

	@Column(name = "widgetmodel", columnDefinition = "TEXT")
	private String widgetModel;

	/**
	 * Empty constructor that will initialize the dashboard with null name,
	 * notice that the name will have to be set before it is saved.
	 */
	public Dashboard() {
		this(null);
	}

	/**
	 * Constructor that will initialize dashboard widget with it's name field.
	 * 
	 * @param name
	 */
	public Dashboard(String name) {
		this(name, null);
	}

	/**
	 * Constructor that will initialize dashboard widget with it's name field
	 * along with the required analytics task. This is the <em> PREFERRED </em>
	 * constructor so that you can save without having a violation constraint.
	 * 
	 * @param name
	 */
	public Dashboard(String name, AnalyticsTask analyticsTask) {
		this.name = name;
		this.analyticsTask = analyticsTask;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AnalyticsTask getAnalyticsTask() {
		return analyticsTask;
	}

	public void setAnalyticsTask(AnalyticsTask template) {
		this.analyticsTask = template;
	}

	public List<AnalyticsOperationInput> getInputs() {
		return analyticsTask.getAllInputs();
	}

	public String getWidgetModel() {
		return widgetModel;
	}

	public void setWidgetModel(String widgetModel) {
		this.widgetModel = widgetModel;
	}

	/**
	 * Setter for the id.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Dashboard clone() throws CloneNotSupportedException {
		Dashboard clone = (Dashboard) super.clone();
		clone.setId(null);
		clone.setAnalyticsTask(analyticsTask);

		return clone;
	}
}