library("VFAnalytics")
library("Cairo")

#### Metric only
metricWithOnlyValue <- MetricNumber(value=101)


#### Metric with ranges
metricNumWithValueAndRanges <- MetricNumber(value=202)
r1 <- MetricRange(min=1, max=100, criticality=1)
r2 <- MetricRange(min=100, max=200, criticality=2)
r3 <- MetricRange(min=200, max=400, criticality=3)

metricNumWithValueAndRanges@ranges <- c(r1, r2, r3)


#### MetricString
metricString <- MetricString(value="metricString")


#### MetricPlot
testPlotVariables <- c(1, 3, 6, 4, 9)

Cairo(file = "temp.png", width=1024, height = 800, type="png",  units = "px", dpi = "auto")
plot(testPlotVariables)
dev.off()

print("Creating plot")
metricPlot <- MetricPlot()
metricPlot@plot <- setPlot(plot="temp.png")
print("Plot is created");
print(metricPlot)


##### Metric Matrix

val1 <- c(1.1,77,78.1,78.2,78.8,79.7,79.9,81.1,81.2,81.8,82.8,83.5)
val2 <- c(76.1,77,78.1,78.2,78.8,79.7,79.9,81.1,81.2,81.8,82.8,200234566.52223)

dataFrame <- data.frame(column1=val1,column2=val2)
metricMatrix <- MetricMatrix(dataFrame)

print(metricMatrix)