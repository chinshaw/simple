package com.simple.domain.model.ui.dashboard;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.simple.domain.model.RequestFactoryEntity;
import com.simple.original.api.orchestrator.Criticality;

/**
 * <p>
 * Represents a range of {@link Number} objects. Most of the code was borrowed
 * from the apache commons NumberRange. I added the getters and changed the min
 * and max to Double from Number. This is to support <code>RequestFactory</code>
 * because it does not support java.lang.Number.
 * </p>
 * 
 */
@Entity
@Access(AccessType.FIELD)
@Table( name="metric_numberrange")
public class NumberRange extends RequestFactoryEntity {

    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 7009240807802261976L;

    public static final String RTYPE = "MetricRange";
	
	/* The minimum number in this range. */
	private Double min;

	/* The maximum number in this range. */
	private Double max;
	
	private Criticality criticality;

	public NumberRange() {
		this(new Double(0.0));
	}

	/**
	 * <p>
	 * Constructs a new <code>NumberRange</code> using <code>number</code> as
	 * both the minimum and maximum in this range.
	 * </p>
	 * 
	 * @param num
	 *            the number to use for this range
	 * @throws NullPointerException
	 *             if the number is <code>null</code>
	 */
	public NumberRange(Double num) {
		if (num == null) {
			throw new NullPointerException("The number must not be null");
		}

		this.min = num;
		this.max = num;
	}

	/**
	 * <p>
	 * Constructs a new <code>NumberRange</code> with the specified minimum and
	 * maximum numbers.
	 * </p>
	 * 
	 * <p>
	 * <em>If the maximum is less than the minimum, the range will be constructed
	 * from the minimum value to the minimum value, not what you would expect!.</em>
	 * </p>
	 * 
	 * @param min
	 *            the minimum number in this range
	 * @param max
	 *            the maximum number in this range
	 * @throws NullPointerException
	 *             if either the minimum or maximum number is <code>null</code>
	 */
	public NumberRange(Double min, Double max) {
		if (min == null) {
			throw new NullPointerException("The minimum value must not be null");
		} else if (max == null) {
			throw new NullPointerException("The maximum value must not be null");
		}

		if (max.doubleValue() < min.doubleValue()) {
			this.min = this.max = min;
		} else {
			this.min = min;
			this.max = max;
		}
	}

	/**
	 * <p>
	 * Returns the minimum number in this range.
	 * </p>
	 * 
	 * @return the minimum number in this range
	 */
	public Double getMinimum() {
		return min;
	}

	/**
	 * <p>
	 * Sets the minimum <code>number</code> in the range.
	 * </p>
	 * 
	 * @param min
	 *            the minimum value for the range.
	 */
	public void setMinimum(Double min) {
		this.min = min;
	}

	/**
	 * <p>
	 * Returns the maximum number in this range.
	 * </p>
	 * 
	 * @return the maximum number in this range
	 */
	public Double getMaximum() {
		return max;
	}

	/**
	 * <p>
	 * Setter for the maximum value.
	 * </p>
	 * 
	 * @param max
	 *            the maximum number for the range
	 */
	public void setMaximum(Double max) {
		this.max = max;
	}
	
	
	public Criticality getCriticality() {
		return criticality;
	}
	
	public void setCriticality(Criticality criticality) {
		this.criticality = criticality;
	}

	/**
	 * <p>
	 * Tests whether the specified <code>number</code> occurs within this range
	 * using <code>double</code> comparison.
	 * </p>
	 * 
	 * @param number
	 *            the number to test
	 * @return <code>true</code> if the specified number occurs within this
	 *         range; otherwise, <code>false</code>
	 */
	public boolean includesNumber(Double number) {
		if (number == null) {
			return false;
		} else {
			return !(min.doubleValue() > number.doubleValue())
					&& !(max.doubleValue() < number.doubleValue());
		}
	}

	/**
	 * <p>
	 * Tests whether the specified range occurs entirely within this range using
	 * <code>double</code> comparison.
	 * </p>
	 * 
	 * @param range
	 *            the range to test
	 * @return <code>true</code> if the specified range occurs entirely within
	 *         this range; otherwise, <code>false</code>
	 */
	public boolean includesRange(NumberRange range) {
		if (range == null) {
			return false;
		} else {
			return includesNumber(range.min) && includesNumber(range.max);
		}
	}

	/**
	 * <p>
	 * Tests whether the specified range overlaps with this range using
	 * <code>double</code> comparison.
	 * </p>
	 * 
	 * @param range
	 *            the range to test
	 * @return <code>true</code> if the specified range overlaps with this
	 *         range; otherwise, <code>false</code>
	 */
	public boolean overlaps(NumberRange range) {
		if (range == null) {
			return false;
		} else {
			return range.includesNumber(min) || range.includesNumber(max)
					|| includesRange(range);
		}
	}

	/**
	 * <p>
	 * Indicates whether some other <code>Object</code> is &quot;equal&quot; to
	 * this one.
	 * </p>
	 * 
	 * @param obj
	 *            the reference object with which to compare
	 * @return <code>true</code> if this object is the same as the obj argument;
	 *         <code>false</code> otherwise
	 */
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof NumberRange)) {
			return false;
		} else {
			NumberRange range = (NumberRange) obj;
			return min.equals(range.min) && max.equals(range.max);
		}
	}

	/**
	 * <p>
	 * Returns a hash code value for this object.
	 * </p>
	 * 
	 * @return a hash code value for this object
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + min.hashCode();
		result = 37 * result + max.hashCode();
		return result;
	}

	/**
	 * <p>
	 * Returns the string representation of this range.
	 * </p>
	 * 
	 * <p>
	 * This string is the string representation of the minimum and maximum
	 * numbers in the range, separated by a hyphen. If a number is negative,
	 * then it is enclosed in parentheses.
	 * </p>
	 * 
	 * @return the string representation of this range
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		if (min.doubleValue() < 0) {
			sb.append('(').append(min).append(')');
		} else {
			sb.append(min);
		}

		sb.append('-');

		if (max.doubleValue() < 0) {
			sb.append('(').append(max).append(')');
		} else {
			sb.append(max);
		}
		
		if (criticality != null) {
			sb.append(criticality.toString());
		}

		return sb.toString();
	}
}
