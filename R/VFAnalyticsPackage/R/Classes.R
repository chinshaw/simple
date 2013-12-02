##== VFAnalytics wrapper classes

Metric <- setClass(Class="Metric", representation(violations="list"))

Violation <- setClass(Class="Violation", 
	representation(subgroup="numeric", severity="numeric", rule="character", detail="character", key="character", paramMap="list"))



RANGE_NORMAL <- 0
RANGE_WARNING <- 1
RANGE_CRITICAL <- 2

#lockBinding("RANGE_CRITICAL", gobalenv())
#lockBinding("RANGE_WARNING", globalenv())
#lockBinding("RANGE_NORMAL", globalenv())

# MetricRange is simply a range object that has a min and max. Simply for consitency with other VF classes.
MetricRange <-setClass(Class="MetricRange", 
	representation(min="numeric", max="numeric", criticality="numeric"))

setClass(Class="Ranges", 
	representation("list"),
	contains = "MetricRange")

# Metric Number class can be any Numeric value. It also contains the three ranges.
MetricNumber <- setClass(Class="MetricNumber",
  representation(ranges = "list", value="numeric"),
  contains="Metric",
  prototype=prototype(new("Metric")))

MetricString <- setClass(Class="MetricString",
  representation(value="character"),
  contains="Metric",
  prototype=prototype(new("Metric")))

MetricPlot <- setClass(Class="MetricPlot",
	representation(plot="raw"),
	contains="Metric",
  prototype=prototype(new("Metric")))


# Create MetricDataFrame class and extend data frame object.
#setOldClass("data.frame")
MetricMatrix <- setClass(Class="MetricMatrix", 
	representation("data.frame"))


# Create MetricDataFrame class and extend data frame object.
#setClass(Class="MetricCollection", 
#	representation("list"), 
#	contains="Metric",
#	prototype=prototype(new("Metric")))
